package se.jelmstrom.sweepstake.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.neo4j.ogm.annotation.Relationship;
import org.neo4j.ogm.annotation.typeconversion.DateLong;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

import static org.neo4j.ogm.annotation.Relationship.INCOMING;
import static org.neo4j.ogm.annotation.Relationship.OUTGOING;

public class Match extends Entity {
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
    @Relationship(type = "PREDICTON", direction = INCOMING)
    private Set<MatchPrediction> predictions = new HashSet<>();

    @JsonProperty
    @Relationship(type = "GROUP", direction = OUTGOING)
    private Group group;

    @JsonProperty
    private CompetitionStage stage;


    public Match() {
    }

    public Match(String home, String away, Date kickoff, Group group) {
        this.group = group;
        this.kickoff = kickoff;
        this.away = away;
        this.home = home;
    }

    public Match(Long id, String home, String away, Date kickoff, Integer homeGoals, Integer awayGoals, Group group) {
        this.id = id;
        this.home = home;
        this.away = away;
        this.kickoff = kickoff;
        this.homeGoals = homeGoals;
        this.awayGoals = awayGoals;
        this.group = group;
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

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public boolean hasResult() {
        return awayGoals != null && homeGoals != null;
    }

    public Integer homePoints() {
        return points(homeGoals, awayGoals);
    }
    public Integer awayPoints() {
        return points(awayGoals, homeGoals);
    }

    private Integer points(Integer awayGoals, Integer homeGoals) {
        if(hasResult()){
            int factor =  awayGoals.compareTo(homeGoals);
            switch (factor) {
                case 1: return 3;
                case 0: return 1;
            }
        }
        return 0;
    }

    public Stream<TeamRecord> records() {
        return Arrays.asList(
                new TeamRecord(home, homeGoals, awayGoals, homePoints())
                , new TeamRecord(away, awayGoals, homeGoals, awayPoints())).stream();

    }
}
