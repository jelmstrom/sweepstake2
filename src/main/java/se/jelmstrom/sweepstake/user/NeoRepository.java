package se.jelmstrom.sweepstake.user;

import se.jelmstrom.sweepstake.neo4j.Neo4jClient;

public class NeoRepository {

    protected final Neo4jClient neo4jClient;

    public NeoRepository(Neo4jClient neo4jClient) {
        this.neo4jClient = neo4jClient;
    }

    protected <T> T saveEntity(T e, int depth){
        neo4jClient.session().save(e, depth);
        return e;
    }


}
