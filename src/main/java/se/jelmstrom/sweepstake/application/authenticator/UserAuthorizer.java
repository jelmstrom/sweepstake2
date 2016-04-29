package se.jelmstrom.sweepstake.application.authenticator;

import io.dropwizard.auth.Authorizer;
import se.jelmstrom.sweepstake.domain.User;

import java.security.Principal;

public class UserAuthorizer implements Authorizer<Principal> {
    @Override
    public boolean authorize(Principal principal, String role) {
        //get user and verify role.
        return ((User)principal).roles().contains(role);
    }
}
