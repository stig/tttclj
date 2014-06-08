(ns tttclj.t-solver
  (:use midje.sweet)
  (:use [tttclj.core])
  (:use [tttclj.solver]))

(facts "about minimax"
       (fact "it picks the center @ xo- --- --- at ply 9"
             (minimax (-> (create-game)
                          (successor 0)
                          (successor 1)) 9) => 4)
       (fact "it picks the center @ blank board at ply 1"
             (minimax (create-game) 1) => 4)
       (fact "it plays a predictable game @ depth 3"
             (loop [game (create-game) history []]
               (if (is-game-over? game)
                 (conj history game)
                 (recur (successor game (minimax game 3)) (conj history game))))
             => [{:player :x, :tiles [:- :- :- :- :- :- :- :- :-]}
                 {:player :o, :tiles [:- :- :- :- :x :- :- :- :-]}
                 {:player :x, :tiles [:- :- :- :- :x :- :- :- :o]}
                 {:player :o, :tiles [:- :- :- :x :x :- :- :- :o]}
                 {:player :x, :tiles [:- :- :- :x :x :o :- :- :o]}
                 {:player :o, :tiles [:- :- :x :x :x :o :- :- :o]}
                 {:player :x, :tiles [:- :- :x :x :x :o :o :- :o]}
                 {:player :o, :tiles [:- :- :x :x :x :o :o :x :o]}
                 {:player :x, :tiles [:- :o :x :x :x :o :o :x :o]}
                 {:player :o, :tiles [:x :o :x :x :x :o :o :x :o]}]))

(facts "about alphabeta"
       (fact "it plays the same game as minimax"
             (loop [game (create-game) history []]
               (if (is-game-over? game)
                 (conj history game)
                 (recur (successor game (alphabeta game 3)) (conj history game))))
             => [{:player :x, :tiles [:- :- :- :- :- :- :- :- :-]}
                 {:player :o, :tiles [:- :- :- :- :x :- :- :- :-]}
                 {:player :x, :tiles [:- :- :- :- :x :- :- :- :o]}
                 {:player :o, :tiles [:- :- :- :x :x :- :- :- :o]}
                 {:player :x, :tiles [:- :- :- :x :x :o :- :- :o]}
                 {:player :o, :tiles [:- :- :x :x :x :o :- :- :o]}
                 {:player :x, :tiles [:- :- :x :x :x :o :o :- :o]}
                 {:player :o, :tiles [:- :- :x :x :x :o :o :x :o]}
                 {:player :x, :tiles [:- :o :x :x :x :o :o :x :o]}
                 {:player :o, :tiles [:x :o :x :x :x :o :o :x :o]}]))

