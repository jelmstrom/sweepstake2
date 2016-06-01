package se.jelmstrom.sweepstake.league;

import se.jelmstrom.sweepstake.domain.League;
import se.jelmstrom.sweepstake.domain.User;
import se.jelmstrom.sweepstake.user.UserRepository;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Set;

@Path("/league")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class LeagueResource {
    private final UserRepository repo;
    private final LeagueService service;

    public LeagueResource(UserRepository repo, LeagueService service) {
        this.repo = repo;
        this.service = service;
    }

    @RolesAllowed("USER")
    @POST
    @Path("/{name}")
    public Response createLeague(User user, @PathParam("name") String leagueName){
        League league =service.createLeague(user, leagueName);

        if(league == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        } else {
            if(!league.getUsers().contains(user)){
                throw new RuntimeException("User not added to league...."); //fail hard.
            }
            return Response.ok(league.getUsers().stream().findFirst().get()).build();
        }
    }



    @RolesAllowed("USER")
    @POST
    @Path("/{name}/join")
    public Response joinLegue(User user, @PathParam("name") String leagueName){
        League league = service.getLeague(leagueName);
        if(league.getId() == null){
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        User stored = repo.getUserById(user.getId());
        stored.getLeagues().add(league);
        league.getUsers().add(stored);
        repo.saveUser(stored);
        return Response.ok(stored).build();
    }

    @RolesAllowed("USER")
    @GET
    @Path("/{id}/leaderboard")
    public Response leaderboard(@PathParam("id") long id){
        Set<User> users = service.getLeagueLeaderboard(id);
        return Response.ok(users).build();
    }
}
