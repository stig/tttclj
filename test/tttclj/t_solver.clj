(ns tttclj.t-solver
  (:use midje.sweet)
  (:use [tttclj.core])
  (:use [tttclj.solver]))

(facts "about minimax"
       (fact "it picks the center @ xo- --- ---"
             (minimax (-> (create-game)
                          (successor 0)
                          (successor 1))) => 4))
