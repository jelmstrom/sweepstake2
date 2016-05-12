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
    public Response getStage(@PathParam("name") String groupName){
        Group group = service.getStage(groupName);
        return Response.ok(group).build();
    }

}
