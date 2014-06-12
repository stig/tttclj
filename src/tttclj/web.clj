(ns tttclj.web
  (:require [chord.http-kit :refer [wrap-websocket-handler]]
            [clojure.core.async :refer [<! >! put! close! go go-loop]]
            [compojure.core :refer [defroutes GET]]
            [compojure.route :refer [resources]]
            [org.httpkit.server :refer [run-server]]
            [tttclj.core :refer [create-game]]))

(defn index [req]
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body "Hello HTTP via Compojure!"})

(defn ws-handler [{:keys [ws-channel] :as req}]
  (println ws-channel)
  (go
    (let [{:keys [message]} (<! ws-channel)]
      (println "Message received:" message)
      (>! ws-channel "Hello client from server!")
      (close! ws-channel))))

(defroutes app
  (resources "/")
  (GET "/ws" [] (-> ws-handler
                    (wrap-websocket-handler {:format :edm})))
  (GET "/" [] index))

(defn -main [& args]
  (run-server app {:port 8080})
  (println "Started server on http://localhost:8080"))
