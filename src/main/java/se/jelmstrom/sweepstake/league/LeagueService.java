package se.jelmstrom.sweepstake.league;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.jelmstrom.sweepstake.domain.League;
import se.jelmstrom.sweepstake.domain.User;
import se.jelmstrom.sweepstake.user.UserRepository;

import java.util.Set;

public class LeagueService {
    private final LeagueRepository repo;
    private final UserRepository userRepo;
    private static final Logger log = LoggerFactory.getLogger(LeagueService.class);

    public LeagueService(LeagueRepository repo, UserRepository userRepo) {
        this.repo = repo;
        this.userRepo = userRepo;
    }

    public League getLeague(String leagueName) {
        return repo.getLeague(leagueName);
    }

    public League createLeague(User user,  String leagueName) {
        League existing = getLeague(leagueName);
        if(existing.getId() != null){
            log.info("League " + leagueName + " already exists");
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
