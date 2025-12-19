from fastapi import FastAPI, HTTPException, Depends
from fastapi.middleware.cors import CORSMiddleware
from pydantic import BaseModel
from typing import List, Optional
import numpy as np
from datetime import datetime
from sqlalchemy.orm import Session
from database import get_db, Product
from ai_models import recommendation_model, anomaly_model, train_models

app = FastAPI(title="MicroBill AI Service", version="1.0.0")

# Configurar CORS
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

class ProductRecommendation(BaseModel):
    productId: int
    productName: str
    price: float
    score: float
    reason: str

class RecommendationResponse(BaseModel):
    customerId: int
    products: List[ProductRecommendation]
    reason: str

class InvoiceItem(BaseModel):
    productId: int
    quantity: int
    unitPrice: float
    taxRate: float

class AnomalyRequest(BaseModel):
    customerId: int
    items: List[InvoiceItem]
    total: float

class AnomalyResponse(BaseModel):
    score: float
    explanation: str

@app.on_event("startup")
async def startup_event():
    """Train AI models on startup"""
    try:
        success = train_models()
        if success:
            print("Modelos de IA entrenados exitosamente")
        else:
            print("Advertencia: Entrenamiento de modelos de IA falló - usando lógica de respaldo")
    except Exception as e:
        print(f"Error training models: {e}")

@app.get("/")
def root():
    return {"service": "MicroBill AI Service", "status": "running"}

@app.get("/health")
def health():
    return {"status": "healthy"}

@app.post("/api/ai/retrain")
def retrain_models():
    """Manually retrain AI models"""
    try:
        success = train_models()
        return {"success": success, "message": "Modelos reentrenados" if success else "Entrenamiento falló"}
    except Exception as e:
        raise HTTPException(status_code=500, detail="Su operación no pudo ser procesada, consulte con el administrador")

@app.post("/api/ai/recommendations", response_model=RecommendationResponse)
def get_recommendations(customerId: int, db: Session = Depends(get_db)):
    """Generate product recommendations for a customer based on purchase history"""
    
    try:
        # Get recommendations from trained model
        recommendations_data = recommendation_model.get_recommendations(customerId, top_k=5)
        
        if not recommendations_data:
            # Fallback to popular products
            popular_products = db.query(Product).limit(3).all()
            recommendations_data = [{
                'productId': p.id,
                'productName': p.name,
                'score': 0.7,
                'reason': 'Producto popular'
            } for p in popular_products]
        
        # Enrich with product details
        recommendations = []
        for rec in recommendations_data:
            product = db.query(Product).filter(Product.id == rec['productId']).first()
            if product:
                recommendations.append(ProductRecommendation(
                    productId=rec['productId'],
                    productName=rec['productName'],
                    price=product.price,
                    score=rec['score'],
                    reason=rec['reason']
                ))
        
        return RecommendationResponse(
            customerId=customerId,
            products=recommendations,
            reason="Basado en su historial de compras y clientes similares"
        )
        
    except Exception as e:
        raise HTTPException(status_code=500, detail="Su operación no pudo ser procesada, consulte con el administrador")

@app.post("/api/ai/anomaly-score", response_model=AnomalyResponse)
def detect_anomaly(request: AnomalyRequest):
    """Detect anomalies in invoice amounts using trained ML model"""
    
    try:
        total_items = len(request.items)
        avg_unit_price = np.mean([item.unitPrice for item in request.items]) if request.items else 0
        total_quantity = sum([item.quantity for item in request.items])
        unique_products = len(set([item.productId for item in request.items]))
        
        invoice_data = {
            'total': request.total,
            'items_count': total_items,
            'avg_item_price': avg_unit_price,
            'total_quantity': total_quantity,
            'unique_products': unique_products
        }
        
        # Use trained model for anomaly detection
        anomaly_score, explanation = anomaly_model.detect_anomaly(invoice_data)
        
        return AnomalyResponse(
            score=round(anomaly_score, 2),
            explanation=explanation
        )
        
    except Exception as e:
        anomaly_score = 0.0
        explanation_parts = []
        
        if request.total > 10000:
            anomaly_score += 0.3
            explanation_parts.append(f"Total alto: ${request.total:.2f}")
        
        if len(request.items) > 20:
            anomaly_score += 0.2
            explanation_parts.append(f"Muchos artículos: {len(request.items)}")
        
        anomaly_score = min(anomaly_score, 1.0)
        explanation = "Análisis de respaldo: " + ("Factura normal" if not explanation_parts else "; ".join(explanation_parts))
        
        return AnomalyResponse(
            score=round(anomaly_score, 2),
            explanation=explanation
        )

if __name__ == "__main__":
    import uvicorn
    uvicorn.run(app, host="0.0.0.0", port=8084)
