package com.epam.rd.autocode.observer.git;

import java.util.*;

public class Branch {

    private final String name;

    private final ArrayList<Commit> commits = new ArrayList<>();

    public Branch(final String name) {
        Objects.requireNonNull(name);
        this.name = name;
    }

    public Branch(final String name, ArrayList<Commit> commits) {
        Objects.requireNonNull(name);
        this.name = name;
        this.commits.addAll(commits);
    }

    public ArrayList<Commit> getCommits() {
        return commits;
    }

    public void addCommit(Commit commit) {
        commits.add(commit);
    }

    public void addAllCommits(ArrayList<Commit> commits) {
        this.commits.addAll(commits);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Branch branch = (Branch) o;
        return name.equals(branch.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return name;
    }
}
