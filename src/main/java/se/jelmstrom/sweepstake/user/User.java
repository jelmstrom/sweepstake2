package se.jelmstrom.sweepstake.user;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Objects;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.validation.constraints.NotNull;
import java.security.Principal;
import java.util.Arrays;
import java.util.List;
@JsonInclude(JsonInclude.Include.NON_NULL)
public class User implements Principal{
    @JsonProperty
    @NotNull
    public final String username;
    @JsonProperty
    @NotNull
    public final String email;
    @JsonProperty
    public final String userId;
    public final boolean isAdmin;
    public final String password;
    private final ObjectMapper objectMapper = new ObjectMapper();

    protected User() {
        username=null;
        email=null;
        userId = null;
        isAdmin = false;
        password = null;
    }

    public User(String username, String email, String userId, String password) {
        this.username = username;
        this.email = email;
        this.userId = userId;
        this.isAdmin= false;
        this.password = password;
    }

    public User(String username, String email, String userId) {
        this.username = username;
        this.email = email;
        this.userId = userId;
        isAdmin = false;
        password = null;
    }


    @Override
    public String toString() {
        try {
            return objectMapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return new ToStringBuilder(this)
                    .append("username :", username)
                    .append("email :", email)
                    .append("userId", userId)
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
                Objects.equal(userId, user.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(username, email, userId);
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


}
