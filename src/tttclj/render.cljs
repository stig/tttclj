(ns tttclj.render
  (:require [chord.client :refer [ws-ch]]
            [cljs.core.async :refer [chan <! >! put! close! timeout]]
            [clojure.string :refer [replace-first]]
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
  [cell player-moves]
  (d/div {:className "cell"
          :onClick #(put! player-moves (:id cell))}
         (if-let [player (:player cell)]
           (d/div {:className (name player)}))))

(q/defcomponent Line
  "Renders a line in the TicTacToe grid"
  [cells player-moves]
  (apply d/div {:className "line"}
         (map #(Cell % player-moves) cells)))

(q/defcomponent Grid
  "Renders a TicTacToe Grid"
  [cells player-moves]
  (apply d/div {:className "grid clearfix"}
         (map #(Line % player-moves) (partition 3 cells))))

(q/defcomponent Game
  "Renders a TicTacToe game"
  [game player-moves]
  (d/div {}
         (Turn game)
         (Grid (:cells game) player-moves)))

(enable-console-print!)

(go
  (let [url (-> (aget js/window "location" "href")
                (replace-first #"http" "ws")
                (replace-first #"index.html" "ws"))
        {:keys [ws-channel error]} (<! (ws-ch url))
        container (.getElementById js/document "main")]
    (if error
      (prn "Couldn't open websocket connection:" error)
      (go-loop []
        (when-let [envelope (<! ws-channel)]
          (q/render (Game (:message envelope) ws-channel) container)
          (recur)))
      (prn (str "cannot read from server-ch" server-ch "any more")))))
  
