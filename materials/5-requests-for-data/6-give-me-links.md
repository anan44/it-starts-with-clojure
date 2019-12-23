# 5.6. Give me links

Now we will utilize bunch of skills we learned so far.
Our next goal is to grab all the links posted and return the list of them.

It is rather common in reddit to just post a link and engage in the conversation regarding it.
So what if we would like to list all the links posted?

Let's get started!

In reddit datastructure if a links beging posted are stored in `:url`.
Text written by author of the post is stored in `:selftext`.
So if the particular post contains nothing inside `:selftext`,
it is safe to assume that the post in question contains only a link.

We will iterate through all the posts and collect the `:url`s of those posts that are lacking content in `:selftext`.

As you might have guessed,
we will be needing a helper function again.

```clojure
(defn links-posted-helper
  [acc x]
  (if (empty? (:selftext x))
    (conj acc (:url x))
    acc))
```

You should be able to figure out what this helper does just by reading the code.
All of the pieces are already familiar to you from earlier.

Just in case we are going to work through this together,
but you should try deducing the functionality by yourself first.

So what does our function do?

It takes an `acc` (accumulator) and map `x` (post in our case).
If the `:selftext` is empty,
`:url` is added to `acc`.
If there is content behind `:selftext`,
then the previous `acc` is returned.

What is left for us is to define a function to iterate through the posts with reduce and our helper function.

```clojure
(defn links-posted
  [posts]
  (reduce links-posted-helper [] posts))
```

With all of this done,
we should be able to call our creation and get a list of links.

```clojure
(links-posted (get-posts))
;=> ["https://github.com/gnarroway/mongo-driver-3"
; "https://stuartsierra.com/2019/12/21/clojure-start-time-in-2019"
; "https://github.com/Provisdom/spectomic"
; "https://link.medium.com/G4s6eRQFA2"
; ...
```

Seems like our creation is working!
There is of course other ways to perform this kinda action,
but this is rather straight forward so we will stick with this.

This concludes our work on filtering the links.
It was also the last operational functionality for our application.
Next will just have to tie it all up with simple console UI.

Next: [Console UI](7-console-ui.md)
