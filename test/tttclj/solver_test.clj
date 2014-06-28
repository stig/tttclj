(ns tttclj.solver-test
  (:require [midje.sweet :refer :all]
            [tttclj.core :refer :all]
            [tttclj.solver :refer :all]))

(facts "about minimax"
       (fact "it picks the center @ xo- --- --- at ply 9"
             (minimax (-> (create-game)
                          (successor 0)
                          (successor 1)) 9) => 4)
       (fact "it picks the center @ blank board at ply 1"
             (minimax (create-game) 1) => 4)
       (fact "it plays a predictable game @ depth 3"
             (loop [game (create-game) history []]
               (if (game-over? game)
                 (conj history game)
                 (recur (successor game (minimax game 3)) (conj history game))))
             => [{:player :x, :tiles [nil nil nil nil nil nil nil nil nil]}
                 {:player :o, :tiles [nil nil nil nil :x nil nil nil nil]}
                 {:player :x, :tiles [nil nil nil nil :x nil nil nil :o]}
                 {:player :o, :tiles [nil nil nil :x :x nil nil nil :o]}
                 {:player :x, :tiles [nil nil nil :x :x :o nil nil :o]}
                 {:player :o, :tiles [nil nil :x :x :x :o nil nil :o]}
                 {:player :x, :tiles [nil nil :x :x :x :o :o nil :o]}
                 {:player :o, :tiles [nil nil :x :x :x :o :o :x :o]}
                 {:player :x, :tiles [nil :o :x :x :x :o :o :x :o]}
                 {:player :o, :tiles [:x :o :x :x :x :o :o :x :o]}]))

(facts "about alphabeta"
       (fact "it plays the same game as minimax"
             (loop [game (create-game) history []]
               (if (game-over? game)
                 (conj history game)
                 (recur (successor game (alphabeta game 3)) (conj history game))))
             => [{:player :x, :tiles [nil nil nil nil nil nil nil nil nil]}
                 {:player :o, :tiles [nil nil nil nil :x nil nil nil nil]}
                 {:player :x, :tiles [nil nil nil nil :x nil nil nil :o]}
                 {:player :o, :tiles [nil nil nil :x :x nil nil nil :o]}
                 {:player :x, :tiles [nil nil nil :x :x :o nil nil :o]}
                 {:player :o, :tiles [nil nil :x :x :x :o nil nil :o]}
                 {:player :x, :tiles [nil nil :x :x :x :o :o nil :o]}
                 {:player :o, :tiles [nil nil :x :x :x :o :o :x :o]}
                 {:player :x, :tiles [nil :o :x :x :x :o :o :x :o]}
                 {:player :o, :tiles [:x :o :x :x :x :o :o :x :o]}]))

