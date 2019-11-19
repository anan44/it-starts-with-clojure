# Requests for Data

In this chapter we are going to be making some [http requests](https://www.tutorialspoint.com/http/http_requests.htm),
and playing with some data sets that we get from the web.
This is a rather common part for modern applications,
so you probably might have done something similar in your previous programming adventures.

Performing this sort of tasks is something Clojure is really amazing at,
so I want to show you some of the better parts of our favorite language.

Some of the documentation for the tools we will be using is a bit lengthy or tricky,
so we'll get you started with them.
We will show you the ropes of it,
as well as point you to right direction for the future readings.

## Our project - Reddit Analyser

More specifically we will be building a small application that fetches data from Reddit.
We will be analyzing the data for [Clojure subreddit](https://www.reddit.com/r/Clojure/).

Our solution will have following requirements:

1. Get 100 most recent posts from /r/Clojure from Reddit.

2. Print out only the good posts that have score higher than 25.

3. Print out the average score of a post.

4. Print out the number of posts per author.

5. Print out total score of each author.

6. Print out all links posted

7. Software can be requested to perform all the requested operations based on arguments given to it.

## Clojurians Community

Since we will scratched Reddit in this chapter,
I'll use that as an opportunity to talk a little bit about Clojure Community.

Clojure has very friendly and welcoming community.
When in doubt it is smart to ask for help,
rather than bang your head endlessly against the wall.
Some places are better for asking for help than others.
Instead of shouting to the wind I would recommend using following options:

### [/r/Clojure in Reddit](https://www.reddit.com/r/Clojure/)

If you are not familiar with this /r/Clojure I strongly recommend following it.
Not only is it a great source for latest Clojure news,
but there are lot of great discussions regarding the language and its ecosystem.

### [Clojurians Slack](http://clojurians.net/)

Another great media for Clojure is Clojurians Slack.
There you have numerous channels for discussion and many friendly and helpful fellow Clojurians to help you.
Additionally many Clojure's Core team members also participate to discussions regularly here.

There is also a dedicated beginners channel that you might wanna checkout!

### [Clojure User Groups](https://clojure.org/community/user_groups)

There are many Clojure user groups.
You can find more a bout them from the link above.
If such an old-school approach suits you better you might want to check them out.
