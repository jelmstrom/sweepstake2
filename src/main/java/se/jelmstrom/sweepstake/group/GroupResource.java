package se.jelmstrom.sweepstake.group;

import se.jelmstrom.sweepstake.domain.Group;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/group")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class GroupResource {
    private final GroupService service;

    public GroupResource(GroupService service) {
        this.service = service;
    }

    @GET
    @Path("/{name}")
    public Response getStage(@PathParam("name") String stageName){
        Group.CompetitionStage stage = Group.CompetitionStage.valueOf(stageName);
        Group group1 = service.getStage(stage);
        return Response.ok(group1).build();
    }

}
