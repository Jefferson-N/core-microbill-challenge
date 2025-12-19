import pandas as pd
import numpy as np
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.metrics.pairwise import cosine_similarity
from sklearn.ensemble import IsolationForest
from sklearn.preprocessing import StandardScaler
from sqlalchemy.orm import Session
from database import get_db, Invoice, InvoiceItem, Product, Customer
from typing import List, Dict, Tuple
import pickle
import os

class RecommendationModel:
    def __init__(self):
        self.customer_product_matrix = None
        self.product_similarity_matrix = None
        self.products_df = None
        self.scaler = StandardScaler()
        
    def train(self, db: Session):
        """Train recommendation model using collaborative filtering and content-based filtering"""
        
        # Load data from database
        invoices_data = []
        query = db.query(Invoice, InvoiceItem, Product, Customer).join(
            InvoiceItem, Invoice.id == InvoiceItem.invoice_id
        ).join(
            Product, InvoiceItem.product_id == Product.id
        ).join(
            Customer, Invoice.customer_id == Customer.id
        )
        
        for invoice, item, product, customer in query.all():
            invoices_data.append({
                'customer_id': customer.id,
                'customer_name': customer.name,
                'product_id': product.id,
                'product_name': product.name,
                'product_code': product.code,
                'quantity': item.quantity,
                'unit_price': item.unit_price,
                'total_amount': item.quantity * item.unit_price,
                'invoice_total': invoice.total
            })
        
        if not invoices_data:
            return False
            
        df = pd.DataFrame(invoices_data)
        
        self.customer_product_matrix = df.groupby(['customer_id', 'product_id']).agg({
            'quantity': 'sum',
            'total_amount': 'sum'
        }).reset_index()
        
        products_query = db.query(Product).all()
        self.products_df = pd.DataFrame([{
            'id': p.id,
            'name': p.name,
            'code': p.code,
            'price': p.price,
            'tax_rate': p.tax_rate
        } for p in products_query])
        
        if len(self.products_df) > 1:
            features = self.products_df[['price', 'tax_rate']].values
            features_scaled = self.scaler.fit_transform(features)
            self.product_similarity_matrix = cosine_similarity(features_scaled)
        
        return True
    
    def get_recommendations(self, customer_id: int, top_k: int = 5) -> List[Dict]:
        """Get product recommendations for a customer"""
        if self.customer_product_matrix is None:
            return []
        
        customer_products = self.customer_product_matrix[
            self.customer_product_matrix['customer_id'] == customer_id
        ]['product_id'].tolist()
        
        if not customer_products:
            popular_products = self.customer_product_matrix.groupby('product_id').agg({
                'quantity': 'sum',
                'total_amount': 'sum'
            }).sort_values('total_amount', ascending=False).head(top_k)
            
            recommendations = []
            for product_id in popular_products.index:
                product_info = self.products_df[self.products_df['id'] == product_id].iloc[0]
                recommendations.append({
                    'productId': int(product_id),
                    'productName': product_info['name'],
                    'score': 0.8,
                    'reason': 'Popular product'
                })
            return recommendations
        
        recommendations = []
        
        similar_customers = self.customer_product_matrix[
            self.customer_product_matrix['product_id'].isin(customer_products)
        ]['customer_id'].unique()
        
        similar_customer_products = self.customer_product_matrix[
            (self.customer_product_matrix['customer_id'].isin(similar_customers)) &
            (~self.customer_product_matrix['product_id'].isin(customer_products))
        ]
        
        if not similar_customer_products.empty:
            recommended_products = similar_customer_products.groupby('product_id').agg({
                'quantity': 'sum',
                'total_amount': 'sum'
            }).sort_values('total_amount', ascending=False).head(top_k)
            
            for product_id in recommended_products.index:
                product_info = self.products_df[self.products_df['id'] == product_id].iloc[0]
                score = min(recommended_products.loc[product_id, 'total_amount'] / 1000, 1.0)
                recommendations.append({
                    'productId': int(product_id),
                    'productName': product_info['name'],
                    'score': round(score, 2),
                    'reason': 'Customers with similar preferences bought this'
                })
        
        return recommendations[:top_k]

class AnomalyDetectionModel:
    def __init__(self):
        self.isolation_forest = IsolationForest(contamination=0.1, random_state=42)
        self.scaler = StandardScaler()
        self.feature_stats = {}
        
    def train(self, db: Session):
        """Train anomaly detection model using historical invoice data"""
        
        invoices_data = []
        query = db.query(Invoice, Customer).join(Customer, Invoice.customer_id == Customer.id)
        
        for invoice, customer in query.all():
            items_count = len(invoice.items)
            avg_item_price = np.mean([item.unit_price for item in invoice.items]) if invoice.items else 0
            total_quantity = sum([item.quantity for item in invoice.items])
            unique_products = len(set([item.product_id for item in invoice.items]))
            
            invoices_data.append({
                'total': invoice.total,
                'items_count': items_count,
                'avg_item_price': avg_item_price,
                'total_quantity': total_quantity,
                'unique_products': unique_products,
                'subtotal': invoice.subtotal,
                'tax_total': invoice.tax_total
            })
        
        if len(invoices_data) < 10:  
            return False
            
        df = pd.DataFrame(invoices_data)
        
        self.feature_stats = {
            'total_mean': df['total'].mean(),
            'total_std': df['total'].std(),
            'items_count_mean': df['items_count'].mean(),
            'avg_item_price_mean': df['avg_item_price'].mean(),
            'total_quantity_mean': df['total_quantity'].mean()
        }
        
        features = df[['total', 'items_count', 'avg_item_price', 'total_quantity', 'unique_products']].values
        features_scaled = self.scaler.fit_transform(features)
        
        self.isolation_forest.fit(features_scaled)
        
        return True
    
    def detect_anomaly(self, invoice_data: Dict) -> Tuple[float, str]:
        """Detect if an invoice is anomalous"""
        if self.isolation_forest is None:
            return 0.0, "Model not trained"
        
        # Extract features
        features = np.array([[
            invoice_data['total'],
            invoice_data['items_count'],
            invoice_data['avg_item_price'],
            invoice_data['total_quantity'],
            invoice_data['unique_products']
        ]])
        
        features_scaled = self.scaler.transform(features)
        
        anomaly_score = self.isolation_forest.decision_function(features_scaled)[0]
        is_outlier = self.isolation_forest.predict(features_scaled)[0] == -1
        
        probability = max(0, min(1, (0.5 - anomaly_score) * 2))
        
        explanation_parts = []
        
        if invoice_data['total'] > self.feature_stats['total_mean'] + 2 * self.feature_stats['total_std']:
            explanation_parts.append(f"Unusually high total: ${invoice_data['total']:.2f}")
        
        if invoice_data['items_count'] > self.feature_stats['items_count_mean'] + 2:
            explanation_parts.append(f"High number of items: {invoice_data['items_count']}")
        
        if invoice_data['avg_item_price'] > self.feature_stats['avg_item_price_mean'] * 3:
            explanation_parts.append(f"High average item price: ${invoice_data['avg_item_price']:.2f}")
        
        if not explanation_parts:
            explanation = "Invoice appears normal based on historical patterns"
        else:
            explanation = "Potential anomaly: " + "; ".join(explanation_parts)
        
        return probability, explanation

# Global model instances
recommendation_model = RecommendationModel()
anomaly_model = AnomalyDetectionModel()

def train_models():
    """Train both AI models with database data"""
    db = next(get_db())
    try:
        rec_success = recommendation_model.train(db)
        anom_success = anomaly_model.train(db)
        return rec_success and anom_success
    finally:
        db.close()