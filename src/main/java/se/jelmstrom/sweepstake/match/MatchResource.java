package se.jelmstrom.sweepstake.match;

import se.jelmstrom.sweepstake.domain.Match;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/match")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MatchResource {

    private final NeoMatchRepo repo;

    public MatchResource(NeoMatchRepo repo) {
        this.repo = repo;
    }

    @GET
    @Path("/list")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Match> addMatch(){
        return null;
    }
}
