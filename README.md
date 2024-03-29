# NYTReader
NYTReader is a simple RSS-like application for reading the public API of The New York Times.

![Alt text](web_hi_res_256.png?raw=true "logo")

It allows users to quickly glance through articles avaiable in the API, open their detailed view and access their full text via a link to the New York Times mobile website.
The app features a database for offline caching of the last 50 articles as well as their pictures.



Screenshots:

![Alt text](list.png?raw=true "logo") ![Alt text](detail.png?raw=true "logo") ![Alt text](giftrump.gif?raw=true "logo")

Since the NYT uses gifs to illustrate their articles, [Glide](https://github.com/bumptech/glide) is used instead of Picasso, because of GIF support.
The implemented architecture is fully asynchronous, with [Otto](http://square.github.io/otto/) and [Retrofit](http://square.github.io/retrofit/) responsible for networking and CursorLoaders for accessing an implemented ContentProvider.
The architecture implementation is based on [these](https://www.linkedin.com/pulse/networking-made-easy-android-retrofit-haider-khan) [two](http://blog.joanzapata.com/robust-architecture-for-an-android-app/) ideas.

Feel free to try the app by downloading an apk [here](NYTReader.apk)
