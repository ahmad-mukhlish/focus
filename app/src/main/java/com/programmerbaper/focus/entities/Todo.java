package com.programmerbaper.focus.entities;

public class Todo {

    private String name;
    private String date;
    private Clock clock;

    public Todo(String name, String date, Clock clock) {
        this.name = name;
        this.date = date;
        this.clock = clock;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public Clock getClock() {
        return clock;
    }
}
