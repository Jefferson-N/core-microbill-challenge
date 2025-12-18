# 🧪 Guía de Pruebas API - Microbill

## 📋 Colección de Postman

### **Archivos Incluidos:**
- `Microbill_API_Collection.postman_collection.json` - Colección completa
- `Microbill_Environment.postman_environment.json` - Variables de entorno

## 🚀 Configuración Inicial

### **1. Importar en Postman:**
1. Abrir Postman
2. Click en "Import"
3. Seleccionar ambos archivos JSON
4. Seleccionar el environment "Microbill - Local Development"

### **2. Verificar Servicios:**
Ejecutar primero los Health Checks para verificar que todos los servicios estén funcionando:
- Auth Service Health
- Management Service Health  
- Billing Service Health
- AI Service Health

## 🔄 Flujo de Pruebas Recomendado

### **Paso 1: Autenticación**
```
🔐 Authentication Service
├── Login Admin (guarda JWT automáticamente)
└── Login User (opcional)
```

### **Paso 2: Gestión de Datos**
```
👥 Management Service - Customers
├── Create Customer (guarda customer_id)
├── Get All Customers
├── Get Customer by ID
└── Update Customer

🏢 Management Service - Providers  
├── Create Provider (guarda provider_id)
└── Get All Providers

📦 Management Service - Products
├── Create Product (guarda product_id)
├── Create Product - Mouse
├── Create Product - Keyboard
└── Get All Products
```

### **Paso 3: Facturación**
```
🧾 Billing Service - Invoices
├── Create Invoice (guarda invoice_id)
├── Get All Invoices
├── Get Invoice by ID
└── Generate Invoice PDF Report
```

### **Paso 4: Inteligencia Artificial**
```
🤖 AI Service - Recommendations & Anomalies
├── Get Product Recommendations
└── Detect Anomalies
```

## 📊 Endpoints Principales

### **🔐 Autenticación**
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | `/api/auth/login` | Login con JWT |

**Usuarios Demo:**
- Admin: `admin@demo.com` / `Admin#123`
- User: `user@demo.com` / `User#123`

### **👥 Gestión de Clientes**
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/customers` | Listar clientes (paginado) |
| POST | `/api/customers` | Crear cliente |
| GET | `/api/customers/{id}` | Obtener cliente por ID |
| PUT | `/api/customers/{id}` | Actualizar cliente |
| DELETE | `/api/customers/{id}` | Eliminar cliente |

### **🏢 Gestión de Proveedores**
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/providers` | Listar proveedores |
| POST | `/api/providers` | Crear proveedor |
| PUT | `/api/providers/{id}` | Actualizar proveedor |
| DELETE | `/api/providers/{id}` | Eliminar proveedor |

### **📦 Gestión de Productos**
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/products` | Listar productos |
| POST | `/api/products` | Crear producto |
| PUT | `/api/products/{id}` | Actualizar producto |
| DELETE | `/api/products/{id}` | Eliminar producto |

### **🧾 Facturación**
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/invoices` | Listar facturas |
| POST | `/api/invoices` | Crear factura |
| GET | `/api/invoices/{id}` | Obtener factura |
| POST | `/api/invoices/{id}/generate-report` | Generar PDF |

### **🤖 Inteligencia Artificial**
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | `/api/ai/recommendations?customerId={id}` | Recomendaciones |
| POST | `/api/ai/anomaly-score` | Detección de anomalías |

## 🧪 Casos de Prueba

### **Test Case 1: Flujo Completo de Facturación**
1. Login como Admin
2. Crear Cliente
3. Crear Proveedor  
4. Crear 3 Productos
5. Crear Factura con múltiples items
6. Generar PDF de la factura
7. Obtener recomendaciones IA
8. Detectar anomalías

### **Test Case 2: Validaciones y Errores**
1. Login con credenciales incorrectas
2. Crear entidades con datos inválidos
3. Acceder a recursos sin autenticación
4. Probar límites de paginación

### **Test Case 3: Inteligencia Artificial**
1. Solicitar recomendaciones para cliente existente
2. Solicitar recomendaciones para cliente inexistente
3. Detectar anomalías en factura normal
4. Detectar anomalías en factura con montos atípicos

## 📈 Validaciones Automáticas

La colección incluye **tests automáticos** que verifican:

- ✅ Códigos de respuesta HTTP correctos
- ✅ Estructura de respuestas JSON
- ✅ Guardado automático de IDs en variables
- ✅ Validación de tokens JWT
- ✅ Verificación de datos de IA

## 🔧 Variables de Entorno

| Variable | Descripción | Valor por Defecto |
|----------|-------------|-------------------|
| `base_url` | URL base de servicios | `http://localhost` |
| `auth_port` | Puerto Auth Service | `8079` |
| `management_port` | Puerto Management | `8081` |
| `billing_port` | Puerto Billing | `8080` |
| `ai_port` | Puerto AI Service | `8082` |
| `jwt_token` | Token JWT (auto) | - |
| `customer_id` | ID Cliente (auto) | - |
| `provider_id` | ID Proveedor (auto) | - |
| `product_id` | ID Producto (auto) | - |
| `invoice_id` | ID Factura (auto) | - |

## 🚨 Troubleshooting

### **Error 500 en Login:**
- Verificar que MySQL esté funcionando
- Verificar que las tablas de usuarios estén creadas
- Revisar logs del Auth Service

### **Error de Conexión:**
- Verificar que Docker Compose esté ejecutándose
- Verificar puertos en `docker-compose ps`
- Verificar health checks

### **JWT Expirado:**
- Ejecutar nuevamente "Login Admin"
- El token se actualiza automáticamente

## 📚 Documentación Swagger

Acceder a la documentación interactiva:
- **Auth Service**: http://localhost:8079/swagger-ui.html
- **Management Service**: http://localhost:8081/swagger-ui.html  
- **Billing Service**: http://localhost:8080/swagger-ui.html
- **AI Service**: http://localhost:8082/docs

---

**🎯 La colección está diseñada para probar todos los requisitos del caso de prueba Java de forma automatizada y secuencial.**