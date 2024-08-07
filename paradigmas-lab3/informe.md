# Informe 

## Como compilar y Correr el programa 

1. En nuestra repositorio, donde tenemos el makefile ejecutamos `$ mvn clean package` obteniendo asi una carpeta target
2. En la carpeta de SPARK_FOLDER/sbin  ./start-master.sh 
3. En la carpeta de SPARK_FOLDER/sbin  ./start-worker.sh spark://localhot:7077 -m 1G -c 1 (hicimos un cambio para que en vez de hacer una instancia de worker haga dos con ese comando) 
4. Para ello ejecutamos: `/<route>/SPARK_FOLDER/bin/spark-submit --class App --master spark://<user>:7077 /<route>/target/<exe>.jar <-flags> 2>/dev/null`

Donde:
* route = ruta absoluta al destino
* user = usuario de la computadora local
* exe = nombre del ejecutable
* flags = flags de impresion (implementacion de lab 2)
* 2>/dev/null = redireccion del output de spark para no llenar el stdout con informacion que no nos interesa una vez que todo esta en funcionamiento

## Metodo de trabajo 

Empezamos usando el esqueleto proporcionado por pagano para guiarnos a lo largo del desarollo del proyecto, el cual fue bastante desafiante como primera toma de contacto con un framework

Pero vamos a hacer un poco de contexto acerca de todo lo utilizado en este laboratorio 

 * Spark = Un framework dedidaco a distribuir tareas entre varias "computadoras" para dividir la carga de trabajo y luego reunir las partes cuando se finaliza el calculo 

 * Maven = Software dedicado a manejar dependencias usadas por el framework de manera automatica, hace uso de un archivo pom.xml donde estan explicitas las dependencias a usar en el proyecto. Hecho para facilitar el trabajo con spark (Aunque trae consigo varios errores a la hora de builder el proyecto)

 Para que usamos el framework? Una tarea puede ser realizada mucho mas eficientemente de una forma distribuida entre varias computadoras, en nuestro caso, cuando los archivos dejan de ser feeds y empiezan a ser gb de texto, una sola computadora no puede con la tarea 

Al pom proveido por la catedra le sumamos algunas dependencias que hacian falta para que corra con nuestro programa. 

Luego de eso corrimos con el comando del profe pagano y lo que vimos, fue unas cuantas (demasiadas para documentar la verdad) de errores momento de alterar el codigo fuente para adaptarlo para Spark

## Cambios al Codigo 

### Notas 

Notamos que todas las clases que forman parte de este espacio de Spark debian implementar la clase Serializable la cual era necesario porque a al hora de correr el programa, Spark debia ser capaz de serializar y distribuir objetos y contexto acerca de ellos para poder mandarlos por la red a los distintos trabajadores 

Otro cambio tambien mas general fue el hecho de la poca compatibilidad con json que tenia Spark, por lo que tuvimos que usar una libreria jackson que tambien hace uso de los json 


### App.java 
Empezamos desglosando desde App.java para ir metiendonos en todos las ramificaciones. Siguiendo el archivo principal del esqueleto notamos que en un momento se inicializaba un espacio de Spark 

SparkSession spark = SparkSession
      .builder()
      .appName("JavaWordCount")
      .getOrCreate();

y en las lineas despues de la creacion de este entorno era habitual crear variables con terminacion RDD para mostrar variables claves a la hora de llenar o mandar trabajo a las funciones que se desprenden de el App.java, por lo tanto hicimos eso 

Descartamos la estructura de articulos y pasamos la lista AllArticles a un .txt para usar el comando que usa el esqueleto para leer y guardar el texto en lineas como un JavaRDD 

y ahora habia llegado el momento de meternos en los metodos hechos para proveer encapsulamiento de la aplicacion principal

### ComputeHeuristic.java

La funcion encargada de extraer candidatos siguiendo la heuristica elegida y luego convertir esos candidatos en namedEntities 

Pero ahora habia recibido un JavaRDD String, por lo que nos vimos obligados a usar funciones proveidas por estos tipos, ya que no se puede iterar de forma convencional sobre ellos para aplicarle algo a cada una. Pero afortunadamente la funcion map y FlatMap fueron las indicadas para aplicarles un proceso a cada uno de los elementos de la lista 

Tuvimos que dropear nuestra forma de Set por una JavaRDD NamedEntity ya que este tipo no soporta la forma de Set, por lo que tambien conllevo a cambios en el algoritmo en si y no solo en los componentes en si 

Pero al final de la funcion el resultado era lo suficientemente parecido para no perder la estructura general del proposito de la funcion, devolver en este caso una lista de estas entidades nombradas como un tipo RDD 

### Heuristics 

Antes de clasificar nuestros candidatos como entidades nombradas, tenemos que convertir nuestro texto en una lista de candidatos que pasaron el pattern de la heuristica y ahora en vez de recibir nuestra lista de String recibimos una lista de JavaRDD String 

De nuevo usamos la funcion map para cada elemento de la lista 


### Config y UserInterface 

Ademas de pasarle un feedkey decidimos que tambien era pertinente que se le quiera pasar un filepath asi un archivo txt con mucha info (como el dump de wikipedia pasado por el profesor M.Pagano)
Asi que hicimos lo propio, agregamos la key -gf y hicimos los chequeos propios para que no se trate de usar los articles y un txt al mismo tiempo 

## Errores 

Mientras compilabamos nos encontramos con varios errores (mas de los que pensabamos la verdad) estos son algunas de ellos 

* "NoSuchFileException src/main/java/data/feeds.json" = No sabemos enteramente porque ocurria este problema, pero se soluciono cambiando el path hacia la carpeta target despues de hacer el mvn install 

* "Type ´json´ not serializable" = tuvimos que cambiar el tipo json por jackson ya que este tipo que como es una libreria no se podia alterar sus implements, eso significaba que teniamos que si o si cambiar por un objeto que se deje serializar


## Resultados 

![Specs](/imgs/neofetch.png)

Luego de los cambios en el codigo era hora de testear

2 Workers 

1 feedkey

        El retardo con el spark fue de 25 a 28 

2 feedKeys (lmgral, lmnoti) 
      El retardo con el spark fue de cerca de 25 a 32 segs

BigDatatxt (quijote)

        Sorprendetemente tarda entre 26 a 32 seg 

BigdataTxt (dumpWiki)

        No pudimos correrlo sin que se muera la pc de uno de los compañeros 😦 

4 Workers 

1 feedkey

        El retardo con el spark fue de 28 a 33 

2 feedKeys (lmgral, lmnoti) 
      El retardo con el spark fue de cerca de 28 a 31 segs

BigDatatxt (quijote)

        Sorprendetemente tarda entre 24 a 29 seg 

BigdataTxt (dumpWiki)

        No pudimos correrlo sin que se muera la pc de uno de los compañeros 😦

        
![Ultimas horas de vida](/imgs/image.png) 

Sin entrar en tantos detalles, se nota que el rendimiento es peor para archivo
 chicos y mas workers, probablemente por el header que conlleva distribuir 
la data entre mas workers que no sale rentable a comparacion
de la computacion que se tiene que hacer

## Conclusion

Siguiendo el hilo de los resultados podemos sacar algunas conclusiones, el header de correr el programa con spark es un tiempo constante el cual vamos a llamar *spark_time* 
el retardo de nuestro programa corriendolo localmente como en el lab2 es *local_run* el cual es proporcional a la cantidad de palabras que tenga la big data que estamos tratando, falta un ultimo tiempo que es el tiempo que tarda los workers en realizar el trabajo que se va a llamar *workers_run* (este tiempo es proporcional a la cantidad de workers) 

Con todo esto podemos armar algo cercano a una ecuacion de tiempo teniendo a n como tamaño de texto 

* lab2 para archivos chicos = *local_run* * n

* lab3 para archivos chichos = *spark_time* + *workers_run* * n/(variable proporcioanl a cantidad de workers)

Ese tiempo costante del spark_time hace que el trabajo demore mas que el lab2 porque aunque el tiempo actual de trabajo es menor ese header por la preparacion y funcionamiento de spark lo hace mas lento 

* lab2 para archivos grandes = *local_run* * n^3

* lab3 para archivos chichos = *spark_time* + *workers_run* * n^3/(variable proporcioanl a cantidad de workers)


y en este caso tenemos un archivo considerablemente mas grande, donde nuestro overhead de spark sigue constante pero el actual trabajo es mucho menor en comparacion, ya que la computacion se divide en los workers, en constraste el lab2 solo tiene una maquina para computar lo que hace que el tiempo se expanda muchisimo mas 
