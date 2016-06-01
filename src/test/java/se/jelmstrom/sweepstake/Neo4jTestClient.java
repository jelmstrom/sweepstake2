package se.jelmstrom.sweepstake;

import org.neo4j.ogm.session.SessionFactory;
import se.jelmstrom.sweepstake.application.NeoConfiguration;
import se.jelmstrom.sweepstake.neo4j.Neo4jClient;

public class Neo4jTestClient extends Neo4jClient {


    public Neo4jTestClient(NeoConfiguration config) {
        super(config);
    }

    @Override
    protected SessionFactory buildSessionFactory(NeoConfiguration config) {
        SessionFactory sessionFactory = new SessionFactory("se.jelmstrom.sweepstake.domain");
        return sessionFactory;
    }
}
