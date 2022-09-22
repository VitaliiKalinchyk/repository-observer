package com.epam.rd.autocode.observer.git;

import java.util.ArrayList;

public class GitRepoObservers {

     private static SimpleRepository simpleRepository;
    public static Repository newRepository() {
        simpleRepository = new SimpleRepository();
        return simpleRepository;
    }

    public static WebHook mergeToBranchWebHook(String branchName) {
        Branch currentBranch = simpleRepository.getBranch(branchName);
        Event currentEvent = new Event(Event.Type.MERGE, currentBranch, new ArrayList<>());
        return new SimpleWebHook(currentBranch, currentEvent);
    }

    public static WebHook commitToBranchWebHook(String branchName) {
        Branch currentBranch = simpleRepository.getBranch(branchName);
        Event currentEvent = new Event(Event.Type.COMMIT, currentBranch, new ArrayList<>());
        return new SimpleWebHook(currentBranch, currentEvent);
    }
}
