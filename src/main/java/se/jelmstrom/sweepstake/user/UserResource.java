package se.jelmstrom.sweepstake.user;


import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.*;

@Path("/user")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {

    Logger logger = LoggerFactory.getLogger(UserResource.class);
    private final UserRepository userRepo;

    public UserResource(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response registerUser(@Valid User user){
        logger.info(user.toString());
        HashSet<User> conflictingUsers = userRepo.findUsers(user);

        logger.info(conflictingUsers.toString());
        if(conflictingUsers.isEmpty()){
            User saved = userRepo.saveUser(user);
            return Response.ok(saved).build();
        } else {
            Set<String> conflictingFields = new HashSet<>();
            conflictingUsers.stream().forEach(conflictUser -> {
                if(StringUtils.equals(user.email, conflictUser.email)){
                    conflictingFields.add("email");
                }
                if(StringUtils.equals(user.username, conflictUser.username)){
                    conflictingFields.add("username");
                }
            });
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(conflictingFields)
                    .build();
        }
    }


    @RolesAllowed("USER")
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUser(@PathParam("id") String userId){
        User users = userRepo.getUserById(userId);
        return Response.ok(users).build();
    }

}
