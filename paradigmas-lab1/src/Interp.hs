module Interp
  ( interp,
    initial,
    ov
  )
where

import Dibujo
import FloatingPic
import Graphics.Gloss (Display (InWindow), color, display, makeColorI, pictures, translate, white, Picture)
import qualified Graphics.Gloss.Data.Point.Arithmetic as V

-- Dada una computación que construye una configuración, mostramos por
-- pantalla la figura de la misma de acuerdo a la interpretación para
-- las figuras básicas. Permitimos una computación para poder leer
-- archivos, tomar argumentos, etc.
initial :: Conf -> Float -> IO ()
initial (Conf n dib intBas) size = display win white $ withGrid fig size
  where
    win = InWindow n (ceiling size, ceiling size) (0, 0)
    fig = interp intBas dib (0, 0) (size, 0) (0, size)
    desp = -(size / 2)
    withGrid p x = translate desp desp $ pictures [p, color grey $ grid (ceiling $ size / 10) (0, 0) x 10]
    grey = makeColorI 100 100 100 100

-- Interpretación de (^^^)
ov :: Picture -> Picture -> Picture
ov p q = pictures [p, q]

--rot45(f)(d, w, h)   f(d+(w+h)/2, (w+h)/2, (h-w)/2)
r45 :: FloatingPic -> FloatingPic
r45 f d w h = f (d V.+ half (w V.+ h)) (half (w V.+ h)) (half (h V.- w))

--rotar(f)(d, w, h) f(d+w, h, -w)
rot :: FloatingPic -> FloatingPic
rot f d w h = f (d V.+ w) h (zero V.- w) 

--espejar(f)(d, w, h) f(d+w, -w, h)
esp :: FloatingPic -> FloatingPic
esp f d w h = f (d V.+ w) (zero V.- w) h 


sup :: FloatingPic -> FloatingPic -> FloatingPic
sup f g d w h = ov (f d w h) (g d w h)

--juntar(m, n, f, g)(d, w, h)
--f(x, w', h) ∪ g(d+w', r'*w, h) con r'=n/(m+n), r=m/(m+n), w'=r*w
jun :: Float -> Float -> FloatingPic -> FloatingPic -> FloatingPic
jun m n f g d w h = ov  (f d (m/(m+n) V.* w) h) 
                        (g (d V.+ (m/(m+n) V.* w)) (n/(m+n) V.* w) h)


--apilar(m, n, f, g)(d, w, h)
--f(d + h', w, r*h) ∪ g(d, w, h') 
--con r' = n/(m+n), r=m/(m+n), h'=r'*h
api :: Float -> Float -> FloatingPic -> FloatingPic -> FloatingPic
api m n f g d w h = ov  (f (d V.+ ((n/(m+n)) V.* h) ) w (m/(m+n) V.* h)) 
                        (g d w (n/(m+n) V.* h))


interp :: Output a -> Output (Dibujo a)
interp basica = foldDib basica rot esp r45 api jun sup