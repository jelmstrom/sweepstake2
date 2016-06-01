package se.jelmstrom.sweepstake.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.neo4j.ogm.annotation.EndNode;
import org.neo4j.ogm.annotation.RelationshipEntity;
import org.neo4j.ogm.annotation.StartNode;

import java.util.Objects;

@RelationshipEntity(type = "PREDICTON")
public class MatchPrediction extends Entity{
    @StartNode
    @JsonProperty
    private User user;
    @JsonProperty
    @EndNode
    private Match match;
    @JsonProperty
    private Integer homeGoals;
    @JsonProperty
    private Integer awayGoals;

    public MatchPrediction() {
    }

    public MatchPrediction(Long id, User user, Match match, Integer homeGoals, Integer awayGoals) {
        this.id = id;
        this.user = user;
        this.match = match;
        this.homeGoals = homeGoals;
        this.awayGoals = awayGoals;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Match getMatch() {
        return match;
    }

    public void setMatch(Match match) {
        this.match = match;
    }

    public Integer getHomeGoals() {
        return homeGoals;
    }

    public void setHomeGoals(Integer homeGoals) {
        this.homeGoals = homeGoals;
    }

    public Integer getAwayGoals() {
        return awayGoals;
    }

    public void setAwayGoals(Integer awayGoals) {
        this.awayGoals = awayGoals;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MatchPrediction)) return false;
        MatchPrediction that = (MatchPrediction) o;
        return Objects.equals(user, that.user) &&
                Objects.equals(match, that.match);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, match);
    }

    /*
        Points for
         - correct winner (sign of compare  (1,0,-1) is equal)
         - correct number of goals for each team
     */
    int score() {
        if(!match.hasResult() || !this.hasResult()){
            return 0;
        }
        int points = 0;
        if(homeGoals.equals(match.getHomeGoals())){
            points++;
        }
        if(awayGoals.equals(match.getAwayGoals())){
            points++;
        }
        if(Integer.compare(homeGoals, awayGoals) == Integer.compare(match.getHomeGoals(), match.getAwayGoals())){
            points++;
        }
        return points;
    }

    public String ofGroupName(){
        return match.getGroup().getGroupName();
    }

    private boolean hasResult() {
        return homeGoals != null && awayGoals != null;
    }
}
