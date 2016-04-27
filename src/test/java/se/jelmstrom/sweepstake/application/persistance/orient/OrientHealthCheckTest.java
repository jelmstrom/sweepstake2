package se.jelmstrom.sweepstake.application.persistance.orient;


import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.setup.Environment;
import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit.DropwizardAppRule;
import org.junit.ClassRule;
import org.junit.Test;
import se.jelmstrom.sweepstake.application.SweepstakeConfiguration;
import se.jelmstrom.sweepstake.application.SweepstakeMain;

import javax.ws.rs.client.Client;
import javax.ws.rs.core.Response;

import static org.assertj.core.api.Assertions.assertThat;


public class OrientHealthCheckTest {


    @ClassRule
    public static final DropwizardAppRule<SweepstakeConfiguration> RULE =
            new DropwizardAppRule<>(SweepstakeMain.class, ResourceHelpers.resourceFilePath("test.yml"));


    @Test
    public void testHealthCheck() {
        Environment env = RULE.getEnvironment();
        Client client = new JerseyClientBuilder(env).build("test client");
        Response response = client.target("http://localhost:8999/healthcheck")
                .request().get();

        assertThat(response.getStatus()).isEqualTo(200);
    }



}
