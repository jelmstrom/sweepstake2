package se.jelmstrom.sweepstake.group;

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
import se.jelmstrom.sweepstake.domain.CompetitionStage;
import se.jelmstrom.sweepstake.domain.Group;
import se.jelmstrom.sweepstake.domain.TeamRecord;
import se.jelmstrom.sweepstake.neo4j.Neo4jClient;
import se.jelmstrom.sweepstake.user.NeoUserRepository;

import javax.ws.rs.core.Response;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.Principal;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class GroupResourceTest {
    private static final NeoConfiguration config = new NeoConfiguration(
            "192.168.59.103:7474"
            , "local"
            , "neo4j");
    private static final Neo4jClient neoClient = new Neo4jClient(config);
    private static final NeoUserRepository matchRepo = new NeoUserRepository(neoClient);

    private static NeoUserRepository userRepo = new NeoUserRepository(neoClient);
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
            .addResource(new GroupResource(new GroupService(repo)))
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
        Group group = repo.getStage(CompetitionStage.GROUP_A);
        group.getMatches().forEach(match -> {
            match.setAwayGoals(0);
            match.setHomeGoals(1);
        });
        group = repo.saveMatches(group);
        Response groupResponse = rule.client().target("/group/GROUP_A").request()
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



}