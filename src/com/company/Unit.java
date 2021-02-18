package com.company;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashMap;


public abstract class Unit implements Serializable {


    private int minutesLeft;
    private boolean running;
    private Customer customer;
    private int unitType;
    private static final long serialVersionUID = 74594L;
    private int turns;

    static final int STANDARD_WASHER = 0;
    static final int STANDARD_DRYER = 1;
    static final String[] typeNames = {"Standard Washer", "Standard Dryer" };

    public Unit(int unitType) {
        this.unitType = unitType;
        running = false;
        turns = 0;
    }

//    public static class Types {
//        private static final HashMap<Integer, Type> types = new HashMap<>();
//        {
//            types.put(Unit.STANDARD_WASHER, new Type(Unit.STANDARD_WASHER, 20, "Standard Washer", 0 ));
//            types.put(Unit.STANDARD_DRYER, new Type(Unit.STANDARD_DRYER, 10, "Standard Dryer", 0));
//        }
//        public HashMap<Integer, Type> get(){
//            return types;
//        }
//
//        public Type get(int type){
//            return types.get(type);
//        }
//
//    }

    public boolean isRunning() {
        return running;
    }

    public int getMinutesLeft() {
        return minutesLeft;
    }

    public void setMinutesLeft(int minutesLeft) {
        this.minutesLeft = minutesLeft;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public void run(Customer customer, int quarters){
        running = true;
        setCustomer(customer);
    }

    public void stop(){
        minutesLeft = 0;
        running = false;
        turns += 1;
    }

    public void update(int minutesPassed){
        minutesLeft -= minutesPassed;
        if(minutesLeft <= 0){
            stop();
        }
    }
    public int getType(){
        return unitType;
    }

    public int getTurns() {
        return turns;
    }

    public void reset(){
        turns = 0;
    }
}
