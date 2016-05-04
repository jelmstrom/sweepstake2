package se.jelmstrom.sweepstake.league;

import se.jelmstrom.sweepstake.domain.League;
import se.jelmstrom.sweepstake.domain.User;
import se.jelmstrom.sweepstake.user.NeoUserRepository;

import java.util.Set;

public class LeagueService {
    private final LeagueRepository repo;
    private final NeoUserRepository userRepo;

    public LeagueService(LeagueRepository repo, NeoUserRepository userRepo) {
        this.repo = repo;
        this.userRepo = userRepo;
    }

    public League getLeague(String leagueName) {
        return repo.getLeague(leagueName);
    }

    public League createLeague(User user,  String leagueName) {
        League existing = getLeague(leagueName);
        if(existing.getId() != null){
            return null;
        }
        League league = new League();
        league.setLeagueName(leagueName);


        User stored = userRepo.getUserById(user.getId());
        stored.getLeagues().add(league);
        league.getUsers().add(stored);
        userRepo.saveUser(stored);
        return league;
    }

    public Set<User> getLeagueLeaderboard(long id) {
        Set<User> leagueLeaderboard = repo.getLeagueLeaderboard(id);
        // calculate points..
        // sort
        return leagueLeaderboard;
    }
}
