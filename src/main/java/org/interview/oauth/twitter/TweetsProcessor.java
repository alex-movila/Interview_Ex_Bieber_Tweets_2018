package org.interview.oauth.twitter;

import twitter4j.Status;
import twitter4j.User;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.groupingBy;

public class TweetsProcessor {

    private Stream<User> sortedUsers;

    private Map<Long, List<Status>> sortedMessagesGroupedByUser;

    public void execute( final List<Status> tweets){
        sortedMessagesGroupedByUser = produceSortedMessagesGroupedByUser(tweets);
        this.sortedUsers = produceSortedUsers(sortedMessagesGroupedByUser);
    }


    /**
     * produce Sorted Messages Grouped By User
     * @param tweets raw tweets
     * @return map with users and their messages sorted by creation date
     */
    private Map<Long, List<Status>> produceSortedMessagesGroupedByUser(final List<Status> tweets){
        //messages grouped by user ID
        Map<Long, List<Status>> messagesGroupedByUser = tweets.stream().collect(groupingBy(status -> status.getUser().getId()));

        //The messages per user should also be sorted chronologically, ascending
        messagesGroupedByUser.keySet().forEach(user_id ->
                messagesGroupedByUser.put(user_id,
                        messagesGroupedByUser.get(user_id).stream()
                                .sorted(Comparator.comparing(Status::getCreatedAt))
                                .collect(Collectors.toList())));
        return messagesGroupedByUser;
    }

    /**
     * produce Sorted Users
     * @param messagesGroupedByUser messages grouped by user
     * @return sorted users by creation date
     */
    private Stream<User> produceSortedUsers(Map<Long, List<Status>> messagesGroupedByUser){
        //get list of users based on user ID list
        final List<User> users = new ArrayList<>();
        messagesGroupedByUser.values().forEach(msgs -> msgs.stream().findAny().ifPresent(msg -> users.add(msg.getUser())));

        //users sorted chronologically, ascending
        return users.stream().sorted(Comparator.comparing(User::getCreatedAt));
    }

    public Map<Long, List<Status>> getSortedMessagesGroupedByUser() {
        return sortedMessagesGroupedByUser;
    }

    public Stream<User> getSortedUsers() {
        return sortedUsers;
    }
}
