(ns tttclj.t-solver
  (:use midje.sweet)
  (:use [tttclj.core])
  (:use [tttclj.solver]))

(facts "about minimax"
       (fact "blocks o's possibility to win @ xo- --- ---"
             (minimax (-> (create-game)
                          (successor 0)
                          (successor 1))) => 7))
