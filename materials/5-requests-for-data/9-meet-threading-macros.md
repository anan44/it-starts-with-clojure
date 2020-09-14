# 5.9. Final Refactoring

We are almost done with this application,
but there is still few more places that require some polishing up.

Next we are going to learn of one of the most loved tools in all Clojure.

## [Threading Macros](https://clojure.org/guides/threading_macros)

Treading macros, also sometimes referred as arrow macros can be very intimidating for the new Clojurists,
at least I remember looking them with terror when I first encountered them.

Arrow macros come in two formats thread-first [`->`](https://clojuredocs.org/clojure.core/-%3E),
and thread-last [`->>`](https://clojuredocs.org/clojure.core/-%3E%3E).
There is also some more advanced versions of threading macros,
but we won't be looking into them now.
In case you are interested you can read more from this [guide](https://clojure.org/guides/threading_macros).

So what are threading macros?
Well in his immense wisdom Sensei Rich Hickey foresaw the __impending doom of thousand nested parentheses__.
He acted fiercely to prevent this disaster and thus the `->` and `->>` were created.

So in short,
threading macros help us to write clearer code instead of dozens and dozens of nested forms.
Code written with treading macro can be read from top to bottom,
unlike normal Clojure code that is being read inside out.

This is again something that that is much easier to show,
instead of trying to explain how it works.

Here is completely made up function that is supposed to create numbers of magical quality as strings.

```clojure
(defn get-magic-numbers
  [x]
  (take x (map str (map #(* % 8) (filter #(= 0 (mod % 7 ))(filter even? (range)))))))

(get-magic-numbers 10)
;=> ("0" "112" "224" "336" "448" "560" "672" "784" "896" "1008")
```

Try to interpret what this function does.
It is not impossible,
not even that hard,
but damn it is tedious.

We can obviously try to make this function clearer with some line breaks,
but it will still look rather terrible.

```clojure
(defn get-magic-numbers
  [x]
  (take x
        (map str
             (map #(* % 8)
                  (filter #(= 0 (mod % 7))
                          (filter even?
                                  (range)))))))
```

Another thing we could try is to use `let` statement extensively to bring some order to this chaos.

```clojure
(defn get-magic-numbers-with-let
  [x]
  (let [evens (filter even? (range))
        div-7 (filter #(= 0 (mod % 7)) evens)
        times-8 (map #(* 8 %) div-7)
        strings (map str times-8)]
    (take x strings)))
```

Now we have made the code look like your average piece of Java.
Yes it is easy to read,
but don't you feel like there is far too much code here.
To make sense of what is happening,
we ended up writing so much more code.

This is where threading macros come in.
In this particular case we will use `->>` also known as thread-last.

```clojure
(defn get-magic-numbers-threading
  [x]
  (->> (range)
       (filter even?)
       (filter #(= 0 (mod % 7)))
       (map #(* 8 %))
       (map str)
       (take x)))
```

Boom!
Significantly less code,
and we can read what happens from top to down,
instead of trying to figure out where one form ends and another begins.

So what `->>` thread-last does,
is that it resolves the first form and then adds it as a last argument to the next form in line.
It will continue doing this until all the forms are solved,
after which the final result is returned.

Thread-first `->` is very similar to `->>`,
only difference is that the result of the previous form is being passed as the first argument instead of the last.
It is useful sometimes,
but most of the time you will be using thread-last,
since it is the far most popular of the threading macros.

I recommend you will get nice and friendly with `->>` soon as possible,
it will not only make your code easier to understand,
but it it will also make your transition to LISP easier,
since you don't have to read the code inside out.

Now that we know of threading macros,
we can move forward and implement one into our code.

## Get posts with threading

Next we are going to refactor our `get-posts` so that it will utilize `->>`.
Our current solution looks like this:

```clojure
(defn get-posts
  []
  (let [body (:body (client/get url options))
        parsed-body (parse-string body true)
        children (:children (:data parsed-body))]
    (map :data children)))
```

And with little tinkering we managed to turn it into this:

```clojure
(defn get-posts
  []
  (let [body (:body (client/get url options))]
    (->> (parse-string body true)
         (:data)
         (:children)
         (map :data))))

```

We use let for the body,
since we want to pass `true` as the last argument for `parse-string`.
After this it is pure threading.

But there is actually one more step that we can make to improve this solution.
we pass a single argument function to threading macro,
such as a keyword for example,
we can omit the parentheses.

Like this:

```clojure
(defn get-posts
  []
  (let [body (:body (client/get url options))]
    (->> (parse-string body true)
         :data
         :children
         (map :data))))
```

This concludes our work on threading.
I recommend that you play around with it,
so it will feel less intimidating.
It is not a difficult concept,
but rather different from how other programming languages handle things.
I am sure you will master it after no time!

This concludes our work on this chapter.
We went through plenty of new things,
which I recommend that you put to the use soon as possible,
so they are not forgotten.

In the end of all of this refactoring your code should look something like this:

```clojure
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
```

I would say that we achieved rather much functionality with about 80 lines of code.
But hey,
that is Clojure for you!

Next we will be learning how to create a simple web backend with Clojure.

Next: [Chapter 6 - Simple Web Application](../6-simple-web-application)