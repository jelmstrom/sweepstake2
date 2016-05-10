package se.jelmstrom.sweepstake.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toMap;

/*
 Neo maps enums as properties or relationships, not as nodes
*/
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true) // for getPoints()
@NodeEntity
public class Group extends Entity{

    @JsonProperty
    private CompetitionStage stage;
    @JsonProperty
    @Relationship(type= "GROUP", direction= Relationship.INCOMING)
    private Set<Match> matches = new HashSet<>();

    public Group() {
    }

    public Group(CompetitionStage stage) {
        this.stage = stage;
    }

    public Group(CompetitionStage stage, Set<Match> matches) {
        this.stage = stage;
        this.matches = matches;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CompetitionStage getStage() {
        return stage;
    }

    public void setStage(CompetitionStage stage) {
        this.stage = stage;
    }

    public Set<Match> getMatches() {
        return matches;
    }

    public void setMatches(Set<Match> matches) {
        this.matches = matches;
    }

    @JsonProperty
    public List<TeamRecord> getStandings() {
        List<TeamRecord> recordMap = this.getMatches().stream()
                .flatMap(match -> match.records()) // returns list of records for each match
                .collect(toMap(  // puth them in a map
                        TeamRecord::getTeam // team name is key
                        , i -> i  // record is value
                        , TeamRecord::merge // merge records with same key
                )).values().stream() // get the values
                .sorted().collect(Collectors.toList());  // sort them
        Collections.reverse(recordMap); // reverse list (since natural order is ascending)
        return recordMap;
    }

}
