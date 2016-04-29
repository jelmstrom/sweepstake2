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
import se.jelmstrom.sweepstake.domain.Match;
import se.jelmstrom.sweepstake.domain.MatchPrediction;
import se.jelmstrom.sweepstake.domain.Stage;
import se.jelmstrom.sweepstake.domain.User;
import se.jelmstrom.sweepstake.neo4j.Neo4jClient;
import se.jelmstrom.sweepstake.user.NeoUserRepository;
import se.jelmstrom.sweepstake.user.UserResource;

import java.security.Principal;
import java.util.Date;
import java.util.HashSet;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static se.jelmstrom.sweepstake.domain.Stage.CompetitionStage.GROUP_A;

public class MatchResourceTest {

    private static final NeoConfiguration config = new NeoConfiguration(
            "192.168.59.103:7474"
            , "local"
            , "neo4j");
    private static final Neo4jClient neoClient = new Neo4jClient(config);
    private static final NeoMatchRepo matchRepo = new NeoMatchRepo(neoClient);

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
            .addResource(new UserResource(userRepo))
            .addResource(new MatchResource(matchRepo))
            .build();

    @Before
    public void setUp() throws Exception {
        neoClient.start();
    }

    @After
    public void tearDown(){
    }

    @Test
    public void insertMatch(){
        Match match = new Match(null, "Sweden", "Brazil", new Date(),1, 0, new Stage(GROUP_A, new HashSet<>()));
        matchRepo.create(match);
        assertThat(matchRepo.list().size(), is(1));
        neoClient.session().delete(match);
    }


    @Test
    public void matchWithPrediction(){
        User user = new User("user", "email", null, "");
        Stage stage1 = new Stage(GROUP_A, new HashSet<>());
        Match match = new Match(null, "Sweden", "Brazil", new Date(),1, 0, stage1);
        Match match2 = new Match(null, "Germany", "Finland", new Date(),11, 0, stage1);
        stage1.getMatches().add(match);
        stage1.getMatches().add(match2);
        matchRepo.create(match);
        matchRepo.create(match2);

        MatchPrediction prediction = new MatchPrediction(null, user, match, 1, 0);
        user.addPrediction(prediction);
        MatchPrediction prediction1 = new MatchPrediction(null, user, match2, 1, 0);
        user.addPrediction(prediction1);

        userRepo.saveUser(user);

        User userById = userRepo.getUserById(user.getId());

        assertThat(userById.getPredictions().size(), is (2));
        Stage stage = userById.getPredictions().iterator().next().getMatch().getStage();
        assertThat(stage, is(notNullValue()));

        neoClient.session().delete(user);
        neoClient.session().delete(match);
        neoClient.session().delete(match2);
        neoClient.session().delete(stage1);
        neoClient.session().delete(prediction);
        neoClient.session().delete(prediction1);



    }
}
