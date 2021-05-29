(ns calculator-api.core
  (:gen-class))

(require '[calculator-api.server.server :as calculator-server])

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (calculator-server/start))
