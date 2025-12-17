# 🧾 Microbill - Sistema de Facturación Completo

Sistema de facturación empresarial con **arquitectura de microservicios**, **IA integrada** y **frontend Angular moderno**.

## 🏗️ Arquitectura de Microservicios

### 🔹 Servicios Backend (Java 21 + Spring Boot 3)
- **🔐 Auth Service** (8079): Autenticación JWT, usuarios, roles
- **📊 Management Service** (8081): CRUD Customers, Providers, Products  
- **🧾 Billing Service** (8080): Facturas, items, reportes PDF, eventos
- **🤖 AI Service** (8082): Python + FastAPI - Recomendaciones y detección de anomalías

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
| **Auth API** | http://localhost:8079/swagger-ui.html | Autenticación |
| **Management API** | http://localhost:8081/swagger-ui.html | Gestión CRUD |
| **Billing API** | http://localhost:8080/swagger-ui.html | Facturación |
| **AI API** | http://localhost:8082/docs | Inteligencia Artificial |
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
- Explicaciones detalladas
- Alertas visuales por nivel de riesgo

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

## 🧪 **Testing**

```bash
# Backend Tests
mvn test

# Frontend Tests  
npm test

# E2E Tests
npm run e2e
```

## 📦 **Tecnologías**

### Backend
- Java 21, Spring Boot 3.5.8
- MySQL 8.0, JPA/Hibernate
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
![Login](docs/screenshots/login.png)

### Dashboard con IA
![Dashboard](docs/screenshots/dashboard.png)

### Formulario de Factura
![Invoice Form](docs/screenshots/invoice-form.png)

## 📈 **Próximas Mejoras**

- [ ] Implementación completa de JasperReports
- [ ] Tests E2E con Cypress
- [ ] CI/CD con GitHub Actions
- [ ] Observabilidad con Prometheus/Grafana
- [ ] Notificaciones push
- [ ] Módulo de inventario avanzado

## 🤝 **Contribución**

1. Fork del proyecto
2. Crear feature branch
3. Commit cambios
4. Push al branch
5. Crear Pull Request

## 📄 **Licencia**

MIT License - ver [LICENSE](LICENSE) para detalles.

---

**Desarrollado con ❤️ usando arquitectura de microservicios, IA y las mejores prácticas de desarrollo.**