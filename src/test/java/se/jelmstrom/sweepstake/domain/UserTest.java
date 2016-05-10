package se.jelmstrom.sweepstake.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.util.Date;
import java.util.HashSet;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;


public class UserTest {

    @Test
    public void testToString() throws Exception {
        String toString = new User("johan", "johan@email.com", 9999L).toString();
        assertThat(toString, is(notNullValue()));
        assertThat(toString, is(equalTo("{\"id\":9999,\"username\":\"johan\",\"email\":\"johan@email.com\",\"predictions\":[],\"leagues\":[],\"admin\":false,\"points\":0}")));
    }

    @Test
    public void parseUserWithBidirectionalRelationships() throws JsonProcessingException {
        User user = new User("tst", "user", 1L, "pwd");
        League e = new League();
        e.getUsers().add(user);
        e.setLeagueName("name");
        e.setId(1L);
        user.getLeagues().add(e);
        String s = new ObjectMapper().writeValueAsString(user);
        System.out.println(s);
    }

    @Test
    public void userPointsCalculatedFromGroupPredictions(){
        User user = new User("", "", 0L);
        Group group = new Group(Group.CompetitionStage.GROUP_A, new HashSet<>());
        Match match1 = new Match(1L, "", "", new Date(), 1, 0, group);
        Match match2 = new Match(2L, "", "", new Date(), 0, 0, group);

        group.getMatches().add(match1);
        group.getMatches().add(match2);

        MatchPrediction prediction1 = new MatchPrediction(1L, user, match1, 1,0);
        MatchPrediction prediction2 = new MatchPrediction(1L, user, match2, 1,0);
        user.addPrediction(prediction1);
        user.addPrediction(prediction2);

        assertThat(user.getPoints(), is(4));
    }


}