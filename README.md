# Algoritmos 4 - FIUBA - TP3

En este trabajo se procede a construir un servicio REST utilizando Https4s.
El servicio expone un endpoint ```/score``` el cual recibe en body un ```InputRow```
que será utilizado para generar un score como respuesta luego de evaluar dicho input en un modelo entrenado previamente.
Para optimizar este request el proyecto consta con una cache.

## Prerrequisitos

Para correr el proyecto es neceario tener instalado SBT.

## Instrucciones

### Ejecución del programa

Por el momento el programa se ejecuta correctamente en IntelliJ idea, pero no en SBT por un problema de dependencias de
Spark.

El programa no acepta argumentos y muestra por pantalla la separación entre sets de entrenamiento y testing.

### Output
...
