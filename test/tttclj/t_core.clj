(ns tttclj.t-core
  (:use midje.sweet)
  (:use [tttclj.core]))

(facts "about `create-game'"
       (fact "x always starts"
             (->> (repeat 100 (create-game))
                 (map :player)
                 (frequencies)) => { :x 100 })
       (fact "no tiles are set to start with"
             (frequencies (:tiles (create-game))) => { :- 9 }))

(facts "about `opponent'"
       (opponent :x) => :o
       (opponent :o) => :x)

(facts "about `make-move'"
       (fact "the player should swap to :o"
             (:player (make-move 3 (create-game))) => :o)
       (fact "one tile is taken now"
             (frequencies (:tiles (make-move 3 (create-game)))) => { :- 8 :x 1}))


(facts "about `possible-moves'"
       (fact "there should be 9 to start"
             (count (possible-moves (create-game))) => 9))
