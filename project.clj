(defproject tttclj "0.0.1-SNAPSHOT"
  :description "A TicTacToe game using Websockets & Clojurescript"
  :dependencies [[compojure "1.1.8" :exclusions [joda-time]]
                 [http-kit "2.1.16"]
                 [jarohen/chord "0.4.1"]
                 [org.clojure/clojure "1.6.0"]
                 [org.clojure/clojurescript "0.0-2227"]
                 [org.clojure/core.async "0.1.303.0-886421-alpha"]
                 [quiescent "0.1.3"]
                 [ring/ring-core "1.1.8"] 
                 [ring/ring-devel "1.1.8"]]
  :plugins [[lein-cljsbuild "1.0.3"]
            [lein-beanstalk "0.2.7"]]
  :ring {:handler tttclj.web/app}
  :war-resources-path "war-resources/.ebextensions"
  :cljsbuild {:builds [{:source-paths ["src"]
                        :jar true
                        :compiler {:output-to "target/classes/public/app.js"
                                   :optimizations :whitespace
                                   :pretty-print true}}]}
  :profiles {:dev {:dependencies [[midje "1.6.3" :exclusions [org.codehaus.plexus/plexus-utils org.clojure/tools.macro]]]}}
  :main tttclj.web)
  
