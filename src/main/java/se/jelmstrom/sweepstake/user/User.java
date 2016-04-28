package se.jelmstrom.sweepstake.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Objects;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.neo4j.ogm.annotation.GraphId;

import javax.validation.constraints.NotNull;
import java.security.Principal;
import java.util.Arrays;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class User implements Principal{

    @GraphId
    @JsonProperty
    private Long userId;
    @JsonProperty
    @NotNull
    private String username;
    @JsonProperty
    private String email;
    @JsonProperty
    private String password;
    private boolean isAdmin;

    public User() {
    }

    public User(String username, String email, Long userId, String password) {
        this.username = username;
        this.email = email;
        this.isAdmin= false;
        this.password = password;
        this.userId = userId;
    }

    public User(String username, String email, Long userId) {
        this.username = username;
        this.email = email;
        this.userId = userId;
        isAdmin = false;
        password = null;
    }


    @Override
    public String toString() {
        try {
            return new ObjectMapper().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return new ToStringBuilder(this)
                    .append("username :", username)
                    .append("email :", email)
                    .append("userId", getUserId())
                    .toString();
        }

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return Objects.equal(username, user.username) &&
                Objects.equal(email, user.email) &&
                Objects.equal(getUserId(), user.getUserId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(username, email, getUserId());
    }

    @Override
    @JsonIgnore
    public String getName() {
        return username;
    }

    @JsonIgnore
    public List<String> roles (){
        return isAdmin? Arrays.asList("ADMIN", "USER"):Arrays.asList("USER");
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }
}
