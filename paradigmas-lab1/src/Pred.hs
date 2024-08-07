module Pred (
  Pred,
  cambiar, anyDib, allDib, orP, andP,falla
) where

--importo dibujos.hs
import Dibujo (Dibujo, change, foldDib,figura)


-- `Pred a` define un predicado sobre figuras básicas. Por ejemplo,
-- `(== Triangulo)` es un `Pred TriOCuat` que devuelve `True` cuando la
-- figura es `Triangulo`.
type Pred a = a -> Bool

-- Dado un predicado sobre figuras básicas, cambiar todas las que satisfacen
-- el predicado por el resultado de llamar a la función indicada por el
-- segundo argumento con dicha figura.
-- Por ejemplo, `cambiar (== Triangulo) (\x -> Rotar (Figura x))` rota
-- todos los triángulos.
cambiar :: Pred a -> (a -> Dibujo a) -> Dibujo a -> Dibujo a
cambiar pred_p f = change f_fig
  where
    f_fig a = if pred_p a then f a else figura a

-- Alguna básica satisface el predicado.
anyDib :: Pred a -> Dibujo a -> Bool
anyDib pred_p = foldDib pred_p id id id (\_ _ a b -> (a||b)) (\_ _ a b -> (a||b)) (||) 


-- Todas las básicas satisfacen el predicado.
allDib :: Pred a -> Dibujo a -> Bool
allDib pred_p = foldDib pred_p id id id (\_ _ a b-> (a&&b)) (\_ _ a b-> (a&&b)) (&&) 

-- Los dos predicados se cumplen para el elemento recibido.
andP :: Pred a -> Pred a -> Pred a
andP pred_a pred_b x = (pred_a x) && (pred_b x)

-- Algún predicado se cumple para el elemento recibido.
orP :: Pred a -> Pred a -> Pred a
orP pred_a pred_b x = (pred_a x) || (pred_b x)

falla = True