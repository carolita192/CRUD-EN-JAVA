@echo off
chcp 65001 > nul
title Sistema de Transporte y Paquetería - db_paqueteria
echo ========================================================
echo INICIANDO SISTEMA DE TRANSPORTE Y PAQUETERÍA
echo ========================================================
echo Verificando base de datos MySQL...
echo Recuerda tener encendido MySQL (XAMPP o Servicio MySQL).
echo.
java -cp "bin;lib/*" com.transporte.Main
pause
