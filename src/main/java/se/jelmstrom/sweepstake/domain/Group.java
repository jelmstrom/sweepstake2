package se.jelmstrom.sweepstake.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

/*
 Neo maps enums as properties or relationships, not as nodes
*/
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true) // for getPoints()
@NodeEntity
public class Group extends Entity{

    @JsonProperty
    @Relationship(type= "GROUP", direction= Relationship.INCOMING)
    private Set<Match> matches = new HashSet<>();

    @JsonProperty
    private String groupName;

    @JsonProperty
    @Relationship(type = "GROUPPREDICTION", direction = Relationship.INCOMING)
    private Set<GroupPrediction> groupPredictions = new HashSet<>();

    public Group() {
    }

    public Group(String name) {
        this.groupName = name;
    }

    public Group(String name, Set<Match> matches) {
        this.groupName = name;
        this.matches = matches;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<Match> getMatches() {
        return matches;
    }

    public void setMatches(Set<Match> matches) {
        this.matches = matches;
    }

    public Set<GroupPrediction> getGroupPredictions() {
        return groupPredictions;
    }

    public boolean addPrediction(GroupPrediction prediction){
        return groupPredictions.add(prediction);
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
                .sorted().collect(toList());  // sort them
        Collections.reverse(recordMap); // reverse list (since natural order is ascending)
        return recordMap;
    }


    public List<String> teams(){
        return matches.stream()
                    .flatMap(match -> Arrays.asList(match.getHome(), match.getAway()).stream())
                    .distinct()
                    .collect(Collectors.toList());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Group)) return false;
        Group group = (Group) o;
        return  Objects.equals(id, group.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(groupName,id);
    }
}
