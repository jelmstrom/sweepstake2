package se.jelmstrom.sweepstake.user;

import com.tinkerpop.blueprints.Compare;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.blueprints.impls.orient.OrientGraphNoTx;
import com.tinkerpop.blueprints.impls.orient.OrientVertex;
import se.jelmstrom.sweepstake.orient.OrientClient;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashSet;

public class UserRepository {
    private final OrientClient oClient;

    public UserRepository(OrientClient oClient) {
        this.oClient = oClient;
    }


    public User saveUser(User user){
        OrientGraph graph = oClient.txGraph();
        OrientVertex orientVertex = null;
        try {
            orientVertex = graph.addVertex("class:User"
                    , "username", user.getUsername()
                    , "email", user.getEmail()
                    , "password", encryptPassword(user.getPassword()));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Incorrect encryption algorithm");
        }
        graph.commit();
        return buildUser(orientVertex);
    }

    public HashSet<User> findUsers(User user) {

        OrientGraphNoTx graph = oClient.factory.getNoTx();
        HashSet<User> users = new HashSet<>();
        Iterable<Vertex> username = graph.query().has("username", Compare.EQUAL, user.getUsername()).vertices();
        Iterable<Vertex> email = graph.query().has("email", Compare.EQUAL, user.getEmail()).vertices();
        graph.commit();

        username.iterator().forEachRemaining(vert -> users.add(buildUser(vert)));


        email.iterator().forEachRemaining(vert -> users.add(buildUser(vert)));

        return users;

    }

    private User buildUser(Vertex vert) {
        return new User(
                    vert.getProperty("username")
                    , vert.getProperty("email")
                    , vert.getId().toString().replace("#", ""));
    }

    public User getUserById(String id){
        OrientGraphNoTx graph = oClient.factory.getNoTx();
        OrientVertex vertex = graph.getVertex(id);
        return buildUser(vertex);
    }

    public boolean deleteUser(String id){
        OrientGraph graph = oClient.txGraph();
        graph.removeVertex(graph.getVertex(id));
        graph.commit();
        return true;
    }

    public User authenticateUser(String username, String password) {
        OrientGraphNoTx graph = oClient.factory.getNoTx();
        try {
            Iterable<Vertex> vertices = graph.query().has("username", Compare.EQUAL, username)
                    .has("password", Compare.EQUAL, encryptPassword(password))
                    .vertices();
            if(vertices.iterator().hasNext()){
                return buildUser(vertices.iterator().next());
            } else {
                return new User();
            }

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Use proper Encryption algorithm");
        }
    }

    private String encryptPassword(String password) throws NoSuchAlgorithmException {
        return new String(MessageDigest.getInstance("MD5").digest(password.getBytes()));
    }
}
