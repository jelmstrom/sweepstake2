package se.jelmstrom.sweepstake.application;

import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.forms.MultiPartBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.eclipse.jetty.servlets.CrossOriginFilter;
import se.jelmstrom.sweepstake.application.authenticator.UserAuthenticator;
import se.jelmstrom.sweepstake.orient.OrientClient;
import se.jelmstrom.sweepstake.orient.OrientHealthCheck;
import se.jelmstrom.sweepstake.user.UserRepository;
import se.jelmstrom.sweepstake.user.UserResource;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import javax.servlet.FilterRegistration.Dynamic;
import java.util.EnumSet;

public class SweepstakeMain extends Application<SweepstakeConfiguration> {
    @Override
    public void run(SweepstakeConfiguration config, Environment environment) throws Exception {

        OrientClient orientClient = new OrientClient(config);
        environment.lifecycle().manage(orientClient);
        UserRepository userRepo = new UserRepository(orientClient);
        environment.jersey().register(new UserResource(userRepo));
        environment.healthChecks().register("database", new OrientHealthCheck(orientClient));

        Dynamic filter = environment.servlets().addFilter("CORS", CrossOriginFilter.class);
        filter.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");
        filter.setInitParameter("allowedOrigins", "http://localhost:63342");
        filter.setInitParameter("allowedHeaders", "Content-Type,Authorization,X-Requested-With,Content-Length,Accept,Origin,Access-Control-Request-Method,Access-Control-Allow-Origin");
        filter.setInitParameter("allowedMethods", "GET,PUT,POST,DELETE,OPTIONS");
        filter.setInitParameter("preflightMaxAge", "5184000"); // 2 months
        filter.setInitParameter("allowCredentials", "true");

        UserAuthenticator.register(environment, config, userRepo);
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
