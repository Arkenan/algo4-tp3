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

## Descripción de la Solución 

### Estructura del Proyecto

```bash
src/
- main/scala/edu.fiuba.fpfiuba43/
--- http/Fpfiuba43Routes.scala # Funciona como un Controller, armando los métodos de get y post.
--- http/Fpfiuba43Server.scala # Comunica las rutas con los servicios y crea el Cache y el Scorer.  
--- models/Models.scala # Modelo en Scala de un DataFrameRow
--- models/HealthCheckMessage.scala # Obtiene el mensaje que se envía cuando se realiza el request.
--- services/HealthCheck # Toma el mensaje de HealthCheckMessage y lo devuelve. 
--- services/ScoreCheck.scala # Coordina el proceso de ejecución para obtener el score.  
--- Cache.scala # Archivo con utilidades para leer y guardar elementos en la BDD.
--- FiubaTransactor.scala # Crea el transactor para poder conectar con la BDD. 
--- Main.scala # Archivo principal de ejecución.
--- Scorer # Levanta el archivo PMML y obtiene el score del row envíado en el request.
```

### Pipeline

El trabajo se compone de dos endpoints: 

__health-check__:

* Se recibe el request con un método GET y se devuelve un mensaje. 

__score__:

* Se recibe el request con un método POST y un body que contiene, en formato JSON, los valores de una row utilizada en el TP 2. 
* Se realiza un decode del JSON y se mapea a un objeto InputRow. 
* Ese objeto se hashea.
* Se chequea si ese hash existe en la BDD (Cache). 
    * En caso afirmativo, se devuelve el score asociado a ese valor de hash.
    * En caso negativo:
        * Se levanta el modelo PMML armado en el TP 2.
        * Se calcula el score con el InputRow obtenido del request.
        * Se guarda el valor en la BDD.
        * Se devuelve el score.

