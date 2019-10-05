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

(get-post-keys ["author" "score" "selftext"])

(defn thing
  []
  (let [posts (get-post-keys ["author" "score" "title" "url" "selftext"])
        good-posts (filter #(> (% "score") 25) posts)]
    good-posts))

(thing)