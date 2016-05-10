package se.jelmstrom.sweepstake.league;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.auth.basic.BasicCredentialAuthFilter;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
import org.junit.After;
import org.junit.ClassRule;
import org.junit.Test;
import org.neo4j.ogm.session.Session;
import se.jelmstrom.sweepstake.application.NeoConfiguration;
import se.jelmstrom.sweepstake.application.authenticator.UserAuthenticator;
import se.jelmstrom.sweepstake.application.authenticator.UserAuthorizer;
import se.jelmstrom.sweepstake.domain.*;
import se.jelmstrom.sweepstake.neo4j.Neo4jClient;
import se.jelmstrom.sweepstake.user.NeoUserRepository;

import javax.ws.rs.core.Response;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.Principal;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

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
            .addResource(new LeagueResource(matchRepo, new LeagueService(new LeagueRepository(neoClient), userRepo)))
            .build();
    private User user;
    private Match match;
    private Group group;

    @After
    public void tearDown(){
        Session session = neoClient.session();
        session.delete(userRepo.getUserById(user.getId()).getLeagues().iterator().next());
        session.delete(user);
        if(group != null){
            session.delete(group);
        }
        if(match != null){
            session.delete(match);
        }
        userRepo.findUsers(user).forEach(session::delete); // any lingering users

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


    @Test
    public void leaderboardReturnsListOfUsers() throws IOException {
        user = new User("test_user", "test_user@email.com", null, "aPassword");
        group = new Group(Group.CompetitionStage.GROUP_A, new HashSet<>());
        match = new Match(null, "Swe", "Den", new Date(), 1, 0, group);
        match.getGroup().getMatches().add(match);
        user.addPrediction(new MatchPrediction(null, user, match, 1, 0));
        userRepo.saveUser(user);

        League league = new League(new HashSet<>(), "TestLeague");
        league.getUsers().add(user);
        user.getLeagues().add(league);


        neoClient.session().save(league);


        Response response = resources.client().target("/league/"+league.getId()+"/leaderboard")
                .request()
                .header("Authorization", "Basic dGVzdF91c2VyOmFQYXNzd29yZA")
                .get();
        assertThat(response.getStatus(), is(200));
        List<User> userListFromString = getUserListFromString((ByteArrayInputStream) response.getEntity());
        assertThat(userListFromString.size(), is(1));

        User u = userListFromString.get(0);
        assertThat(u.getPredictions().size(), is(1));
        assertThat(userListFromString.contains(user), is(true));
    }

    private List<User> getUserListFromString(ByteArrayInputStream entity) throws IOException {
        byte[] bytes = new byte[entity.available()];
        entity.read(bytes);
        String bodyString = new String(bytes);
        ObjectMapper objectMapper = new ObjectMapper();
        List<User> value = objectMapper.readValue(bodyString, objectMapper.getTypeFactory().constructCollectionType(List.class, User.class));
        return value;
        };
}
