package se.jelmstrom.sweepstake.group;

import se.jelmstrom.sweepstake.domain.Group;
import se.jelmstrom.sweepstake.domain.GroupPrediction;
import se.jelmstrom.sweepstake.domain.User;
import se.jelmstrom.sweepstake.user.UserService;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

@Path("/group")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class GroupResource {
    private final GroupService service;
    private final UserService userService;

    public GroupResource(GroupService service, UserService userService) {
        this.service = service;
        this.userService = userService;
    }

    @GET
    @Path("/{name}")
    public Response getGroup(@PathParam("name") String groupName){
        Group group = service.getStage(groupName);
        return Response.ok(group).build();
    }

    @RolesAllowed("USER")
    @POST
    @Path("/prediction/{userId}")
    public Response predictGroup(@Context SecurityContext context, @Valid GroupPrediction prediction){
        User u = (User) context.getUserPrincipal();
        if(u.getId() != prediction.getUser().getId()){
            Response.status(401).build();
        }
        User userById = userService.getUserById(u.getId());
        service.storePrediction(prediction, userById);

        return Response.ok().build();
    }

}
