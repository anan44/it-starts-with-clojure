(defproject calculator-api "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [ring "1.8.1"]
                 [ring/ring-mock "0.4.0"]
                 [metosin/reitit "0.4.2"]
                 [metosin/ring-http-response "0.9.1"]]

  :main ^:skip-aot calculator-api.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
