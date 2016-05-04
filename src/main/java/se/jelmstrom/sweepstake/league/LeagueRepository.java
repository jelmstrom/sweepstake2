package se.jelmstrom.sweepstake.league;

import se.jelmstrom.sweepstake.domain.League;
import se.jelmstrom.sweepstake.domain.User;
import se.jelmstrom.sweepstake.neo4j.Neo4jClient;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class LeagueRepository  {

    private final Neo4jClient oClient;

    public LeagueRepository(Neo4jClient oClient) {
        this.oClient = oClient;
    }

    public League getLeague(String leagueName) {
        Map<String, String> parameters = new HashMap();
        parameters.put("leagueName", leagueName);
        League league = oClient.session().queryForObject(
                League.class
                , "MATCH (u:League) where u.leagueName = {leagueName} RETURN u"
                , parameters);
        return league == null?new League():oClient.session().load(League.class, league.getId(), 1);
    }

    public Set<User> getLeagueLeaderboard(long id) {
        //load league -> user -> MatchPrediction  -> match: 4 levels
        return oClient.session().load(League.class, id, 4).getUsers();
    }
}
