package se.jelmstrom.sweepstake.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.neo4j.ogm.annotation.EndNode;
import org.neo4j.ogm.annotation.RelationshipEntity;
import org.neo4j.ogm.annotation.StartNode;

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
}
