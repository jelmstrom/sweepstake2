package se.jelmstrom.sweepstake.league;

import se.jelmstrom.sweepstake.domain.League;
import se.jelmstrom.sweepstake.domain.User;
import se.jelmstrom.sweepstake.user.NeoUserRepository;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/league")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class LeagueResource {
    private final NeoUserRepository repo;

    public LeagueResource(NeoUserRepository repo) {
        this.repo = repo;
    }

    @RolesAllowed("USER")
    @POST
    @Path("/{name}")
    public Response createLeague(User user, @PathParam("name") String leagueName){
        League existing = repo.getLeague(leagueName);
        if(existing.getId() != null){
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        League league = new League();
        league.setLeagueName(leagueName);
        User stored = repo.getUserById(user.getId());
        stored.getLeagues().add(league);
        repo.saveUser(stored);
        return Response.ok(stored).build();
    }

    @RolesAllowed("USER")
    @POST
    @Path("/{name}/join")
    public Response joinLegue(User user, @PathParam("name") String leagueName){
        League league = repo.getLeague(leagueName);
        if(league.getId() == null){
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        User stored = repo.getUserById(user.getId());
        stored.getLeagues().add(league);
        league.getUsers().add(stored);
        repo.saveUser(stored);
        return Response.ok(stored).build();
    }
}
