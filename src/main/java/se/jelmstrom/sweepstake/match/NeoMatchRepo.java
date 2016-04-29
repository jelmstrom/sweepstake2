package se.jelmstrom.sweepstake.match;

import org.neo4j.ogm.session.Session;
import org.neo4j.ogm.transaction.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.jelmstrom.sweepstake.domain.Match;
import se.jelmstrom.sweepstake.neo4j.Neo4jClient;

import java.util.Collection;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class NeoMatchRepo {
    private final Logger logger = LoggerFactory.getLogger(NeoMatchRepo.class);
    private final Neo4jClient client;

    public NeoMatchRepo(Neo4jClient client) {
        this.client = client;

    }


    public Match create(Match match){
        Session session = client.session();
        Transaction transaction = session.beginTransaction();
        session.save(match);
        transaction.commit();
        logger.debug(match.toString());
        return match;
    }

    public List<Match> list(){
        Session session = client.session();
        Collection<Match> matches = session.loadAll(Match.class);
        return matches.stream().collect(toList());
    }
}
