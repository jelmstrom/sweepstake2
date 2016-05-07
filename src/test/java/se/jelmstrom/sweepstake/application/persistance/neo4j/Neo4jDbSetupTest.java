package se.jelmstrom.sweepstake.application.persistance.neo4j;

import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.setup.Environment;
import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit.DropwizardAppRule;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.neo4j.ogm.session.Session;
import se.jelmstrom.sweepstake.application.NeoConfiguration;
import se.jelmstrom.sweepstake.application.SweepstakeConfiguration;
import se.jelmstrom.sweepstake.application.SweepstakeMain;
import se.jelmstrom.sweepstake.domain.User;
import se.jelmstrom.sweepstake.neo4j.Neo4jClient;

import javax.ws.rs.client.Client;
import javax.ws.rs.core.Response;
import java.util.Collection;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;

public class Neo4jDbSetupTest {

    private Neo4jClient neo4jClient;
    @ClassRule
    public static final DropwizardAppRule<SweepstakeConfiguration> RULE =
            new DropwizardAppRule<>(SweepstakeMain.class
                    , ResourceHelpers.resourceFilePath("test.yml"));


    @Test
    public void testHealthCheck() {
        Environment env = RULE.getEnvironment();
        Client client = new JerseyClientBuilder(env).build("test client");
        Response response = client.target("http://localhost:8999/healthcheck")
                .request().get();
        Assertions.assertThat(response.getStatus()).isEqualTo(200);
    }

    @Before
    public void setUp(){
        NeoConfiguration neoConfiguration = new NeoConfiguration();
        neoConfiguration.setCredentials("local");
        neoConfiguration.setUser("neo4j");
        neoConfiguration.setLocation("192.168.59.103:7474");
        neo4jClient = new Neo4jClient(neoConfiguration);

    }

    @Test
    public void initGraph(){
        Session session = neo4jClient.session();
        session.beginTransaction();
        User object = new User("tst", "tst", null, "");
        session.save(object, 1);
        Collection<User> neoUsers = session.loadAll(User.class);
        assertThat(neoUsers.size(), is(greaterThan(0)));
        session.delete(object);
    }

}
