package se.jelmstrom.sweepstake.neo4j;

import io.dropwizard.lifecycle.Managed;
import org.neo4j.ogm.MetaData;
import org.neo4j.ogm.config.Configuration;
import org.neo4j.ogm.session.Session;
import org.neo4j.ogm.session.SessionFactory;
import se.jelmstrom.sweepstake.application.NeoConfiguration;

public class Neo4jClient implements Managed {

    private final NeoConfiguration config;
    private final SessionFactory sessionFactory;

    public Neo4jClient(NeoConfiguration config) {
        this.config = config;

        Configuration neoConfig = new Configuration();
        String credentials = "";
        String value = "http://" +
                config.getUser()+
                ":" +
                config.getCredentials() +
                "@"
                 +config.getLocation();
        neoConfig.set("URI", value);
        neoConfig.set("driver","org.neo4j.ogm.drivers.http.driver.HttpDriver");

        sessionFactory = new SessionFactory(neoConfig, "se.jelmstrom.sweepstake");
    }


    @Override
    public void start() throws Exception {
        // init stuff
    }

    @Override
    public void stop() throws Exception {
        // cleanup
    }

    public Session session(){
        return sessionFactory.openSession();
    }

    public MetaData metadata(){
        return sessionFactory.metaData();
    }
}
