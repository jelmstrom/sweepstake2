package se.jelmstrom.sweepstake.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.HashSet;
import java.util.Set;

@NodeEntity
public class League extends Entity{

    @JsonProperty
    private String leagueName;
    @Relationship(type= "LEAGUE", direction= Relationship.INCOMING)
    private Set<User> users  = new HashSet<>();

    public League() {

    }
    public League(Set<User> users, String leagueName) {
        this.users = users;
        this.leagueName = leagueName;
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
