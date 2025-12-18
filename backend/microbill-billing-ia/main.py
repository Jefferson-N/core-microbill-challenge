from fastapi import FastAPI, HTTPException
from fastapi.middleware.cors import CORSMiddleware
from pydantic import BaseModel
from typing import List, Optional
import numpy as np
from datetime import datetime

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
    score: float
    reason: str

class RecommendationResponse(BaseModel):
    customerId: int
    products: List[ProductRecommendation]
    generatedAt: str

class InvoiceItem(BaseModel):
    productId: int
    quantity: int
    unitPrice: float
    taxRate: float

class AnomalyRequest(BaseModel):
    invoiceId: int
    customerId: int
    items: List[InvoiceItem]
    total: float

class AnomalyResponse(BaseModel):
    invoiceId: int
    score: float
    isAnomaly: bool
    explanation: str

@app.get("/")
def root():
    return {"service": "MicroBill AI Service", "status": "running"}

@app.get("/health")
def health():
    return {"status": "healthy"}

@app.post("/api/ai/recommendations", response_model=RecommendationResponse)
def get_recommendations(customerId: int):
    """Generate product recommendations for a customer based on purchase history"""
    
    # Simulación de recomendaciones basadas en historial
    recommendations = [
        ProductRecommendation(
            productId=1,
            productName="Laptop Dell XPS 15",
            score=0.92,
            reason="Frequently purchased by similar customers"
        ),
        ProductRecommendation(
            productId=2,
            productName="Mouse Logitech MX Master",
            score=0.87,
            reason="Complementary to previous purchases"
        ),
        ProductRecommendation(
            productId=3,
            productName="USB-C Hub",
            score=0.78,
            reason="Popular accessory for your device category"
        )
    ]
    
    return RecommendationResponse(
        customerId=customerId,
        products=recommendations,
        generatedAt=datetime.now().isoformat()
    )

@app.post("/api/ai/anomaly-score", response_model=AnomalyResponse)
def detect_anomaly(request: AnomalyRequest):
    """Detect anomalies in invoice amounts using statistical analysis"""
    
    # Calcular métricas de la factura
    total_items = len(request.items)
    avg_unit_price = np.mean([item.unitPrice for item in request.items])
    total_quantity = sum([item.quantity for item in request.items])
    
    # Simulación de detección de anomalías
    # En producción, esto usaría modelos ML entrenados con datos históricos
    anomaly_score = 0.0
    explanation_parts = []
    
    # Regla 1: Total muy alto
    if request.total > 10000:
        anomaly_score += 0.3
        explanation_parts.append(f"High total amount: ${request.total:.2f}")
    
    # Regla 2: Cantidad inusual de items
    if total_items > 20:
        anomaly_score += 0.2
        explanation_parts.append(f"Unusual number of items: {total_items}")
    
    # Regla 3: Precio unitario promedio muy alto
    if avg_unit_price > 1000:
        anomaly_score += 0.25
        explanation_parts.append(f"High average unit price: ${avg_unit_price:.2f}")
    
    # Regla 4: Cantidad total muy alta
    if total_quantity > 100:
        anomaly_score += 0.25
        explanation_parts.append(f"High total quantity: {total_quantity}")
    
    # Normalizar score entre 0 y 1
    anomaly_score = min(anomaly_score, 1.0)
    
    is_anomaly = anomaly_score > 0.5
    
    if not explanation_parts:
        explanation = "Invoice appears normal based on historical patterns"
    else:
        explanation = "Potential anomaly detected: " + "; ".join(explanation_parts)
    
    return AnomalyResponse(
        invoiceId=request.invoiceId,
        score=round(anomaly_score, 2),
        isAnomaly=is_anomaly,
        explanation=explanation
    )

if __name__ == "__main__":
    import uvicorn
    uvicorn.run(app, host="0.0.0.0", port=8084)
