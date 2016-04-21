package se.jelmstrom.sweepstake.orient;

import com.codahale.metrics.health.HealthCheck;
import com.orientechnologies.common.exception.OException;
import com.orientechnologies.orient.core.exception.OStorageException;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;

import static com.codahale.metrics.health.HealthCheck.Result.*;

public class OrientHealthCheck extends HealthCheck {
    private final OrientClient client;

    public OrientHealthCheck(OrientClient client) {
        this.client = client;
    }


    @Override
    protected Result check() throws Exception {
        OrientGraph tx = null;
        try {
             tx = client.factory.getTx();
            return healthy();
        } catch (OException ex){
            return unhealthy(ex.getMessage());
        } finally{
            if(null != tx) {
                tx.shutdown(false);
            }
        }
    }
}
