module Dibujos.Fractal where

import Dibujo (Dibujo, figura, juntar, apilar, rot45, rotar, espejar, encimar4, cuarteto, r180, r270, (^^^))
import FloatingPic(Conf(..), Output, half)
import qualified Graphics.Gloss.Data.Point.Arithmetic as V
import Graphics.Gloss (Picture(Blank), line, polygon, pictures) 
import Dibujos.Feo (interpBas, Basica)

-- Les ponemos colorcitos para que no sea _tan_ feo
data Color = Azul | Rojo
    deriving (Show, Eq)


-- Supongamos que eligen.
type Escher = Bool

--Definicion de dibujo
dibVacio :: Dibujo Escher
dibVacio = figura False

-- El dibujo u.
dibujoU :: Dibujo Escher -> Dibujo Escher
dibujoU d = encimar4 (espejar (rot45 d))

-- El dibujo t.
dibujoT :: Dibujo Escher -> Dibujo Escher
dibujoT p = p ^^^ (dib1 ^^^ dib2)
    where
        dib1 = espejar (rot45 p)
        dib2 = r270 dib1

-- Esquina con nivel de detalle en base a la figura p.
esquina :: Int -> Dibujo Escher -> Dibujo Escher
esquina 1 p = cuarteto dibVacio dibVacio dibVacio (dibujoU p)
esquina n p = cuarteto corner side (rotar side) (dibujoU p)
    where
        corner = esquina (n-1) p
        side = lado (n-1) p


-- Lado con nivel de detalle.
lado :: Int -> Dibujo Escher -> Dibujo Escher
lado 1 p = cuarteto dibVacio dibVacio (rotar t) t  where t = dibujoT p
lado n p = cuarteto side side (rotar t) t
    where 
        t = dibujoT p
        side = lado(n-1) p


row :: [Dibujo a] -> Dibujo a
row [] = error "row: no puede ser vacío"    
row [d] = d
row (d:ds) = juntar 1 (fromIntegral $ length ds) d (row ds)

column :: [Dibujo a] -> Dibujo a
column [] = error "column: no puede ser vacío"
column [d] = d
column (d:ds) = apilar 1 (fromIntegral $ length ds) d (column ds)

grilla :: [[Dibujo a]] -> Dibujo a
grilla = column . map row


-- Por suerte no tenemos que poner el tipo!
noneto p q r s t u v w x = grilla [[p,q,r],
                                   [s,t,u],
                                   [v,w,x]]

-- El dibujo de fractal:
fractal :: Int -> Escher -> Dibujo Escher
fractal n dib = noneto (esquina n (figura dib)) (lado n (figura dib)) (r270(esquina n (figura dib)))
                 (rotar (lado n (figura dib))) (dibujoU (figura dib)) (r270 (lado n (figura dib)))
                 (rotar (esquina n (figura dib))) (r180(lado n (figura dib))) (r180(esquina n (figura dib)))

interpBasica :: Output Escher
interpBasica False _ _ _ = Blank
interpBasica True x y w = pictures [line [x, x V.+ y V.- w], line [x V.+ y, x V.+ w]]

fractalConf :: Conf
fractalConf = Conf {
    name = "Fractal",
    pic = fractal 5 True,
    bas = interpBasica
}