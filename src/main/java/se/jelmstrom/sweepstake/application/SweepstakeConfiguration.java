package se.jelmstrom.sweepstake.application;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;

public class SweepstakeConfiguration extends Configuration {

    public String version = "1.0.0";
    @JsonProperty
    private NeoConfiguration neoConfiguration= new NeoConfiguration();
    @JsonProperty
    public String corsLocations = "http://vmtips.herokuapp.com/";

    public SweepstakeConfiguration() {
    }

    public NeoConfiguration getNeoConfiguration() {
        return neoConfiguration;
    }
}
