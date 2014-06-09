(ns tttclj.render
  (:require [quiescent :as q :include-macros true]
            [quiescent.dom :as d]))


(def state {:tiles [:- :- :- 
                    :o :x :o
                    :x :o :x]
            :player :x})


(q/defcomponent Turn
  "Renders a component saying whose turn it is"
  [player]
  (d/p {} (str "It is " player "'s turn:")))

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
         (Turn (:player game))
         (Grid (:tiles game))))

(q/render (Game state)
          (.getElementById js/document "main"))
