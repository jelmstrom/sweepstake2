package se.jelmstrom.sweepstake.user;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import se.jelmstrom.sweepstake.application.OrientConfiguration;
import se.jelmstrom.sweepstake.application.SweepstakeConfiguration;
import se.jelmstrom.sweepstake.orient.OrientClient;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;


public class UserRepositoryTest {

    public static final OrientConfiguration ORIENT_CONFIG = new OrientConfiguration(
        "remote:192.168.59.103/vmtipstest"
        , "test"
        , "testpwd"
        , 10
);

    UserRepository repo = new UserRepository(new OrientClient(new SweepstakeConfiguration(ORIENT_CONFIG)));
    private  User user;

    @Before
    public void setUp(){
        user = repo.saveUser(new User("test", "test", null, "pwd"));
    }

    @After
    public void tearDown(){
        assertThat(repo.deleteUser(user.getUserId()), is(true));
        assertThat(repo.findUsers(user).size(), is(0));
    }
    @Test
    public void testSaveUser() throws Exception {
        assertThat(repo.getUserById(user.getUserId()), is(equalTo(user)));

    }

    @Test
    public void testFindUsers() throws Exception {
        assertThat(repo.findUsers(user).size(), is(1));
    }


}