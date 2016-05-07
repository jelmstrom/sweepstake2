package se.jelmstrom.sweepstake.neo4j;

import io.dropwizard.lifecycle.Managed;
import org.neo4j.ogm.MetaData;
import org.neo4j.ogm.config.Configuration;
import org.neo4j.ogm.session.Session;
import org.neo4j.ogm.session.SessionFactory;
import se.jelmstrom.sweepstake.application.NeoConfiguration;
import se.jelmstrom.sweepstake.domain.Match;
import se.jelmstrom.sweepstake.domain.Stage;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Date;

public class Neo4jClient implements Managed {

    private final NeoConfiguration config;
    private final SessionFactory sessionFactory;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM, HH.mm, yyyy Z");


    public Neo4jClient(NeoConfiguration config) {
        this.config = config;

        Configuration neoConfig = new Configuration();
        String credentials = "";
        String value = "http://" +
                config.getUser()+
                ":" +
                config.getCredentials() +
                "@"
                 +config.getLocation();
        neoConfig.set("URI", value);
        neoConfig.set("driver","org.neo4j.ogm.drivers.http.driver.HttpDriver");

        sessionFactory = new SessionFactory(neoConfig, "se.jelmstrom.sweepstake.domain");
    }


    @Override
    public void start() throws Exception {
        if(config.purge){
            session().purgeDatabase();
        }
        Collection<Stage> stages = session().loadAll(Stage.class);
        if(stages.isEmpty()) {
            Stage A = new Stage(Stage.CompetitionStage.GROUP_A);
            stages.add(A);
            Stage B = new Stage(Stage.CompetitionStage.GROUP_B);
            stages.add(B);
            Stage C = new Stage(Stage.CompetitionStage.GROUP_C);
            stages.add(C);
            Stage D = new Stage(Stage.CompetitionStage.GROUP_D);
            stages.add(D);
            Stage E = new Stage(Stage.CompetitionStage.GROUP_E);
            stages.add(E);
            Stage F = new Stage(Stage.CompetitionStage.GROUP_F);
            stages.add(F);
            Stage LAST_16 = new Stage(Stage.CompetitionStage.LAST_16);
            stages.add(LAST_16);
            Stage QF = new Stage(Stage.CompetitionStage.QUARTER_FINAL);
            stages.add(QF);
            Stage SF = new Stage(Stage.CompetitionStage.SEMI_FINAL);
            stages.add(SF);
            Stage fin = new Stage(Stage.CompetitionStage.FINAL);
            stages.add(fin);
            stages.stream().forEach(stage -> session().save(stage));
            Collection<Match> matches = session().loadAll(Match.class);
            if (matches.isEmpty()) {
                matches.add(buildMatch("10 June, 21.00","France", "Romania",  A));
                matches.add(buildMatch("11 June, 15.00","Albania", "Switzerland",  A));
                matches.add(buildMatch("11 June, 18.00","Wales", "Slovakia",  B));
                matches.add(buildMatch("11 June, 21.00","England", "Russia",  B));
                matches.add(buildMatch("12 June, 15.00","Turkey", "Croatia",  D));
                matches.add(buildMatch("12 June, 18.00","Poland", "Northern Ireland",  C));
                matches.add(buildMatch("12 June, 21.00","Germany", "Ukraine",  C));
                matches.add(buildMatch("13 June, 15.00","Spain", "Czech Republic",  D));
                matches.add(buildMatch("13 June, 18.00","Republic of Ireland", "Sweden",  E));
                matches.add(buildMatch("13 June, 21.00","Belgium", "Italy",   E));
                matches.add(buildMatch("14 June, 18.00","Austria", "Hungary",  F));
                matches.add(buildMatch("14 June, 21.00","Portugal", "Iceland",  F));
                matches.add(buildMatch("15 June, 15.00","Russia", "Slovakia",   B));
                matches.add(buildMatch("15 June, 18.00","Romania", "Switzerland",   A));
                matches.add(buildMatch("15 June, 21.00","France", "Albania",   A));
                matches.add(buildMatch("16 June, 15.00","England", "Wales",   B));
                matches.add(buildMatch("16 June, 18.00","Ukraine", "Northern Ireland",  C));
                matches.add(buildMatch("16 June, 21.00","Germany", "Poland",   C));
                matches.add(buildMatch("17 June, 15.00","Italy", "Sweden",   E));
                matches.add(buildMatch("17 June, 18.00","Czech Republic", "Croatia",   D));
                matches.add(buildMatch("17 June, 21.00","Spain", "Turkey",   D));
                matches.add(buildMatch("18 June, 15.00","Belgium", "Republic of Ireland",  E));
                matches.add(buildMatch("18 June, 18.00","Iceland", "Hungary",   F));
                matches.add(buildMatch("18 June, 21.00","Portugal", "Austria",   F));
                matches.add(buildMatch("19 June, 21.00","Switzerland", "France",  A));
                matches.add(buildMatch("19 June, 21.00","Romania", "Albania",  A));
                matches.add(buildMatch("20 June, 21.00","Slovakia", "England",  B));
                matches.add(buildMatch("20 June, 21.00","Russia", "Wales",  B));
                matches.add(buildMatch("21 June, 18.00","Northern Ireland", "Germany",  C));
                matches.add(buildMatch("21 June, 18.00","Ukraine", "Poland",  C));
                matches.add(buildMatch("21 June, 21.00","Croatia", "Spain",  D));
                matches.add(buildMatch("21 June, 21.00","Czech Republic", "Turkey",  D));
                matches.add(buildMatch("22 June, 18.00","Hungary", "Portugal",  F));
                matches.add(buildMatch("22 June, 18.00","Iceland", "Austria",  F));
                matches.add(buildMatch("22 June, 21.00","Sweden", "Belgium",  E));
                matches.add(buildMatch("22 June, 21.00","Italy", "Republic of Ireland",  E));
                matches.stream().forEach(match -> session().save(match, 1));
            }

        }



/*




 25 June, 15.00 (St-Etienne): Runner-up Group A", "Runner-up C – Match 1
 25 June, 18.00 (Paris): Winner B", "Third-place A/C/D – Match 2
 25 June, 21.00 (Lens): Winner D", "Third-place B/E/F – Match 3
 26 June, 15.00 (Lyon): Winner A", "Third-place C/D/E – Match 4
 26 June, 18.00 (Lille): Winner C", "Third-place A/B/F – Match 5
 26 June, 21.00 (Toulouse): Winner F", "Runner-up E – Match 6
 27 June, 18.00 (St-Denis): Winner E", "Runner-up D – Match 7
 27 June, 21.00 (Nice): Runner-up B", "Runner-up F – Match 8

• For which third place team will play in each tie, see Article 17.03 of the Official Regulations

Quarter-finals
 30 June, 21.00 (Marseille): Winner Match 1", "Winner Match 3 – QF1
 1 July, 21.00 (Lille): Winner Match 2", "Winner Match 6 – QF2
 2 July, 21.00 (Bordeaux): Winner Match 5", "Winner Match 7 – QF3
 3 July, 21.00 (St-Denis): Winner Match 4", "Winner Match 8 – QF4

*/
    }

    private Match buildMatch(String time, String home, String away, Stage group){
        return new Match(home, away, toDate(time), group);
    }

    
    private Date toDate(String s) {
        ZonedDateTime time = ZonedDateTime.parse(s + ", 2016 +0100", formatter);
        return Date.from(time.toInstant());
    }

    @Override
    public void stop() throws Exception {
        // cleanup
    }

    public Session session(){
        return sessionFactory.openSession();
    }

    public MetaData metadata(){
        return sessionFactory.metaData();
    }
}
