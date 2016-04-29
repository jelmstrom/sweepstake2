package se.jelmstrom.sweepstake.league;

import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.auth.basic.BasicCredentialAuthFilter;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
import org.junit.After;
import org.junit.ClassRule;
import org.junit.Test;
import se.jelmstrom.sweepstake.application.NeoConfiguration;
import se.jelmstrom.sweepstake.application.authenticator.UserAuthenticator;
import se.jelmstrom.sweepstake.application.authenticator.UserAuthorizer;
import se.jelmstrom.sweepstake.domain.League;
import se.jelmstrom.sweepstake.domain.User;
import se.jelmstrom.sweepstake.neo4j.Neo4jClient;
import se.jelmstrom.sweepstake.user.NeoUserRepository;

import javax.ws.rs.core.Response;
import java.security.Principal;
import java.util.HashSet;

import static javax.ws.rs.client.Entity.json;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class LeagueResourceTest {

    private static final NeoConfiguration config = new NeoConfiguration(
            "192.168.59.103:7474"
            , "local"
            , "neo4j");
    private static final Neo4jClient neoClient = new Neo4jClient(config);
    private static final NeoUserRepository matchRepo = new NeoUserRepository(neoClient);

    private static NeoUserRepository userRepo = new NeoUserRepository(neoClient);
    @ClassRule
    public static final ResourceTestRule resources = ResourceTestRule.builder()
            .addProvider(new AuthDynamicFeature(new BasicCredentialAuthFilter.Builder<>()
                    .setAuthenticator(new UserAuthenticator(userRepo))
                    .setAuthorizer(new UserAuthorizer())
                    .setRealm("vmtips")
                    .buildAuthFilter()))
            .addProvider(RolesAllowedDynamicFeature.class)
            .addProvider(new AuthValueFactoryProvider.Binder<>(Principal.class))
            .addResource(new LeagueResource(matchRepo))
            .build();
    private User user;

    @After
    public void tearDown(){
        neoClient.session().delete(userRepo.getUserById(user.getId()).getLeagues().iterator().next());
        neoClient.session().delete(user);
        neoClient.session().deleteAll(League.class);
        userRepo.findUsers(user).forEach(u -> userRepo.deleteUser(u.getId()));

    }

    @Test
    public void createsLeague(){
        user = new User("test_user", "test_user@email.com", null, "aPassword");
        userRepo.saveUser(user);

        Response response = resources.client().target("/league/test")
                .request()
                .header("Authorization", "Basic dGVzdF91c2VyOmFQYXNzd29yZA")
                .post(json(user));
        assertThat(response.getStatus(), is(200));
        assertThat(userRepo.getUserById(user.getId()).getLeagues().size(), is(1));

    }

    @Test
    public void joinLeagueAddsToUsersLeagues(){
        user = new User("test_user", "test_user@email.com", null, "aPassword");
        userRepo.saveUser(user);
        League league = new League(new HashSet<>(), "TestLeague");
        neoClient.session().save(league);

        Response response = resources.client().target("/league/TestLeague/join")
                .request()
                .header("Authorization", "Basic dGVzdF91c2VyOmFQYXNzd29yZA")
                .post(json(user));
        assertThat(response.getStatus(), is(200));
        assertThat(userRepo.getUserById(user.getId()).getLeagues().size(), is(1));

    }

}
