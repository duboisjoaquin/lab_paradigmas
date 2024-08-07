---
title: Laboratorio de Programación Orientada a Objetos
author: acá van sus nombres
---

El enunciado del laboratorio se encuentra en [este link](https://docs.google.com/document/d/1wLhuEOjhdLwgZ4rlW0AftgKD4QIPPx37Dzs--P1gIU4/edit#heading=h.xe9t6iq9fo58).

# 1. Tareas
Pueden usar esta checklist para indicar el avance.

## Verificación de que pueden hacer las cosas.
- [OK] Java 17 instalado. Deben poder compilar con `make` y correr con `make run` para obtener el mensaje de ayuda del programa.

## 1.1. Interfaz de usuario
- [OK] Estructurar opciones
- [OK] Construir el objeto de clase `Config`

## 1.2. FeedParser
- [OK] `class Article`
    - [OK] Atributos
    - [OK] Constructor
    - [OK] Método `print`
    - [OK] _Accessors_
- [OK?] `parseXML`

## 1.3. Entidades nombradas
- [OK] Pensar estructura y validarla con el docente
- [OK] Implementarla
- [OK] Extracción
    - [3/3] Implementación de heurísticas
- [OK] Clasificación
    - [OK] Por tópicos
    - [OK] Por categorías
- Estadísticas
    - [OK] Por tópicos
    - [OK] Por categorías
    - [OK] Impresión de estadísticas

## 1.4 Limpieza de código
- [OK] Pasar un formateador de código
- [OK] Revisar TODOs

# 2. Experiencia
Fue la primera vez que usamos java como lenguaje para realizar un proyecto, el cambio de entorno de un paradigma funcional a uno imperativo nos hizo pensar la forma de resolver problemas desde otro punto de vista, la eleccion de java fue muy buena para el proyecto, ya que la creacion de objetos para estructurar nuestra informacion extraida ayudaba mucho a el entendimiento del programa, manejar estas estructuras de datos era realmente simple y muy claro, ya que la creacion de un objeto de una clase te daba acceso a todos los metodos publicos desarollados en la clase, ademas de que la importacion de packages como carpetas para que se haga como una caja alrededor de nuestra clase hacia todo mas simple 
Realmente fue una buena experiencia que tuvimos con el segundo laboratorio, esperamos con ansias el tercer laboratorio y muchas gracias por leer 

ATTE copilots-buddies 


# 3. Preguntas
1. Explicar brevemente la estructura de datos elegida para las entidades nombradas.
2. Explicar brevemente cómo se implementaron las heurísticas de extracción.


1. Elegimos crear una super clase NamedEntity que va a ser padre de las respectivas clases Person, Location, Organization, Other. Nos parecio una buena solucion ya que viendolo del lado del subtipado las 4 clases nombradas pueden identificarse como entidades nombradas. Siguiendo la definicion de entidad nombrada que puede extenderse a cosas que no forman parte de nuestro diccionario 

NamedEntity
            |
            |--->Person 
            |--->Location
            |--->Organization
            |--->Other 


2. Siguiendo el ejemplo proveido por la catedra, creamos un lenguaje regular para ingresarlo como patron y tratar de matchearlo con palabras de los articulos extraidos 
    AllCapitalizedHeuristic = Heuristica principalmente hecha para obtener candidatos referentes a agrupaciones usando acronimos. EJ = CGT , VW etc 
    BrandHeuristic = Heuristica como dice el nombre, orientada a palabras qeu usan letras combinadas con palabras para hacer formaciones de empresas como 7-eleven 

# 4. Extras
Completar si hacen algo.