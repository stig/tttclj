(ns tttclj.prep
  (:require [tttclj.core :refer [game-over? winner]]))

(defn- prep-cell [player]
  (if (= player :-)
    {}
    {:player player}))

(defn- prep-cells [cells]
  (->> cells
      (map prep-cell)
      (map-indexed #(assoc %2 :id %1))))

(defn prep [game]
  "Given a game, produces a data structure fit for consumption on 
the client side. It looks something like this:

; :x is the winner
{:winner :x :cells [{:id 0 :player :x} {:id 1} ... ]}

; a draw
{:winner nil :cells [{:id 0 :player :x} {:id 1} ... ]}

; game is not over yet
{:turn: :x :cells [{:id 0 :player :x} {:id 1} ... ]}
"
  (assoc 
      (if (game-over? game)
        {:winner (winner game)}
        {:turn (:player game)})
    :cells (prep-cells (:tiles game))))
