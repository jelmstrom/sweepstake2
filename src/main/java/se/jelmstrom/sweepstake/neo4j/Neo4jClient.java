package se.jelmstrom.sweepstake.neo4j;

import io.dropwizard.lifecycle.Managed;
import org.neo4j.ogm.MetaData;
import org.neo4j.ogm.config.Configuration;
import org.neo4j.ogm.session.Session;
import org.neo4j.ogm.session.SessionFactory;
import se.jelmstrom.sweepstake.application.NeoConfiguration;
import se.jelmstrom.sweepstake.domain.Match;

import java.util.Collection;

public class Neo4jClient implements Managed {

    private final NeoConfiguration config;
    private final SessionFactory sessionFactory;

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
        Collection<Match> matches = session().loadAll(Match.class);
        if(matches.isEmpty()){
          //  matches.add(new Match());
        }

        /*

        Friday 10 June, 21.00 (St-Denis): France v Romania – Group A
Saturday 11 June, 15.00 (Lens): Albania v Switzerland – Group A
Saturday 11 June, 18.00 (Bordeaux): Wales v Slovakia – Group B
Saturday 11 June, 21.00 (Marseille): England v Russia – Group B
Sunday 12 June, 15.00 (Paris): Turkey v Croatia – Group D
Sunday 12 June, 18.00 (Nice): Poland v Northern Ireland – Group C
Sunday 12 June, 21.00 (Lille): Germany v Ukraine – Group C
Monday 13 June, 15.00 (Toulouse): Spain v Czech Republic – Group D
Monday 13 June, 18.00 (St-Denis): Republic of Ireland v Sweden – Group E
Monday 13 June, 21.00 (Lyon): Belgium v Italy – Group E
Tuesday 14 June, 18.00 (Bordeaux): Austria v Hungary – Group F
Tuesday 14 June, 21.00 (St-Etienne): Portugal v Iceland – Group F

Wednesday 15 June, 15.00 (Lille): Russia v Slovakia – Group B
Wednesday 15 June, 18.00 (Paris): Romania v Switzerland – Group A
Wednesday 15 June, 21.00 (Marseille): France v Albania – Group A
Thursday 16 June, 15.00 (Lens): England v Wales – Group B
Thursday 16 June, 18.00 (Lyon): Ukraine v Northern Ireland – Group C
Thursday 16 June, 21.00 (St-Denis): Germany v Poland – Group C
Friday 17 June, 15.00 (Toulouse): Italy v Sweden – Group E
Friday 17 June, 18.00 (St-Etienne): Czech Republic v Croatia – Group D
Friday 17 June, 21.00 (Nice): Spain v Turkey – Group D
Saturday 18 June, 15.00 (Bordeaux): Belgium v Republic of Ireland – Group E
Saturday 18 June, 18.00 (Marseille): Iceland v Hungary – Group F
Saturday 18 June, 21.00 (Paris): Portugal v Austria – Group F

Sunday 19 June, 21.00 (Lille): Switzerland v France – Group A
Sunday 19 June, 21.00 (Lyon): Romania v Albania – Group A
Monday 20 June, 21.00 (St-Etienne): Slovakia v England – Group B
Monday 20 June, 21.00 (Toulouse): Russia v Wales – Group B
Tuesday 21 June, 18.00 (Paris): Northern Ireland v Germany – Group C
Tuesday 21 June, 18.00 (Marseille): Ukraine v Poland – Group C
Tuesday 21 June, 21.00 (Bordeaux): Croatia v Spain – Group D
Tuesday 21 June, 21.00 (Lens): Czech Republic v Turkey – Group D
Wednesday 22 June, 18.00 (Lyon): Hungary v Portugal – Group F
Wednesday 22 June, 18.00 (St-Denis): Iceland v Austria – Group F
Wednesday 22 June, 21.00 (Nice): Sweden v Belgium – Group E
Wednesday 22 June, 21.00 (Lille): Italy v Republic of Ireland – Group E

Round of 16
Saturday 25 June, 15.00 (St-Etienne): Runner-up Group A v Runner-up C – Match 1
Saturday 25 June, 18.00 (Paris): Winner B v Third-place A/C/D – Match 2
Saturday 25 June, 21.00 (Lens): Winner D v Third-place B/E/F – Match 3
Sunday 26 June, 15.00 (Lyon): Winner A v Third-place C/D/E – Match 4
Sunday 26 June, 18.00 (Lille): Winner C v Third-place A/B/F – Match 5
Sunday 26 June, 21.00 (Toulouse): Winner F v Runner-up E – Match 6
Monday 27 June, 18.00 (St-Denis): Winner E v Runner-up D – Match 7
Monday 27 June, 21.00 (Nice): Runner-up B v Runner-up F – Match 8

• For which third place team will play in each tie, see Article 17.03 of the Official Regulations

Quarter-finals
Thursday 30 June, 21.00 (Marseille): Winner Match 1 v Winner Match 3 – QF1
Friday 1 July, 21.00 (Lille): Winner Match 2 v Winner Match 6 – QF2
Saturday 2 July, 21.00 (Bordeaux): Winner Match 5 v Winner Match 7 – QF3
Sunday 3 July, 21.00 (St-Denis): Winner Match 4 v Winner Match 8 – QF4

Semi-finals
Wednesday 6 July, 21.00 (Lyon): Winner QF1 v Winner QF2 – SF1
Thursday 7 July, 21.00 (Marseille): Winner QF3 v Winner QF4 – SF2

Final
Sunday 10 July, 21.00 (St-Denis): Winner SF1 v Winner SF2
         */
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
