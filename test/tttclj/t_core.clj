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

(facts "about `successor'"
       (fact "should swap the player to :o"
             (:player game1) => :o)
       (fact "one tile is taken now"
             (frequencies (:tiles (successor (create-game) 3))) => { :- 8 :x 1})
       (fact "cannot make same move again"
             (successor (successor (create-game) 3) 3) => nil))

(facts "about `possible-moves'"
       (fact "there should be 9 to start"
             (count (possible-moves (create-game))) => 9)
       (fact "there should be 8 after the first move"
             (count (possible-moves (successor (create-game) 3))) => 8))

(facts "about `fitness'"
       (fact "--- --- ---"
             (fitness (create-game)) => 0)
       (fact "x-- --- ---"
             (fitness (successor (create-game) 0)) => -3)
       (fact "-x- --- ---"
             (fitness (successor (create-game) 1)) => -2)
       (fact "--- -x- ---"
             (fitness (successor (create-game) 4)) => -4)
       (fact "o-- -x- ---"
             (fitness (-> (create-game) (successor 4) (successor 0))) => 1)
       (fact "-o- -x- ---"
             (fitness (-> (create-game) (successor 4) (successor 1))) => 2)
       (fact "--o -xx ---"
             (fitness (-> (create-game) (successor 4) (successor 2) (successor 5))) => -9))

