(defproject watney "0.1.0-SNAPSHOT"
  :description "HTML to Markdown converter"
  :url "https://github.com/philippkueng/watney"
  :license {:name "MIT"
            :url "http://opensource.org/licenses/MIT"}
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [enlive "1.1.6"]]
  :profiles {:dev {:dependencies [[midje "1.7.0" :exclusions [org.clojure/tools.namespace org.clojure/clojure]]
                                  [markdown-clj "0.9.69" :exclusions [org.clojure/clojure]]]
                   :plugins [[lein-midje "3.1.3"]]}})
