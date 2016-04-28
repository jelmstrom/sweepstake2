package se.jelmstrom.sweepstake.application.authenticator;

import com.google.common.base.Optional;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import io.dropwizard.auth.basic.BasicCredentialAuthFilter;
import io.dropwizard.auth.basic.BasicCredentials;
import io.dropwizard.setup.Environment;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
import se.jelmstrom.sweepstake.application.SweepstakeConfiguration;
import se.jelmstrom.sweepstake.user.NeoUserRepository;
import se.jelmstrom.sweepstake.user.User;

import java.security.Principal;

public class UserAuthenticator implements Authenticator<BasicCredentials, Principal> {
    private final NeoUserRepository userRepo;

    public UserAuthenticator(NeoUserRepository userRepo) {
        this.userRepo = userRepo;
    }


    @Override
    public Optional<Principal> authenticate(BasicCredentials credentials) throws AuthenticationException {


        User user = userRepo.authenticateUser(credentials.getUsername(), credentials.getPassword());
        if(user.getUserId() != null){
            return Optional.of(user);
        }
        return Optional.absent();
    }


    public static void register(Environment environment, SweepstakeConfiguration config, NeoUserRepository repo) {

        BasicCredentialAuthFilter<Principal> filter = new BasicCredentialAuthFilter.Builder<>()
                .setAuthenticator(new UserAuthenticator(repo))
                .setAuthorizer(new UserAuthorizer())
                .setRealm("vmtips")
                .buildAuthFilter();
        AuthDynamicFeature authDynamicFeature = new AuthDynamicFeature(filter);
        environment.jersey().register(authDynamicFeature);
        environment.jersey().register(RolesAllowedDynamicFeature.class);
    }
}
