package se.jelmstrom.sweepstake.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.Relationship;
import org.neo4j.ogm.annotation.typeconversion.DateLong;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class Match {
    @GraphId
    @JsonProperty
    private Long id;
    @JsonProperty
    private String home;
    @JsonProperty
    private String away;
    @DateLong
    private Date kickoff;
    @JsonProperty
    private Integer homeGoals;
    @JsonProperty
    private Integer awayGoals;
    @JsonProperty
    @Relationship(type= "PREDICTON", direction= Relationship.INCOMING)
    private Set<MatchPrediction> predictions = new HashSet<>();

    @JsonProperty
    @Relationship(type= "STAGE", direction= Relationship.OUTGOING)
    private Stage stage;


    public Match() {
    }

    public Match(Long id, String home, String away, Date kickoff, Integer homeGoals, Integer awayGoals, Stage stage) {
        this.id = id;
        this.home = home;
        this.away = away;
        this.kickoff = kickoff;this.homeGoals = homeGoals;
        this.awayGoals = awayGoals;
        this.stage = stage;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHome() {
        return home;
    }

    public void setHome(String home) {
        this.home = home;
    }

    public String getAway() {
        return away;
    }

    public void setAway(String away) {
        this.away = away;
    }

    public Date getKickoff() {
        return kickoff;
    }

    public void setKickoff(Date kickoff) {
        this.kickoff = kickoff;
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
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("home", home)
                .append("away", away)
                .append("kickoff", kickoff)
                .append("homeGoals", homeGoals)
                .append("awayGoals", awayGoals)
                .toString();
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
