package se.jelmstrom.sweepstake.orient;

import com.orientechnologies.orient.core.iterator.ORecordIteratorClass;
import com.orientechnologies.orient.core.metadata.schema.OClass;
import com.orientechnologies.orient.core.metadata.schema.OSchemaProxy;
import com.orientechnologies.orient.core.record.impl.ODocument;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.blueprints.impls.orient.OrientGraphFactory;
import io.dropwizard.lifecycle.Managed;
import se.jelmstrom.sweepstake.application.SweepstakeConfiguration;

public class OrientClient implements Managed {

    public final OrientGraphFactory factory;

    public OrientClient(SweepstakeConfiguration config) {

        OrientGraphFactory factory
                = new OrientGraphFactory(config.getOrientConfig().dbUrl
                                    , config.getOrientConfig().userName
                                    , config.getOrientConfig().pwd)
                    .setupPool(1,config.getOrientConfig().poolSize);
        factory.setAutoStartTx(false);
        factory.setMaxRetries(5);

        this.factory = factory;
    }


    @Override
    public void start() throws Exception {

        OrientGraph graph = factory.getTx();
        OSchemaProxy schema = graph.getRawGraph().getMetadata().getSchema();
        OClass user = schema.getClass("User");
        if(user == null){
            graph.command(new OCommandSQL("CREATE CLASS User extends V")).execute();
            graph.command(new OCommandSQL("CREATE PROPERTY User.username STRING")).execute();
            graph.command(new OCommandSQL("CREATE PROPERTY User.email STRING")).execute();
            graph.commit();
        }
    }

    @Override
    public void stop() throws Exception {

    }

    public OrientGraph txGraph(){
        return factory.getTx();
    }
}
