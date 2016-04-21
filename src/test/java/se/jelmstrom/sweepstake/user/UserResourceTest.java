package se.jelmstrom.sweepstake.user;

import com.orientechnologies.orient.core.sql.OCommandSQL;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.auth.basic.BasicCredentialAuthFilter;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
import org.glassfish.jersey.test.grizzly.GrizzlyWebTestContainerFactory;
import org.junit.*;
import se.jelmstrom.sweepstake.application.OrientConfiguration;
import se.jelmstrom.sweepstake.application.SweepstakeConfiguration;
import se.jelmstrom.sweepstake.application.authenticator.UserAuthenticator;
import se.jelmstrom.sweepstake.application.authenticator.UserAuthorizer;
import se.jelmstrom.sweepstake.orient.OrientClient;

import javax.ws.rs.core.Response;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.Principal;
import java.util.HashSet;

import static javax.ws.rs.client.Entity.json;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;


public class UserResourceTest {

    public static final OrientConfiguration ORIENT_CONFIG = new OrientConfiguration(
            "remote:192.168.59.103/vmtipstest"
            , "test"
            , "testpwd"
            , 10
    );
    private static final OrientClient oClient = new OrientClient(new SweepstakeConfiguration(ORIENT_CONFIG));
    private static final UserRepository userRepo = new UserRepository(oClient);
    @ClassRule
    public static final ResourceTestRule resources = ResourceTestRule.builder()
            .addProvider(new AuthDynamicFeature(new BasicCredentialAuthFilter.Builder<Principal>()
                    .setAuthenticator(new UserAuthenticator(userRepo))
                    .setAuthorizer(new UserAuthorizer())
                    .setRealm("vmtips")
                    .buildAuthFilter()))
            .addProvider(RolesAllowedDynamicFeature.class)
            .addProvider(new AuthValueFactoryProvider.Binder<>(Principal.class))
            .addResource(new UserResource(new UserRepository(oClient)))
            .build();


    @Rule
    public ResourceTestRule rule = ResourceTestRule
            .builder()
            .setTestContainerFactory(new GrizzlyWebTestContainerFactory())
            .addProvider(new AuthDynamicFeature(new BasicCredentialAuthFilter.Builder<Principal>()
                    .setAuthenticator(new UserAuthenticator(userRepo))
                    .setAuthorizer(new UserAuthorizer())
                    .setRealm("vmtips")
                    .buildAuthFilter()))
            .addProvider(RolesAllowedDynamicFeature.class)
            .addProvider(new AuthValueFactoryProvider.Binder<>(Principal.class))
            .addResource(new UserResource(userRepo))
            .build();

    @Before
    public void setUp() throws Exception {

        oClient.start();
    }

    @After
    public void after() {
        oClient.txGraph().command(new OCommandSQL("DELETE VERTEX User where username = 'johane'")).execute();
        oClient.txGraph().commit();
    }

    @Test
    public void testRegisterUser() throws Exception {
        User user = createUser();

    }

    @Test
    public void rejectsUserWithIncorrectEmail() throws Exception {
        User user = new User("johane", null, "1");

        Response post = resources.client().target("/user")
                .request()
                .post(json(user));

        assertThat(post.getStatus(), is(422));

    }

    @Test
    public void rejectsUserIfUsernameIsTaken() throws Exception {
        User user = new User("johane", "johan@email.com", "1", "password");
        User user2 = new User("johane", "johan.e@email.com", "1", "password");

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
        int read = entity.read(bytes);
        return new String(bytes);
    }

    @Test
    public void rejectsUserIfEmailIsTaken() throws Exception {
        User user = new User("johane", "johan@email.com", "1", "password");
        User user2 = new User("johan", "johan@email.com", "1", "password");

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
        User user = new User("johane", "johan@email.com", "1", "password");
        User user2 = new User("johane", "johan@email.com", "1", "password");

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
        HashSet<User> users = userRepo.findUsers(user);

        Response response = resources.client().target("/user/"+users.iterator().next().userId)
                .request()
                .header("Authorization", "Basic ZZ9oYW5lOmFQYXNzd29yZAZZ")
                .get();

        assertThat(response.getStatus(), is(401));
    }

    @Test
    public void shouldAllowAccessIfAuthIsCorrect() {
        User user = createUser();
        HashSet<User> users = userRepo.findUsers(user);

        Response response = resources.client().target("/user/"+users.iterator().next().userId)
                .request()
                .header("Authorization", "Basic am9oYW5lOmFQYXNzd29yZA==")
                .get();

        assertThat(response.getStatus(), is(200));
    }

    @Test
    public void loginReturnsUser() throws IOException {
        User user = createUser();
        HashSet<User> users = userRepo.findUsers(user);

        Response response = resources.client().target("/user/login")
                .request()
                .post(json(user));

        assertThat(response.getStatus(), is(200));
        ByteArrayInputStream entity = (ByteArrayInputStream) response.getEntity();

        String bodyContent = getEntityAsString(entity);
        assertThat(bodyContent, containsString("\"email\""));
        assertThat(bodyContent, containsString("\"username\""));
    }

    private User createUser() {
        User user = new User("johane", "johan@email.com", "1", "aPassword");

        Response post = resources.client().target("/user")
                .request()
                .post(json(user));

        assertThat(post.getStatus(), is(Response.Status.OK.getStatusCode()));
        return user;
    }
}