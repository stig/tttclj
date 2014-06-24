(ns tttclj.web-test
  (:require [clojure.core.async :refer [>! <! <!! go chan]]
            [midje.sweet :refer :all]
            [tttclj.web :refer :all]))

(fact "it unwraps a message"
      (let [ws-chan (chan)
            proxy (envelope-unwrapper ws-chan)]
        (go
          (>! ws-chan {:message "lol"})
          (>! ws-chan {:message "lol!!!"}))
        (<!! proxy) => "lol"
        (<!! proxy) => "lol!!!"))
