package se.jelmstrom.sweepstake.neo4j;

import com.codahale.metrics.health.HealthCheck;
import org.neo4j.ogm.metadata.ClassInfo;

import java.util.Collection;

import static com.codahale.metrics.health.HealthCheck.Result.healthy;

public class NeoConfigHealthCheck extends HealthCheck {
    private final Neo4jClient client;

    public NeoConfigHealthCheck(Neo4jClient client) {
        this.client = client;
    }


    @Override
    protected Result check() throws Exception {
        Collection<ClassInfo> classInfos = client.metadata().persistentEntities();
        return healthy();
    }
}
