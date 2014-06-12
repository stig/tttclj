(defproject tttclj "0.0.1-SNAPSHOT"
  :description "Cool new project to do things and stuff"
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/core.async "0.1.303.0-886421-alpha"]
                 [org.clojure/clojurescript "0.0-2227"]
                 [http-kit "2.1.16"]
                 [quiescent "0.1.3"]
                 [jarohen/chord "0.4.1"]
                 [compojure "1.1.8" :exclusions [joda-time]]]
  :profiles {:dev {:dependencies [[midje "1.5.0" :exclusions [org.codehaus.plexus/plexus-utils org.clojure/tools.macro]]]
                   :plugins [[lein-cljsbuild "1.0.3"]]
                   :cljsbuild {:builds [{:source-paths ["src"]
                                         :compiler {:output-to "target/classes/public/app.js"
                                                    :optimizations :whitespace
                                                    :pretty-print true}}]}}}
  :main tttclj.web)
  
