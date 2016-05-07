package se.jelmstrom.sweepstake.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.HashSet;
import java.util.Set;
/*
 Neo maps enums as properties or relationships, not as nodes
*/
@NodeEntity
public class Stage extends Entity{
    public enum CompetitionStage {
        GROUP_A("A"),
        GROUP_B("B"),
        GROUP_C("C"),
        GROUP_D("D"),
        GROUP_E("E"),
        GROUP_F("F"),
        LAST_16("16"),
        QUARTER_FINAL("QF"),
        SEMI_FINAL("SF"),
        FINAL("F");

        private String label;

        CompetitionStage(String label) {
            this.label = label;
        }

        public String getlabel(){
            return label;
        }
    }

    @JsonProperty
    private CompetitionStage stage;

    @JsonProperty
    @Relationship(type= "STAGE", direction= Relationship.INCOMING)
    private Set<Match> matches = new HashSet<>();


    public Stage() {
    }

    public Stage(CompetitionStage stage) {
        this.stage = stage;
    }

    public Stage(CompetitionStage stage, Set<Match> matches) {
        this.stage = stage;
        this.matches = matches;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CompetitionStage getStage() {
        return stage;
    }

    public void setStage(CompetitionStage stage) {
        this.stage = stage;
    }

    public Set<Match> getMatches() {
        return matches;
    }

    public void setMatches(Set<Match> matches) {
        this.matches = matches;
    }
}
