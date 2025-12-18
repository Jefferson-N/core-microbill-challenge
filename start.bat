@echo off
REM ===========================================
REM MICROBILL - SCRIPT DE INICIO COMPLETO
REM ===========================================

echo 🚀 Iniciando Microbill - Sistema de Facturación
echo ================================================

REM Verificar si Docker está ejecutándose
docker info >nul 2>&1
if %errorlevel% neq 0 (
    echo ❌ Error: Docker no está ejecutándose
    echo    Por favor, inicie Docker Desktop y vuelva a intentar
    pause
    exit /b 1
)

REM Verificar si existe el archivo .env
if not exist .env (
    echo ⚠️  Archivo .env no encontrado
    echo    Copiando .env.example como .env...
    copy .env.example .env
    echo ✅ Archivo .env creado. Revise y ajuste las configuraciones si es necesario.
)

REM Limpiar contenedores anteriores
echo 🧹 Limpiando contenedores anteriores...
docker-compose down --remove-orphans

REM Construir e iniciar todos los servicios
echo 🔨 Construyendo e iniciando servicios...
docker-compose up --build -d

REM Esperar a que los servicios estén listos
echo ⏳ Esperando a que los servicios estén listos...
timeout /t 30 /nobreak >nul

REM Verificar estado de los servicios
echo 📊 Estado de los servicios:
docker-compose ps

echo.
echo 🎉 ¡Microbill iniciado exitosamente!
echo ================================================
echo 📱 Frontend:           http://localhost:4200
echo 🔐 Auth Service:       http://localhost:8081/swagger-ui.html
echo 📊 Management Service: http://localhost:8082/swagger-ui.html
echo 🧾 Billing Service:    http://localhost:8083/swagger-ui.html
echo 🤖 AI Service:         http://localhost:8084/docs
echo 🐰 RabbitMQ:           http://localhost:15672
echo 🗄️  MySQL:             localhost:3306
echo.
echo 👤 Usuarios demo:
echo    Admin: admin@demo.com / Admin#123
echo    User:  user@demo.com / User#123
echo.
echo 🛑 Para detener: docker-compose down
echo.
pause