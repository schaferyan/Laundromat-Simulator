package com.company;

import java.io.Serializable;
import java.time.LocalTime;

public class Customer implements Serializable {



    private final String name;
    private final int id;
    private final int loads;
    private final LocalTime entryTime;
    private LocalTime exitTime = LocalTime.of(0, 0);
    private LocalTime washerQueueEntry = LocalTime.of(0, 0);
    private LocalTime washerQueueExit = LocalTime.of(0, 0);
    private LocalTime dryerQueueEntry = LocalTime.of(0, 0);
    private LocalTime dryerQueueExit = LocalTime.of(0, 0);


    private static final long serialVersionUID = 257835L;

    public Customer(String name, int id, LocalTime entryTime) {
        this.name = name;
        this.id = id;
        this.entryTime = entryTime;
        this.loads = 1;
    }

    public Customer(String name, int id, LocalTime entryTime, int loads) {
        this.name = name;
        this.id = id;
        this.entryTime = entryTime;
        this.loads = loads;
    }

    public LocalTime getExitTime() {
        return exitTime;
    }

    public void setExitTime(LocalTime exitTime) {
        this.exitTime = exitTime;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public LocalTime getEntryTime() {
        return entryTime;
    }


    public LocalTime getWasherQueueEntry() {
        return washerQueueEntry;
    }

    public void setWasherQueueEntry(LocalTime washerQueueEntry) {
        this.washerQueueEntry = washerQueueEntry;
    }

    public LocalTime getWasherQueueExit() {
        return washerQueueExit;
    }

    public void setWasherQueueExit(LocalTime washerQueueExit) {
        this.washerQueueExit = washerQueueExit;
    }

    public LocalTime getDryerQueueEntry() {
        return dryerQueueEntry;
    }

    public void setDryerQueueEntry(LocalTime dryerQueueEntry) {
        this.dryerQueueEntry = dryerQueueEntry;
    }

    public LocalTime getDryerQueueExit() {
        return dryerQueueExit;
    }

    public void setDryerQueueExit(LocalTime dryerQueueExit) {
        this.dryerQueueExit = dryerQueueExit;
    }

    public int getLoads() {
        return loads;
    }
}
