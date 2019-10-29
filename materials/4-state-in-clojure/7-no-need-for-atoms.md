# 4.7. No Need for Atoms

Atom is useful tool and I wanted to tell you about it,
so that not knowing about it won't bother you like it did bother me.

Even though `atom`s are useful,
it should not be over used.
Especially when the reality is that we rarely really need it.
In many cases we can store the state in other ways,
recursion being the most common way to do so.

As mentioned before Clojure does not handle classical recursion very well,
thus we rely on loop/recur when utilizing recursion.

We built this application using an `atom`,
since it was an easy way to demonstrate the functionality of `atom`s.
But the fact that we did build this application utilizing `atom`s does not mean we had to do so,
nor that we should do so.
In fact this sort of application should be built using recursion.

We will now refactor the application to work without `atom`s.

Let's start by deleting the `def` shoppings form.
We won't be needing that anymore.
We can also delete the add-shopping function,
since that only directly deals with the `atom`.
Nor will we need add-product-to-shoppings,
so let's delete that too.

At this point we have relatively little code left,
and our main function is nonfunctional,
since it relies on the functions we deleted.

Let's change the main function a bit:

```clojure
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
```

So quite a bit has changed here,
but not too much to make it unrecognizable.

Let's walk through the changes we did.

We added another parameter to the `loop`.
It now has bindings *shoppings* and *choice*.
Because we changed the `loop`'s bindings,
we had to also change the `recur`s accordingly.
So in case the user selects 1,
we are now recurring the loop with a new shoppings vector that is the result of old vector with new item added to it.
The item is created directly by prompting for the product and amount inside the map.
For the default case, `recur` now recurs with the same shoppings as we had on the current iteration,
so the state won't change if this occurs.
Lastly we also modified the final part where we save the list to the file by referring to our shopping vector instead of an `atom`.

As you can see we have now far less code,
our state is handled by recursion and the code is easier to read,
since there is less going on in general.

If we would like to,
we could abstract the creation of single shopping to a dedicated function.

Such a function would look something like this:

```clojure
(defn prompt-shopping
  []
  {:product (prompt "What to buy?")
   :amount  (prompt "How many?")})
```

You are free to do so if you wish to.
It could be argued that it would clarify the `loop` in main function,
but it is also rather unnecessary,
since this function does very little.

All in all the final solution looks something like this:

```clojure
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
```

## Last words on the topic

I am sure some authors would disagree with me telling you about the `atom` this early.
I indeed did have a few deep conversations regarding this decision with my colleagues,
since they too doubted it a bit.

I truly hope that I have managed to convince you to not use `atom`s when not needed.
It can be very tempting but is not necessary very often.

New programmers sometimes feel tempted to create `atom`s inside functions to create a state inside the lexical scope.
This is something I would strongly advice against.
I have never met or heard about a situation where that would be a good idea.

As a rule of thumb always try solving your state issues with recursion. If that fails,
then look for other options such as `atom`s or databases or etc.

With this final warning we finalize the section on `atom`s and state in Clojure.

Next: [Chapter 5 - Requests for Data](../5-requests-for-data)
