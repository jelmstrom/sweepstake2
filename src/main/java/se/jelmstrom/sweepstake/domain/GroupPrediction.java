package se.jelmstrom.sweepstake.domain;

import org.neo4j.ogm.annotation.EndNode;
import org.neo4j.ogm.annotation.RelationshipEntity;
import org.neo4j.ogm.annotation.StartNode;

import java.util.List;
import java.util.Objects;

@RelationshipEntity(type="TABLEPREDICTION")
public class GroupPrediction extends Entity {

    @EndNode
    private Group group;
    @StartNode
    private User user;
    private String[] positions = new String[4];

    public GroupPrediction() {
    }

    public GroupPrediction(Group group, User user, String[] positions) {
        this.group = group;
        this.user = user;
        this.positions = positions;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String[] getPositions() {
        return positions;
    }

    public void setPositions(String[] positions) {
        this.positions = positions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GroupPrediction)) return false;
        GroupPrediction that = (GroupPrediction) o;
        return Objects.equals(group, that.group) &&
                Objects.equals(user, that.user);
    }
    @Override
    public int hashCode() {
        int hash = Objects.hash(group, user);
        return hash;
    }

    public int score() {
        int points = 0;
        List<TeamRecord> standings = group.getStandings();
        if(positions[0] != null && positions[0].equals(standings.get(0).getTeam())){
            points +=5;
        }
        if(positions[1] != null && positions[1].equals(standings.get(1).getTeam())){
            points +=3;
        }
        if(positions[2] != null && positions[2].equals(standings.get(2).getTeam())){
            points +=1;
        }
        if(positions[3] != null && positions[3].equals(standings.get(3).getTeam())){
            points +=1;
        }
        return points;
    }
}
