(ns tttclj.render
  (:require [chord.client :refer [ws-ch]]
            [cljs.core.async :refer [chan <! >! put! close! timeout]]
            [quiescent :as q :include-macros true]
            [quiescent.dom :as d])
  (:require-macros [cljs.core.async.macros :refer [go go-loop]]))

(q/defcomponent Turn
  "Renders a component saying whose turn it is"
  [game]
  (d/p {} 
       (if-let [player (:turn game)]
         (str "It is " (name player) "'s turn")
         (if-let [winner (:winner game)]
           (str (name winner) " won the match!")
           "You managed a draw!"))))

(q/defcomponent Cell
  "Renders a cell in the TicTacToe grid"
  [cell]
  (d/div {:className "cell"}
         (if-let [player (:player cell)]
           (d/div {:className (name player)}))))

(q/defcomponent Line
  "Renders a line in the TicTacToe grid"
  [cells]
  (apply d/div {:className "line"}
         (map Cell cells)))

(q/defcomponent Grid
  "Renders a TicTacToe Grid"
  [cells]
  (apply d/div {:className "grid clearfix"}
         (map Line (partition 3 cells))))

(q/defcomponent Game
  "Renders a TicTacToe game"
  [game]
  (d/div {}
         (Turn game)
         (Grid (:cells game))))

(enable-console-print!)

(go
  (let [server-ch (<! (ws-ch "ws://localhost:8080/ws" {:format :edn}))
        container (.getElementById js/document "main")]
    (go-loop []
      (when-let [envelope (<! (:ws-channel server-ch))]
        (q/render (Game (:message envelope)) container)
        (prn envelope)
        (recur)))
    (prn (str "cannot read from server-ch" server-ch "any more"))))
  
