# 📋 MicroBill API Specification

## 🔐 Authentication Service (Port 8081)

### Demo Users
```sql
-- Admin User
email: admin@demo.com
password: Admin#123
role: ADMIN

-- Regular User  
email: user@demo.com
password: User#123
role: USER
```

### Endpoints

#### POST /api/auth/login
**Description**: Authenticate user and get JWT token

**Request Body**:
```json
{
  "email": "admin@demo.com",
  "password": "Admin#123"
}
```

**Response (200)**:
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "user": {
    "id": 1,
    "email": "admin@demo.com",
    "role": "ADMIN"
  }
}
```

#### POST /api/auth/validate
**Description**: Validate JWT token

**Headers**:
```
Authorization: Bearer <token>
```

**Response (200)**:
```json
{
  "valid": true,
  "user": {
    "id": 1,
    "email": "admin@demo.com",
    "role": "ADMIN"
  }
}
```

---

## 📊 Management Service (Port 8082)

### Customers

#### GET /api/customers
**Description**: Get paginated list of customers

**Query Parameters**:
- `page` (default: 0)
- `size` (default: 20)

**Response (200)**:
```json
{
  "content": [
    {
      "id": 1,
      "name": "Acme Corporation",
      "docNumber": "123456789",
      "email": "contact@acme.com",
      "address": "123 Main St, New York, NY 10001"
    }
  ],
  "totalElements": 1,
  "totalPages": 1,
  "size": 20,
  "number": 0
}
```

#### POST /api/customers
**Description**: Create new customer

**Request Body**:
```json
{
  "name": "New Customer Corp",
  "docNumber": "999888777",
  "email": "contact@newcustomer.com",
  "address": "456 Business Ave, Suite 200"
}
```

**Response (201)**:
```json
{
  "id": 3,
  "name": "New Customer Corp",
  "docNumber": "999888777",
  "email": "contact@newcustomer.com",
  "address": "456 Business Ave, Suite 200"
}
```

#### PUT /api/customers/{id}
**Description**: Update existing customer

**Request Body**:
```json
{
  "name": "Updated Customer Corp",
  "docNumber": "999888777",
  "email": "updated@customer.com",
  "address": "789 New Address St"
}
```

#### DELETE /api/customers/{id}
**Description**: Delete customer

**Response**: 204 No Content

### Products

#### GET /api/products
**Description**: Get paginated list of products

**Response (200)**:
```json
{
  "content": [
    {
      "id": 1,
      "code": "LAPTOP-DELL-001",
      "name": "Dell XPS 15",
      "price": 1500.00,
      "stock": 25,
      "taxRate": 0.19
    }
  ],
  "totalElements": 5,
  "totalPages": 1
}
```

#### POST /api/products
**Description**: Create new product

**Request Body**:
```json
{
  "code": "PROD-NEW-001",
  "name": "New Product",
  "price": 299.99,
  "stock": 50,
  "taxRate": 0.19
}
```

### Providers

#### GET /api/providers
**Description**: Get paginated list of providers

**Response (200)**:
```json
{
  "content": [
    {
      "id": 1,
      "name": "Tech Supplier Inc",
      "rucNif": "B12345678",
      "email": "sales@techsupplier.com",
      "address": "789 Industrial Blvd"
    }
  ]
}
```

---

## 🧾 Billing Service (Port 8083)

### Invoices

#### GET /api/invoices
**Description**: Get paginated list of invoices

**Response (200)**:
```json
{
  "content": [
    {
      "id": 1,
      "customerId": 1,
      "providerId": 1,
      "subtotal": 1589.99,
      "taxTotal": 302.10,
      "total": 1892.09,
      "status": "DRAFT",
      "createdAt": "2024-01-15T10:30:00",
      "items": [
        {
          "id": 1,
          "productId": 1,
          "quantity": 1,
          "unitPrice": 1500.00,
          "taxRate": 0.19
        },
        {
          "id": 2,
          "productId": 2,
          "quantity": 1,
          "unitPrice": 89.99,
          "taxRate": 0.19
        }
      ]
    }
  ]
}
```

#### POST /api/invoices
**Description**: Create new invoice with automatic calculations

**Request Body**:
```json
{
  "customerId": 1,
  "providerId": 1,
  "items": [
    {
      "productId": 1,
      "quantity": 2,
      "unitPrice": 1500.00,
      "taxRate": 0.19
    },
    {
      "productId": 2,
      "quantity": 1,
      "unitPrice": 89.99,
      "taxRate": 0.19
    }
  ]
}
```

**Response (201)**:
```json
{
  "id": 2,
  "customerId": 1,
  "providerId": 1,
  "subtotal": 3089.99,
  "taxTotal": 587.10,
  "total": 3677.09,
  "status": "DRAFT",
  "createdAt": "2024-01-15T14:25:00",
  "items": [
    {
      "id": 3,
      "productId": 1,
      "quantity": 2,
      "unitPrice": 1500.00,
      "taxRate": 0.19
    },
    {
      "id": 4,
      "productId": 2,
      "quantity": 1,
      "unitPrice": 89.99,
      "taxRate": 0.19
    }
  ]
}
```

#### POST /api/invoices/{id}/generate-report
**Description**: Generate PDF report for invoice

**Response**: PDF file download

**Headers**:
```
Content-Type: application/pdf
Content-Disposition: attachment; filename="invoice-1.pdf"
```

---

## 🤖 AI Service (Port 8084)

### Product Recommendations

#### POST /api/ai/recommendations?customerId={id}
**Description**: Get AI-powered product recommendations for customer

**Response (200)**:
```json
{
  "customerId": 1,
  "products": [
    {
      "productId": 3,
      "productName": "Wireless Mouse Pro",
      "price": 79.99,
      "score": 0.92,
      "reason": "Frequently purchased by similar customers"
    },
    {
      "productId": 4,
      "productName": "USB-C Hub",
      "price": 49.99,
      "score": 0.87,
      "reason": "Complementary to previous purchases"
    }
  ],
  "reason": "Based on your purchase history and similar customers"
}
```

### Anomaly Detection

#### POST /api/ai/anomaly-score
**Description**: Detect anomalies in invoice data

**Request Body**:
```json
{
  "customerId": 1,
  "total": 15000.00,
  "items": [
    {
      "productId": 1,
      "quantity": 10,
      "unitPrice": 1500.00,
      "taxRate": 0.19
    }
  ]
}
```

**Response (200)**:
```json
{
  "score": 0.85,
  "explanation": "Potential anomaly: Unusually high total: $15000.00; High quantity: 10"
}
```

#### POST /api/ai/retrain
**Description**: Manually retrain AI models with latest data

**Response (200)**:
```json
{
  "success": true,
  "message": "Models retrained successfully"
}
```

---

## 🔧 Common Response Codes

| Code | Description |
|------|-------------|
| 200 | Success |
| 201 | Created |
| 204 | No Content |
| 400 | Bad Request - Validation errors |
| 401 | Unauthorized - Invalid/missing token |
| 403 | Forbidden - Insufficient permissions |
| 404 | Not Found |
| 500 | Internal Server Error |

## 🔐 Authentication

### Public Endpoints (No Authentication Required)
- POST `/api/auth/login` - User login
- POST `/api/auth/register` - User registration
- GET `/health` - Health check (AI Service)

### Protected Endpoints (🔒 JWT Required)
All other endpoints require JWT token in header:
```
Authorization: Bearer <token>
```

**Services requiring authentication:**
- ✅ Management Service - All endpoints
- ✅ Billing Service - All endpoints
- ✅ AI Service - All endpoints (except /health)
- ✅ Auth Service - User management endpoints

## 📊 Pagination

List endpoints support pagination with query parameters:
- `page`: Page number (0-based, default: 0)
- `size`: Page size (default: 20, max: 100)

## ✅ Validation Rules

### Customer
- `name`: Required, 1-255 characters
- `docNumber`: Required, unique, 1-50 characters
- `email`: Valid email format
- `address`: Optional, max 500 characters

### Product
- `code`: Required, unique, 1-50 characters
- `name`: Required, 1-255 characters
- `price`: Required, positive number
- `stock`: Required, non-negative integer
- `taxRate`: Required, 0.0-1.0

### Invoice
- `customerId`: Required, must exist
- `providerId`: Required, must exist
- `items`: Required, at least 1 item
- `items[].productId`: Required, must exist
- `items[].quantity`: Required, positive integer
- `items[].unitPrice`: Required, positive number
- `items[].taxRate`: Required, 0.0-1.0

## 🧪 Test Data

The system includes demo data for testing:

**Customers**:
- Acme Corporation (ID: 1)
- Tech Solutions LLC (ID: 2)

**Products**:
- Dell XPS 15 - $1500.00 (ID: 1)
- Wireless Mouse - $89.99 (ID: 2)
- Monitor 4K - $599.99 (ID: 3)
- Keyboard Mechanical - $149.99 (ID: 4)
- USB-C Hub - $79.99 (ID: 5)

**Providers**:
- Tech Supplier Inc (ID: 1)
- Office Equipment Co (ID: 2)