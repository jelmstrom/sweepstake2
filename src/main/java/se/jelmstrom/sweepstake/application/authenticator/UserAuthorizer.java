package se.jelmstrom.sweepstake.application.authenticator;

import io.dropwizard.auth.Authorizer;
import se.jelmstrom.sweepstake.user.User;

import java.security.Principal;

/**
 * Created by jelmstrom on 15/04/16.
 */
public class UserAuthorizer implements Authorizer<Principal> {
    @Override
    public boolean authorize(Principal principal, String role) {
        //get user and verify role.
        return ((User)principal).roles().contains(role);
    }
}
