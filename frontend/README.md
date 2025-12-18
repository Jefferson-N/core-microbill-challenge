# 🎨 Microbill Frontend - Angular 21

Frontend de la aplicación de facturación con arquitectura de microservicios.

## 🚀 Configuración y Ejecución

### Prerrequisitos
- Node.js 18+
- npm 10+
- Angular CLI 21+

### Instalación
```bash
npm install
```

### Desarrollo
```bash
# Servidor de desarrollo (puerto 4200)
npm start

# Con proxy para microservicios
npm run serve:proxy

# Modo desarrollo específico
npm run start:dev
```

### Producción
```bash
# Build para producción
npm run build:prod

# Servidor con configuración de producción
npm run start:prod
```

### Testing
```bash
# Ejecutar tests unitarios
npm test

# Tests en modo watch
npm run test:watch
```

## 🔧 Configuración de Microservicios

### URLs de Servicios (Development)
- **Auth Service**: http://localhost:8079/api
- **Management Service**: http://localhost:8081/api  
- **Billing Service**: http://localhost:8080/api
- **AI Service**: http://localhost:8082/api

### Proxy Configuration
El archivo `proxy.conf.json` redirige las llamadas API a los microservicios correspondientes durante el desarrollo.

## 📁 Estructura del Proyecto

```
src/app/
├── auth/                 # Autenticación y login
├── core/                 # Servicios centralizados
│   └── services/         # BillingService, ManagementService
├── dashboard/            # Componente principal
├── clients/              # Gestión de clientes
├── suppliers/            # Gestión de proveedores  
├── products/             # Gestión de productos
├── invoice/              # Facturas y formulario con IA
└── reports/              # Reportes PDF
```

## 🎯 Funcionalidades

- ✅ **Login JWT** - Autenticación segura
- ✅ **CRUD Completo** - Clientes, Proveedores, Productos
- ✅ **Facturación Inteligente** - Con IA integrada
- ✅ **Reportes PDF** - Generación y descarga
- ✅ **Responsive Design** - PrimeNG + Angular Material

## 🔐 Usuarios Demo

- **Admin**: `admin@demo.com` / `Admin#123`
- **User**: `user@demo.com` / `User#123`

## 🛠️ Tecnologías

- **Angular 21** - Framework principal
- **PrimeNG** - Componentes UI
- **RxJS** - Programación reactiva
- **TypeScript** - Lenguaje tipado
- **SCSS** - Estilos avanzados