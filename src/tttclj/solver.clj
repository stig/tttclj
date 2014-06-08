(ns tttclj.solver
  (:use tttclj.core))

(defn minimax-inner [game]
  (if (is-game-over? game)
    (fitness game)
    (->> (possible-moves game)
         (map (fn [move] [move (- (minimax-inner (successor game move)))]))
         (into {})
         (apply max-key val)
         (key))))

(defn minimax [game]
  (->> (possible-moves game)
       (map (fn [m] [m (minimax-inner (successor game m))]))
       (into {})
       (apply max-key val)
       (key)))
