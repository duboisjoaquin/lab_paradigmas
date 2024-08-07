module Main (main) where
import Pred
import Dibujo
import Test.HUnit

testPred :: Test
testPred = TestList [
    -- cambiar
    "Test cambiar" ~: cambiar (== 5) (const (figura 7)) (figura 5) ~?= figura 7,
    "Test cambiar" ~: cambiar (== 5) (const (figura 5)) (figura 5) ~?= figura 5,
    "Test cambiar" ~: cambiar (== True) (const (figura False)) (figura True) ~?= figura False,
    -- anyDib
    "Test anyDib" ~: anyDib (== 3) (figura 3) ~?= True,
    "Test anyDib" ~: anyDib (== 8) (figura 3) ~?= False,
    "Test anyDib" ~: anyDib (== 8) (rotar (figura 8)) ~?= True,
    -- allDib
    "Test allDib" ~: allDib (== 8) (figura 8) ~?= True,
    "Test allDib" ~: allDib (== 8) (figura 3) ~?= False,
    "Test allDib" ~: allDib (== 8) (encimar (rotar (figura 8)) (figura 8)) ~?= True,
    -- andP
    "Test andP" ~: andP (> 0) (< 5) 4 ~?= True,
    "Test andP" ~: andP (> 0) (< 5) 6 ~?= False,

    -- orP
    "Test orP" ~: orP (> 0) (> 5) 2 ~?= True,
    "Test orP" ~: orP (> 4) (> 5) 1  ~?= False
    ]

main :: IO ()
main = do
    putStrLn "Running Test Suite for Pred."
    runTestTTAndExit testPred