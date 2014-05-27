(ns tttclj.core)

(defn create-game []
  {:player :x
   :tiles (repeat 9 :-) } )

 (defn possible-moves [game]
  (->> (map-indexed vector (:tiles game))
       (keep #(cond (= :- (second %)) (first %)))))


  
