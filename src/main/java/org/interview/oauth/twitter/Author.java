package org.interview.oauth.twitter;

import twitter4j.User;

import java.util.StringJoiner;

/**
 * tweet author with only needed details
 */
public class Author {

    private User user;

    public Author(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", this.getClass().getSimpleName() + "[", "]")
                .add("id = " + user.getId())
                .add("creation date (epoch seconds) = " + user.getCreatedAt().toInstant().getEpochSecond())
                .add("name = " + user.getName())
                .add("screenName = " + user.getScreenName())
                .toString();
    }
}
