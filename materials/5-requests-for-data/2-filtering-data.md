# 5.2 Filtering Data

Thanks to Reddit and its amazing API,
we now have access to large sets of real data.
Processing data is something that Clojure really shines at.

We have already met [`loop`](https://clojuredocs.org/clojure.core/loop),
[`recur`](https://clojuredocs.org/clojure.core/recur),
and [`map`](https://clojuredocs.org/clojure.core/map).

Now we are going to get familiar with other tool for working with sequences.
Meet [`filter`](https://clojuredocs.org/clojure.core/filter)

Filter does exactly what it says.
It filters values from a sequence.
Many languages have similar features,
so this might not be something novel.
Regardless of that Clojure's filter works wonders.

It is a clean and simple solution for remove unwanted items from sequence.
It works somewhat like this:

```clojure
(filter odd? [1 2 3 4 5 6 7 8 9])
;=> (1 3 5 7 9)
```

[`odd?`](https://clojuredocs.org/clojure.core/odd_q) is a clojure function that returns true if the number in question is odd number.
In otherwise it returns false.

So filter takes two arguments: predicate and a collection.
It applies the given predicate function to each value in the collection and removes all values that do not return true.

The out put is always a sequence.
So do not expect to get vector back if you input a vector.

## Predicates

Filter itself are very much straight forward, and there is not much to talk about them. But they do lead us to the concept of [predicates](https://www.tutorialspoint.com/clojure/clojure_predicates.htm).

Predicates are functions that evaluate to true or false.
In Clojure these are commonly the functions with question marks at the end of them.

Clojure core naturally offers many predicates,
but we should not let that limit us,
since it is really easy to write our own predicates as well.

Let's write our own example predicate:

```clojure
(defn larger-than-5?
  [x]
  (> x 5))
```

This function returns true if the number given is larger than 5:

```clojure
(larger-than-5? 6)
;=> true
(larger-than-5? 3)
;=> false
```

After this we can effortlessly use our new predicate with filter:

```clojure
(filter larger-than-5? [1 2 3 4 5 6 7 8 9])
;=> (6 7 8 9)
```

## Real Data, Real Issues

Now we are familiar with `filter`.
Before we get to filtering,
we'll have to work with some data structures.

Most of our tasks we set to ourselves are related to the posts and the data regarding them.
The API does provide us with all this data,
but it is a bit nested so we'll have to do some data parsing to access the data effectively.

### Parsing JSON

Before we can access JSON data from response body affectively,
we will have parse it from string into sensible data structures.
Such as vectors and maps.
In order to do the said parsing,
we'll be using external dependency cheshire.
We already added the necessary dependencies for cheshire to our project in the beginning of this chapter.
Now we will have add a require statement for it into our code,
and use it into our advantage.

In the begin of our core file we should have the ns form.
It should look currently something like this:

```clojure
(ns reddit-analyser.core
  (:require [clj-http.client :as client])
  (:gen-class))
```

We'll make the changes so that it will look like this:

```clojure
(ns reddit-analyser.core
  (:require [clj-http.client :as client]
            [cheshire.core :refer [parse-string]])
  (:gen-class))
```

Don't forget to eval the ns form in your REPL after making the changes,
otherwise you might be up for bad time (errors).

Now that we have the require clause under control,
we can try our new tools.

Let's start by accessing body of our response.
This is simply done by using `:body` with our statement from previous part:

```clojure
(:body (client/get url options))
;=> "{\"kind\": \"Listing\", \"data\": {\"modhash\": \"\", \"dist\": 100,...
```

As you can see,
we are able to access the body,
but it is all just one massive string.
That is not very informative nor fun to use.

Our new friend cheshire offers funky tool as `parse-string` that we just imported to the project.
It is able to parse data structures from JSON strings.
Let's give it a try:

```clojure
(parse-string (:body (client/get url options)))
;=>
;{"kind" "Listing",
; "data" {"modhash" "",
;         "dist" 100,
;...
```

As you can se we get out the same JSON data,
but this time in Clojure data structures.
This is already something we can work with,
and if you come from Languages like Java or C#,
you might actually be rather impressed with this already.
This is not a trivial trick with strongly typed languages

You might also notice that the maps in this structure use strings as keys.
This is fine,
but it is not very idiomatical Clojure.
In Clojure we like to use `:keywords`as key values.
It not only looks better,
but provides some convenience when using the maps.

Luckily for us,
the parse-string takes additional parameter that we can use.
By passing additional `true` value as parameter,
we'll inform the function that we would prefer using keywords instead of strings.

Let's give it a shot:

```clojure
(parse-string (:body (client/get url options)) true)
;=>
;{:kind "Listing",
; :data {:modhash "",
;        :dist 100,
;...
```

Thats cool, right?
At least I think it is very cool.

### Parsing data

We have already previously learned how data from maps is access,
so this should not be nothing fundamentally new.
We will just use our skills from previous chapters to real world data.

Now that we have nicely parsed the whole body into sensible data structures,
it is time to get more familiar with it.
I'll recommend you spend few minutes looking how the body works.
It has a ton of data,
but it is rather neatly structured so it is rather easy to work with.
If you understand at least a bit what the body contains,
the following content will be more interesting to you,
since you won't be just blindly following my instructions.

_Few minutes pass_.
Now that you have gotten familiar with the body's content,
we will start accessing it a bit more.

We are most interested in the posts themselves.
As you should now know,
the posts themselves are inside data->children.
This section then again contains kind and data.
This last data is the actual details regarding single post.

So next we wish to have a collection of only this data.

Since fetching the data and parsing the data into our required format is going to take some work,
let's define it all into single function `get-posts`.

```clojure
(defn get-posts
  []
  (let [body (:body (client/get url options))]
    (map :data (:children (:data (parse-string body true))))))
```

Here we have all the logic required for fetching all the posts and parsing them into one collection.
In this implementation we have a rather much happening on single line.
Let's use the tools we have already learned to make it a bit more readable.
`let`should do the job for now.

```clojure
(defn get-posts
  []
  (let [body (:body (client/get url options))
        parsed-body (parse-string body true)
        children (:children (:data parsed-body))]
    (map :data children)))
```

It is a bit more code,
but that is a price we often have to pay for clarity.

With this we have the posts data parsed into nice format for our next tasks.

### Only good posts filter

A bit earlier we learned the basics of filtering.
Next we will use those skills to our real data from Reddit.

There is a lot of posts in every sub-reddit,
but we are busy people with no time to read them all.
What if we would only be interested in posts that community has validated as "good posts".
We could assume that a post with Score over 15 is probably a good post.

So how would we get around filtering out all the posts that have score less than 15?

Let's start by defining a predicate for defining if a post is good or not.
Let's call this predicate function `good-post?`.
It should look like something like this:

```clojure
(defn good-post?
  [post]
  (> (:score post) 15))
```

It is a simple function that returns true if the post in question has a score over 15,
otherwise it false will be returned.

With this done,
let's use this predicate to filter down our posts.

```clojure
(defn only-good-posts
  [posts]
  (filter good-post? posts))
```

Easiest way to see if our function works,
is to see if the count of posts is reduced below 100.

```clojure
(count (only-good-posts (get-posts)))
;=> 74
```

Your number will most likely differ from mine since you are doing this on different time.

If you wish to see what posts remain after filtering you can call the functions without `count`:

```clojure
(only-good-posts (get-posts))
```

This concludes our part regarding filters.
At this point I recommend that you will take a small break from this guide,
and build a few filters of your own to this reddit data.

You should grasp how it works after no time!

Next: [Reducing complexity](3-reducing-complexity.md)