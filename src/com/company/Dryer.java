package com.company;


import java.util.Random;

public abstract class Dryer extends Unit{

    private int dryerID;
    private double cash = 0;


    public double getCash() {
        return cash;
    }

    public int getDryerID() {
        return dryerID;
    }

    public void setDryerID(int dryerID) {
        this.dryerID = dryerID;
    }

    public Dryer(int unitType) {
        super(unitType);

    }


    @Override
    public void reset(){
        cash = 0;
        super.reset();
    }

    @Override
    public void run(Customer customer, int quarters) {
//        System.out.println("Dryer " + dryerID + "started.");
        //initializing a 30 minute cycle for now, will build in option to set cycle based on load type and size
        Random random = new Random();
        int minutes = random.nextInt(20) + 20;
        setMinutesLeft(minutes);

        cash += quarters * 0.25;
        //
        super.run(customer, quarters);
    }

    @Override
    public void stop() {
//        System.out.println("Dryer " + dryerID + "stopped.");
        super.stop();
    }

    @Override
    public void update(int minutesPassed) {
        super.update(minutesPassed);
    }
}
