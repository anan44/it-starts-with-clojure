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

3. It will keep prompting the user until empty line is provided

4. In the end the shopping list is saved to a text file

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
