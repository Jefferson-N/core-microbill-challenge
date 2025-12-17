# 📌 CH Invoice Service – Frontend

## 🎯 Objetivo del ejercicio
Este proyecto corresponde al **frontend Angular** solicitado en el ejercicio.  
Está desarrollado con **Angular v22.20.0 (Node.js)** y expone una interfaz web para gestionar facturas, clientes, productos y proveedores.  
La integración con el backend (Spring Boot) se realiza vía servicios REST documentados con Swagger.

---
## 🔗 Repositorios relacionados
- **Backend(Java) Spring**: [ch-invoice-backend](https://github.com/Jefferson-N/ch-invoice-backend)
---
## 🚀 Funcionalidades implementadas
- **Login de usuario** con JWT para controlar acceso.  
- **Formulario de factura** con:
  - Datos de cliente  
  - Lista de productos  
  - Datos de proveedor  
  - Campos de venta: subTotal, IVA y total  
- **Operaciones CRUD** completas sobre facturas.  
- **Visualización de reportes PDF** generados por el backend.  
- **Integración con RabbitMQ (indirecta)**: el frontend consume los resultados de procesos asíncronos gestionados por el backend.  

👉 Caso implementado: al crear una factura, el frontend envía la solicitud al backend, que publica un evento en RabbitMQ y actualiza el stock.

---

# 📂 Arquitectura del Proyecto
Estructura modular siguiendo buenas prácticas de Angular:

- **auth/** → componentes y servicios de autenticación.
- **clients/** → gestión de clientes.
- **invoice/** → componentes de facturación (formulario, lista, detalle).
- **products/** → gestión de productos.
- **suppliers/** → gestión de proveedores.
- **shared/** → componentes, directivas y recursos compartidos.
- **core/** → servicios centrales, interceptores y utilidades comunes.

---

## 📑 Documentación de APIs
El frontend consume las APIs documentadas en el backend:  
- Swagger UI: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)  
- OpenAPI JSON: [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)

---

## 🐳 Ejecución con Docker

### 1. Configuración de entorno
Renombrar `.env.dist` a `.env` y configurar las variables:

```env
# Backend
# Backend
API_URL=http://localhost:8080/api/v1

# Aplicación
APP_VERSION=0.0.1
APP_PORT=4200
```
## 2. Archivo Dockerfile

```
# Etapa 1: Build de Angular
FROM node:22.20.0 AS build
WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . .
RUN npm run build --prod

# Etapa 2: Servir con Nginx
FROM nginx:alpine
COPY --from=build /app/dist/ch-invoice-frontend /usr/share/nginx/html
EXPOSE 80
```
---

### 3. Archivo docker-compose.yml
```
version: '3.8'

networks:
  frontend-net:
    driver: bridge

services:
  angular-app:
    build: .
    image: invoice-frontend:0.0.1
    container_name: invoice-frontend
    ports:
      - "${APP_PORT}:80"
    environment:
      API_URL: ${API_URL}
    networks:
      - frontend-net
```
---
# 🚀 Opción 1: Levantar con Docker
Requisitos previos
 - Docker instalado (versión 20+ recomendada).
 - Docker Compose para orquestar servicios.
 - Node.js v22.20.0 instalado en tu máquina (para compilar localmente si lo prefieres).
 - Comandos

Comandos

```bash
docker-compose up -d
Reiniciar servicio
```
```bash
docker-compose restart angular-app
```
---

# 📌 Valor Profesional
Este servicio frontend demuestra:

- Uso de Angular en un entorno real.  
- Integración con un backend Spring Boot vía REST.  
- Arquitectura modular y escalable.  
- Seguridad con JWT y control de acceso.  
- Despliegue profesional con Docker Compose.  

---

# 📌 Entrega
- Código en repositorio **Git**.  
- Scripts de configuración incluidos.  
- Documentación en este archivo `README.md`.  

---

# 📌 Conclusiones
El diseño del proyecto permite la **separación del frontend y backend en servicios independientes**, facilitando la escalabilidad y el mantenimiento.  
La arquitectura está preparada para evolucionar hacia un modelo de **microfrontends**, donde cada módulo pueda desplegarse de forma autónoma.  

---

# 📌 Futuras mejoras
- Integrar notificaciones en tiempo real (WebSockets).  
- Mejorar la experiencia de usuario con lazy loading y optimización de performance.  
- Separar módulos en microfrontends para mayor escalabilidad.  

---

# 📌 Contacto
2025 Jefferson-N. Todos los derechos reservados.

