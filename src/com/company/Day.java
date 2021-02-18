package com.company;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalTime.*;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

public class Day implements Serializable {
    private HashMap<Integer, Washer> washers;
    private HashMap<Integer, Dryer> dryers;
    private PriorityQueue<Customer> customers;
    LocalDate date;
    double cashTotalToday;
    private static final long serialVersionUID = 12794L;

    public Day(HashMap<Integer, Washer> washers, HashMap<Integer, Dryer> dryers, PriorityQueue<Customer> customers, LocalDate date, double cashTotalToday) {
        this.washers = washers;
        this.dryers = dryers;
        this.customers = customers;
        this.date = date;
        this.cashTotalToday = cashTotalToday;
    }
}
