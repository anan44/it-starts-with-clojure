# 4.4. Terminal interface

With conditions in our pocket we will need just few more pieces to build a simple terminal interface.
Firstly we will be needing a [read-line](https://clojuredocs.org/clojure.core/read-line).

## [read-line](https://clojuredocs.org/clojure.core/read-line)

`read-line` reads the [stdin](https://en.wikipedia.org/wiki/Standard_streams) (known as \*in\*) and returns that value.
In our case the stdin is the terminal as it very often is.
`read-line` is fairly simple to use since it takes no arguments what so ever.

```clojure
(read-line) ;; now write hello to your stdin
;=> "hello"
```

There is not much more to say about the `read-line` function,
so lets move on. Next we will meet `loop`.

## [loop](https://clojuredocs.org/clojure.core/loop) and [recur](https://clojuredocs.org/clojure.core/recur)

Clojure's [special form](https://clojure.org/reference/special_forms) `loop` can be a bit tricky to explain,
but we will soon see how good of a job I will manage to do in it.

`loop` is a [lexical context](https://en.wikipedia.org/wiki/Scope_(computer_science)#Lexical_scope_vs._dynamic_scope) just like [let](https://clojuredocs.org/clojure.core/let),
with a significant difference.
When combined with `recur` function `loop` enables [tail recursion](https://en.wikipedia.org/wiki/Tail_call) in Clojure.
[Recursion](https://en.wikipedia.org/wiki/Recursion) is a powerful technique that plays a crucial role in Functional Programming.
Unfortunately the JVM (on which Clojure is hosted) [does not have a very good support for recursion](https://purelyfunctional.tv/article/problems-with-the-jvm/),
and using default recursion will easily lead to StackOverflowError.
Thus we have loop/recur.

So without further ado,
we will explore loop/recur by examples.

Let's count from 0 to 10 using loop/recur.

```clojure
(loop [num 0]
  (if (> num 10)
    (println "done")
    (do (println num)
        (recur (inc num)))))
```

WOW! Thats a handful!
Let's dissect it a bit and see what it actually is.

hint: [do](https://clojuredocs.org/clojure.core/do) let's us evaluate multiple expressions and return the value of the last.
This is often used with `println`,
but it has other uses as well.

```clojure
(loop [num 0]                ;; bind 0 to num in lexical context
  (if (> num 10)             ;; check if num is larger than 0
    (println "done")         ;; if so, print "done"
    (do (println num)        ;; start do expression and print num
        (recur (inc num))))) ;; call recur inside the do statement
```

So what does `recur` actually do?
[recur](https://clojuredocs.org/clojure.core/recur) is a [special form](https://clojure.org/reference/special_forms#recur),
which allows us to to recall `loop` with new bindings.
In other words we can start running the code again in loop-ish manner,
but with new _num_ value.
In our case we are rebinding num to num + 1.
Just like `let`,
`loop` as well can take multiple bindings and it is rather common.

I cannot in good conscience tell you about `recur` without mentioning,
that it can also be called outside a loop to recall a function that it is in with new bindings.
This is rather rare and I'm having a hard time coming up with any examples.
But you should know it is possible.

I suggest that you play around with loop/recur.
It can feel a bit weird or intimidating,
but you will get ahold of it quite quickly.

Below are a few examples that might help you to get started:

Hint: check [rest](https://clojuredocs.org/clojure.core/rest) and [first](https://clojuredocs.org/clojure.core/first)

```clojure
(loop [a-vec []                       ;; bind empty vector to a-vec
       word "Hickey"]                 ;; bind "Hickey" to word
  (if (empty? word)                   ;; check if there is characters left in word
    a-vec                             ;; if there is not, then return a-vec
    (recur (conj a-vec (first word))  ;; if there is, then recur loop adding first letter of the word to a-vec
           (rest word))))             ;; and binding rest of the letter (except first) to word
```

`first` and `rest` play a crucial role in how we process collections in clojure.
You will see them pretty much every where.
`rest` returns all but the first element of a collection.
`first` does exactly the opposite.

```clojure
(first [:a :b :c])
;=> :a

(rest [:a :b :c]
;=> (:b :c))
```

Also notice that rest returns a seq regardless of the input type.

```clojure
(loop [a-list '()
       a-vec [1 3 5 3 23 21 12 39]]
  (if (empty? a-vec)
    a-list
    (recur (conj a-list (* (first a-vec) 2))
           (rest a-vec))))
```

Here we double all the elements in a-vec and return them in a reversed list.
Can you figure out why the list is being reversed?

Hint: try to remember what `conj` does with with different collection types.

If you are looking for an extra challenge,
try modifying this so that it does not reverse the list.

### About loop and laziness

After you get the hang of `loop`,
it might be tempting to use it for all of your collection manipulation and filtering needs.

It can indeed perform the tasks of [filter](https://clojuredocs.org/clojure.core/filter),
[map](https://clojuredocs.org/clojure.core/map),
and [reduce](https://clojuredocs.org/clojure.core/reduce).

(These are three stages of data manipulation that are present in almost all Functional Programming code)

But even though `loop` can perform all of these tasks,
you should avoid using it and favour other functions when possible.
This is due to the fact that `loop` is not [lazy](http://clojure-doc.org/articles/language/laziness.html),
thus by using `loop` you reduce Clojure's ability to optimize its performance.
This is something that might not be an issue for you,
since regardless what you do Clojure is rather performant,
but it never hurts to write better code, right?

With that being said,
don't stress about whether you are using `loop` too much.
It is often justified and it will soon become one of your very good friends.

## Basic terminal interface

So let's make a very basic terminal interface.
This is kinda example,
so we won't be using this exact interface in our shopping-list.
But we will be building something very similar.

**WARNING:** If you are using VSCode with Calva, please do not enter the code below into your editor. It may [break Calva Jack-In ](https://github.com/BetterThanTomorrow/calva/issues/377).

So we will have `loop`,
where the bind value is read-line
(yes this is totally possible, we can bind value to output of a function).

```clojure
(loop [you-say (read-line)]
  (if (= you-say "quit")
    (println "we are done")
    (recur (do (println you-say)
               (read-line)))))
```

This function will prompt the user for an input, print out the input and continue this way until the user enters _quit_.
I am sure you have seen similar solutions in other languages,
since they are rather popular when learning to program.

So now we should know enough to build an actual MVP.

Next: [Creating MVP](5-creating-mvp.md)
