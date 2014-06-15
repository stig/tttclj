(ns tttclj.solver
  (:require [tttclj.core :refer [game-over? successor possible-moves fitness]]))

(defn- mm [game depth]
  (if (or (zero? depth) (game-over? game))
    (fitness game)
    (->> (possible-moves game)
         (map (fn [move] (- (mm (successor game move) (dec depth)))))
         (apply max))))

(defn minimax [game depth]
  (->> (possible-moves game)
       (map (fn [move] [move (- (mm (successor game move) (dec depth)))]))
       (apply max-key second)
       (first)))

(defn- ab [game depth alpha beta]
  (if (or (zero? depth) (game-over? game))
    (fitness game)
    (loop [moves (possible-moves game) bound alpha]
      (if (empty? moves)
        bound
        (let [sc (- (ab
                     (successor game (first moves))
                     (dec depth)
                     (- beta)
                     (- bound)))]
          (if (>= sc beta)
            sc
            (recur (rest moves) (max sc bound))))))))

(defn alphabeta [game depth]
  (->> (possible-moves game)
       (map (fn [move] [move (- (ab 
                                 (successor game move)
                                 (dec depth)
                                 Integer/MIN_VALUE
                                 Integer/MAX_VALUE))]))
       (apply max-key second)
       (first)))
