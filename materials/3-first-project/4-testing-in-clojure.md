# 4. Testing in Clojure

We have now successfully written a functional Clojure project.
The code runs and thats great.
But most of the time it is good to have tests written for your code,
to have certainty that our code actually works as we intended it to.
This is especially useful if you wish to refactor your code.

So how are tests written in Clojure?
Well you might have noticed that in your project there is a folder called tests.
All the tests in Clojure live in this folder.
From there you can find sum_em_up folder and from it core_test.clj file.
This file should contain all the tests for our sum_em_up.core [namespace](https://www.braveclojure.com/organization/).
Similar patterns can be find in many other languages,
so this might not be new to you.

## Importing from other namespaces

Let's navigate to the pre-mentioned file.
You can see that Leiningen has provided us with an example how test should look like.

At the top of the file we have some imports.
Clojure's imports are quite straight forward as seen here.
In the same form as the test name space sum-em-up.core-test is declared we can see :require form.

```clojure
(ns sum-em-up.core-test
  (:require [clojure.test :refer :all]
            [sum-em-up.core :refer :all]))
```

This here says that we are requiring all the functions from name space clojure.test,
and all functions from sum-em-up.core namespace.
This way we don't have to write the full names of the functions (namespace/function-name),
but instead we can refer to them with just the function name part.

```clojure
(sum-em-up.core/str->long "123")
;; vs
(str->long "123)
```

Both of these are valid syntax,
but but often it is convenient to have the functions required,
so there wont be need to bloat your code with unnecessary parts.

You will get more familiar with namespaces and imports on time,
but for now this will be enough.
In case you want to read more now,
I recommend [Librarian's Tale from Clojure for Brave and True](https://www.braveclojure.com/organization/).
But for now this might not be necessary.

## Writing tests

Let's start by bisecting the test already written for us by Leiningen.

The code is rather simple:

```clojure
(deftest a-test
  (testing "FIXME, I fail."
    (is (= 1 0))))
```

[deftest](https://clojuredocs.org/clojure.test/deftest) defines the test function that takes no arguments.
In side its body you can find [testing](https://clojuredocs.org/clojure.test/testing)
form with [is](https://clojuredocs.org/clojure.test/is)
forms and equality [=](https://clojuredocs.org/clojure.core/=) forms.

So what does each of this do?

### [deftest](https://clojuredocs.org/clojure.test/deftest)

These are the functions that are being run when tests are being run.

### [testing](https://clojuredocs.org/clojure.test/testing)

testing is used to define the context of what is being tested.
With string you can explain what is the feature you are testing,
and then have the actual test forms following the string.
The use of testing function is not mandatory when writing tests,
but is recommendable for increased clarity on your test code.
Additionally,
if your test fails the string from testing is provided with the failing test's data.
This helps us to better understand which test failed.

### [is](https://clojuredocs.org/clojure.test/is)

is is the generic assertion macro in Clojure.
All the tests are wrapped in it.
Unlike in the example test,
is can also take an additional message argument following the assertion form,
though we won't be using that here.

### [=](https://clojuredocs.org/clojure.core/=)

Clojure's equality check.
The = function does not check if the two arguments provided are the same,
but rather if they are equal to each other.
Can be used on most of Clojure's data types.

Now that we have covered all the parts required for testing we can go through what happens in our example test that Leiningen has kindly provided.
First the deftest defines this test and gives it a name _a-test_.
After that testing function provides rather ominous explanation for the test _"FIXME, I fail."_,
which might be giving us some indications what will happen with this test when we run it.
After this is asserts if the equality function actually returns true or not.

This should be rather simple and easy to follow.
So now that we have that covered up,
let's try running the tests.

Some editor's might provide some tooling for running the tests,
but I would recommend running the tests from command line using Leiningen.
It should be easy and fast. Just run the following command in command line from the project directory.

```text
lein test

=> lein test sum-em-up.core-test

lein test :only sum-em-up.core-test/a-test

FAIL in (a-test) (core_test.clj:7)
FIXME, I fail.
expected: (= 0 1)
  actual: (not (= 0 1))

Ran 1 tests containing 1 assertions.
1 failures, 0 errors.
Tests failed.
```

As the description ominously indicated,
the test did actually fail.
This might also be obvious from looking at the what is actually being tested:

```clojure
(= 0 1)
```

Since 0 is not equal to 1 the function will return false,
which will make our test fail.

Let's try to fix this test.
It should not be hard to make this test succeed.
We simply have to turn this equality test to return true.
If we change it to (= 1 1) the test should pass.
After committing the changes,
we can run the tests again and see them pass with flying colors.

```text
lein test
=> lein test sum-em-up.core-test

Ran 1 tests containing 1 assertions.
0 failures, 0 errors.
```

Awesome we made our first test run and pass.
Even though this sad little test does not actually test anything,
it is still a huge step towards understanding how testing works in Clojure.

## Writing meaningful tests

It's now the time for us to write tests that actually tests something meaningful.
In the mean while I might rant a bit about testing in general.

When talking about testing it is almost completely impossible to not to mention [Test Driven Development (TDD)](https://en.wikipedia.org/wiki/Test-driven_development),
which is a fine concept that is spoke of a lot of,
but used not even remotely as often.
TDD guides us to test practically every inch of our code,
since the tests are written before the actual code is written.
In Clojure this kind of testing might be easy,
but also a bit unnecessary.
Most of the functions we use in Clojure are [Pure Functions](https://en.wikipedia.org/wiki/Pure_function),
so they are extremely easy to test.
Furthermore,
we almost always test our code manually with REPL while we code.
After all this is the Clojure way of developing.
This means that we already have rather working test in our hands when we call the code from REPL.

Usually when writing code in Clojure,
it is a good idea to use your REPL tests as a starting point to your actual tests.
It will save you a lot of trouble in long run.

So should we test all the code we write?
Probably not.
If there is a function that sums just two numbers together without any magic,
there might not be need to test this function.
But a rule of thump it is better to test rather too much than too little.
While you work with Clojure more,
you will eventually start recognizing what is worth testing and what is not.

Then to the actual tests! So far in our code we have two functions in addition to the main function.
Since main function often have side effects, they can be tricky to test,
and testing them might be unnecessary if the [unit tests](https://en.wikipedia.org/wiki/Unit_testing) have been written well.

### Testing str->long

We can start with the easiest one.
Testing this functions should be easy.
All it has to do is to turn strings to numbers.

```clojure
(deftest str->long-test
  (testing "Casting text to numbers."
    (is (= 15 (str->long "15")))))
```

After we run the tests again, we see that this works.
But we will get adventurous and try some more interesting inputs,
just to to make sure that our functions actually works.

The functions seems to work also with negative numbers:

```clojure
(deftest str->long-test
  (testing "Casting text to numbers"
    (is (= 15 (str->long "15")))
    (is (= -100 (str->long "-100")))))
```

How about decimal numbers?
Would they work?

```clojure
(deftest str->long-test
  (testing "Casting text to numbers"
    (is (= 15 (str->long "15")))
    (is (= -100 (str->long "-100")))
    (is (= 0.5 (str->long "0.5")))))
```

After running the tests now we will be hit by the unforgiving reality that our function does not work with decimals.

```text
lein test
=> ein test sum-em-up.core-test

lein test :only sum-em-up.core-test/str->long-test

ERROR in (str->long-test) (NumberFormatException.java:65)
Casting text to numbers
expected: (= 0.5 (str->long "0.5"))
  actual: java.lang.NumberFormatException: For input string: "0.5"
 at java.lang.NumberFormatException.forInputString (NumberFormatException.java:65)
 ...
```

It seems like our code does not work with decimals,
because the data type long does not support decimal numbers.
In order to get support for decimal numbers we should actually cast our strings into decimals,
but then the functions would no longer be str->long,
would it?

So we will remove this last test and consider str->long tested.
By definition this function is supposed to turn strings to long,
and thus it should not be able to handle decimals.
Nevertheless, it is up to us to decide if our program should be able to deal with decimals.
If you feel so, try making the necessary changes to the code so it will be able to deal with decimals.
And don't forget to write the tests for it too!

_Hint: Try using writing str->decimal function. Use str->long as an example_

So our final version of the test is now looking like this:

```clojure
(deftest str->long-test
  (testing "Casting text to numbers"
    (is (= 15 (str->long "15")))
    (is (= -100 (str->long "-100")))))
```

We are satisfied with this test,
and thus we'll move forward.

### Testing sum

We can now happily move to the sum function.
This either should not be very hard.

```clojure
(deftest sum-test
  (testing "Adding up numbers in collections of different lengths and types."
    (is (= 10 (sum [5 5])))
    (is (= 0 (sum '(10 -5 -3 -1 -1))))
    (is (= 100 (sum #{25 75})))
    (is (= 0 (sum [])))))
```

Here we test if the sum function actually works with vectors, lists and sets.
We also make sure that the function does not blow up or fail if no numbers are being provided.

After we run the tests with Leiningen again,
we can happily come to the conclusion that our functions are working wonderfully.

### Writing testable code

Now we with our both functions we have tested all the functions except the main function.
So have we now tested our code enough?
It somehow does not feel so.

When writing Clojure (or any code in fact) it is crucial to write kinda code that will be easy to test.
And what kind of code is easy to test you might ask?
Well Pure Functions are extremely easy to test,
and that is one of the main reasons why we like them so much.

Currently we have quite a bit of our business logic just inside the main function.
The easiest way to get more of this code tested is to write more of the logic into pure functions.

Let's get back to the our main function and take a good lok at it:

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

So which parts of this code would be worth testing.

Well file-name just uses [first](https://clojuredocs.org/clojure.core/first) function on args,
and we can be quite sure that it works as intended since it comes straight from Clojure.core.
Text on the other hand uses slurp that is not a pure function,
but also comes from Clojure.core,
so there is probably no need to test that either.
str-coll does not come from clojure.core,
but comes from clojure.string instead.
This as well is a trusted source,
and we have no reason to doubt that it would not work as intended.
But we do give the function our own regex pattern, which could potentially be flawed.
Thus it would be super nice to get this functionality under the test coverage.

So let's write a new function to wrap this functionality into.
Write the following code into your sum-em-up.core.clj.

```clojure
(defn str->number-coll
  "Turns a string of numbers into collection of numbers"
  [text]
 (clojure.string/split text #"\s+"))
```

This function should be easy to test.
Let's not forget to add this function also to our main function.

Our whole core.clj code should look something like this now:

```clojure
(ns sum-em-up.core
  (:gen-class))

(defn sum
  "Sums a vector of numbers"
  [a-seq]
  (apply + a-seq))

(defn str->long
  [x]
  (Long/valueOf x))

(defn split-words
  "Turns a string of numbers into collection of numbers"
  [text]
 (clojure.string/split text #"\s+"))

(defn -main
  [& args]
  (let [file-name (first args)
        text (slurp file-name)
        str-coll (split-words text)
        long-coll (map str->long str-coll)
        total (sum long-coll)]
    (println total)))
```

Don't forget to try if your code still works!

With that being done,
we can return to testing.

Now that we have defined split-words,
we should write some tests for it to ensure that it will work as intended.

Let's add the following test to core_test.clj

```clojure
(deftest split-words-test
  (testing "Tests splitting words from string"
    (is (= ["1" "2" "3"] (split-words "1 2 3")))
    (is (= ["Hello" "World" (split-words "Hello World")]))))
```

With that being done we complete our section on testing.
This also completes our first project.
If you feel like there is more that should be tested in the code,
feel free to do so and add those tests to the project.

Congratulations on completing your very first Clojure project!

Next: [Chapter 4 - State in Clojure](../4-state-in-clojure)
