(ns tttclj.render
  (:require [chord.client :refer [ws-ch]]
            [cljs.core.async :refer [chan <! >! put! close! timeout]]
            [quiescent :as q :include-macros true]
            [quiescent.dom :as d])
  (:require-macros [cljs.core.async.macros :refer [go go-loop]]))

(def state {:tiles [:x :o :-
                    :x :- :o
                    :- :x :o]
            :player-turn :x})


(q/defcomponent Turn
  "Renders a component saying whose turn it is"
  [game]
  (d/p {} 
       (if-let [player (:player-turn game)]
         (str "It is " (name player) "'s turn")
         (if-let [winner (:winner game)]
           (str (name winner) " won the match!")
           "You managed a draw!"))))

(q/defcomponent Cell
  "Renders a cell in the TicTacToe grid"
  [cell]
  (d/div {:className "cell"}
         (if (not (= cell :-))
           (d/div {:className (name cell)}))))

(q/defcomponent Line
  "Renders a line in the TicTacToe grid"
  [cells]
  (apply d/div {:className "line"}
         (map Cell cells)))

(q/defcomponent Grid
  "Renders a TicTacToe Grid"
  [tiles]
  (apply d/div {:className "grid clearfix"}
         (map Line (partition 3 tiles))))

(q/defcomponent Game
  "Renders a TicTacToe game"
  [game]
  (d/div {}
         (Turn game)
         (Grid (:tiles game))))

(q/render (Game state)
          (.getElementById js/document "main"))


(enable-console-print!)

(go
  (prn "hello")
  (let [server-ch (<! (ws-ch "ws://localhost:8080/ws"))]
    (go
      (prn (<! server-ch)))))
