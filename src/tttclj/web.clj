(ns tttclj.web
  (:require [chord.http-kit :refer [wrap-websocket-handler]]
            [clojure.core.async :refer [<! >! put! close! go go-loop timeout]]
            [compojure.core :refer [defroutes GET]]
            [compojure.route :refer [resources]]
            [org.httpkit.server :refer [run-server]]
            [ring.util.response :refer [redirect]]
            [tttclj.core :refer [create-game possible-moves successor game-over?]]
            [tttclj.prep :refer [prep]]
            [tttclj.solver :refer [alphabeta]]))

(defn- make-ai-move [g]
  (successor g (alphabeta g 3)))

(defn ws-handler [{:keys [ws-channel] :as req}]
  (println "Opened connection from" (:remote-addr req))
  (let [game (atom (create-game))]
    ;; Let's send a new game straight away.
    (go
      (>! ws-channel (prep @game)))
    ;; Wait for moves from client. Apply them, and send new board back.
    (go-loop []
      (when-let [{:keys [message error] :as msg} (<! ws-channel)]
        (when-not error
          (when (some #(= % message) (possible-moves @game))
            (swap! game #(successor % message))
            (>! ws-channel (prep @game))
            (when-not (game-over? @game)
              (<! (timeout 500))
              (swap! game make-ai-move)
              (>! ws-channel (prep @game))))
          (when-not (game-over? @game)
            (recur)))))))

(defroutes app
  (resources "/")
  (GET "/ws" [] (-> ws-handler
                    (wrap-websocket-handler {:format :edn})))
  (GET "/" [] (redirect "/index.html")))

(defn -main [& args]
  (let [port (if (empty? args) 8080 (Integer. (first args)))]
    (run-server app {:port port :join? false})
    (println (str "Started server on http://localhost:" port))))
