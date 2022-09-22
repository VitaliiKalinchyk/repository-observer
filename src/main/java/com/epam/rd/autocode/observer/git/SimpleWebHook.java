package com.epam.rd.autocode.observer.git;

import java.util.ArrayList;
import java.util.List;

public class SimpleWebHook implements WebHook {

    private final Branch branch;

    private final Event event;

    private final List<Event> caughtEvents = new ArrayList<>();

    public SimpleWebHook(Branch branch, Event event) {
        this.branch = branch;
        this.event = event;
    }

    @Override
    public String branch() {
        return branch.toString();
    }

    @Override
    public Event.Type type() {
        return event.type();
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
