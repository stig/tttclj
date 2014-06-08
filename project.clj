(defproject tttclj "0.0.1-SNAPSHOT"
  :description "Cool new project to do things and stuff"
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/clojurescript "0.0-2227"]
                 [http-kit "2.1.16"]
                 [compojure "1.1.8"]]
  :profiles {:dev {:dependencies [[midje "1.5.0"]]
                   :plugins [[lein-cljsbuild "1.0.3"]]
                   :cljsbuild {:builds [{:source-paths ["src"]
                                         :compiler {:output-to "target/classes/public/app.js"
                                                    :optimizations :whitespace
                                                    :pretty-print true}}]}}}
  :main tttclj.web)
  
