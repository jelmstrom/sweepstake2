package se.jelmstrom.sweepstake.application;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;

public class SweepstakeConfiguration extends Configuration {

    public String version = "1.0.0";
    @JsonProperty
    private OrientConfiguration orientConfig= new OrientConfiguration();

    public SweepstakeConfiguration() {
    }

    public SweepstakeConfiguration(OrientConfiguration orientConfig) {
        this.orientConfig = orientConfig;
    }

    public OrientConfiguration getOrientConfig() {
        return orientConfig;
    }
}
