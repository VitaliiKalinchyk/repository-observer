package com.epam.rd.autocode.observer.git;

import java.util.*;

public class SimpleRepository implements Repository {
    private final Map<String, Branch> branches = new LinkedHashMap<>(){{
        put("main", new Branch("main"));
    }};

    List<WebHook> commitHooks = new ArrayList<>();

    List<WebHook> mergeHooks = new ArrayList<>();

    @Override
    public Branch getBranch(String name) {
        return branches.get(name);
    }

    @Override
    public Branch newBranch(Branch sourceBranch, String name) {
        if (branches.containsKey(name) || !branches.containsValue(sourceBranch)){
            throw new IllegalArgumentException();
        }
        Branch newBranch = new Branch(name, sourceBranch.getCommits());
        branches.put(name, newBranch);
        return newBranch;
    }

    @Override
    public Commit commit(Branch branch, String author, String[] changes) {
        Commit newCommit = new Commit(author, changes);
        if (!branch.getCommits().contains(newCommit)) {
            branch.addCommit(newCommit);
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
        for (Commit commit : sourceBranch.getCommits()) {
            if (!targetBranch.getCommits().contains(commit)){
                newCommits.add(commit);
            }
        }
        if (newCommits.size() != 0) {
            for (WebHook webHook : mergeHooks) {
                if (webHook.branch().equals(targetBranch.toString()))
                    webHook.onEvent(new Event(Event.Type.MERGE, targetBranch, newCommits));
            }
            targetBranch.addAllCommits(newCommits);
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
