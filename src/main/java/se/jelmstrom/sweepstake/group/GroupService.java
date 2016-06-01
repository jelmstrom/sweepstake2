package se.jelmstrom.sweepstake.group;

import se.jelmstrom.sweepstake.domain.Group;
import se.jelmstrom.sweepstake.domain.GroupPrediction;
import se.jelmstrom.sweepstake.domain.User;

public class GroupService {
    private final GroupRepository repo;


    public GroupService(GroupRepository repo) {
        this.repo = repo;
    }

    public Group getStage(String stageName){
        return repo.getGroup(stageName);
    }

    public void storePrediction(GroupPrediction prediction, User userById) {
        if(!userById.getGroupPredictions().contains(prediction)){
            userById.addGroupPrediction(prediction);
        } else {
            userById.getGroupPredictions().stream()
                    .filter(p -> p.equals(prediction))
                    .forEach(p -> p.setPositions(prediction.getPositions()));
        }
        repo.storePrediction(prediction);
    }
}
