# State in Clojure

## Full Disclaimer

After some consideration I have decided to teach you about management [state](https://en.wikipedia.org/wiki/State_(computer_science)) in Clojure a bit earlier than most other sources.
This is mostly due to the fact that when I was learning the basics of the language,
it heavily annoyed me to not understand how this was done until much later.
It indeed bugged me so much that it was hard for me to focus on learning the language and the functional paradigm.
That being said,
please keep in mind that this comes perhaps a bit earlier than it should,
but it should not be an issue for you.

## About the Project

As with the first project,
we will be building some simple (and rather useless) application,
and in the meanwhile we will learn some new Clojure.

This time we are building a command line application for creating a shopping list.
The application will have following requirements.

1. It prompts the user from command line for:
    - Product name
    - Quantity to be bought

2. Saves user inputs to data structure

3. Keep prompting user until user tells she is finished with the shopping-list

4. In the end the shopping list is saved to a text file in human readable form

Requirements for this are quite simple.
But some of these things are not as simple in functional paradigm,
as they would be in [imperative programming](https://en.wikipedia.org/wiki/Imperative_programming).
This of course does not mean that would be especially hard to create a such a solution in Clojure, as we will shortly see.

We will try to get some repetition also involved with the assignment,
to enforce learnings from the past chapters.
But some things are left to you at this point.

It is expected that you will try all the functions in the REPL as we go,
I will also recommend that you will try to write tests for some of these functions as we go.
Again try to avoid copying code from the guide or just reading along.
The learning is most effective when you write and run the code on your own machine.

## [1. Adding to Data Structures](./1-adding-to-data-structures.md)

We will learn how to add elements to data structures.
You will learn about conj, concat and cons.

## [2. Storing State with Atom](./2-storing-state-with-atom.md)

You get familiar with Atom and learn to mutate state in Clojure.
We'll also learn about def for saving variables.

## [3. Conditional Love](./3-conditional-love.md)

We will learn about boolean logic in Clojure.

## [4. Terminal Interface](./4-terminal-interface.md)

We will make new friends with loop and recur functions,
as well as with read-line and do functions.

## [5. Creating MVP](./5-creating-mvp.md)

We will create a first functional minimum viable product of our application.

## [6. Not so MVP](./6-not-so-mvp.md)

We will improve the functionality of the previous application,
and do some refactoring to enhance the quality of the code.

## [7. No Need for Atoms](./7-no-need-for-atoms.md)

We'll majorly refactor our application,
and discover how atoms are not necessary for managing state in Clojure.
