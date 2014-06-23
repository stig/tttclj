(ns tttclj.game-loop
  (:require [clojure.core.async :refer [<! >! go go-loop timeout]]
            [tttclj.core :refer [possible-moves successor game-over?]]
            [tttclj.prep :refer [prep]]
            [tttclj.solver :refer [alphabeta]]))

(defn- make-ai-move [g]
  (successor g (alphabeta g 3)))

(defn start-game-loop [game chan]
  (let [game (atom game)]
    (go (>! chan (prep @game)))
    ;; Wait for moves from client. Apply them, and send new board back.
    (go-loop []
      (when-let [{:keys [message error] :as msg} (<! chan)]
        (when-not error
          (when (some #(= % message) (possible-moves @game))
            (swap! game #(successor % message))
            (>! chan (prep @game))
            (when-not (game-over? @game)
              (<! (timeout 500))
              (swap! game make-ai-move)
              (>! chan (prep @game))))
          (when-not (game-over? @game)
            (recur)))))))

