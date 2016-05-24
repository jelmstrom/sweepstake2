package se.jelmstrom.sweepstake.group;

import se.jelmstrom.sweepstake.domain.Group;

public class GroupService {
    private final GroupRepository repo;

    public GroupService(GroupRepository repo) {
        this.repo = repo;
    }

    public Group getStage(String stageName){
        return repo.getGroup(stageName);
    }

}
