(ns tttclj.solver
  (:use tttclj.core))

(defn minimax-inner [game depth]
  (if (or (zero? depth)
          (is-game-over? game))
    (fitness game)
    (->> (possible-moves game)
         (map (fn [move] (- (minimax-inner (successor game move) (dec depth)))))
         (apply max))))

(defn minimax [game depth]
  (->> (possible-moves game)
       (map (fn [move] [move (- (minimax-inner (successor game move) (dec depth)))]))
       (apply max-key second)
       (first)))
