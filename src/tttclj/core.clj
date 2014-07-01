(ns tttclj.core)

(defn create-game []
  {:player :x
   :tiles (vec (repeat 9 nil)) } )

(defn- opponent [player]
  (cond (= :x player) :o
        (= :o player) :x))

(defn legal-move? [game move]
  "Is move legal in this game?"
  (not (get (:tiles game) move)))

(defn- is-winning-line? [line] 
  (or
   (every? #{:o} line)
   (every? #{:x} line)))

(defn- winner? [line]
  (if (empty? line)
    nil
    (first line)))

(defn lines []
  "All possible winning lines"
  [[0 1 2] ;; horizontal
   [3 4 5]
   [6 7 8]

   [0 3 6] ;; vertical
   [1 4 7]
   [2 5 8]

   [0 4 8] ;; diagonal
   [2 4 6]])

(defn winner [game]
  "Returns the winning player, or nil if there is no winner"
  (let [tiles (:tiles game)]
    (->> (lines)
         (map #(map tiles %))
         (filter is-winning-line?)
         (first)
         (winner?))))


(defn game-over? [game] 
  "Returns `true' if the game is finished, nil otherwise"
  (if (not-any? nil? (:tiles game))
    true
    (not (nil? (winner game)))))

(defn successor [game move]
  {:pre [(not (game-over? game)) (legal-move? game move)]}
  "Returns the game state resulting from current player picking the given slot"
  (let [p (:player game)
        t (:tiles game)
        g (assoc game :player (opponent p))]
    (assoc-in g [:tiles move] p)))

(defn possible-moves [game]
  {:pre [(not (game-over? game))]}
  "Returns a vector of possible moves at this game state"
  (->> (:tiles game)
       (map-indexed vector) 
       (keep #(cond (nil? (second %)) (first %)))))


(defn fitness [game]
  "Calculates the fitness of a particular game state"
  (let [tiles (:tiles game)
        player (:player game)
        opponent (opponent player)
        scores { player 1, opponent -1, nil 0}] 

    ;; for all possible winning lines..
    (->> (lines)

         ;; find pieces at locations for each line
         (map #(map tiles %))

         ;; discard all-empty lines
         (remove #(every? nil? %))

         ;; discard any lines with both players' pieces,
         ;; as these cannot make winning lines
         (remove #(and
                   (some #{:o} %)
                   (some #{:x} %)))

         ;; map to player scores per line
         (map #(reduce + (map scores %)))

         ;; transform scores so that two pieces on one line 
         ;; becomes an advantage
         (map (fn [x] (* x x x)))

         ;; Add up total score for the board
         (reduce +))))


