(ns sum-em-up.core
  (:gen-class))

(defn sum
  "Sums a vector of numbers"
  [a-seq]
  (apply + a-seq))

(defn str->long
  [x]
  (Long/valueOf x))

(defn split-words
  "Turns a string of numbers into collection of numbers"
  [text]
 (clojure.string/split text #"\s+"))


(= ["1" "2" "3"] (split-words "1 2 3"))
(split-words "")

(defn -main
  [& args]
  (let [file-name (first args)
        text (slurp file-name)
        str-coll (split-words text)
        long-coll (map str->long str-coll)
        total (sum long-coll)]
    (println total)))