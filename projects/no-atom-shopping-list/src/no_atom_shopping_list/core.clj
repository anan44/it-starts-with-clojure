(ns no-atom-shopping-list.core
  (:gen-class))

(def output-file "./things-to-buy.txt")
(def options "Enter a number:\n1. Add product\n2. Save shopping list")

(defn prompt
  [msg]
  (println msg)
  (read-line))


(defn shopping->str
  [x]
  (str (:product x) " * " (:amount x)))

(defn shopping-printable
  [shoppings]
  (clojure.string/join
    "\n"
    (map shopping->str shoppings)))

(defn save-shopping-list
  [file shoppings]
  (spit file (shopping-printable shoppings)))


(defn -main
  [& args]
  (loop [shoppings []
         choice (prompt options)]
    (case choice
      "1" (recur (conj shoppings {:product (prompt "What to buy?")
                                  :amount (prompt "How many?")})
                 (prompt options))
      "2" (do (save-shopping-list output-file shoppings)
              (println "Shopping list saved"))
      (do (println "Invalid choice!!! Try again")
          (recur shoppings
                 (prompt options))))))

