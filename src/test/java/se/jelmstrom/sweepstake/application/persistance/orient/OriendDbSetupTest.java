package se.jelmstrom.sweepstake.application.persistance.orient;




import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.blueprints.impls.orient.OrientVertex;
import org.junit.Test;
import se.jelmstrom.sweepstake.application.SweepstakeConfiguration;
import se.jelmstrom.sweepstake.orient.OrientClient;

import java.io.IOException;

public class OriendDbSetupTest {


    @Test
    public void connectToRemoteDb() throws IOException {
        SweepstakeConfiguration config = new SweepstakeConfiguration();
        OrientClient service = new OrientClient(config);
        System.out.println(service.factory.getAvailableInstancesInPool());

        OrientGraph graph = service.factory.getTx();
        OrientVertex team1234 = graph.addVertex("team1234");
        team1234.setProperty("name", "myTeam");
        graph.getVertices().forEach(System.out::println);
        graph.commit();
        graph.getVertices().forEach(v -> System.out.println(v.getPropertyKeys()));

    }
}
