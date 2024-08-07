---
title: Laboratorio de Funcional
author: Dubois Joaquin, Ricci Mateo, Miranda Santiago
---
La consigna del laboratorio está en https://tinyurl.com/funcional-2024-famaf

# 1. Tareas
Pueden usar esta checklist para indicar el avance.

## Verificación de que pueden hacer las cosas.
- [x] Haskell instalado y testeos provistos funcionando. (En Install.md están las instrucciones para instalar.)

## 1.1. Lenguaje
- [x] Módulo `Dibujo.hs` con el tipo `Dibujo` y combinadores. Puntos 1 a 3 de la consigna.
- [x] Definición de funciones (esquemas) para la manipulación de dibujos.
- [x] Módulo `Pred.hs`. Punto extra si definen predicados para transformaciones innecesarias (por ejemplo, espejar dos veces es la identidad).

## 1.2. Interpretación geométrica
- [x] Módulo `Interp.hs`.

## 1.3. Expresión artística (Utilizar el lenguaje)
- [x] El dibujo de `Dibujos/Feo.hs` se ve lindo.
- [x] Módulo `Dibujos/Grilla.hs`.
- [x] Módulo `Dibujos/Escher.hs`.
- [x] Listado de dibujos en `Main.hs`.

## 1.4 Tests
- [x] Tests para `Dibujo.hs`.
- [x] Tests para `Pred.hs`.

# 2. Experiencia
La distribucion de las tareas dada por la consigna ayudo mucho a que la desenvoltura del trabajo sea fluida y a su vez entender bien que hacia cada parte del proyecto 

Aprendimos mucho sobre el lenguaje funcional elegido para el lab y nos ayudo a pensar la composicion de codigo de otro punto de vista (el cual normalmente es una vista mas de paradigma imperativo) lo cual fue frustrante al comienzo pero recompensador al terminar el laboratorio 

Creemos que es importante buscar ese punto de vista diferente a la hora de encarar un problema ademas de agilizar la busqueda de informacion o documentacion en internet y este lab fue la combinacion perfecta para ejercitar estos dos fundamentos

# 3. Preguntas
Al responder tranformar cada pregunta en una subsección para que sea más fácil de leer.

1. ¿Que funcion cumple cada modulo y porque no seria una buena idea poner todo junto?

    Cada modulo maneja su propio nivel de abstraccion, unos dependiendo de otros para el uso de sintaxis y algunos dependiendo de funciones de otros modulos para facilitar o hacer mas transparente el funcionamiento de todo el codigo en general.

    * `Dibujo.hs`: Uno de los pilares a la hora desarollar nuestros dibujos, no importa ninguna funcion de otro archivo sino que exporta la totalidad de las suyas, define el tipo *Dibujo* que contiene la estructura basica de sintaxis que se mantiene durante todo el laboratorio, ademas de eso define funciones que manejan predicados que usan el lenguaje definido por *Dibujo* (`foldDib`, `mapDib` etc..)
    * `Pred.hs`: Este archivo esta pensado para desarollar funciones que comprueben cosas acerca de las Figuras Basicas cuando se les pasa un predicado, ademas de esto define el tipo *Pred a* como una funcion que toma el tipo polimorfico a y devuelve un bool, exportando este tipo para poder usarlo en el header de algunas funciones en otros archivos 
    * `Interp.hs`: Es la interpretacion en vectores de los tipos definidos en `Dibujo.hs`. En `Dibujo.hs` se podia definir un predicado de la forma `Encimar( (Rotar ( Figura Triangulo) ) Figura Cuadrado)`, siendo esto solo una forma de describir la figura que deberia mostrarse por pantalla, pero en el archivo `Interp.hs` efectivamente bajamos hasta el nivel de abstraccion mas bajo y manejamos los vectores que van a hacer dependiendo de los constructores que tenga el dibujo una serie de cambios en los vectores que componen el dibujo en si 
    * `FloatingPic.hs`: La definicion de este archivo es mas que nada para ayudar a que el manejo de vectores en Interp.hs sea mas visible, exportando funciones como `half()`. Y principalmente definiendo el tipo *FloatingPic* (que es una funcion que toma 3 vectores y devuelve una `Picture` -> tipo definido en Gloss) y el tipo *Output a* (que es una funcion que toma un tipo polimorfico a y devuelve una funcion del tipo *FloatingPic*)

2. ¿Por qué las figuras básicas no están incluidas en la definición del lenguaje, y en vez de eso, es un parámetro del tipo?

    Es simplemente una cuestion de flexibilidad, si estuvieran metidas a la fuerza en la definicion de tipo no podriamos utilizar nuestro tipo dibujo para otro proyecto que involucre utilizar otras basicas que no sean las que ya agregamos a la "fuerza" en la definicion de tipo.
    De esta forma dejamos mas camino para tanto facilitar el testeo como para cambiar el dibujo de manera mas facil  


3. ¿Qué ventaja tiene utilizar una función de `fold` sobre hacer pattern-matching directo?

    El hacer pattern-matching directo probablemente haria que la funcion sea mas entendible a simple vista, pero le sacaria demasiada poder a la funcion 
    ya que con `fold` al pasarle las funciones que quiero aplicar a cada constructor que tenga el dibujo puedo definir un monton de comportamientos sobre los cuales el patter matching tendria una sola funcionalidad 
    con fold se podria definir una suma de las figuras si usamos `Dibujo Int` o como lo usamos en el lab en `Pred.hs` donde usamos la funcion `fold` para ir desenvolviendo el dibujo y bifurcando el predicado cuando nos topamos con una funcion que toma dos basicas 



4. ¿Cuál es la diferencia entre los predicados definidos en Pred.hs y los tests?

    Los predicados de Pred.hs son mucho mas flexibles ya que son la definicion de una funcion que espera parametros para comprobar algo sobre las basicas del dibujo, en cambioa los test son casos independientes y aislados para comprobar que las funciones funcionen correctamente




# 4. Extras
 
Se agrego el dibujo Fractal, tomando como base el Dibujo escher, solo que con cruces.

Se agrego el dibujo Fibonacci el cual dibuja la estructura fibonacci con rectangulos terminando con dos rectangulos negros, en la funcion que se pasa para pic en Conf, hay un comentario que dice donde se puede cambiar el numero para que el dibujo haga una representacion mas grande o mas chica 
Ej: "fiboRec 12 1" hace la secuencia fibonacci de 12, si o si el numero de la secuencia tiene que ser > 0 y el tercer parametro tiene que empezar con 1 para que funcione de manera correcta, y este va a ser nuestro unico elemento en la grilla.
