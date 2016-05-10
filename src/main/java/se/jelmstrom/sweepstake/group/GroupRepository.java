package se.jelmstrom.sweepstake.group;

import se.jelmstrom.sweepstake.domain.CompetitionStage;
import se.jelmstrom.sweepstake.domain.Group;
import se.jelmstrom.sweepstake.neo4j.Neo4jClient;

import java.util.HashMap;
import java.util.Map;

public class GroupRepository {
    private final Neo4jClient neoClient;

    public GroupRepository(Neo4jClient neo4jClient) {
        this.neoClient = neo4jClient;
    }


    public Group getStage(CompetitionStage competitionStage){
        String query = "MATCH (group:Group)-[:GROUP]-(match:Match) where group.stage = {stage_name} RETURN group, match";
        Map<String, String> parameters = new HashMap<>();
        parameters.put("stage_name", competitionStage.name());
        Group group = neoClient.session().queryForObject(Group.class, query, parameters);
        return neoClient.session().load(Group.class, group.getId(), 1);
    }


    public Group saveMatches(Group group) {
        neoClient.session().save(group);
        return neoClient.session().load(Group.class, group.getId(), 1);
    }
}
