package se.jelmstrom.sweepstake.user;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.jelmstrom.sweepstake.domain.User;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Set;

@Path("/user")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {

    private Logger logger = LoggerFactory.getLogger(UserResource.class);
    private final UserService service;
    public UserResource(UserService service) {
        this.service = service;
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response registerUser(@Valid User user){
        logger.info(user.toString());
        if(user.getEmail() == null){
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        Set<String> conflictingFields = service.createUser(user);
        if(conflictingFields.isEmpty()) {
            return Response.ok(user).build();
        } else {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(conflictingFields)
                    .build();

        }
    }


    @POST
    @Path("/login")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response login(@Valid User user){
        User authenticated = service.authenticateUser(user.getUsername(), user.getPassword());
        if(authenticated.getId() != null) {
            return Response.ok(authenticated).build();
        } else {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

    }

    @RolesAllowed("USER")
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUser(@PathParam("id") Long userId){
        User user = service.getUserById(userId);
        return Response.ok(user).build();
    }

}
