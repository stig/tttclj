(ns tttclj.web
  (:require [chord.http-kit :refer [wrap-websocket-handler]]
            [clojure.core.async :refer [>! <! go go-loop chan]]
            [compojure.core :refer [defroutes GET]]
            [compojure.route :refer [resources]]
            [org.httpkit.server :refer [run-server]]
            [ring.util.response :refer [redirect]]
            [tttclj.core :refer [create-game]]
            [tttclj.game-loop :refer [start-game-loop]]))

(defn envelope-unwrapper [ws-chan]
  (let [proxy (chan)]
    (go-loop []
      (when-let [msg (<! ws-chan)]
        (>! proxy (:message msg))
        (recur)))
    proxy))

(defn ws-handler [{:keys [ws-channel] :as req}]
  (println "Opened connection from" (:remote-addr req))
  (start-game-loop (create-game) ws-channel))

(defroutes app
  (resources "/")
  (GET "/ws" [] (wrap-websocket-handler ws-handler {:format :edn}))
  (GET "/" [] (redirect "/index.html")))

(defn -main [& args]
  (let [port (if (empty? args) 8080 (Integer. (first args)))]
    (run-server app {:port port :join? false})
    (println (str "Started server on http://localhost:" port))))
