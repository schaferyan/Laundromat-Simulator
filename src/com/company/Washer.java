package com.company;




public abstract class Washer extends Unit {

    private int washerID;
    private double cash = 0;


    public Washer(int unitType) {
        super(unitType);
    }

    public int getWasherID() {
        return washerID;
    }

    public void setWasherID(int washerID) {
        this.washerID = washerID;
    }


    @Override
    public void stop(){
//        System.out.println("Washer " + washerID + " stopped");
        super.stop();
    }

    @Override
    public void update(int minutesPassed) {
        super.update(minutesPassed);

    }

    public double getCash() {
        return cash;
    }

    @Override
    public void reset(){
        cash = 0;
        super.reset();
    }

    @Override
    public void run(Customer customer, int quarters){
//        System.out.println("Washer " + washerID + " started");
        super.run(customer, quarters);
        cash += quarters * 0.25;
    }
}
