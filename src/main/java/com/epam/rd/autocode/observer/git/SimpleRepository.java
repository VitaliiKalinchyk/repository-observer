package com.epam.rd.autocode.observer.git;

import java.util.*;

public class SimpleRepository implements Repository {
    private final Map<Branch, ArrayList<Commit>> branches =
            new LinkedHashMap<>(Map.of(new Branch("main"), new ArrayList<>()));

    private final RepositoryObservers repositoryObservers = new RepositoryObservers();

    @Override
    public Branch getBranch(String name) {
        return branches.keySet()
                       .stream()
                       .filter(b -> b.toString().equals(name))
                       .findFirst()
                       .orElse(null);
    }

    @Override
    public Branch newBranch(Branch sourceBranch, String name) {
        Branch newBranch = new Branch(name);
        if (branches.containsKey(newBranch) || !branches.containsKey(sourceBranch)){
            throw new IllegalArgumentException();
        }
        branches.put(newBranch, new ArrayList<>(branches.get(sourceBranch)));
        return newBranch;
    }

    @Override
    public Commit commit(Branch branch, String author, String[] changes) {
        Commit newCommit = new Commit(author, changes);
        branches.get(branch).add(newCommit);
        repositoryObservers.notifyObservers(Event.Type.COMMIT, branch, List.of(newCommit));
        return newCommit;
    }

    @Override
    public void merge(Branch sourceBranch, Branch targetBranch) {
        ArrayList<Commit> newCommits = new ArrayList<>(){{
            addAll(branches.get(sourceBranch));
            removeAll(branches.get(targetBranch));
        }};
        if (!newCommits.isEmpty()) {
            branches.get(targetBranch).addAll(newCommits);
            repositoryObservers.notifyObservers(Event.Type.MERGE, targetBranch, newCommits);
        }
    }

    @Override
    public void addWebHook(WebHook webHook) {
        repositoryObservers.addObserver(webHook);
    }

    @Override
    public void removeWebHook(WebHook webHook) {
        repositoryObservers.removeObserver(webHook);
    }
}