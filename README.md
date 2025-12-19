# 🧾 Microbill - Sistema de Facturación Completo

Sistema de facturación empresarial con **arquitectura de microservicios**, **IA integrada** y **frontend Angular moderno**.

## 🚀 Inicio Rápido con Docker

### Ejecución con un solo comando:

**Windows:**
```bash
start.bat
```

**Linux/Mac:**
```bash
chmod +x start.sh
./start.sh
```

### Configuración manual:
```bash
# 1. Copiar configuración
cp .env.example .env

# 2. Iniciar todos los servicios
docker-compose up --build -d

# 3. Verificar estado
docker-compose ps
```

## 🏗️ Arquitectura de Microservicios

### 🔹 Servicios Backend (Java 21 + Spring Boot 3)
- **🔐 Auth Service** (8081): Autenticación JWT, usuarios, roles
- **📊 Management Service** (8082): CRUD Customers, Providers, Products  
- **🧾 Billing Service** (8083): Facturas, items, reportes PDF, eventos
- **🤖 AI Service** (8084): Python + FastAPI - Recomendaciones y detección de anomalías

### 🔹 Frontend (Angular 21 + PrimeNG)
- **🎨 Frontend** (4200): SPA con lazy loading, guards, interceptors

### 🔹 Infraestructura
- **🗄️ MySQL**: Base de datos compartida
- **🐰 RabbitMQ**: Mensajería asíncrona
- **🐳 Docker**: Containerización completa

## ✨ Funcionalidades Implementadas

### ✅ **Autenticación y Seguridad**
- Login con JWT
- Guards y interceptors
- Usuarios demo: `admin@demo.com/Admin#123`, `user@demo.com/User#123`

### ✅ **Gestión Completa (CRUD)**
- **Clientes**: Nombre, documento, email, dirección
- **Proveedores**: Nombre, RUC/NIF, email, dirección  
- **Productos**: Código, nombre, precio, stock, impuestos
- **Facturas**: Items, cálculos automáticos, estados

### ✅ **Facturación Inteligente**
- Formulario dinámico con cálculo en tiempo real
- Subtotal, IVA/Impuestos, Total automático
- Validaciones y controles de stock

### ✅ **Inteligencia Artificial**
- **Recomendaciones**: Productos sugeridos por cliente
- **Detección de Anomalías**: Score de riesgo y explicación
- Panel lateral con sugerencias en tiempo real

### ✅ **Reportes y Eventos**
- Generación de PDF (preparado para JasperReports)
- Eventos `InvoiceCreated` con RabbitMQ
- Descarga directa desde frontend

## 🚀 Instalación y Ejecución

### Prerrequisitos
- Docker & Docker Compose
- Java 21 (para desarrollo local)
- Node.js 18+ (para desarrollo local)
- Python 3.11+ (para desarrollo local)

### 🐳 **Ejecución Completa con Docker**
```bash
# Clonar repositorio
git clone <repository-url>
cd core-microbill-challenge

# Levantar toda la infraestructura
docker-compose up -d

# Verificar servicios
docker-compose ps
```

### 🔧 **Desarrollo Local**
```bash
# 1. Infraestructura
docker-compose up -d mysql rabbitmq

# 2. Backend Services
cd backend/microbill-authentication-service && mvn spring-boot:run
cd backend/microbill-management-service && mvn spring-boot:run  
cd backend/microbill-billing-service && mvn spring-boot:run

# 3. AI Service (Python)
cd backend/microbill-billing-ia
pip install -r requirements.txt
python main.py

# 4. Frontend (Angular)
cd frontend
npm install
npm start
```

## 🌐 **Acceso a Servicios**

| Servicio | URL | Descripción |
|----------|-----|-------------|
| **Frontend** | http://localhost:4200 | Aplicación Angular |
| **Auth API** | http://localhost:8081swagger-ui/index.html | Autenticación |
| **Management API** | http://localhost:8082swagger-ui/index.html | Gestión CRUD |
| **Billing API** | http://localhost:8083swagger-ui/index.html | Facturación |
| **AI API** | http://localhost:8084/docs | Inteligencia Artificial |
| **RabbitMQ** | http://localhost:15672 | Management UI |

## 📋 **Endpoints Principales**

### 🔐 Autenticación
```
POST /api/auth/login → JWT Token
```

### 📊 Gestión
```
GET/POST/PUT/DELETE /api/customers
GET/POST/PUT/DELETE /api/providers  
GET/POST/PUT/DELETE /api/products
```

### 🧾 Facturación
```
GET/POST/PUT/DELETE /api/invoices
POST /api/invoices/{id}/generate-report → PDF
```

### 🤖 Inteligencia Artificial
```
POST /api/ai/recommendations?customerId={id} → Productos sugeridos
POST /api/ai/anomaly-score → Detección de anomalías
```

## 🎯 **Flujo de Usuario**

1. **Login** → Autenticación con JWT
2. **Dashboard** → Vista general del sistema
3. **Gestión** → CRUD de Customers, Providers, Products
4. **Facturación** → Crear facturas con IA integrada
   - Seleccionar cliente y proveedor
   - Agregar productos (con recomendaciones IA)
   - Cálculo automático de totales
   - Detección de anomalías en tiempo real
   - Generar y descargar PDF
5. **Reportes** → Visualización y descarga de documentos

## 🧠 **Características de IA**

### Recomendaciones Inteligentes
- Análisis del historial de compras
- Sugerencias personalizadas por cliente
- Integración en tiempo real en el formulario

### Detección de Anomalías
- Score de riesgo (0-100%)
- Análisis de patrones atípicos

## 🏛️ **Arquitectura Técnica**

### Backend (Hexagonal + Contract-First)
- **Dominio**: Entidades con lógica de negocio
- **Puertos**: Interfaces para casos de uso
- **Adaptadores**: Implementaciones (REST, JPA, RabbitMQ)
- **OpenAPI**: Generación automática de DTOs

### Frontend (Angular + PrimeNG)
- **Lazy Loading**: Módulos bajo demanda
- **Reactive Forms**: Formularios reactivos
- **Guards**: Protección de rutas
- **Interceptors**: Manejo automático de JWT
- **Services**: Comunicación con microservicios

### Comunicación
- **HTTP REST**: Entre frontend y backend
- **RabbitMQ**: Eventos asíncronos
- **JWT**: Autenticación stateless

## 📦 **Tecnologías**

### Backend
- Java 21, Spring Boot 3.5.8
- MySQL 9.4, JPA/Hibernate
- RabbitMQ, JWT
- MapStruct, Lombok
- JasperReports, OpenAPI

### Frontend  
- Angular 21, TypeScript
- PrimeNG, RxJS
- Reactive Forms, Guards
- Docker, Nginx

### AI & DevOps
- Python 3.11, FastAPI
- NumPy, Pandas, Scikit-learn
- Docker Compose
- MySQL, RabbitMQ

## 🎨 **Capturas de Pantalla**

### Login
![Login](entregables/img/login.png)

### Dashboard
![Dashboard](entregables/img/dashboard.png)

### Gestión de Clientes
![Customers](entregables/img/customers.png)

### Gestión de Proveedores
![Providers](entregables/img/providers.png)

### Gestión de Productos
![Products](entregables/img/products.png)

### Lista de Facturas
![Invoices](entregables/img/invoices.png)

### Formulario de Nueva Factura
![New Invoice](entregables/img/new-invoice.png)

### Reportes y Gestión Avanzada
![Reports](entregables/img/reports.png)

## 📁 **Recursos Adicionales**

### Colección de Postman
- **Archivo**: `Microbill_API_Collection.postman_collection.json`
- **Descripción**: Colección completa para probar todos los endpoints
- **Incluye**: Autenticación, CRUD completo, IA, y casos de prueba
- **Variables**: Configuración automática de tokens y IDs

### Importar en Postman
1. Abrir Postman
2. Importar → Seleccionar archivo `Microbill_API_Collection.postman_collection.json`
3. Configurar variables de entorno si es necesario
4. Ejecutar requests en orden (Login primero)

## 📈 **Próximas Mejoras**

- [ ] CI/CD con GitHub Actions
- [ ] Observabilidad con Prometheus/Grafana
- [ ] Notificaciones push


---
