(ns tttclj.t-prep
  (:require [midje.sweet :refer :all]
            [tttclj.core :refer :all]
            [tttclj.prep :refer :all]))

(fact
 "It returns cells with ids from 0-8 inclusive"
 
 (->> (create-game)
      prep
      :cells
      (map :id))
 =>
 (range 0 9))

(fact
 "It annotates game with winner"
 
 (-> (create-game)
     (successor 0)
     (successor 8)
     (successor 1)
     (successor 7)
     (successor 2)
     prep
     :winner)
 =>
 :x)

