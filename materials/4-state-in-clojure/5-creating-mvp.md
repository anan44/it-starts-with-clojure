# 4.5. Creating MVP

So now we should know enough to actually build an interface for our shopping application.
(That took a while, right.)

So far your code should have atom shoppings and function add-shopping.
That's not much,
but it is a start.

```clojure
(def shoppings (atom []))

(defn add-shopping
  [shopping]
  (swap! shoppings conj shopping))
```

So what we want next?
Our end goal is to be able to save shopping objects to a vector.
We also want to save it out of the vector to a file.
We are also to keen to communicate with out user effortlessly,
so we don't have to worry about that aspect constantly.

## Prompting

So let's start by creating nice function for prompting information from the user.
read-line is nice,
but we want to wrap it up a bit to make it more convenient for us.

```clojure
(defn prompt
  [msg]
  (println msg)
  (read-line))
```

Here we have nice prompt function that takes a message that will be printed to the user,
and then what ever the user writes to the command line will be received by the program.
So essentially it works very same way as prompt() in python.

As before,
always remember to try your code in REPL.
It is crucial part of development in Clojure.
Give it a try (don't leave this part to your code,
it is not part of the final solution):

```clojure
(prompt "Who are you?") ;; now write your name to terminal
=> "John"
```

Great! Our function seems to work as intended.

## Combining functions to building blocks

Next thing we want to do is to create a function for the whole process of adding a new product from terminal to our atom.
Let's create a function add-product-to-shoppings.
This function will wrap prompt's and our add-shopping function to single function.

```clojure
(defn add-product-to-shoppings
  []
  (let [product (prompt "What to buy?")
        amount (prompt "How many?")]
    (add-shopping {:product product
                   :amount  amount}))
```

Remember to write this all of this code by yourself.
Copy-pasting will do you no good.

So what did we do here?

Well we bind out puts of two prompt functions to _product_ and _amount_.
Then we create a map where :product is product and :amount is amount.
Finally we call our add-shopping with this newly created map as parameter.

Remember to try this in REPL.
It is a good idea to call it few times to make sure that the list is built as we intend it to be.

## Interface

Now we should have enough to build a raw prototype of our interface.
For now we are still going to ignore the need to print the list to a file,
and focus to creating that list from the interface.

So we will create an interface,
which will ask us if we want to add items or print the list.
Since this is the main part of our code,
we will be writing it directly to the -main function that runs when the program is being run.

```clojure
(defn -main
  [& args]
  (loop [choice (prompt "Enter a number:\n1. Add product\n2. Save shopping list")]
    (if (= choice "1")
      (do (add-product-to-shoppings)
          (recur (prompt "Enter a number:\n1. Add product\n2. Save shopping list")))
      (if (= choice "2")
        (println "Shopping list saved")
        (do (println "Invalid choice!!! Try again")
            (recur (prompt "Enter a number:\n1. Add product\n2. Save shopping list")))))))
```

So -main function always take arguments [& args],
but we won't wont be using those for now.
What we are interested here is the loop.
So in loop we bind output of the prompt to _choice_.

If the the choice is "1",
we will call add-product-to-shoppings and recur the loop with the same prompt.
Remember that add-product-to-shoppings takes no params.
If the choice is not 1 we will proceed to the second if statement.

Here we will check if the choice is "2".
If it is,
we will will print "Shopping list saved".
Please note that we don't actually save anything at this point.
We will simply leave that as a future problem to be solved.

Finally, if the choice is not "2" we will print a message informing the user of invalid input,
and we will recur the loop again with the same message.

All in all the functionality is rather simple,
but the nested if-statements might look hostile.
I am sure we can do something about those later,
but let's finalize our MVP first.

If you test this in REPL,
depending on your editor you might reach weird results.
Like the prompt text might appear after you actually gave the answer.
Don't worry about this. It should work just fine,
when we actually compile and run the code.

## Saving to file

So now that we have our application partially working,
it is time to write the shopping list to a file.

This is rather straightforward.
To write to a file we use [spit](https://clojuredocs.org/clojure.core/spit).

### [spit](https://clojuredocs.org/clojure.core/spit)

Spit is cousin of slurp that we met before.
It does exactly the opposite of what slurp does.

It can be used to write to different outputs,
but we will now use it to write to a file.

Let's give it a try:

```clojure
(spit "./spit-test.txt" "Hello from the file")
=> nil
```

You should now we able to find the file _spit-test.txt_ from your project root.
You can also verify that that the file has the provided text inside.

Another handy thing is that we can append to the file by providing option for that.

```clojure
(spit "./spit-test.txt" "\n second line" :append true)
=> nil
```

If you look to the previous file you can see that a second line has appeared there.

### Writing shopping list to file

So now that we know how to write a shopping list to a file,
let's modify our -main function to do so.

```clojure
(defn -main
  [& args]
  (loop [choice (prompt "Enter a number:\n1. Add product\n2. Save shopping list")]
    (if (= choice "1")
      (do (add-product-to-shoppings)
          (recur (prompt "Enter a number:\n1. Add product\n2. Save shopping list")))
      (if (= choice "2")
        (do (spit "./things-to-buy.txt" @shoppings) ;; We changed this part
            (println "Shopping list saved"))
        (do (println "Invalid choice!!! Try again")
            (recur (prompt "Enter a number:\n1. Add product\n2. Save shopping list")))))))

```

So we added do statement and a spit function inside it it.
Also note that the previous println is inside this function as well.

Let's run this version.
Write few products and quatities when the application prompts you.
After you are done select the option for saving the shopping list.

You can see that a file is indeed created on you computer.
But unfortunately the file does not look very human friendly.

things-to-by.txt should look something like this (depending what you wrote):

```text
[{:product "Milk", :amount "3"} {:product "Cola", :amount "15"}]
```

Nevertheless it does work, and you can kind of read it,
thus it meets the requirements of MVP.

In the next section we will refactor the code a bit.
We will also improve the functionality so that it is no longer MVP
