package se.jelmstrom.sweepstake.domain;

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

}