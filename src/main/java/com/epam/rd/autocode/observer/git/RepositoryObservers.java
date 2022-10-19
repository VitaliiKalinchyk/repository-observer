package com.epam.rd.autocode.observer.git;

import java.util.ArrayList;
import java.util.List;

public class RepositoryObservers {

    private final List<WebHook> webHooks = new ArrayList<>();

    void addObserver(WebHook webHook) {
        webHooks.add(webHook);
    }

    void removeObserver(WebHook webHook) {
        webHooks.remove(webHook);
    }

    void notifyObservers(Event.Type type, Branch branch, List<Commit> commits) {
        webHooks.stream()
                .filter(webHook -> webHook.type().equals(type))
                .filter(webHook -> webHook.branch().equals(branch.toString()))
                .findFirst()
                .ifPresent(webHook -> webHook.onEvent(new Event(type, branch, commits)));
    }

}
