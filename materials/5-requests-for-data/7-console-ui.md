# 5.7. Console UI

We have done all of this before already,
but we will keep on doing it.
I don't want to teach you how to write functions what only work in void.

To keep it all at least bit interesting,
let's try some new way to create this UI.
The UI will be creating won't be pretty,
mostly because we don't want to waste time on things like that now.
We will be creating using recursion.
It is much more idiomatic way of performing tasks like this than for example `loop`that we used before.

(Note: I have heard that some REPL implementation in certain editors might act funny when using `read-line` inside a recursive function,
so if you run into issues, try using `lein run` from command line instead.)

First lets define the choices we wish to have in our UI:

```clojure
(def ui-choices
  "1: All posts
2: Only good posts
3: Average score
4: Author post count
5: Author total score
6: Links posted
Enter choice:\n")
```

We are defining this as a separate `def` statement,
so a lengthy text like this won't pollute our business logic with unnecessary boilerplate.

Next we will define our run-ui function:

```clojure
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

```

This function takes two arguments.
The first being the choice made by the user.
User input in other words.
The second one is posts.
The function check is the choice is empty.
So if the user provided no input,
the function will print out "done." and return nil (remember clojure print statements return nil).
If the choice is any number from 1 to 6 the corresponding function is called,
and the output of the function is printed by `clojure.pprint/pprint`.
If the choice is something else than 1-6,
the choices will pre printed again.
After printing the result of `case`,
the run-ui will recursively call itself with new user input and same posts.

So what is [`clojure.pprint/pprint`](https://clojuredocs.org/clojure.pprint/pprint)?
It is definitely something we have not seen before.

Well in clojure not all functions are part of clojure.core.
With these functions we have to either include them in the `:require` statement in the beginning of the file,
or alternatively specify their namespace when calling them.
When you need a single function from a namespace just once,
like we do here,
it can be a good idea to just refer to the function with its namespace.
But if you are using the function in question more than once or using multiple functions from the same namespace,
it is definitely a better idea to include the functions to `:require` statement.

All that is left to do,
is to add `run-ui` into the main function that is being called when the program runs.

```clojure
(defn -main
  [& args]
  (do (println ui-choices)
      (run-ui (read-line) (get-posts))))
```

Great.
In a world of poor software development this would conclude our work here,
but luckily we are learning good practices here.

Currently our code looks something like this:

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
         :data
         :children
         (map :data))))

(defn good-post?
  [post]
  (> (:score post) 15))

(defn only-good-posts
  [posts]
  (filter #(> (:score %) 15) posts))

(defn average-score
  [posts]
  (float
    (/ (reduce + (map :score posts))
       (count posts))))

(defn post-count-helper
  [acc x]
  (update acc x (fnil inc 0)))

(defn author-post-count
  [posts]
  (let [authors (map :author posts)]
    (reduce post-count-helper {} authors)))

(defn total-score-helper
  [acc x]
  (update
    acc
    (:author x)
    (fnil (partial + (:score x)) 0)))

(defn author-total-score
  [posts]
  (reduce total-score-helper {} posts))

(defn links-posted-helper
  [acc x]
  (if (empty? (:selftext x))
    (conj acc (:url x))
    acc))

(defn links-posted
  [posts]
  (reduce links-posted-helper [] posts))

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

In the next section we are going to refactor this code to be more idiomatic clojure.
In the mean while we will learn few more new tricks.

Before we start refactoring I would advice you to write some test for this code using your learning from [3.4. Testing in Clojure](../3-first-project/4-testing-in-clojure.md).
Writing tests is a great way to learn to code,
so I will not show you what kind of test are required here.
I leave that to be decided by you.

After you are done with writing some tests,
and you feel like moving forward,
we can jump to the next section

Next: [Idiomatic Refactoring](8-idiomatic-refactoring.md)
