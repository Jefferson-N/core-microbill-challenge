@echo off
REM ===========================================
REM MICROBILL - SCRIPT DE PARADA
REM ===========================================

echo 🛑 Deteniendo Microbill - Sistema de Facturación
echo ================================================

REM Detener todos los servicios
echo ⏹️  Deteniendo servicios...
docker-compose down

REM Opcional: Limpiar volúmenes (descomente si desea eliminar datos)
REM echo 🗑️  Eliminando volúmenes de datos...
REM docker-compose down -v

REM Opcional: Limpiar imágenes (descomente si desea eliminar imágenes)
REM echo 🧹 Limpiando imágenes...
REM docker system prune -f

echo ✅ Microbill detenido exitosamente
pause