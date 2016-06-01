package se.jelmstrom.sweepstake.user;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.jelmstrom.sweepstake.domain.MatchPrediction;
import se.jelmstrom.sweepstake.domain.User;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

@Api(value = "/user/", description = "user stuff")
@Path("/user/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {

    private Logger logger = LoggerFactory.getLogger(UserResource.class);
    private final UserService service;
    public UserResource(UserService service) {
        this.service = service;
    }

    @ApiOperation(value = "Register a user", response = Response.class)
    @ApiResponses(value = {
            @ApiResponse(code=400, message = "Username or email taken")
    })
    @POST
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


    @ApiOperation(value = "get user predictions")
    @RolesAllowed("USER")
    @GET
    @Path("/prediction/{groupId}")
    public List<MatchPrediction> getUserPredictions(@Context SecurityContext context, @PathParam("groupId") String groupId){
        User userPrincipal = (User) context.getUserPrincipal();
        List<MatchPrediction> matchPredictions = service.predictionsFor(userPrincipal, groupId);
        Iterator<MatchPrediction> iterator = matchPredictions.iterator();
        while(iterator.hasNext()) {
            MatchPrediction next = iterator.next();
            next.getUser().getPredictions().clear(); // scrub json
            next.getMatch().getGroup().getMatches().clear();
        }
        return matchPredictions;
    }



    @POST
    @Path("/login")
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
    public Response getUser(@PathParam("id") Long userId){
        User user = service.getUserById(userId);
        return Response.ok(user).build();
    }

}
