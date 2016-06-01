package se.jelmstrom.sweepstake.match;

import se.jelmstrom.sweepstake.domain.MatchPrediction;
import se.jelmstrom.sweepstake.domain.User;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public class MatchService {
    private final NeoMatchRepo repo;

    public MatchService(NeoMatchRepo repo) {
        this.repo = repo;
    }

    public void savePredictions(List<MatchPrediction> updatedPredictions, User user) {
        Set<MatchPrediction> matchPredictions = user.getPredictions();
        merge(matchPredictions, updatedPredictions);
        repo.savePredictions(user);
    }

    private void merge(Set<MatchPrediction> existingPredictions, List<MatchPrediction> updatedPredictions) {
         updatedPredictions.stream().forEach(item -> updatePrediction(existingPredictions, item));

    }

    private void updatePrediction(Set<MatchPrediction>stored, MatchPrediction updated) {
        Optional<MatchPrediction> storedPrediction = stored.stream().filter(item -> item.equals(updated)).findFirst();
        if(storedPrediction.isPresent()){
            MatchPrediction matchPrediction = storedPrediction.get();
               matchPrediction.setAwayGoals(updated.getAwayGoals());
               matchPrediction.setHomeGoals(updated.getHomeGoals());
                matchPrediction.setMatch(updated.getMatch());
           } else { // new prediction
               stored.add(updated);
           }
    }
}
