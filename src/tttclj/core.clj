(ns tttclj.core)

(defn create-game []
  {:player :x
   :tiles (apply vector (repeat 9 :-)) } )

(defn opponent [player]
  (cond (= :x player) :o
        (= :o player) :x))

(defn make-move [idx game]
  (let [p (:player game)
        t (:tiles game)
        g (assoc game :player (opponent p))]
    (assoc-in g [:tiles idx] p)))

(defn possible-moves [game]
  (->> (map-indexed vector (:tiles game))
       (keep #(cond (= :- (second %)) (first %)))))


  
