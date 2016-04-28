package se.jelmstrom.sweepstake.user;

import org.neo4j.ogm.session.Session;
import org.neo4j.ogm.transaction.Transaction;
import se.jelmstrom.sweepstake.neo4j.Neo4jClient;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.toSet;

public class NeoUserRepository {
    private final Neo4jClient oClient;

    public NeoUserRepository(Neo4jClient oClient) {
        this.oClient = oClient;
    }


    public User saveUser(User user){
        Session session = oClient.session();
        Transaction transaction = session.beginTransaction();
        try {
            user.setPassword(encryptPassword(user.getPassword()));
            session.save(user, 1);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("can not encrypt passwords to store in DB");
        }
        transaction.commit();
        return user;
    }

    public Set<User> findUsers(User user) {

        Map<String, String> parameters = new HashMap();
        parameters.put("username", user.getUsername());
        parameters.put("email", user.getEmail());
        Iterable<User> results = oClient.session().query(
                User.class
                , "MATCH (u) WHERE u.username = {username} OR u.email = {email} RETURN u"
                , parameters);

        return StreamSupport.stream(results.spliterator(), true).collect(toSet());


    }


    public User getUserById(String id){
       return oClient.session().load(User.class, Long.parseLong(id));
    }

    public boolean deleteUser(String id){
        Transaction transaction = oClient.session().beginTransaction();
        oClient.session().delete(getUserById(id));
        transaction.commit();
        return true;
    }

    public User authenticateUser(String username, String password) {
        try {
            Map<String, String> parameters = new HashMap();
            parameters.put("username", username);
            parameters.put("password", encryptPassword(password));
            User user = oClient.session().queryForObject(
                    User.class
                    , "MATCH (u) where u.username = {username} AND u.password = {password} RETURN u"
                    , parameters);
            return user == null?new User():user;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Use proper Encryption algorithm");
        }
    }

    private String encryptPassword(String password) throws NoSuchAlgorithmException {
        return new String(MessageDigest.getInstance("MD5").digest(password.getBytes()));
    }
}
