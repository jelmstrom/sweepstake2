package se.jelmstrom.sweepstake.application;


import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;

public class OrientConfiguration extends Configuration {
    @JsonProperty
    public String dbUrl = "remote:192.168.59.103/vmtips";
    @JsonProperty
    public String userName = "test";
    @JsonProperty
    public String pwd = "testpwd";
    @JsonProperty
    public int poolSize = 10;

    public OrientConfiguration() {
    }

    public OrientConfiguration(String dbUrl, String userName, String pwd, int poolSize) {
        this.dbUrl = dbUrl;
        this.userName = userName;
        this.pwd = pwd;
        this.poolSize = poolSize;
    }
}
