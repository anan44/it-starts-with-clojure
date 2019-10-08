(ns reddit-analyser.core
  (:require [clj-http.client :as client]
            [cheshire.core :refer [parse-string]])
  (:gen-class))

(def header {:headers {"User-agent" "munjuttu"}})

(def url "https://www.reddit.com/r/Clojure.json")


(defn get-post-keys
  [keys]
  (let [post-parser #(select-keys (get % "data") keys)
        posts (-> (client/get url header)
                  (:body)
                  (parse-string)
                  (get-in ["data" "children"]))]
    (map post-parser posts)))

(defn only-good-posts
  []
  (let [posts (get-post-keys ["author" "score" "title" "url"])
        good-posts (filter #(> (% "score") 15) posts)]
    good-posts))

(defn links-posted
  []
  (let [posts (get-post-keys ["selftext" "url"])]
    (reduce (fn [acc post]
              (if (empty? (post "selftext"))
                (conj acc (post "url"))
                acc)) [] posts)))

(defn main
  [& args]
  (println (links-posted)))