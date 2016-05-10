package se.jelmstrom.sweepstake.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.ObjectUtils;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true) // for getPoints()
public class TeamRecord implements Comparable{

    private  String team;
    private  Integer goalsFor;
    private  Integer goalsAgainst;
    private  Integer points;
    private int played;

    public TeamRecord() {
    }

    public TeamRecord(String team, Integer goalsFor, Integer goalsAgainst, Integer points) {
        this.team = team;
        this.goalsFor = goalsFor;
        this.goalsAgainst = goalsAgainst;
        this.points = points;
        if(isPlayed()){
            played = 1;
        }
    }

    private boolean isPlayed() {
        return goalsFor != null && goalsAgainst != null && points != null;
    }


    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public Integer getGoalsFor() {
        return goalsFor;
    }

    public void setGoalsFor(Integer goalsFor) {
        this.goalsFor = goalsFor;
    }

    public Integer getGoalsAgainst() {
        return goalsAgainst;
    }

    public void setGoalsAgainst(Integer goalsAgainst) {
        this.goalsAgainst = goalsAgainst;
    }

    @JsonProperty
    public Integer getGoalDifference() {
        if(goalsFor != null && goalsAgainst != null){
            return goalsFor - goalsAgainst;
        }
        return null;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }



    private TeamRecord merge(TeamRecord record){
        if(!this.isPlayed()) {
            return record;
        } else if (!record.isPlayed()){
            return this;
        } else {
            goalsFor= (goalsFor + record.goalsFor);
            goalsAgainst= goalsAgainst + record.goalsAgainst;
            points= points + record.points;
            played += record.played;
        }

        return this;

    }


    public static TeamRecord merge(TeamRecord record, TeamRecord two){
        return record.merge(two);
    }


    public  int getPlayed() {
        return played;
    }



    @Override
    public int compareTo(Object o) {
        int result = 0;
        if(o instanceof TeamRecord){
            TeamRecord that = (TeamRecord) o;
                result = ObjectUtils.compare(this.points, that.points);
                if (result != 0) {
                    return result;
                }
                result = ObjectUtils.compare(this.getGoalDifference(), that.getGoalDifference());
                if (result != 0) {
                    return result;
                }
                result = ObjectUtils.compare(this.getGoalsFor(), that.getGoalsFor());
                if (result != 0) {
                    return result;
                }

                result = that.getTeam().compareTo(this.getTeam());
                if (result != 0) {
                    return result;
                }
        }
        return result;
    }
}
