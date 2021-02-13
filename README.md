# Algoritmos 4 - FIUBA - TP3

En este trabajo se procede a construir un servicio REST utilizando Https4s.
El servicio expone un endpoint ```/score``` el cual recibe en body un ```InputRow```
que será utilizado para generar un score como respuesta luego de evaluar dicho input en un modelo entrenado previamente.
Para optimizar este request el proyecto consta con una cache.

## Prerrequisitos

Para correr el proyecto es neceario tener instalado SBT.

## Instrucciones

### Ejecución del programa

Para ejecutar el programa se debe corer:

```
sbt run
```

Puede probar la rest api a través de los siguientes endpoints:

```
localhost:8080/health-check
```
ó

```
localhost:8080/score
```
Para este segundo debe pasar en el body un json con la estructura correspondiente al InputRow definida en el archivo ```Models.scala```
A continuación se presenta un ejemplo del mismo:

```json
{"id" : 158,
 "date" : "2020-12-02T14:49:15.841609",
 "last" : 0.0,
 "close" : 148.0,
 "diff" : 0.0,
 "curr": "D",
 "unit" : "TONS",
 "dollarBN": 2.919,
 "dollarItau": 2.91,
 "wDiff": -148.0
}
```

### Output

Al hacer un ```GET``` al endpoint ```/health-check``` se retornará la siguiente iformación:

```json
{
    "version": "0.1",
    "maintainer": "Disfuncionales"
}
```

Si en cambio realizamos un ```POST``` al endpoint ```/score``` se retorna un json de la siguiente estructura:

```json
{
    "score": 180.0618351404948
}
```

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
--- services/ScoreService.scala # Coordina el proceso de ejecución para obtener el score.  
--- Cache.scala # Clase con utilidades para leer y guardar elementos en la BDD.
--- FiubaTransactor.scala # Crea el transactor para poder conectar con la BDD. 
--- Main.scala # Archivo principal de ejecución.
--- Scorer.scala # Levanta el archivo PMML y obtiene el score del row envíado en el request.
```

### Pipeline

El trabajo se compone de dos endpoints: 

```/health-check```

* Se recibe el request con un método ```GET``` y se devuelve un mensaje. 

```/score```

* Se recibe el request con un método ```POST``` y un body que contiene, en formato JSON, los valores de una row utilizada en el TP 2. 
* Se realiza un decode del JSON y se mapea a un objeto InputRow. 
* Ese objeto se hashea.
* Se chequea si ese hash existe en la BDD (Cache). 
    * En caso afirmativo, se devuelve el score asociado a ese valor de hash.
    * En caso negativo:
        * Se levanta el modelo PMML armado en el TP 2.
        * Se calcula el score con el InputRow obtenido del request.
        * Se guarda el valor en la BDD.
        * Se devuelve el score.

## Performance 

Al tomar el tiempo entre que se manda el request y se recibe el response, la primera vez, donde se debe calcular el score dado que no lo tiene en la BDD, se tarda ```0.505249827``` segundos. 

Cuando se vuelve a mandar el mismo request y el score se encuentra en la BDD, el tiempo que tarda es ```0.4858165``` segundos. 

Esto demuestra que agregar una cache no optimiza de manera significativa la obtención del score.