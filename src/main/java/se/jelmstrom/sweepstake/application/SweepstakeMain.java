package se.jelmstrom.sweepstake.application;

import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.forms.MultiPartBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.eclipse.jetty.servlets.CrossOriginFilter;
import se.jelmstrom.sweepstake.application.authenticator.UserAuthenticator;
import se.jelmstrom.sweepstake.group.GroupRepository;
import se.jelmstrom.sweepstake.group.GroupResource;
import se.jelmstrom.sweepstake.group.GroupService;
import se.jelmstrom.sweepstake.league.LeagueRepository;
import se.jelmstrom.sweepstake.league.LeagueResource;
import se.jelmstrom.sweepstake.league.LeagueService;
import se.jelmstrom.sweepstake.neo4j.Neo4jClient;
import se.jelmstrom.sweepstake.neo4j.NeoConfigHealthCheck;
import se.jelmstrom.sweepstake.user.NeoUserRepository;
import se.jelmstrom.sweepstake.user.UserResource;
import se.jelmstrom.sweepstake.user.UserService;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration.Dynamic;
import java.util.EnumSet;

public class SweepstakeMain extends Application<SweepstakeConfiguration> {
    @Override
    public void run(SweepstakeConfiguration config, Environment environment) throws Exception {

        Neo4jClient neo4jClient = new Neo4jClient(config.getNeoConfiguration());
        environment.lifecycle().manage(neo4jClient);

        NeoUserRepository userRepository = new NeoUserRepository(neo4jClient);
        environment.jersey().register(new UserResource(new UserService(userRepository)));
        environment.jersey().register(new LeagueResource(userRepository, new LeagueService(new LeagueRepository(neo4jClient), userRepository)));
        environment.jersey().register(new GroupResource(new GroupService(new GroupRepository(neo4jClient))));


        Dynamic filter = environment.servlets().addFilter("CORS", CrossOriginFilter.class);
        filter.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");
        filter.setInitParameter("allowedOrigins", "http://localhost:63343");
        filter.setInitParameter("allowedHeaders", "Content-Type,Authorization,X-Requested-With,Content-Length,Accept,Origin,Access-Control-Request-Method,Access-Control-Allow-Origin");
        filter.setInitParameter("allowedMethods", "GET,PUT,POST,DELETE,OPTIONS");
        filter.setInitParameter("preflightMaxAge", "5184000"); // 2 months
        filter.setInitParameter("allowCredentials", "true");

        UserAuthenticator.register(environment, config, userRepository);
        environment.healthChecks().register("neo4j", new NeoConfigHealthCheck(neo4jClient));
    }


    @Override
    public void initialize(Bootstrap<SweepstakeConfiguration> bootstrap) {
        bootstrap.addBundle(new AssetsBundle("/assets/application", "/", null, "app"));
        bootstrap.addBundle(new MultiPartBundle());
    }

    public static void main(String[] args) throws Exception {
        new SweepstakeMain().run(args);
    }
}
