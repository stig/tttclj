(ns tttclj.web
  (:require [chord.http-kit :refer [wrap-websocket-handler]]
            [clojure.core.async :refer [<! >! put! close! go go-loop timeout]]
            [compojure.core :refer [defroutes GET]]
            [compojure.route :refer [resources]]
            [org.httpkit.server :refer [run-server]]
            [tttclj.core :refer [create-game possible-moves successor is-game-over?]]
            [tttclj.prep :refer [prep]]))

(defn index [req]
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body "Hello HTTP via Compojure!"})

(defn- make-random-move [g]
  (successor g (rand-nth (possible-moves g))))

(defn ws-handler [{:keys [ws-channel] :as req}]
  (println (:async-channel req))
  (let [game (atom (create-game))]
    (go-loop []
      (when-not (is-game-over? @game)
        (let [move (:message (<! ws-channel))]
          (when (contains? (apply vector (possible-moves @game)) move)
            (swap! game #(successor % move))
            (>! ws-channel (prep @game))
            (<! (timeout 500))
            (when-not (is-game-over? @game)
              (swap! game make-random-move)
              (>! ws-channel (prep @game))))))
      (recur))
    (go
      (>! ws-channel (prep @game)))))

(defroutes app
  (resources "/")
  (GET "/ws" [] (-> ws-handler
                    (wrap-websocket-handler {:format :edn})))
  (GET "/" [] index))

(defn -main [& args]
  (run-server #'app {:port 8080})
  (println "Started server on http://localhost:8080"))
