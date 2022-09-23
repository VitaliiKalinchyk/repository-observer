package com.epam.rd.autocode.observer.git;

import java.util.*;

public class SimpleRepository implements Repository {
    private final Map<Branch, ArrayList<Commit>> branches = new LinkedHashMap<>(){{
        put(new Branch("main"), new ArrayList<>());
    }};

    List<WebHook> commitHooks = new ArrayList<>();

    List<WebHook> mergeHooks = new ArrayList<>();

    @Override
    public Branch getBranch(String name) {
        Branch currentBranch = new Branch(name);
        if (branches.containsKey(currentBranch))
            return currentBranch;
        return null;
    }

    @Override
    public Branch newBranch(Branch sourceBranch, String name) {
        Branch newBranch = new Branch(name);
        if (branches.containsKey(newBranch) || !branches.containsKey(sourceBranch)){
            throw new IllegalArgumentException();
        }
        branches.put(newBranch, new ArrayList<>());
        branches.get(newBranch).addAll(branches.get(sourceBranch));
        return newBranch;
    }

    @Override
    public Commit commit(Branch branch, String author, String[] changes) {
        Commit newCommit = new Commit(author, changes);
        if (!branches.get(branch).contains(newCommit)) {
            branches.get(branch).add(newCommit);
            for (WebHook webHook : commitHooks) {
                if (webHook.branch().equals(branch.toString()))
                    webHook.onEvent(new Event(Event.Type.COMMIT, branch, new ArrayList<>() {{
                        add(newCommit);
                    }}));
            }
        }
        return newCommit;
    }

    @Override
    public void merge(Branch sourceBranch, Branch targetBranch) {
        ArrayList<Commit> newCommits = new ArrayList<>();
        for (Commit commit : branches.get(sourceBranch)) {
            if (!branches.get(targetBranch).contains(commit)){
                newCommits.add(commit);
            }
        }
        if (newCommits.size() != 0) {
            for (WebHook webHook : mergeHooks) {
                if (webHook.branch().equals(targetBranch.toString()))
                    webHook.onEvent(new Event(Event.Type.MERGE, targetBranch, newCommits));
            }
            branches.get(targetBranch).addAll(newCommits);
        }
    }

    @Override
    public void addWebHook(WebHook webHook) {
        if (webHook.type() == Event.Type.COMMIT) {
            commitHooks.add(webHook);
        } else {
            mergeHooks.add(webHook);
        }
    }
}
