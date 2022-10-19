package com.epam.rd.autocode.observer.git;

import java.util.*;

public class SimpleWebHook implements WebHook {

    private final Branch branch;

    private final Event.Type eventType;

    private final List<Event> caughtEvents = new ArrayList<>();

    public SimpleWebHook(String branchName, Event.Type eventType) {
        this.branch = new Branch(branchName);
        this.eventType = eventType;
    }

    @Override
    public String branch() {
        return branch.toString();
    }

    @Override
    public Event.Type type() {
        return eventType;
    }

    @Override
    public List<Event> caughtEvents() {
        return caughtEvents;
    }

    @Override
    public void onEvent(Event event) {
        caughtEvents.add(event);
    }
}
