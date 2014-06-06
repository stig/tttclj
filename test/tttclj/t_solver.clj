(ns tttclj.t-solver
  (:use midje.sweet)
  (:use [tttclj.core])
  (:use [tttclj.solver]))

(facts "about minimax"
       (fact "it picks any move from the starting position"
             (minimax (create-game)) => 8))
