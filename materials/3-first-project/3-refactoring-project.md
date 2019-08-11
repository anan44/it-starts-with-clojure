# [3. Refactoring the project](https://en.wikipedia.org/wiki/Code_refactoring)

Writing real-world code never should end with first working MVP.
It would be generally frowned upon to leave the code in such condition.
Yes it might work,
but even with such a small amount of code we managed to somehow make it a bit messy or unclear.

We will pick up from where we left our code in the part 3.1.
From there we will try to make the code more developer friendly.

To make code more friendly idiomatic and friendly for eyes it is a good idea to follow the [Clojure Style Guide](https://guide.clojure.style/).
Our code is mostly already in good order regarding this,
but there is still room for improvement.

One of the main issues with our code now is the -main function.

```clojure
(defn -main
  [& args]
  (println (sum (map str->long (clojure.string/split (slurp "numbers.txt") #"\s+")))))
```

It is now what we often refer as one-liner.
The fact that you can write your whole program on single line,
does not mean you should.

So how do we fix this?
Well first we can try to make some sense to it by breaking it to multiple lines.
It sometimes helps.

```clojure
(defn -main
  [& args]
  (println
    (sum
      (map
        str->long
        (clojure.string/split
          (slurp "numbers.txt")
          #"\s+")))))

```

Well at least we don't have to be scrolling sideways with our editor,
but other than that it is still quite much a mess.

In other programming languages we would often try to solve similar issues with saving the results of each function to a variables.
Even though clojure does provide possibility to save values to variables with [def](https://clojuredocs.org/clojure.core/def),
it is not meant for this kind of usage.
We will not even try that.
(we will cover def later, so don't wonder about it now.)

So how will we solve this? Well the go-to tool for this sort of situation is [let](https://clojuredocs.org/clojure.core/let) function.

## [Let us be friends](https://clojurebridge.org/community-docs/docs/clojure/let/)

[comment]: #(TODO: re write the let part.)

Let is a special form that binds values to names.
These are referred as lexical bindings in Clojure.
Many other languages refer similar concepts as local variables.
So with let we can create bindings that stop existing after the let form ends.
I think it is easier to show than to explain how this actually works.

```clojure
(let [x 10]
  (+ x x))
=> 20

(let [y 15]
  (+ y x))
=> Syntax error compiling at (core.clj:17:3).
Unable to resolve symbol: x in this context
```

Values bind with let are usable inside the let form,
but outside of it.
Let form always return the last form within it,
much like functions in Clojure.

```clojure
(let [x 5
      y 20]
  (+ x y))
=> 25
```

Let is not limited to single binding.
In fact we could bind as many values as we please.

```clojure
(let [x 5
      y (+ x 100)]
  y)
=> 105
```

Also values that have been bind first are usable when binding following values.
Due to this it is never necessary nor a good practice to have nested let forms.

I hope the examples above gave you some perspective on the essence of let.
It is a tool that you will be using more and more when you dive deeper to the rabbit hole of Clojure.

## Refactoring with let

We are now familiar with let,
and we can start using it to clean up our -main function.

So what we are going to do is to bind temporary results with let to make whole thing clearer.

```clojure
(defn -main
  [& args]
  (let [text (slurp "numbers.txt")
        str-coll (clojure.string/split text #"\s+")
        long-coll (map str->long str-coll)
        total (sum long-coll)]
    (println total)))
```

Here is what we did:

1. We bind all the text from file to **text**.

2. We bind a collection numbers in string format to **str-coll**.

3. We bind collection of numbers in long format to **long-coll**

4. We sum of all the numbers in long-coll.

5. Finally print the total

In each step we used the values defined in the previous steps.
Not only is this code easier to read,
but it is also is far easier to debug if something seems fishy.
Just simply print out the value/values that you think might be something else than it should be.

```clojure
(defn -main
  [& args]
  (let [text (slurp "numbers.txt")
        str-coll (clojure.string/split text #"\s+")
        long-coll (map str->long str-coll)
        total (sum long-coll)]
    (println str-coll) ;; like this
    (println total)))
```

Even though using let can make wonders to the readability of your code,
there is also a golden line.
Not every single tiny value needs to be bind with let.
It could easily be argued that we might have gone a bit over the edge with our example here.
In the end you will be the judge of what is the right amount of binding for readability of your code.
Experiment with different approaches while writing Clojure,
and in time you will find the balance between too little and too much let.

## Using args

Currently our project is always dependent on file _numbers.txt_,
and this kinda sucks.
What if we would to check sum of numbers in some other file?
We would have to either rewrite our code and recompile the uberjar to do so.
Or we would have to rename the file.
Neither of these options really seems tempting.
It would be much better if we could define the file name when running the application.
Luckily this is possible.

It is possible to define functions that take unlimited number of arguments.
Our -main function is such a function.
This functionality is rather simple.
We use **& args** to specify that there might be more arguments.
The word used after & can be anything you wish.

```clojure
(defn all-but-first
  "returns a collection with all but the first argument"
  [x & more]
  more)

(all-but-first 1 2 3 4)
=> (2 3 4)
```

Main takes no mandatory arguments,
but it can be provided with unlimited number of arguments that will all be saved to args.
Let's change a our -main function a bit more,
so it will receive the name file we wish to read the numbers from.

```clojure
(defn -main
  [& args]
  (let [file-name (first args)
        text (slurp file-name)
        str-coll (clojure.string/split text #"\s+")
        long-coll (map str->long str-coll)
        total (sum long-coll)]
    (println total)))
```

Now we need to call our program a bit differently if when running it from command line.
Like this:

```bash
lein run ourFileName

# forexample like this
lein run numbers.txt
```

