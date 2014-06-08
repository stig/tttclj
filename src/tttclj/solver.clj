(ns tttclj.solver
  (:use tttclj.core))

(defn minimax-inner [game]
  (if (is-game-over? game)
    (fitness game)
    (->> (possible-moves game)
         (map (fn [move] (- (minimax-inner (successor game move)))))
         (apply max))))

(defn minimax [game]
  (->> (possible-moves game)
       (map (fn [move] [move (- (minimax-inner (successor game move)))]))
       (apply max-key second)
       (first)))
