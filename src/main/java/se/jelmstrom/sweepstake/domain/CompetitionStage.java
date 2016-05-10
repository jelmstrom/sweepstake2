package se.jelmstrom.sweepstake.domain;

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

