module Dibujos.Fibonacci where
    
import Dibujo (Dibujo, figura, juntar, apilar, rot45, rotar, r180, encimar, espejar)
import FloatingPic(Conf(..), Output, half, zero, invert)
import qualified Graphics.Gloss.Data.Point.Arithmetic as V
import Graphics.Gloss ( Picture, blue, red, black, color, line, pictures )

data Color = Azul | Rojo | Negro
    deriving (Show, Eq)

data BasicaSinColor = Rectangulo | Cruz | Triangulo | Efe | Cuadrado
    deriving (Show, Eq)

type Basica = (BasicaSinColor, Color)

colorear :: Color -> Picture -> Picture
colorear Azul = color blue
colorear Rojo = color red
colorear Negro = color black

interpBasicaSinColor :: Output BasicaSinColor
interpBasicaSinColor Cuadrado x y w = line [x, x V.+ y, x V.+ y V.+ w, x V.+ w, x]
interpBasicaSinColor Rectangulo x y w = line [x, x V.+ y, x V.+ y V.+ w, x V.+ w, x]
interpBasicaSinColor Cruz x y w = pictures [line [x, x V.+ y V.+ w], line [x V.+ y, x V.+ w]]
interpBasicaSinColor Triangulo x y w = line $ map (x V.+) [(0,0), y V.+ half w, w, (0,0)]
interpBasicaSinColor Efe x y w = line . map (x V.+) $ [
        zero,uX, p13, p33, p33 V.+ uY , p13 V.+ uY,
        uX V.+ 4 V.* uY ,uX V.+ 5 V.* uY, x4 V.+ y5,
        x4 V.+ 6 V.* uY, 6 V.* uY, zero
    ]
    where
        p33 = 3 V.* (uX V.+ uY)
        p13 = uX V.+ 3 V.* uY
        x4 = 4 V.* uX
        y5 = 5 V.* uY
        uX = (1/6) V.* y
        uY = (1/6) V.* w

interpBas :: Output Basica
interpBas (b, c) x y w = colorear c $ interpBasicaSinColor b x y w

fig :: BasicaSinColor -> Dibujo Basica
fig b = figura (b, Negro)

figR :: BasicaSinColor -> Dibujo Basica
figR b = figura (b, Rojo)

figA :: BasicaSinColor -> Dibujo Basica
figA b = figura (b, Azul)

fiboRec :: BasicaSinColor -> Int -> Int -> Dibujo Basica
fiboRec b 1 _ = (fig b)
fiboRec b 2 0 = apilar 1 1 (fig b) (fig b)
fiboRec b 2 1 = juntar 1 1 (fig b) (fig b)
fiboRec b n 0 = apilar 1.5 1 (figA b) (r180 (fiboRec b (n-1) 1))
fiboRec b n 1 = juntar 1.5 1 (figR b) (fiboRec b (n-1) 0)

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

testAll :: Dibujo Basica
testAll = grilla [[fiboRec Rectangulo 16 1]]


fibonacciConf :: Conf
fibonacciConf = Conf {
    name = "Fibonacci"
    , pic = testAll
    , bas = interpBas
}