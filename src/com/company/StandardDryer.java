package com.company;

import java.io.Serializable;

public class StandardDryer extends Dryer {

    private enum Configuration implements Serializable {
        HIGH, PERMPRESS, WRINKLE, DELICATES, AIRFLUFF;
    }

    Configuration configuration;

    public StandardDryer() {
        super(Unit.STANDARD_DRYER);
        configuration = Configuration.PERMPRESS;
    }

}
