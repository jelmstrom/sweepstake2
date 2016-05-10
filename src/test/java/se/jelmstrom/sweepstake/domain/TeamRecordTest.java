package se.jelmstrom.sweepstake.domain;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

public class TeamRecordTest {
    @Test
    public void mergeSouldHandleTwoFullRecords() throws Exception {
        TeamRecord one = new TeamRecord("a", 1, 0, 3);
        TeamRecord two = new TeamRecord("a", 2, 2, 1);

        TeamRecord result = TeamRecord.merge(one, two);
        assertThat(result.getGoalDifference(), is(1));
        assertThat(result.getGoalsAgainst(), is(2));
        assertThat(result.getGoalsFor(), is(3));
        assertThat(result.getPoints(), is(4));
        assertThat(result.getPlayed(), is(2));

    }

    @Test
    public void mergeSouldHandleEmptySecondRecord() throws Exception {
        TeamRecord one = new TeamRecord("a", 1, 0, 3);
        TeamRecord two = new TeamRecord("a", null, null, null);

        TeamRecord result = TeamRecord.merge(one, two);
        assertThat(result.getGoalDifference(), is(1));
        assertThat(result.getGoalsAgainst(), is(0));
        assertThat(result.getGoalsFor(), is(1));
        assertThat(result.getPoints(), is(3));
        assertThat(result.getPlayed(), is(1));

    }

    @Test
    public void mergeSouldHandleEmptyFirstRecord() throws Exception {
        TeamRecord two = new TeamRecord("a", 1, 0, 3);
        TeamRecord one = new TeamRecord("a", null, null, null);

        TeamRecord result = TeamRecord.merge(one, two);
        assertThat(result.getGoalDifference(), is(1));
        assertThat(result.getGoalsAgainst(), is(0));
        assertThat(result.getGoalsFor(), is(1));
        assertThat(result.getPoints(), is(3));
        assertThat(result.getPlayed(), is(1));

    }

    @Test
    public void mergeSouldHandleTwoEmptyRecords() throws Exception {
        TeamRecord two = new TeamRecord("a", null, null, null);
        TeamRecord one = new TeamRecord("b", null, null, null);

        TeamRecord result = TeamRecord.merge(one, two);
        assertThat(result.getGoalDifference(), is(nullValue()));
        assertThat(result.getGoalsAgainst(), is(nullValue()));
        assertThat(result.getGoalsFor(), is(nullValue()));
        assertThat(result.getPoints(), is(nullValue()));
        assertThat(result.getPlayed(), is(0));
    }

    @Test
    public void compareEmptyRecords(){

        TeamRecord two = new TeamRecord("a", null, null, null);
        TeamRecord one = new TeamRecord("b", null, null, null);
        assertThat(two.compareTo(one), is(1));
        assertThat(one.compareTo(two), is(-1));
    }


    @Test
    public void lossIsConsideredBetterThanNotPLayed(){

        TeamRecord one = new TeamRecord("a", 0, 1, 0);
        TeamRecord two = new TeamRecord("b", null, null, null);
        assertThat(one.compareTo(two), is(1));
        assertThat(two.compareTo(one), is(-1));
    }

}