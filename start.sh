#!/bin/bash

# ===========================================
# MICROBILL - SCRIPT DE INICIO COMPLETO
# ===========================================

echo "🚀 Iniciando Microbill - Sistema de Facturación"
echo "================================================"

# Verificar si Docker está ejecutándose
if ! docker info > /dev/null 2>&1; then
    echo "❌ Error: Docker no está ejecutándose"
    echo "   Por favor, inicie Docker Desktop y vuelva a intentar"
    exit 1
fi

# Verificar si existe el archivo .env
if [ ! -f .env ]; then
    echo "⚠️  Archivo .env no encontrado"
    echo "   Copiando .env.example como .env..."
    cp .env.example .env
    echo "✅ Archivo .env creado. Revise y ajuste las configuraciones si es necesario."
fi

# Limpiar contenedores anteriores (opcional)
echo "🧹 Limpiando contenedores anteriores..."
docker-compose down --remove-orphans

# Construir e iniciar todos los servicios
echo "🔨 Construyendo e iniciando servicios..."
docker-compose up --build -d

# Esperar a que los servicios estén listos
echo "⏳ Esperando a que los servicios estén listos..."
sleep 30

# Verificar estado de los servicios
echo "📊 Estado de los servicios:"
docker-compose ps

echo ""
echo "🎉 ¡Microbill iniciado exitosamente!"
echo "================================================"
echo "📱 Frontend:           http://localhost:4200"
echo "🔐 Auth Service:       http://localhost:8081/swagger-ui.html"
echo "📊 Management Service: http://localhost:8082/swagger-ui.html"
echo "🧾 Billing Service:    http://localhost:8083/swagger-ui.html"
echo "🤖 AI Service:         http://localhost:8084/docs"
echo "🐰 RabbitMQ:           http://localhost:15672"
echo "🗄️  MySQL:             localhost:3306"
echo ""
echo "👤 Usuarios demo:"
echo "   Admin: admin@demo.com / Admin#123"
echo "   User:  user@demo.com / User#123"
echo ""
echo "🛑 Para detener: docker-compose down"