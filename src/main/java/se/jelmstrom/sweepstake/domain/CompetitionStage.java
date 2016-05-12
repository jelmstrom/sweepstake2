package se.jelmstrom.sweepstake.domain;

public enum CompetitionStage {
        GROUP("G"),
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

