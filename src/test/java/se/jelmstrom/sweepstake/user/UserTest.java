package se.jelmstrom.sweepstake.user;

import org.hamcrest.core.Is;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;


public class UserTest {

    @Test
    public void testToString() throws Exception {
        String toString = new User("johan", "johan@email.com", "ABCDEFG").toString();
        assertThat(toString, is(notNullValue()));
        System.out.println(toString);
        assertThat(toString, is(equalTo("{\"username\":\"johan\",\"email\":\"johan@email.com\",\"userId\":\"ABCDEFG\",\"isAdmin\":false}")));
    }
}