(ns tttclj.solver
  (:use tttclj.core))

(defn minimax-inner [game]
  ;; (println game ";; =>" (fitness game))
  (if (is-game-over? game)
    (fitness game)
    (loop [moves (possible-moves game)
           max_sc Integer/MIN_VALUE]
      (if-let [move (first moves)]
        (let [succ (successor game move)
              sc (- (minimax-inner succ))]
          (recur (rest moves) (if (> sc max_sc) sc max_sc)))
        max_sc))))

(defn minimax-middle [game]
  (if (is-game-over? game)
    {}
    (->> (possible-moves game)
         (map (fn [m]
                [m (minimax-inner (successor game m))]))
         (into {}))))

(defn minimax [game]
  (if-let [scores (minimax-middle game)]
    (key (apply max-key val scores))
    nil))
