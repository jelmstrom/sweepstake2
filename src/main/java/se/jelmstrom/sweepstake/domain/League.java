package se.jelmstrom.sweepstake.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.HashSet;
import java.util.Set;

@NodeEntity
public class League {
    @GraphId
    private Long id;
    @JsonProperty
    @Relationship(type= "LEAGUE", direction= Relationship.INCOMING)
    private Set<User> users  = new HashSet<>();
    @JsonProperty
    private String leagueName;

    public League() {

    }
    public League(Set<User> users, String leagueName) {
        this.users = users;
        this.leagueName = leagueName;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public String getLeagueName() {
        return leagueName;
    }

    public void setLeagueName(String leagueName) {
        this.leagueName = leagueName;
    }
}
