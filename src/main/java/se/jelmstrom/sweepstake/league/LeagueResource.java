package se.jelmstrom.sweepstake.league;

import se.jelmstrom.sweepstake.domain.League;
import se.jelmstrom.sweepstake.domain.User;
import se.jelmstrom.sweepstake.user.NeoUserRepository;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

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
    public User createLeague(User user, @PathParam("name") String leagueName){
        League league = new League();
        league.setLeagueName(leagueName);
        User stored = repo.getUserById(user.getId());
        stored.getLeagues().add(league);
        repo.saveUser(stored);
        return stored;
    }
}
