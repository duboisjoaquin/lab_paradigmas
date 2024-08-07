module Main (main) where

import Test.HUnit
import Dibujo

-- Definimos algunas figuras para las pruebas
fig1, fig2, fig3, fig4 :: Dibujo String
fig1 = figura "fig1"
fig2 = figura "fig2"
fig3 = figura "fig3"
fig4 = figura "fig4"

figA :: Int -> Dibujo Int
figA = figura

-- Definimos las pruebas
testRotar :: Test
testRotar = TestCase (assertEqual "rotar fig1" (rotar fig1) (rotar fig1))

testEspejar :: Test
testEspejar = TestCase (assertEqual "espejar fig1" (espejar fig1) (espejar fig1))

testEncimar :: Test
testEncimar = TestCase (assertEqual "encimar fig1 fig2" (encimar fig1 fig2) (encimar fig1 fig2))

testApilar :: Test
testApilar = TestCase (assertEqual "apilar 1 1 fig1 fig2" (apilar 1 1 fig1 fig2) (apilar 1 1 fig1 fig2))

testJuntar :: Test
testJuntar = TestCase (assertEqual "juntar 1 1 fig1 fig2" (juntar 1 1 fig1 fig2) (juntar 1 1 fig1 fig2))

--Rotaciones por composicion

testR90 :: Test
testR90 = TestCase (assertEqual "r90 fig1" (r90 fig1) (rotar fig1))

testR180 :: Test
testR180 = TestCase (assertEqual "r180 fig1" (r180 fig1) (rotar (rotar fig1)))

testR270 :: Test
testR270 = TestCase (assertEqual "r270 fig1" (r270 fig1) (rotar (rotar (rotar fig1))))

-- Definimos las pruebas para los combinadores
testEncimarOp :: Test
testEncimarOp = TestCase (assertEqual "fig1 ^^^ fig2" (fig1 ^^^ fig2) (encimar fig1 fig2))

testApilarOp :: Test
testApilarOp = TestCase (assertEqual "fig1 .-. fig2" (fig1 .-. fig2) (apilar 1 1 fig1 fig2))

testJuntarOp :: Test
testJuntarOp = TestCase (assertEqual "fig1 /// fig2" (fig1 /// fig2) (juntar 1 1 fig1 fig2))

-- Definimos las pruebas para encimar4, cuarteto y ciclar

testEncimar4 :: Test
testEncimar4 = TestCase (assertEqual "encimar4 fig1" (encimar4 fig1) (encimar (r270(fig1)) (encimar (r180(fig1)) (encimar (rotar(fig1)) (fig1)))))

testCuarteto :: Test
testCuarteto = TestCase (assertEqual "cuarteto fig1" (cuarteto fig1 fig2 fig3 fig4) (apilar 1 1 (juntar 1 1 fig1 fig2) (juntar 1 1 fig3 fig4)))

testCiclar :: Test
testCiclar = TestCase (assertEqual "ciclar fig1" (ciclar fig1) (apilar 1 1 (juntar 1 1 fig1 (rotar fig1)) (juntar 1 1 (r180 fig1) (r270 fig1))))
--ciclar f =  (.-.) ((///) f (rotar f) ) ((///) (r180 f) (r270 f))

--Test para mapDib, change y FoldDib


-- Pruebas para mapDib
testMapDib :: Test
testMapDib = TestCase (assertEqual "mapDib (+1) (figA 1)" (mapDib (+1) (figA 1)) (figA 2))

-- -- Pruebas para change
testChange :: Test
testChange = TestCase (assertEqual "change (\\x -> figA (x+1)) (figA 1)" (change (\x -> figA (x+1)) (figA 1)) (figA 2))

-- -- Pruebas para foldDib
testFoldDib :: Test
testFoldDib = TestCase  (assertEqual "foldDib (+1) id id id (\\_ _ x y -> x + y) (\\_ _ x y -> x + y) (+) (apilar 1 1 (figA 1) (figA 2))"
                        (foldDib (+1) id id id (\_ _ x y -> x + y) (\_ _ x y -> x + y) (+) (apilar 1 1 (figA 1) (figA 2))) 5)

const1 :: a -> Int
const1 a = 1

testFoldDib2 :: Test
testFoldDib2 = TestCase  (assertEqual "foldDib const1 id id id (\\_ _ x y -> x + y) (\\_ _ x y -> x + y) (+) (apilar 1 1 (figA 1) (figA 2))"
                        (foldDib const1 id id id (\_ _ x y -> x + y) (\_ _ x y -> x + y) (+) (apilar 1 1 (figA 1) (figA 2))) 2)

-- Agrupamos las pruebas
tests :: Test
tests = TestList [testRotar, testEspejar, testEncimar, testApilar, testJuntar, testEncimarOp, testApilarOp, testJuntarOp, testR90, testR180, testR270, testMapDib, testChange,testFoldDib, testFoldDib2, testEncimar4, testCuarteto, testCiclar]

-- Función principal que ejecuta las pruebas
main :: IO ()
main = do
    putStrLn "Running Test Suite for Dibujos."
    runTestTTAndExit tests