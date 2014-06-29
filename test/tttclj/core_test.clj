(ns tttclj.core-test
  (:require [midje.sweet :refer :all]
            [tttclj.core :refer :all]))

(facts "about `create-game'"
       (fact "x always starts"
             (->> (repeat 100 (create-game))
                 (map :player)
                 (frequencies)) => { :x 100 })
       (fact "no tiles are set to start with"
             (frequencies (:tiles (create-game))) => { nil 9 }))

(facts "about `successor'"
       (fact "should swap the player to :o"
             (:player (successor (create-game) 0)) => :o)
       (fact "one tile is taken now"
             (frequencies (:tiles (successor (create-game) 3))) => { nil 8 :x 1})
       (fact "cannot make same move again"
             (successor (successor (create-game) 3) 3) => (throws java.lang.AssertionError)))

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

(def game-drawn
  (-> (create-game)
      (successor 0)   ;; x-- --- ---
      (successor 3)   ;; x-- o-- ---
      (successor 1)   ;; xx- o-- ---
      (successor 4)   ;; xx- oo- ---
      (successor 6)   ;; xx- oo- x--
      (successor 2)   ;; xxo oo- x--
      (successor 5)   ;; xxo oox x--
      (successor 7)   ;; xxo oox xo-
      (successor 8))) ;; xxo oox xox

(def game-won-by-x
  (-> (create-game)
      (successor 0)   ;; x-- --- ---
      (successor 3)   ;; x-- o-- ---
      (successor 1)   ;; xx- o-- ---
      (successor 4)   ;; xx- oo- ---
      (successor 2))) ;; xxx oo- ---

(facts "about `game-over?'"
       (fact "is not true at start of game"
             (game-over? (create-game)) => false)
       (fact "is true when no more moves left"
             (game-over? game-drawn) => true)
       (fact "is true when there is a winning line"
             (game-over? game-won-by-x) => true))

(facts "about `winner'"
       (fact "is not true at start of game"
             (winner (create-game)) => nil)
       (fact "is not true one move in"
             (winner (-> (create-game) (successor 0))) => nil)
       (fact "returns winner when there is a winning line"
             (winner game-won-by-x) => :x))
