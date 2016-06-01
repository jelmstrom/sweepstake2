package se.jelmstrom.sweepstake.user;

import org.neo4j.ogm.session.Session;
import org.neo4j.ogm.transaction.Transaction;
import se.jelmstrom.sweepstake.domain.User;
import se.jelmstrom.sweepstake.neo4j.Neo4jClient;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.toSet;

public class UserRepository extends NeoRepository {

    public UserRepository(Neo4jClient oClient) {
        super(oClient);
    }


    public User saveUser(User user){
        return saveUser(user, 1);
    }

    public Set<User> findUsers(User user) {

        Map<String, String> parameters = new HashMap();
        parameters.put("username", user.getUsername());
        parameters.put("email", user.getEmail());
        Iterable<User> results = neo4jClient.session().query(
                User.class
                , "MATCH (u) WHERE u.username = {username} OR u.email = {email} RETURN u"
                , parameters);

        return StreamSupport.stream(results.spliterator(), true).map(u -> getUserById(u.getId())).collect(toSet());


    }


    public User getUserById(Long id){
        return super.loadEntity(User.class, id, 3);
    }

    public boolean deleteUser(Long id){
        Transaction transaction = neo4jClient.session().beginTransaction();
        neo4jClient.session().delete(getUserById(id));
        return true;
    }

    public User authenticateUser(String username, String password) {
        try {
            Map<String, String> parameters = new HashMap();
            parameters.put("username", username);
            parameters.put("password", encryptPassword(password));
            User user = neo4jClient.session().queryForObject(
                    User.class
                    , "MATCH (u) where u.username = {username} AND u.password = {password} RETURN u"
                    , parameters);
            return user == null?new User(): getUserById(user.getId(), 1);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Use proper Encryption algorithm");
        }
    }

    private String encryptPassword(String password) throws NoSuchAlgorithmException {
        return password;
    }

    public User saveUser(User user, int depth) {
        Session session = neo4jClient.session();
        try {
            user.setPassword(encryptPassword(user.getPassword()));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("can not encrypt passwords to store in DB");
        }
        return super.saveEntity(user, depth);
    }

    public User getUserById(Long id, int depth) {
        return super.loadEntity(User.class, id, depth);
    }
}
