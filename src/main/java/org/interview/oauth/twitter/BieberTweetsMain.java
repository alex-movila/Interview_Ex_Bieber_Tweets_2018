package org.interview.oauth.twitter;

import org.apache.logging.log4j.Logger;
import twitter4j.*;

import java.time.Instant;
import java.util.*;
import java.util.stream.Stream;

/*
TODO: publish as rest services
TODO: use Spring dependency injection, convert to Spring Boot app , deploy to cloud
TODO: dockerize (not working - need to include also maven dependencies) (https://vidhyachari.wordpress.com/2017/08/28/dockerize-your-java-application-quick-start-guide/)
TODO: generate accesskey?
TODO: epoch formatting, use also timezone for conversion ?
TODO: log output as JSON
*/
public class BieberTweetsMain {

    private static final int NO_TWEETS_PER_PAGE = 50;
    private static final String TWEETS_FILTER_KEYWORD = "bieber";
    private static final int MAX_FETCH_TIME_IN_SECONDS = 30;
    private static final int MAX_TWEETS = 100;

    private final static Logger logger = org.apache.logging.log4j.LogManager.getLogger(BieberTweetsMain.class);

    public static void main(String[] args) {

        initializeLogger();

        TwitterFactory tf = new TwitterFactory();
        Twitter twitter = tf.getInstance();

        List<Status> tweets = new ArrayList<>();
        long durationInSeconds = 0;
        try {
             durationInSeconds = fetchFilteredMessages(twitter, tweets, TWEETS_FILTER_KEYWORD);
        } catch (TwitterException te) {
            logger.error( "Failed to search tweets: ", te);
            System.exit(-1);
        }

        logger.info(String.format("Number of messages per second: %d", tweets.size() / durationInSeconds));

        TweetsProcessor processor = new TweetsProcessor();
        processor.execute(Collections.unmodifiableList( tweets));

        Stream<User> sortedUsers = processor.getSortedUsers();
        Map<Long, List<Status>> sortedMessagesGroupedByUser =  processor.getSortedMessagesGroupedByUser();

        sortedUsers.forEachOrdered(user -> printUserAndHisMessages(user, sortedMessagesGroupedByUser.get(user.getId())));

        System.exit(0);
    }

    /**
     * read public tweets filtered by keyword
     * @param twitter Twitter API instance
     * @param outputTweets output messages
     * @return number of messages read per second
     * @throws TwitterException Twitter API exception
     */
    private static long fetchFilteredMessages(final Twitter twitter, final List<Status> outputTweets, final String keyword) throws TwitterException {
        Query query = new Query(keyword);
        query.setCount(NO_TWEETS_PER_PAGE);
        QueryResult result;
        long beginTimeStampInSeconds = Instant.now().getEpochSecond();
        long currentTimeStampSeconds;
        long diffTimeStampInSeconds;

        do {
            result = twitter.search(query);
            outputTweets.addAll(result.getTweets());
            currentTimeStampSeconds = Instant.now().getEpochSecond();
            diffTimeStampInSeconds = currentTimeStampSeconds - beginTimeStampInSeconds;
        } while (outputTweets.size() <= MAX_TWEETS
                && diffTimeStampInSeconds <= MAX_FETCH_TIME_IN_SECONDS
                && (query = result.nextQuery()) != null);

        return diffTimeStampInSeconds;
    }

    private static void printUserAndHisMessages(User user, List<Status> tweets) {
        printUser(user);
        tweets.forEach(BieberTweetsMain::printTweet);
    }

    private static void printTweet(Status tweet) {
        logger.info(new Message(tweet));
    }

    private static void printUser(User user) {
        logger.info(new Author(user));
    }


    private static void initializeLogger() {
        System.setProperty("Log4jContextSelector", "org.apache.logging.log4j.core.async.AsyncLoggerContextSelector");
        // Increase asynchronous logger buffer size to 1M messages
        System.setProperty("AsyncLogger.RingBufferSize", "1048576");
    }
}
