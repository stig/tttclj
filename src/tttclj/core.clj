(ns tttclj.core)

(defn create-game []
  {:player :x
   :tiles (apply vector (repeat 9 :-)) } )

(defn- opponent [player]
  (cond (= :x player) :o
        (= :o player) :x))

(defn- is-legal-move? [game idx]
  (= :- (nth (:tiles game) idx)))

(defn successor [game idx]
  "Returns the game state resulting from current player picking the given slot"
  (cond (is-legal-move? game idx)
        (let [p (:player game)
              t (:tiles game)
              g (assoc game :player (opponent p))]
          (assoc-in g [:tiles idx] p))))

(defn possible-moves [game]
  "Returns a vector of possible moves at this game state"
  (->> (map-indexed vector (:tiles game))
       (keep #(cond (= :- (second %)) (first %)))))

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

(defn fitness [game]
  "Calculates the fitness of a particular game state"
  (let [tiles (:tiles game)
        player (:player game)
        opponent (opponent player)
        scores { player 1, opponent -1, :- 0}] 

    ;; for all possible winning lines..
    (->> (lines)

         ;; find picees at locations for each line
         (map #(map tiles %))

         ;; discard all-empty lines
         (remove #(every? (fn [x] (= x :-)) %))

         ;; discard any lines with both players' pieces,
         ;; as these cannot make winning lines
         (remove #(every? (into #{} %) #{:x :o}))

         ;; map to player scores per line
         (map #(reduce + (map scores %)))

         ;; transform scores so that two pieces on one line 
         ;; becomes an advantage
         (map (fn [x] (* x x x)))

         ;; Add up total score for the board
         (reduce +))))

(defn- is-winning-line? [line] 
  (or
   (every? #{:o} line)
   (every? #{:x} line)))

(defn- winner? [line]
  (if (empty? line)
    nil
    (first line)))

(defn winner [game]
  "Returns the winning player, or nil if there is no winner"
  (let [tiles (:tiles game)]
    (->> (lines)
         (map #(map tiles %))
         (filter is-winning-line?)
         (first)
         (winner?))))


(defn is-game-over? [game] 
  "Returns `true' if the game is finished, nil otherwise"
  (if (not-any? #{:-} (:tiles game))
    true
    (not (nil? (winner game)))))

