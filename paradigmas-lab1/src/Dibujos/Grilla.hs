module Dibujos.Grilla where
import FloatingPic(Conf(..), Output)
import Dibujo (Dibujo, juntar, apilar, figura)
import Graphics.Gloss

type Basica = (Int , Int)

dibujoItem :: Int -> Int -> Dibujo Basica
dibujoItem a b = figura (a,b)


interpBas:: Output Basica 
interpBas gridItem (x, y) (w,_) (_,h) = translate xPos yPos texto     
    where         
        scaleTam = 0.15         
        xPos = x + w/4         
        yPos = y + h/4         
        texto = scale scaleTam scaleTam $ text $ show gridItem

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
testAll = grilla [[dibujoItem x y | y <- [0..7]] | x <- [0..7]]

-- texto :: int -> int -> Dibujo Basica
-- texto i j = figura i j

-- str :: int -> int -> String
-- str i j = show i ++ " " ++ show j


-- testAll :: Dibujo Basica
-- testAll = grilla [
--    [texto 0 0, texto 0 1, texto 0 0, texto 0 0, texto 0 0, texto 0 0, texto 0 0, texto 0 0],
--    [texto 0 0, texto 0 0, texto 0 0, texto 0 0, texto 0 0, texto 0 0, texto 0 0, texto 0 0],
--    [texto 0 0, texto 0 0, texto 0 0, texto 0 0, texto 0 0, texto 0 0, texto 0 0, texto 0 0],
--    [texto 0 0, texto 0 0, texto 0 0, texto 0 0, texto 0 0, texto 0 0, texto 0 0, texto 0 0]
--    ]

grillaConf :: Conf
grillaConf = Conf {
    name = "Grilla"
    , pic = testAll
    , bas = interpBas
}