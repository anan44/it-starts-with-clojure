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

if in Clojure is rather different from if statements in other programming languages.
In face if is something called a [_special form_](https://clojure.org/reference/special_forms).
if in Clojure is kinda like a function,
just like almost everything else is.
Only difference to actual functions is that you cannot pass if around,
in other words it is not a 1st class citizen.
So how can if be like a function?
That makes no sense.
Well actually it does and it is rather simple really.

if takes 3 parameters.
Those being test, then, else?
Question mark in the end of else indicates that is is non-obligatory.
So what does this mean?

```clojure
(if true "yes" "no")
=> "yes"

(if false "yes" "no")
=> "no"

(if false "yes")
=> nil
```