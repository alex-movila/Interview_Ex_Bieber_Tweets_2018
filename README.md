# Bieber Twitter Client Java Exercise #

You are requested to develop a simple application that covers all the requirements listed below. To have an indication of the criteria that will be used to judge your submission, all the following are considered as metrics of good development:

+ Correctness of the implementation
+ Decent test coverage
+ Code cleanliness
+ Efficiency of the solution
+ Careful choice of tools and data formats
+ Use of production-ready approaches

## Task ##

We would like you to write code that will cover the functionality explained below and provide us with the source, instructions to build and run the appliocation  as well as a sample output of an execution:

+ Connect to the [Twitter Streaming API](https://dev.twitter.com/streaming/overview)
    * Use the following values:
        + Consumer Key: ...?
        + Consumer Secret: ...?
    * The app name will be `java-exercise`
    * You will need to login with Twitter
+ Filter messages that track on "bieber"
+ Retrieve the incoming messages for 30 seconds or up to 100 messages, whichever comes first
+ Your application should return the messages grouped by user (users sorted chronologically, ascending)
+ The messages per user should also be sorted chronologically, ascending
+ For each message, we will need the following:
    * The message ID
    * The creation date of the message as epoch value
    * The text of the message
    * The author of the message
+ For each author, we will need the following:
    * The user ID
    * The creation date of the user as epoch value
    * The name of the user
    * The screen name of the user
+ All the above infomation is provided in either SDTOUT or a log file
+ You are free to choose the output format, provided that it makes it easy to parse and process by a machine

### __Bonus points for:__ ###

+ Keep track of messages per second statistics across multiple runs of the application
+ The application can run as a Docker container

## Provided functionality ##

The present project in itself is a [Maven project](http://maven.apache.org/) that contains one class that provides you with a `com.google.api.client.http.HttpRequestFactory` that is authorised to execute calls to the Twitter API in the scope of a specific user.
You will need to provide your _Consumer Key_ and _Consumer Secret_ and follow through the OAuth process (get temporary token, retrieve access URL, authorise application, enter PIN for authenticated token).
With the resulting factory you are able to generate and execute all necessary requests.
If you want to, you can also disregard the provided classes or Maven configuration and create your own application from scratch.
