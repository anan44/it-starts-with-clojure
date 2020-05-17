# Simple web application

In this chapter we will be focusing on the thing that Clojure really shines on.
Web applications.
And it is easy to say that web development with Clojure is just amazing.
This is mainly due two factors:
Functional programming is a really good fit with web development,
and Clojure ecosystem rather well geared towards the web development.

There is many ways to build a Clojure web application.
Unlike in many other languages,
in Clojure it is popular to build web application from scatch without use of an existing framework.
Most other languages have their goto web framework (like Spring, Ruby On Rails, Django),
but in Clojure relying on such a framework is not as common,
and we tend to see more modular web application patched together from various components.

## Our project - Calculator API

We are going to create extremely simple calculator API.
Since we want to focus on creating web application and the basic we components,
we won't be creating anything super cool and fancy,
which would distract us from the main components.

When we are familiar with the basic components,
we can build something more useful and cool.

Our solution will have following requirements:

1. API has /hello endoint that returns a message: Hello World!

2. API has endpoints for following arithmetic operations.
   - addition
   - subtraction
   - multiplication
   - division

3. Each endpoint can take two variables. Namely x and y

4. All endpoints are are tested

As you can see we have a rather short list of requirements this time,
but we will add a small twist to this while we build the application.
We will try a bit different approach with each endpoint,
so you will be familiariesed with different techniques.

## [1. Setting up](1-setting-up.md)