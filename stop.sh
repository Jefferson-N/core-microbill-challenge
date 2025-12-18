#!/bin/bash

# ===========================================
# MICROBILL - SCRIPT DE PARADA
# ===========================================

echo "🛑 Deteniendo Microbill - Sistema de Facturación"
echo "================================================"

# Detener todos los servicios
echo "⏹️  Deteniendo servicios..."
docker-compose down

# Opcional: Limpiar volúmenes (descomente si desea eliminar datos)
# echo "🗑️  Eliminando volúmenes de datos..."
# docker-compose down -v

# Opcional: Limpiar imágenes (descomente si desea eliminar imágenes)
# echo "🧹 Limpiando imágenes..."
# docker system prune -f

echo "✅ Microbill detenido exitosamente"