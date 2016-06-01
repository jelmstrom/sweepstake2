package se.jelmstrom.sweepstake.match;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.jelmstrom.sweepstake.domain.MatchPrediction;
import se.jelmstrom.sweepstake.domain.User;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Path("/match")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MatchResource {
    private static final Logger log = LoggerFactory.getLogger(MatchResource.class);
    private final MatchService service;

    public MatchResource(MatchService service) {
        this.service = service;
    }

    @RolesAllowed("USER")
    @POST
    @Path("/predictions")
    public Response submitPreditcions(@Context SecurityContext context, List<MatchPrediction> predictions){

        User user = (User) context.getUserPrincipal();
        List<MatchPrediction> collect = predictions.stream().filter(pred -> !pred.getUser().equals(user)).collect(toList());
        if(collect.isEmpty()){
            log.debug(predictions.toString());
            service.savePredictions(predictions, user);
            return Response.ok().build();
        } else {
            return Response.status(403).build();
        }

    }
}
