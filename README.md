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

