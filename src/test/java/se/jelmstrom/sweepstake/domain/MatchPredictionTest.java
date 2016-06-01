package se.jelmstrom.sweepstake.domain;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class MatchPredictionTest {


    @Test
    public void parseJson() throws IOException {
        String json="[\n" +
                "    {\n" +
                "      \"id\": null,\n" +
                "      \"user\": {\n" +
                "        \"id\": 797,\n" +
                "        \"username\": \"johan\",\n" +
                "        \"email\": \"johan.elmstrom@gmail.com\",\n" +
                "        \"password\": \"password\",\n" +
                "        \"predictions\": [],\n" +
                "        \"leagues\": [],\n" +
                "        \"groupPredictions\": [],\n" +
                "        \"admin\": false,\n" +
                "        \"points\": 0\n" +
                "      },\n" +
                "      \"match\": {\n" +
                "        \"id\": 774,\n" +
                "        \"home\": \"Romania\",\n" +
                "        \"away\": \"Switzerland\",\n" +
                "        \"kickoff\": 1466010000000,\n" +
                "        \"homeGoals\": null,\n" +
                "        \"awayGoals\": null,\n" +
                "        \"predictions\": [],\n" +
                "        \"group\": {" +
                "           \"id\":755 \n," +
                "           \"groupName\": \"A\"" +
                "        },\n" +
                "        \"stage\": null\n" +
                "      },\n" +
                "      \"homeGoals\": \"0\",\n" +
                "      \"awayGoals\": \"0\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": null,\n" +
                "      \"user\": 797,\n" +
                "      \"match\": {\n" +
                "        \"id\": 762,\n" +
                "        \"home\": \"Albania\",\n" +
                "        \"away\": \"Switzerland\",\n" +
                "        \"kickoff\": 1465653600000,\n" +
                "        \"homeGoals\": null,\n" +
                "        \"awayGoals\": null,\n" +
                "        \"predictions\": [],\n" +
                "        \"group\": 755,\n" +
                "        \"stage\": null\n" +
                "      },\n" +
                "      \"homeGoals\": \"0\",\n" +
                "      \"awayGoals\": \"0\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": null,\n" +
                "      \"user\": 797 ,\n" +
                "      \"match\": {\n" +
                "        \"id\": 761,\n" +
                "        \"home\": \"France\",\n" +
                "        \"away\": \"Romania\",\n" +
                "        \"kickoff\": 1465588800000,\n" +
                "        \"homeGoals\": null,\n" +
                "        \"awayGoals\": null,\n" +
                "        \"predictions\": [],\n" +
                "        \"group\": 755,\n" +
                "        \"stage\": null\n" +
                "      },\n" +
                "      \"homeGoals\": \"0\",\n" +
                "      \"awayGoals\": \"0\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": null,\n" +
                "      \"user\": 797,\n" +
                "      \"match\": {\n" +
                "        \"id\": 786,\n" +
                "        \"home\": \"Romania\",\n" +
                "        \"away\": \"Albania\",\n" +
                "        \"kickoff\": 1466366400000,\n" +
                "        \"homeGoals\": null,\n" +
                "        \"awayGoals\": null,\n" +
                "        \"predictions\": [],\n" +
                "        \"group\": 755,\n" +
                "        \"stage\": null\n" +
                "      },\n" +
                "      \"homeGoals\": \"0\",\n" +
                "      \"awayGoals\": \"0\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": null,\n" +
                "      \"user\": 797,\n" +
                "      \"match\": {\n" +
                "        \"id\": 775,\n" +
                "        \"home\": \"France\",\n" +
                "        \"away\": \"Albania\",\n" +
                "        \"kickoff\": 1466020800000,\n" +
                "        \"homeGoals\": null,\n" +
                "        \"awayGoals\": null,\n" +
                "        \"predictions\": [],\n" +
                "        \"group\": 755,\n" +
                "        \"stage\": null\n" +
                "      },\n" +
                "      \"homeGoals\": \"0\",\n" +
                "      \"awayGoals\": \"0\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": null,\n" +
                "      \"user\": 797,\n" +
                "      \"match\": {\n" +
                "        \"id\": 785,\n" +
                "        \"home\": \"Switzerland\",\n" +
                "        \"away\": \"France\",\n" +
                "        \"kickoff\": 1466366400000,\n" +
                "        \"homeGoals\": null,\n" +
                "        \"awayGoals\": null,\n" +
                "        \"predictions\": [],\n" +
                "        \"group\": 755,\n" +
                "        \"stage\": null\n" +
                "      },\n" +
                "      \"homeGoals\": \"0\",\n" +
                "      \"awayGoals\": \"0\"\n" +
                "    }\n" +
                "  ]";


        List<MatchPrediction> list = new ObjectMapper().readValue(json, new TypeReference<List<MatchPrediction>>() {
        });
        assertThat(list.isEmpty(), is(false));

    }

}
