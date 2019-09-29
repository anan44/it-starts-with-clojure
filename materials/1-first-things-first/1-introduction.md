# It starts with Clojure: a practical guide to Clojure

## Who is this guide for

This guide is aimed for programers with prior experience in some other language.
We will not be covering very basics of programming (at least intentionally).
Unlike most clojure materials,
this guide does not expect the reader to have experience in Java,
even though can be helpful.

## Origins of this guide

While getting started with clojure myself I noticed that there was relatively little
materials available in comparison to many other languages.
This is obviously expected,
since Clojure is not as mainstream as Python, Java or JavaScript.
But the other thing I noticed was that most of the material available is rather academic on their approach,
and they tended to focus strongly on Function Programming (FP) instead of how to build things on Clojure.

I found this quite annoying since even though I was interested in FP,
my primary interest was to start building actual things with Clojure.
Many guides/books play extensively in REPL (Read-Eval-Print-Loop),
instead of actually building anything that can be deployed or even run from command line.

## Purpose of this guide

Purpose of this guide is to help anyone interested in Clojure to get started and to
build real world applications with the language instead of going to the deep-end
with wonderful functionalities of FP and Clojure.
If favor of getting up and running with Clojure,
this guide focuses on building real world applications,
instead of taking a deep-dive to fine functionalities of Clojure and FP paradigm.
When purposefully skipping parts of the language,
we will try to at least mention them,
or provide some links and 3rd party materials to learn about these things.

**In no way is this guide aiming to be a complete guide or even complete introduction to the language or the FP paradigm.**
The purpose is to provide enough knowledge to get started and guide readers who wish to learn more to other sources.

The guide is aiming to be a Open Source living document,
to which others are welcome to commit to and be part of.

## How to read this guide

Each sub folder of **./materials/** can be considered as a chapter,
and each .md file inside a folder can be considered a section.

While reading this guide it is recommended writing all the parts on your own,
not just copying what is written here.
No one ever learned anything from copy-pasting.

In the context of evaluating code or using REPL,
we will be using symbol `;=>` to express the output of the code.

Like this:

```clojure
(+ 1 2 3)
;=> 6
```

Many of the headings in these documents are actually hyperlinks,
and in most of the cases they lead to a section of official Clojure documentation.
If the explanations of this guide feel vague or too brief,
or you simply wish to know more about the topic at hand,
it is encouraged to browse through these documentations.
(IDE section makes an exception here. Links there lead to god knows where.)

Next: [Installation](2-installation.md)
