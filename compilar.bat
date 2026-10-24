@echo off
chcp 65001 > nul
title Compilando Sistema de Transporte y Paquetería
echo ========================================================
echo COMPILANDO PROYECTO JAVA (db_paqueteria)
echo ========================================================

if not exist bin mkdir bin

echo Compilando archivos fuente...
javac -encoding UTF-8 -cp "lib/*" -d bin src/com/transporte/*.java src/com/transporte/*/*.java

if %ERRORLEVEL% EQU 0 (
    echo.
    echo [OK] ¡Compilación exitosa! Archivos .class generados en la carpeta bin.
) else (
    echo.
    echo [ERROR] Ocurrió un error durante la compilación.
)
pause
