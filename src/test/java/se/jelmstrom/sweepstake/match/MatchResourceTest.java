package se.jelmstrom.sweepstake.match;

import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.auth.basic.BasicCredentialAuthFilter;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import se.jelmstrom.sweepstake.application.NeoConfiguration;
import se.jelmstrom.sweepstake.application.authenticator.UserAuthenticator;
import se.jelmstrom.sweepstake.application.authenticator.UserAuthorizer;
import se.jelmstrom.sweepstake.domain.Group;
import se.jelmstrom.sweepstake.domain.Match;
import se.jelmstrom.sweepstake.domain.MatchPrediction;
import se.jelmstrom.sweepstake.domain.User;
import se.jelmstrom.sweepstake.group.GroupRepository;
import se.jelmstrom.sweepstake.neo4j.Neo4jClient;
import se.jelmstrom.sweepstake.user.NeoUserRepository;
import se.jelmstrom.sweepstake.user.UserResource;
import se.jelmstrom.sweepstake.user.UserService;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import java.security.Principal;
import java.util.*;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class MatchResourceTest {

    private static final NeoConfiguration config = new NeoConfiguration(
            "192.168.59.103:7474"
            , "local"
            , "neo4j");
    private static final Neo4jClient neoClient = new Neo4jClient(config);
    private static final NeoMatchRepo matchRepo = new NeoMatchRepo(neoClient);

    private static NeoUserRepository userRepo = new NeoUserRepository(neoClient);
    private static UserService userService = new UserService(userRepo);

    private static final GroupRepository repo = new GroupRepository(neoClient);
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
            .addResource(new MatchResource(matchRepo, new MatchService(matchRepo)))
            .build();
    private User user;

    @Before
    public void setUp() throws Exception {
        neoClient.start();
    }

    @After
    public void tearDown() {
        neoClient.session().purgeDatabase();
    }

    @Test
    public void insertMatch(){
        neoClient.session().purgeDatabase();
        Match match = new Match(null, "Sweden", "Brazil", new Date(),1, 0, new Group("A", new HashSet<>()));
        matchRepo.create(match);
        assertThat(matchRepo.list().size(), is(1));
    }


    @Test
    public void matchWithPrediction(){

        neoClient.session().purgeDatabase();
        User user = new User("user", "email", null, "");
        Group group1 = new Group("A", new HashSet<>());
        Match match = new Match(null, "Sweden", "Brazil", new Date(),1, 0, group1);
        Match match2 = new Match(null, "Germany", "Finland", new Date(),11, 0, group1);
        group1.getMatches().add(match);
        group1.getMatches().add(match2);
        matchRepo.create(match);
        matchRepo.create(match2);

        MatchPrediction prediction = new MatchPrediction(null, user, match, 1, 0);
        user.addPrediction(prediction);
        MatchPrediction prediction1 = new MatchPrediction(null, user, match2, 1, 0);
        user.addPrediction(prediction1);

        userRepo.saveUser(user);

        User userById = userRepo.getUserById(user.getId());

        assertThat(userById.getPredictions().size(), is (2));
        Group group = userById.getPredictions().iterator().next().getMatch().getGroup();
        assertThat(group, is(notNullValue()));

    }

    @Test
    public void submitPredictsionsStores(){
        user = new User("test_user", "test_user@email.com", null, "aPassword");
        Group g = repo.getGroup("A");
        userRepo.saveUser(user);
        List<MatchPrediction> predictions = new ArrayList<>();
        g.getMatches().forEach(match -> predictions.add(new MatchPrediction(null, user, match, 1, 0)));

        Response res = resources.client().target("/match/predictions").request()
                .header("Authorization", "Basic dGVzdF91c2VyOmFQYXNzd29yZA")
                .post(Entity.json(predictions));
        Object entity = res.getEntity();
        System.out.println(entity.toString());
        assertThat(res.getStatus(), is(200));
        assertThat(matchRepo.predictionsFor(user).size(), is(6));
    }

    @Test
    public void submitPredictsionsUpdatesExisting(){
        user = new User("test_user", "test_user@email.com", null, "aPassword");
        Group g = repo.getGroup("A");
        g.getMatches().forEach(match -> user.addPrediction(new MatchPrediction(null, user, match, 0, 1)));
        userRepo.saveUser(user);
        matchRepo.savePredictions(user);
        Set<MatchPrediction> matchPredictions = matchRepo.predictionsFor(user);
        assertThat(matchPredictions.size(), is(6));
        matchPredictions.stream().forEach(item -> {
            assertThat(item.getHomeGoals(), is(0));
            assertThat(item.getAwayGoals(), is(1));

        });

        List<MatchPrediction> predictions = new ArrayList<>();
        g.getMatches().forEach(match -> predictions.add(new MatchPrediction(null, user, match, 1, 0)));
        user.getPredictions().addAll(predictions);
        Response res = resources.client().target("/match/predictions").request()
                .header("Authorization", "Basic dGVzdF91c2VyOmFQYXNzd29yZA")
                .post(Entity.json(predictions));
        Object entity = res.getEntity();
        System.out.println(entity.toString());
        assertThat(res.getStatus(), is(200));
        Set<MatchPrediction> updatedPredictions = matchRepo.predictionsFor(user);
        assertThat(updatedPredictions.size(), is(6));
        updatedPredictions.stream().forEach(item -> {
            assertThat(item.getHomeGoals(), is(1));
            assertThat(item.getAwayGoals(), is(0));

        });
    }
}
