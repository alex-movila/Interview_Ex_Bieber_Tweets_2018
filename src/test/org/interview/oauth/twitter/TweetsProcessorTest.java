package org.interview.oauth.twitter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import twitter4j.Status;
import twitter4j.User;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TweetsProcessorTest {

    @Mock
    private Status status1;
    @Mock
    private Status status2;

    @Mock
    private Status status3;
    @Mock
    private Status status4;

    @Mock
    private User user1;

    @Mock
    private User user2;

    private static SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");

    private List<Status> tweets = new ArrayList<>();

    private static long USER_ID1 = 1;
    private static long USER_ID2 = 2;

    private static final String STATUS_DATE_1 = "05/02/2017";
    private static final String STATUS_DATE_2 = "06/02/2017";
    private static final String STATUS_DATE_3 = "07/02/2017";
    private static final String STATUS_DATE_4 = "08/02/2017";
    private static final String USER_DATE_1 = "01/02/2017";
    private static final String USER_DATE_2 = "02/02/2017";


    @Before
    public void setUp(){
        when(status1.getUser()).thenReturn(user1);
        when(status2.getUser()).thenReturn(user1);
        when(status3.getUser()).thenReturn(user2);
        when(status4.getUser()).thenReturn(user2);

        when(status1.getCreatedAt()).thenReturn(toDate(STATUS_DATE_1));
        when(status2.getCreatedAt()).thenReturn(toDate(STATUS_DATE_2));
        when(status3.getCreatedAt()).thenReturn(toDate(STATUS_DATE_3));
        when(status4.getCreatedAt()).thenReturn(toDate(STATUS_DATE_4));

        when(user1.getId()).thenReturn(USER_ID1);
        when(user2.getId()).thenReturn(USER_ID2);

        when(user1.getCreatedAt()).thenReturn(toDate(USER_DATE_1));
        when(user2.getCreatedAt()).thenReturn(toDate(USER_DATE_2));

        tweets.add( status4);
        tweets.add( status3);

        tweets.add( status2);
        tweets.add( status1);
    }


    private Date toDate(String dateInString){
        try {
            return dateFormatter.parse(dateInString);
        } catch (ParseException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }


    @Test
    public void tweetsProcessorTest(){
        TweetsProcessor tweetsProcessor = new TweetsProcessor();
        tweetsProcessor.execute( tweets);
        Map<Long, List<Status>> sortedMessagesGroupedByUser = tweetsProcessor.getSortedMessagesGroupedByUser();
        assertEquals(sortedMessagesGroupedByUser.size(), 2);
        assertEquals(sortedMessagesGroupedByUser.get( USER_ID1).size(), 2);
        assertEquals(sortedMessagesGroupedByUser.get( USER_ID2).size(), 2);

        assertTrue(getCreatedAt(sortedMessagesGroupedByUser, USER_ID1, 0)
                .before(getCreatedAt(sortedMessagesGroupedByUser, USER_ID1, 1)));

        assertTrue(getCreatedAt(sortedMessagesGroupedByUser, USER_ID2, 0)
                .before(getCreatedAt(sortedMessagesGroupedByUser, USER_ID2, 1)));

        List<User> users = tweetsProcessor.getSortedUsers().collect(Collectors.toList());
        assertEquals(users.size(), 2);
        assertTrue(getCreatedAt(users, 0)
                .before(getCreatedAt(users, 1)));

    }

    private Date getCreatedAt( Map<Long, List<Status>> sortedMessagesGroupedByUser, long user_id, int msgIndex){
        return sortedMessagesGroupedByUser.get( user_id).get(msgIndex).getCreatedAt();
    }

    private Date getCreatedAt( List<User> users, int index){
        return users.get(index).getCreatedAt();
    }


}
