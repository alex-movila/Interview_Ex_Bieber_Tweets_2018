package org.interview.oauth.twitter;

import twitter4j.Status;

import java.util.StringJoiner;

/**
 * twitter message with only needed details
 */
public class Message {

    private Status status;

    public Message(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", this.getClass().getSimpleName() + "[", "]")
                .add("creation date (epoch seconds) = " + status.getCreatedAt().toInstant().getEpochSecond())
                .add("id = " + status.getId())
                .add("screenName = " + status.getUser().getScreenName())
                .add("text = " + status.getText())
                .toString();
    }
}
