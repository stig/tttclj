(defproject tttclj "0.0.1-SNAPSHOT"
  :description "Cool new project to do things and stuff"
  :dependencies [[org.clojure/clojure "1.4.0"]
                 [http-kit "2.1.16"]
                 [compojure "1.1.8"]]
  :profiles {:dev {:dependencies [[midje "1.5.0"]]}}
  :main tttclj.web)
  
