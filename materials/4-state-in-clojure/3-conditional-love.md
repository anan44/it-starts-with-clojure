# 4.3. Conditional Love

We will now build the command line interface,
so the user will be able to communicate with our application.
This is for sure a challenge you have faced multiple times in other languages,
and it is often quite trivial.
This is also the case in Clojure.

To be able to interface with the application,
we need to learn about some very basic building blocks in Clojure.
[Flow Control Expressions](https://clojure.org/guides/learn/flow#_flow_control_expressions).
More precisely we will learn about if.

## [if](https://clojure.org/guides/learn/flow#_if)

First of all,
this section is rather late in this course,
and I apologize for that.
There simply was not suitable place to slide it in earlier.
Back to the topic.

`if` in Clojure is rather different from `if` statements in other programming languages.
In face `if` is something called a [_special form_](https://clojure.org/reference/special_forms).
`if` in Clojure is kinda like a function,
just like almost everything else is.
Only difference to actual functions is that you cannot pass `if` around,
in other words it is not a 1st class citizen.
So how can `if` be like a function?
"That makes no sense."
Well actually it does,
and it is rather simple really.

`if` takes 3 parameters.
Those being test, then, else?
Question mark in the end of else indicates that is is non-obligatory.
So what does this mean?

```clojure
(if true "yes" "no")
;=> "yes"

(if false "yes" "no")
;=> "no"

(if false "yes")
;=> nil
```

So unlike in so many programming languages Clojure does not provide if statement,
which allows us to take different paths in our programs.
This would be rather imperative manner.
Instead if in Clojure returns one of two values.
Yet nothing stops us from returning results of completely different functions,
which will effectively lead to more functional style solutions.

This would look like something like this:

```clojure
(if (is-sold? product)
  (ship-to-customer product customer)
  (hype-on-marketplace product marketplace))
```

You might also have noticed how I split the `if` statement to three lines.
This is an idiomatic way especially with any longer than super short `if` statements.
For more idiomatic Clojure code remember to visit [the style guide](https://guide.clojure.style/).

When handling boolean operations in Clojure,
it is good to note that almost all values are what we would consider truthy.
Like in many languages the value does not have to be explicitly true to be considered truthy.
In fact only `nil` and `false` are considered falsy.
Everything else will always evaluate to truthy in boolean terms.

Obviously Clojure as a mature language offers many alternatives for `if`,
but we will leave them for later for now.

Next: [Terminal Interface](4-terminal-interface.md)
