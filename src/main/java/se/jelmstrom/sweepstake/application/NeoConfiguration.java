package se.jelmstrom.sweepstake.application;

import com.fasterxml.jackson.annotation.JsonProperty;

public class NeoConfiguration {
    @JsonProperty
    private String location = "192.168.59.103:7474";
    @JsonProperty
    private String credentials = "local";
    @JsonProperty
    private String user = "neo4j";

    public NeoConfiguration() {
    }

    public NeoConfiguration(String location, String credentials, String user) {
        this.location = location;
        this.credentials = credentials;
        this.user = user;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCredentials() {
        return credentials;
    }

    public void setCredentials(String credentials) {
        this.credentials = credentials;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
