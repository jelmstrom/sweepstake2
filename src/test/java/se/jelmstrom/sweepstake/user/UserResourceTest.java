package se.jelmstrom.sweepstake.user;

import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.auth.basic.BasicCredentialAuthFilter;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import se.jelmstrom.sweepstake.Neo4jTestClient;
import se.jelmstrom.sweepstake.application.NeoConfiguration;
import se.jelmstrom.sweepstake.application.authenticator.UserAuthenticator;
import se.jelmstrom.sweepstake.application.authenticator.UserAuthorizer;
import se.jelmstrom.sweepstake.domain.League;
import se.jelmstrom.sweepstake.domain.User;
import se.jelmstrom.sweepstake.neo4j.Neo4jClient;

import javax.ws.rs.core.Response;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.Principal;
import java.util.Set;

import static javax.ws.rs.client.Entity.json;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;



public class UserResourceTest {


    private static final NeoConfiguration config = new NeoConfiguration(
            "192.168.59.103:7474"
            , "local"
            , "neo4j");
    private static final Neo4jClient neoClient = new Neo4jTestClient(config);
    private static final UserRepository userRepo = new UserRepository(neoClient);
    private static final UserService userService= new UserService(userRepo);

    @ClassRule
    public static final ResourceTestRule resources = ResourceTestRule.builder()
            .addProvider(new AuthDynamicFeature(new BasicCredentialAuthFilter.Builder<>()
                    .setAuthenticator(new UserAuthenticator(userRepo))
                    .setAuthorizer(new UserAuthorizer())
                    .setRealm("vmtips")
                    .buildAuthFilter()))
            .addProvider(RolesAllowedDynamicFeature.class)
            .addProvider(new AuthValueFactoryProvider.Binder<>(Principal.class))
            .addResource(new UserResource(userService))
            .build();

    @Before
    public void setUp() throws Exception {
        neoClient.start();
    }

    @After
    public void after() {
        Set<User> users = userRepo.findUsers(new User("test_user", "test_user@email.com",  null, ""));
        users.forEach(user -> neoClient.session().delete(user));
    }

    @Test
    public void testRegisterUser() throws Exception {
        createUser();
    }

    @Test
    public void rejectsUserWithIncorrectEmail() throws Exception {
        User user = new User("johane", null,null);

        Response post = resources.client().target("/user")
                .request()
                .post(json(user));

        assertThat(post.getStatus(), is(400));

    }

    @Test
    public void rejectsUserIfUsernameIsTaken() throws Exception {
        User user = new User("test_user", "johan@email.com", null, "password");
        User user2 = new User("test_user", "johan.e@email.com", null, "password");

        Response post = resources.client().target("/user")
                .request()
                .post(json(user));

        assertThat(post.getStatus(), is(Response.Status.OK.getStatusCode()));

        post = resources.client().target("/user")
                .request()
                .post(json(user2));

        assertThat(post.getStatus(), is(Response.Status.BAD_REQUEST.getStatusCode()));
        ByteArrayInputStream entity = (ByteArrayInputStream) post.getEntity();

        String bodyContent = getEntityAsString(entity);
        assertThat(bodyContent, is(equalTo("[\"username\"]")));
    }

    private String getEntityAsString(ByteArrayInputStream entity) throws IOException {
        byte[] bytes = new byte[entity.available()];
        entity.read(bytes);
        return new String(bytes);
    }

    @Test
    public void rejectsUserIfEmailIsTaken() throws Exception {
        User user = new User("test_usere", "test_user@email.com", null, "password");
        User user2 = new User("test_user", "test_user@email.com", null, "password");

        Response post = resources.client().target("/user")
                .request()
                .post(json(user));

        assertThat(post.getStatus(), is(Response.Status.OK.getStatusCode()));

        post = resources.client().target("/user")
                .request()
                .post(json(user2));

        assertThat(post.getStatus(), is(Response.Status.BAD_REQUEST.getStatusCode()));
        ByteArrayInputStream entity = (ByteArrayInputStream) post.getEntity();

        String bodyContent = getEntityAsString(entity);
        assertThat(bodyContent, is(equalTo("[\"email\"]")));
    }

    @Test
    public void rejectsUserIfEmailAndUsernameIsTaken() throws Exception {
        User user = new User("test_user", "test.user@email.com", null, "password");
        User user2 = new User("test_user", "test.user@email.com", null, "password");

        Response post = resources.client().target("/user")
                .request()
                .post(json(user));

        assertThat(post.getStatus(), is(Response.Status.OK.getStatusCode()));

        post = resources.client().target("/user")
                .request()
                .post(json(user2));

        assertThat(post.getStatus(), is(Response.Status.BAD_REQUEST.getStatusCode()));
        ByteArrayInputStream entity = (ByteArrayInputStream) post.getEntity();

        String bodyContent = getEntityAsString(entity);
        assertThat(bodyContent, containsString("\"email\""));
        assertThat(bodyContent, containsString("\"username\""));
    }

    @Test
    public void shouldBlockAccessIfAuthIsIncorrect() {
        User user = createUser();
        Set<User> users = userRepo.findUsers(user);

        Response response = resources.client().target("/user/"+users.iterator().next().getId())
                .request()
                .header("Authorization", "Basic ZZ9oYW5lOmFQYXNzd29yZAZZ")
                .get();

        assertThat(response.getStatus(), is(401));
    }

    @Test
    public void shouldAllowAccessIfAuthIsCorrect() {
        User user = createUser();
        Set<User> users = userRepo.findUsers(user);

        Response response = resources.client().target("/user/"+users.iterator().next().getId())
                .request()
                .header("Authorization", "Basic dGVzdF91c2VyOmFQYXNzd29yZA")
                .get();

        assertThat(response.getStatus(), is(200));
    }

    @Test
    public void loginReturnsUser() throws IOException {
        User user = createUser();
        Set<User> users = userRepo.findUsers(user);

        Response response = resources.client().target("/user/login")
                .request()
                .post(json(user));

        assertThat(response.getStatus(), is(200));
        ByteArrayInputStream entity = (ByteArrayInputStream) response.getEntity();

        String bodyContent = getEntityAsString(entity);
        assertThat(bodyContent, containsString("\"email\""));
        assertThat(bodyContent, containsString("\"username\""));
        assertThat(bodyContent, containsString("\"points\""));

    }


    @Test
    public void userBelongsToLeague(){
        User user = new User("user", "email", null, "pwd");
        League league = new League();
        league.getUsers().add(user);
        user.getLeagues().add(league);

        userRepo.saveUser(user);
        User userById = userRepo.getUserById(user.getId());
        assertThat(userById, is(notNullValue()));
        assertThat(userById.getLeagues().iterator().next(), is(notNullValue()));

        neoClient.session().delete(user);
        neoClient.session().delete(league);

    }

    private User createUser() {
        User user = new User("test_user", "test_user@email.com", null, "aPassword");

        Response post = resources.client().target("/user")
                .request()
                .post(json(user));

        assertThat(post.getStatus(), is(Response.Status.OK.getStatusCode()));
        return user;
    }
}