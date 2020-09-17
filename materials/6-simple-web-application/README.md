# Simple web application

In this chapter we will be focusing on the thing that Clojure really shines on.
Web applications.
And it is easy to say that web development with Clojure is just amazing.
This is mainly due two factors:
Functional programming is a really good fit with web development,
and Clojure ecosystem rather well geared towards the web development.

There is many ways to build a Clojure web application.
Unlike in many other languages,
in Clojure it is popular to build web application from scratch without use of an existing framework.
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

We will cover boring parts of setting up the web application such as boiler plates and dependencies.
We will also scratch the surface of Clojure Web Development components such as Ring, Reitit and Muuntaja.
This helps you to setup your own web applications and gives a nice template to use for your Clojure Web Projects

## [2. Hello Reitit](2-hello-reitit.md)

We will make our first route and learn how craft responses in Ring.
In the mean while we will learn a bit more about muuntaja and handy tools for our responses.

## [3. Query Parameters](3-query-parameters.md)

We will start crafting the calculator API and learn about Query Parameters and how to access them.
We will also briefly visit how we can create some structure to our routes with Nested Route Data.

## [4. Body Parameters](4-body-parameters.md)

While continuing to work with calculator API we will quickly learn about Body parameters and how to access them.

## [5. Path Parameters](5-path-parameters.md)

We will quickly go through how to use path parameters with Reitit.

## [6. Request Coercion](6-request-coercion.md)

After covering the necessary basics we are finally ready to dive to the good stuff.
We will get to know request coercion and have our first interactions with Spec.
We will also go through some useful middleware that is necessary for coercion.

## [7. Response Coercion](7-response-coercion.md)

We will continue our adventures with Coercion.
This time we meet Response Coercion and learn what that is all about or why would you want to use it?

## [8. Refactoring](8-refactoring.md)

TODO
