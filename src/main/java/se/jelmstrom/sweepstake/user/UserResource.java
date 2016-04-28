package se.jelmstrom.sweepstake.user;


import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashSet;
import java.util.Set;

@Path("/user")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {

    Logger logger = LoggerFactory.getLogger(UserResource.class);
    private final NeoUserRepository userRepo;

    public UserResource(NeoUserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response registerUser(@Valid User user){
        logger.info(user.toString());
        if(user.getEmail() == null){
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        Set<User> conflictingUsers = userRepo.findUsers(user);

        logger.info(conflictingUsers.toString());
        if(conflictingUsers.isEmpty()){
            User saved = userRepo.saveUser(user);
            return Response.ok(saved).build();
        } else {
            Set<String> conflictingFields = new HashSet<>();
            conflictingUsers.stream().forEach(conflictUser -> {
                if(StringUtils.equals(user.getEmail(), conflictUser.getEmail())){
                    conflictingFields.add("email");
                }
                if(StringUtils.equals(user.getUsername(), conflictUser.getUsername())){
                    conflictingFields.add("username");
                }
            });
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
        User authenticated = userRepo.authenticateUser(user.getUsername(), user.getPassword());
        if(authenticated.getUserId() != null) {
            return Response.ok(authenticated).build();
        } else {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

    }

    @RolesAllowed("USER")
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUser(@PathParam("id") String userId){
        User user = userRepo.getUserById(userId);
        return Response.ok(user).build();
    }

}
