--http://aprendehaskell.es/content/Modulos.html

module Dibujo (Dibujo,figura,
              rotar,espejar,encimar, juntar, apilar,
              rot45,r90,r180,r270,
              (^^^),(.-.),(///),comp, --combinadores
              cuarteto,encimar4,ciclar,change,foldDib,mapDib) where

-- <Dibujo> ::= Figura <Fig> | Rotar <Dibujo> | Espejar <Dibujo> 
--    | Rot45 <Dibujo>
--    | Apilar <Float> <Float> <Dibujo> <Dibujo> 
--    | Juntar <Float> <Float> <Dibujo> <Dibujo> 
--    | Encimar <Dibujo> <Dibujo>

-- nuestro lenguaje 
data Dibujo a = Figura a 
              |Rotar (Dibujo a) 
              |Espejar (Dibujo a) 
              |Rot45 (Dibujo a) 
              |Apilar Float Float (Dibujo a) (Dibujo a)
              |Juntar Float Float (Dibujo a) (Dibujo a)
              |Encimar (Dibujo a) (Dibujo a) 
              deriving(Eq,Show)



-- combinadores
infixr 6 ^^^

infixr 7 .-.

infixr 8 ///

--funcion que compone funciones
comp :: Int -> (a -> a) -> a -> a
comp 1 f = f
comp n f = comp (n-1) f . f


-- Funciones constructoras (Abstraccion de los constructores)
figura :: a -> Dibujo a
figura = Figura

encimar :: Dibujo a -> Dibujo a -> Dibujo a
encimar = Encimar

apilar :: Float -> Float -> Dibujo a -> Dibujo a -> Dibujo a
apilar = Apilar

juntar  :: Float -> Float -> Dibujo a -> Dibujo a -> Dibujo a
juntar = Juntar

rot45 :: Dibujo a -> Dibujo a
rot45 = Rot45

rotar :: Dibujo a -> Dibujo a
rotar = Rotar


espejar :: Dibujo a -> Dibujo a
espejar = Espejar

--combinadores

-- Superpone un dibujo con otro.
(^^^) :: Dibujo a -> Dibujo a -> Dibujo a
(^^^) = encimar

-- Pone el primer dibujo arriba del segundo, ambos ocupan el mismo espacio.
(.-.) :: Dibujo a -> Dibujo a -> Dibujo a
(.-.) = apilar 1 1

-- Pone un dibujo al lado del otro, ambos ocupan el mismo espacio.
(///) :: Dibujo a -> Dibujo a -> Dibujo a
(///) = juntar 1 1

-- rotaciones
r90 :: Dibujo a -> Dibujo a
r90 = rotar

r180 :: Dibujo a -> Dibujo a
r180 = comp 2 rotar

r270 :: Dibujo a -> Dibujo a
r270 = comp 3 rotar

-- una figura repetida con las cuatro rotaciones, superimpuestas.
encimar4 :: Dibujo a -> Dibujo a
encimar4 f = (^^^) (r270 f) $ (^^^) (r180 f) $ (^^^) (rotar f) f
-- f $ g = f(g)  => encimar4 f = (^^^) (r270 f) ( (^^^) (r180 f) ( (^^^) (rotar f) f)

-- cuatro figuras en un cuadrante.
cuarteto :: Dibujo a -> Dibujo a -> Dibujo a -> Dibujo a -> Dibujo a
cuarteto a b c d = (.-.) ((///) a b) ((///) c d) 

-- un cuarteto donde se repite la imagen, rotada (¡No confundir con encimar4!)
ciclar :: Dibujo a -> Dibujo a
ciclar f =  (.-.) ((///) f (rotar f) ) ((///) (r180 f) (r270 f))

-- map para nuestro lenguaje
mapDib :: (a -> b) -> Dibujo a -> Dibujo b
mapDib f (Figura a) = Figura (f a)
mapDib f (Rotar a) = Rotar (mapDib f a)
mapDib f (Espejar a) = Espejar (mapDib f a)
mapDib f (Rot45 a) = Rot45 (mapDib f a)
mapDib f (Apilar n1 n2 a b) = Apilar n1 n2 (mapDib f a) (mapDib f b)
mapDib f (Juntar n1 n2 a b) = Juntar n1 n2 (mapDib f a) (mapDib f b)
mapDib f (Encimar a b) = Encimar (mapDib f a) (mapDib f b)



-- verificar que las operaciones satisfagan
-- 1. map figura = id
-- 2. map (g . f) = mapDib g . mapDib f

-- Cambiar todas las básicas de acuerdo a la función.
change :: (a -> Dibujo b) -> Dibujo a -> Dibujo b
change f (Figura a) = f a
change f (Rotar a) = Rotar (change f a)
change f (Espejar a) = Espejar (change f a)
change f (Rot45 a) = Rot45 (change f a)
change f (Apilar n1 n2 a b) = Apilar n1 n2 (change f a) (change f b)
change f (Juntar n1 n2 a b) = Juntar n1 n2 (change f a) (change f b)
change f (Encimar a b) = Encimar (change f a) (change f b)

-- Principio de recursión para Dibujos. 
foldDib ::
  (a -> b) ->
  (b -> b) ->
  (b -> b) ->
  (b -> b) ->
  (Float -> Float -> b -> b -> b) ->
  (Float -> Float -> b -> b -> b) ->
  (b -> b -> b) ->
  Dibujo a ->
  b
foldDib f_figura _ _ _ _ _ _ (Figura a) = f_figura a
foldDib f_figura f_rotar f_espejar f_rot45 f_apilar f_juntar f_encimar (Rotar a) = f_rotar (foldDib f_figura f_rotar f_espejar f_rot45 f_apilar f_juntar f_encimar a)
foldDib f_figura f_rotar f_espejar f_rot45 f_apilar f_juntar f_encimar (Espejar a) = f_espejar (foldDib f_figura f_rotar f_espejar f_rot45 f_apilar f_juntar f_encimar a)
foldDib f_figura f_rotar f_espejar f_rot45 f_apilar f_juntar f_encimar (Rot45 a) = f_rot45 (foldDib f_figura f_rotar f_espejar f_rot45 f_apilar f_juntar f_encimar a)
foldDib f_figura f_rotar f_espejar f_rot45 f_apilar f_juntar f_encimar (Apilar n1 n2 a b) = f_apilar n1 n2 (foldDib f_figura f_rotar f_espejar f_rot45 f_apilar f_juntar f_encimar a) (foldDib f_figura f_rotar f_espejar f_rot45 f_apilar f_juntar f_encimar b)
foldDib f_figura f_rotar f_espejar f_rot45 f_apilar f_juntar f_encimar (Juntar n1 n2 a b) = f_juntar n1 n2 (foldDib f_figura f_rotar f_espejar f_rot45 f_apilar f_juntar f_encimar a) (foldDib f_figura f_rotar f_espejar f_rot45 f_apilar f_juntar f_encimar b)
foldDib f_figura f_rotar f_espejar f_rot45 f_apilar f_juntar f_encimar (Encimar a b) = f_encimar (foldDib f_figura f_rotar f_espejar f_rot45 f_apilar f_juntar f_encimar a) (foldDib f_figura f_rotar f_espejar f_rot45 f_apilar f_juntar f_encimar b)



