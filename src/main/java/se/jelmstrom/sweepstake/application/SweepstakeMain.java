package se.jelmstrom.sweepstake.application;

import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.forms.MultiPartBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.swagger.config.ScannerFactory;
import io.swagger.jaxrs.config.BeanConfig;
import io.swagger.jaxrs.config.DefaultJaxrsScanner;
import io.swagger.jersey.listing.ApiListingResourceJSON;
import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.jelmstrom.sweepstake.application.authenticator.UserAuthenticator;
import se.jelmstrom.sweepstake.group.GroupRepository;
import se.jelmstrom.sweepstake.group.GroupResource;
import se.jelmstrom.sweepstake.group.GroupService;
import se.jelmstrom.sweepstake.league.LeagueRepository;
import se.jelmstrom.sweepstake.league.LeagueResource;
import se.jelmstrom.sweepstake.league.LeagueService;
import se.jelmstrom.sweepstake.match.MatchResource;
import se.jelmstrom.sweepstake.match.MatchService;
import se.jelmstrom.sweepstake.match.NeoMatchRepo;
import se.jelmstrom.sweepstake.neo4j.Neo4jClient;
import se.jelmstrom.sweepstake.neo4j.NeoConfigHealthCheck;
import se.jelmstrom.sweepstake.user.UserRepository;
import se.jelmstrom.sweepstake.user.UserResource;
import se.jelmstrom.sweepstake.user.UserService;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration.Dynamic;
import java.util.EnumSet;

public class SweepstakeMain extends Application<SweepstakeConfiguration> {
    private static final Logger log = LoggerFactory.getLogger(SweepstakeMain.class);
    @Override
    public void run(SweepstakeConfiguration config, Environment environment) throws Exception {

        Neo4jClient neo4jClient = new Neo4jClient(config.getNeoConfiguration());
        environment.lifecycle().manage(neo4jClient);

        UserRepository userRepository = new UserRepository(neo4jClient);
        UserService userService = new UserService(userRepository);
        environment.jersey().register(new UserResource(userService));
        LeagueRepository leagueRepository = new LeagueRepository(neo4jClient);
        LeagueService leagueService = new LeagueService(leagueRepository, userRepository);
        environment.jersey().register(new LeagueResource(userRepository, leagueService));
        GroupService groupService = new GroupService(new GroupRepository(neo4jClient));
        environment.jersey().register(new GroupResource(groupService, userService));
        NeoMatchRepo repo = new NeoMatchRepo(neo4jClient);
        MatchService service = new MatchService(repo);
        environment.jersey().register(new MatchResource(service));
        configureSwagger(environment);


        Dynamic filter = environment.servlets().addFilter("CORS", CrossOriginFilter.class);
        filter.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");
        log.info("CORS: " + config.corsLocations);
        filter.setInitParameter("allowedOrigins", config.corsLocations);
        filter.setInitParameter("allowedHeaders", "Content-Type,Authorization,X-Requested-With,Content-Length,Accept,Origin,Access-Control-Request-Method,Access-Control-Allow-Origin");
        filter.setInitParameter("allowedMethods", "GET,PUT,POST,DELETE,OPTIONS");
        filter.setInitParameter("preflightMaxAge", "5184000"); // 2 months
        filter.setInitParameter("allowCredentials", "true");

        UserAuthenticator.register(environment, config, userRepository);
        environment.healthChecks().register("neo4j", new NeoConfigHealthCheck(neo4jClient));
    }

    void configureSwagger(Environment environment) {

        environment.jersey().register(new ApiListingResourceJSON());
        ScannerFactory.setScanner(new DefaultJaxrsScanner());

        BeanConfig beanConfig = new BeanConfig();
        beanConfig.setResourcePackage("se.jelmstrom.sweepstake");
        beanConfig.setTitle("Sweepstake");
        beanConfig.setSchemes(new String[]{"http"});
        beanConfig.setHost("localhost:8081");
        beanConfig.setBasePath("/rest");
        beanConfig.setScan(true);

    }


    @Override
    public void initialize(Bootstrap<SweepstakeConfiguration> bootstrap) {
        bootstrap.addBundle(new AssetsBundle("/assets/application", "/", null, "app"));
        bootstrap.addBundle(new AssetsBundle("/assets/swagger", "/swagger", null, "swagger"));
        bootstrap.addBundle(new MultiPartBundle());
    }

    public static void main(String[] args) throws Exception {
        new SweepstakeMain().run(args);
    }
}
