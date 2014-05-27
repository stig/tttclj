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

(def game1 (make-move 3 (create-game)))

(facts "about `make-move'"
       (fact "the player should swap to :o"
             (:player game1) => :o)
       (fact "one tile is taken now"
             (frequencies (:tiles game1)) => { :- 8 :x 1})
       (fact "cannot make same move again"
             (make-move 3 (make-move 3 (create-game))) => nil))

(facts "about `possible-moves'"
       (fact "there should be 9 to start"
             (count (possible-moves (create-game))) => 9)
       (fact "there should be 8 after the first move"
             (count (possible-moves game1)) => 8))
