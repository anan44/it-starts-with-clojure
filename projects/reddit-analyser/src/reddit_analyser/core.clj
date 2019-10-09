(ns reddit-analyser.core
  (:require [clj-http.client :as client]
            [cheshire.core :refer [parse-string]])
  (:gen-class))

(def header {:headers {"User-agent" "munjuttu"}})

(def url "https://www.reddit.com/r/Clojure.json")

(defn get-posts
  []
  (let [body (:body (client/get url header))]
    (->> (parse-string body true)
         :data
         :children
         (map :data))))

(defn only-good-posts
  [posts]
  (filter #(> (:score %) 15) posts))

(defn average-score
  [posts]
  (float
    (/ (reduce + (map :score posts))
       (count posts))))

; See also https://clojuredocs.org/clojure.core/frequencies
(defn author-post-count
  [posts]
  (reduce (fn [acc v]
            (update acc v (fnil inc 0)))
          {}
          (map :author posts)))

(defn author-total-score
  [posts]
  (reduce (fn [acc m]
            (update acc (:author m) (fnil (partial + (:score m)) 0)))
          {}
          posts))

(defn links-posted
  [posts]
  (reduce (fn [acc post]
            (if (empty? (:selftext post))
              (conj acc (:url post))
              acc)) [] posts))

(defn main
  [& args]
  (let [posts (get-posts)]
    (println (links-posted posts))))
