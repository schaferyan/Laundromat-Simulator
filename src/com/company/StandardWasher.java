package com.company;

import java.io.Serializable;

public class StandardWasher extends Washer implements Serializable {


//    private Temp temp;
//    private Soil soil;
    private Cycle cycle;
    private static final long serialVersionUID = 235097L;

    public StandardWasher() {
        super(Unit.STANDARD_WASHER);
//        temp = Temp.COLD;
//        soil = Soil.MEDIUM;
        cycle = Cycle.MEDIUM;
    }

//    public enum Temp implements Serializable {
//        HOT, WARM, COLD;
//    }

//    public enum Soil implements Serializable{
//        LIGHT, MEDIUM, HEAVY;
//    }

    public enum Cycle implements Serializable{
        SHORT, MEDIUM, LONG;
    }



//    public Temp getTemp() {
//        return temp;
//    }
//
//    public void setTemp(Temp temp) {
//        this.temp = temp;
//    }
//
//    public Soil getSoil() {
//        return soil;
//    }
//
//    public void setSoil(Soil soil) {
//        this.soil = soil;
//    }

    public Cycle getCycle() {
        return cycle;
    }

    public void setCycle(Cycle cycle) {
        this.cycle = cycle;
    }

    @Override
    public void stop() {
        super.stop();
    }

    @Override
    public void update(int minutesPassed) {
        super.update(minutesPassed);
    }

    @Override
    public void run(Customer customer, int quarters) {
        super.run(customer, quarters);

        switch (cycle) {
            case SHORT:
                setMinutesLeft(25);
                break;

            case MEDIUM:
                setMinutesLeft(40);
                break;

            case LONG:
                setMinutesLeft(60);
                break;

            default:
                System.out.println("No cycle selected, washer will not run");
                setMinutesLeft(0);
                stop();
        }
    }
}
