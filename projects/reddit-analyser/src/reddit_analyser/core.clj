(ns reddit-analyser.core
  (:require [clj-http.client :as client]
            [cheshire.core :refer [parse-string]])
  (:gen-class))

(def options {:headers      {"User-agent" "mega-secret-1337"}
              :query-params {:limit 100}})

(def url "https://www.reddit.com/r/Clojure.json")

(defn get-posts
  []
  (let [body (:body (client/get url options))]
    (->> (parse-string body true)
         (:data)
         (:children)
         (map :data))))

(defn only-good-posts
  [posts]
  (filter #(> (:score %) 15) posts))

(defn average-score
  [posts]
  (let [post-count (count posts)
        total-score (reduce + (map :score posts))]
    (float (/ total-score post-count))))

(defn author-post-count
  [posts]
  (frequencies (map :author posts)))

(defn author-total-score
  [posts]
  (reduce (fn [acc m]
            (update acc
                    (:author m)
                    (fnil (partial + (:score m)) 0)))
          {}
          posts))

(defn links-posted
  [posts]
  (reduce (fn [acc post]
            (if (empty? (:selftext post))
              (conj acc (:url post))
              acc))
          []
          posts))

(def ui-choices
  "1: All posts
2: Only good posts
3: Average score
4: Author post count
5: Author total score
6: Links posted
Enter choice:\n")

(defn run-ui
  [choice posts]
  (if (empty? choice)
    (println "done.")
    (do (clojure.pprint/pprint (case choice
                                 "1" posts
                                 "2" (only-good-posts posts)
                                 "3" (average-score posts)
                                 "4" (author-post-count posts)
                                 "5" (author-total-score posts)
                                 "6" (links-posted posts)
                                 (println ui-choices)))
        (run-ui (read-line) posts))))

(defn -main
  [& args]
  (do (println ui-choices)
      (run-ui (read-line) (get-posts))))
