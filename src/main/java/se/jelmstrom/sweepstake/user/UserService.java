package se.jelmstrom.sweepstake.user;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.jelmstrom.sweepstake.domain.User;

import java.util.HashSet;
import java.util.Set;

public class UserService {
    private final NeoUserRepository repo;
    private static Logger logger= LoggerFactory.getLogger(UserService.class);

    public UserService(NeoUserRepository repo) {
        this.repo = repo;
    }


    public Set<String> createUser(User user) {
        Set<String> conflictingFields = new HashSet<>();
        Set<User> conflictingUsers = repo.findUsers(user);
        logger.info(conflictingUsers.toString());
        if(conflictingUsers.isEmpty()){
            repo.saveUser(user);
        } else {
            conflictingUsers.stream().forEach(conflictUser -> {
                if(StringUtils.equals(user.getEmail(), conflictUser.getEmail())){
                    conflictingFields.add("email");
                }
                if(StringUtils.equals(user.getUsername(), conflictUser.getUsername())){
                    conflictingFields.add("username");
                }
            });
        }
        return conflictingFields;
    }

    public User authenticateUser(String username, String password) {
        return repo.authenticateUser(username, password);
    }

    public User getUserById(Long userId) {
        return repo.getUserById(userId);
    }
}
