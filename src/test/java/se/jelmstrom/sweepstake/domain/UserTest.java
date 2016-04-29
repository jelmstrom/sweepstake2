package se.jelmstrom.sweepstake.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;


public class UserTest {

    @Test
    public void testToString() throws Exception {
        String toString = new User("johan", "johan@email.com", 9999L).toString();
        assertThat(toString, is(notNullValue()));
        assertThat(toString, is(equalTo("{\"userId\":9999,\"username\":\"johan\",\"email\":\"johan@email.com\",\"predictions\":[],\"admin\":false}")));
    }

    @Test
    public void processJson() throws JsonProcessingException {
        User user = new User("tst", "user", 1L, "pwd");
        League e = new League();
        e.getUsers().add(user);
        e.setLeagueName("name");
        e.setId(1L);
        user.getLeagues().add(e);
        String s = new ObjectMapper().writeValueAsString(user);
        System.out.println(s);
    }


}