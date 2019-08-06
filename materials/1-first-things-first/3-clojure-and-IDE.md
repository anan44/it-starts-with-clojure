# Clojure and IDEs

When I personally first time ever encountered Clojure and tried toying with it,
the editors were HUGE pain in the ass and one of the reasons why I did not find the language pleasant at all.
Due to this I decided to dedicate a tiny chapter for this issue in the guide.
No one should give up on Clojure due to poor IDE as I did in 2015.

For programming in general there is a ton of great editors available.
Some of them are better suited for some languages than others.
As an example of this, writing Java without IntelliJ IDEA is quite much easier than doing the said task with [Atom](https://atom.io/).
In other words different tools are suitable for different tasks.
Due to the fact that Clojure is a LISP it requires an editor with a bit different capabilities than most other languages.
Also due to the fact that it is not Object Oriented Programming (OOP) language,
IntelliSense is not going to provide as much help as it does inother languages.

Below is rather opinionated guide to IDEs for those who aim to get their hands dirty with Clojure.

## [Visual Studio Code](https://code.visualstudio.com/) + [Calva](https://marketplace.visualstudio.com/items?itemName=betterthantomorrow.calva) (recommended for getting started)

Visual Studio Code (VSC) is a great editor in general.
It might not be the ideal editor for Clojure,
but I recommend it because it very easy to setup and get started with.

### How to install Visual Studio Code

1. Easiest way to install VSC is to download it from their website:
   https://code.visualstudio.com/

2. After this you have to install [Calva plugin](https://marketplace.visualstudio.com/items?itemName=betterthantomorrow.calva).
   This will provide linting and easy access to REPL. Calva plugin can be installed from VSC Marketplace.

3. With Calva plugin correctly installed we can connect VSC to running REPL

TODO: Something about structural editing in calva

## Other options

### [Intellij IDEA](https://www.jetbrains.com/idea/) + [Cursive](https://cursive-ide.com/)

Even though IDEA is superb editor for Java and many other langauges,
it does not really do well with Clojure without some additional help.
This additional help is a magnificent plugin known as Cursive.
Together these two create a programming environment loved by many.
The unfortunately this requires a bit of configuring and is not completely free.
Neither IDEA or Cursive is free, even though there is free options available from both.
If you decide to use this combination, please make sure that you either purchase the required
licenses or only work in projects that are suitable for the free licenses of both of the products.


As I mentioned there are plenty of editors out there.
As usual [Slant](https://www.slant.co/topics/11929/~ide-for-clojure) provides a nice comparison between the options.

### [Emacs](https://www.gnu.org/software/emacs/) + [Cider](https://github.com/clojure-emacs/cider)

Emacs is a classic editor in Clojure circles for a reason.
It is powerful and extremely well suited for for LISPs like Clojure.
With that being said I cannot with good conscience recommend it for a new born Clojurist.
While being a powerful editor, Emacs has a bit nasty learning curve.
Clojure requires a rather different way of thinking than many other languages.
While getting used to this new way of thinking,
I would not recommend trying to learn a new kind of editor at the same time.

### [Vim](https://www.vim.org/) + [Fireplace](https://github.com/tpope/vim-fireplace)

Vim and Fireplace both take time to learn and configure.
But some people are really passionate about Vim so this option has to be pointed out for them.

### [NightCode](https://sekao.net/nightcode/)

Editor especially made for Clojure. Worth testing out.
TODO: write more about NightCode
