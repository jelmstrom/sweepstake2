package se.jelmstrom.sweepstake.group;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import se.jelmstrom.sweepstake.domain.GroupPrediction;
import se.jelmstrom.sweepstake.domain.TeamRecord;
import se.jelmstrom.sweepstake.domain.User;
import se.jelmstrom.sweepstake.neo4j.Neo4jClient;
import se.jelmstrom.sweepstake.user.UserRepository;
import se.jelmstrom.sweepstake.user.UserService;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.Principal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class GroupResourceTest {
    private static final NeoConfiguration config = new NeoConfiguration(
            "192.168.59.103:7474"
            , "local"
            , "neo4j");
    private static final Neo4jClient neoClient = new Neo4jClient(config);
    private static final UserRepository matchRepo = new UserRepository(neoClient);

    private static final UserRepository userRepo = new UserRepository(neoClient);
    private static final GroupRepository repo = new GroupRepository(neoClient);
    @ClassRule
    public static final ResourceTestRule rule = ResourceTestRule.builder()
            .addProvider(new AuthDynamicFeature(new BasicCredentialAuthFilter.Builder<>()
                    .setAuthenticator(new UserAuthenticator(userRepo))
                    .setAuthorizer(new UserAuthorizer())
                    .setRealm("vmtips")
                    .buildAuthFilter()))
            .addProvider(RolesAllowedDynamicFeature.class)
            .addProvider(new AuthValueFactoryProvider.Binder<>(Principal.class))
            .addResource(new GroupResource(new GroupService(repo), new UserService(userRepo)))
            .build();

    @Before
    public void setUp() throws Exception {
        neoClient.start();
    }
    @After
    public void tearDown() {
        neoClient.session().purgeDatabase();
    }

    @Test
    public void shouldLoadGroupA() throws IOException {
        Group group = repo.getGroup("A");
        group.getMatches().forEach(match -> {
            match.setAwayGoals(0);
            match.setHomeGoals(1);
        });
        group = repo.saveMatches(group);
        Response groupResponse = rule.client().target("/group/A").request()
                .header("Authorization", "Basic dGVzdF91c2VyOmFQYXNzd29yZA")
                .get();

        ByteArrayInputStream entity = (ByteArrayInputStream) groupResponse.getEntity();
        byte[] bytes = new byte[entity.available()];
        entity.read(bytes);
        String bodyString = new String(bytes);
        Group returned = new ObjectMapper().readValue(bodyString, Group.class);
        List<TeamRecord> recordMap = returned.getStandings();

        assertThat(recordMap.size(), is(4));
        assertThat(recordMap.get(0).getPoints(), is(6));
        assertThat(recordMap.get(0).getTeam(), is("France"));
        assertThat(recordMap.get(1).getPoints(), is(6));
        assertThat(recordMap.get(2).getPoints(), is(3));
        assertThat(recordMap.get(3).getPoints(), is(3));
        assertThat(recordMap.get(3).getTeam(), is("Switzerland"));
    }

    @Test
    public void userPredictionsStored(){
        User user = new User("test_user", "test_user@email.com", null, "aPassword");
        GroupRepository groupRepo = new GroupRepository(neoClient);
        Group group =   groupRepo.getGroup("A");
        GroupPrediction prediction = new GroupPrediction(group, user, group.teams().toArray(new String[4]));
        user.addGroupPrediction(prediction);
        userRepo.saveUser(user, 2);

        User stored = userRepo.findUsers(user).iterator().next();
        assertThat(stored.getGroupPredictions().size(), is(1));
        assertThat(stored.getGroupPredictions().contains(prediction), is(true));
    }

    @Test
    public void scoresTablePrediction(){
        User user = new User("test_user", "test_user@email.com", null, "aPassword");
        GroupRepository groupRepo = new GroupRepository(neoClient);
        Group group =   groupRepo.getGroup("A");
        List<String> teams = group.teams();
        teams.sort(String::compareTo);
        GroupPrediction prediction = new GroupPrediction(group, user, teams.toArray(new String[4]));
        user.addGroupPrediction(prediction);
        group.getMatches().forEach(match -> {match.setAwayGoals(0); match.setHomeGoals(1);});
        assertThat(user.getPoints(), is(1));
    }

    @Test
    public void submitUserPredictions() throws JsonProcessingException {
        User user = new User("test_user", "test_user@email.com", null, "aPassword");
        GroupRepository groupRepo = new GroupRepository(neoClient);
        Group group =   groupRepo.getGroup("A");
        String[] positions = group.teams().toArray(new String[4]);
        GroupPrediction prediction = new GroupPrediction(group, user, positions);
        user.addGroupPrediction(prediction);
        userRepo.saveUser(user);

        List<String> list = Arrays.asList(prediction.getPositions());
        Collections.reverse(list);
        prediction.setPositions(list.toArray(new String[4]));
        Entity<GroupPrediction> entity = Entity.json(prediction);
        Response response = rule.client().target("/group/prediction/"+user.getId()).request()
                .header("Authorization", "Basic dGVzdF91c2VyOmFQYXNzd29yZA")
                .post(entity);

        User stored = userRepo.getUserById(user.getId());
        assertThat(response.getStatus(), is(200));
        Set<GroupPrediction> groupPredictions = stored.getGroupPredictions();
        assertThat(groupPredictions.size(), is(1));
        GroupPrediction storedPred = groupPredictions.iterator().next();
        assertThat(storedPred.getPositions()[0], is(equalTo(prediction.getPositions()[3])));
        assertThat(storedPred.getPositions()[1], is(equalTo(prediction.getPositions()[2])));
        assertThat(storedPred.getPositions()[2], is(equalTo(prediction.getPositions()[1])));
        assertThat(storedPred.getPositions()[3], is(equalTo(prediction.getPositions()[0])));
    }


}