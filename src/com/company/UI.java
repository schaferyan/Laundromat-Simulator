package com.company;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Scanner;

public class UI {

    public static Laundromat laundromat;
    public static Scanner scanner;


    private String namePrompt;
    private String optDuration;
    private String days;
    private String optSetDate;
    private String showDate;
    private String optSetHours;
    private String showHoursText;
    private String optNumCustomers;
    private String customers;
    private String optPurchase;
    private String showUnits;
    private String optSetPrices;
    private String optRunSim;
    private String optExit;
    private String dateFormatReminder;
    private String timeFormatReminder;
    private String openTimePrompt;
    private String closeTimePrompt;
    private String unitPurchasePrompt;
    private String unitQuantityPrompt;
    private String unitPricePrompt;
    private String changePricePrompt;
    private String exitText;
    private String cashUnit;
    private String showPriceText;
    private String showInventoryText;
    private String newPricePrompt;
    private String optLoad;
    private String optNew;
    private String loadPrompt;
    private String optOptions;
    private String chooseOptionPrompt;
    private String invalidInputText;
    private String showAvgNumTurnsTxt;
    private String showAvgDailyProfitTxt;
    private String showTotalProfitTxt;
    private String showAvgWaitTimeTxt;
    private String showLongestWaitTimeTxt;
    private String optAvProf;
    private String optTotProf;
    private String optAvWaitTime;
    private String optLongWaitTime;
    private String optMainMenu;
    private String optData;
    private String notConfiguredText;
    StringBuilder showPriceStringBuilder;

    static {
        laundromat = new Laundromat();
        scanner = new Scanner(System.in);
    }

    public void englishStrings() {
        namePrompt = "Enter a name for the simulation: ";
        optDuration = "Set the duration of the simulation in days";
        days = "days";
        optSetDate = "Change the start date of the simulation";
        optSetHours = "Change the hours of operation";
        showHoursText = "Currently the Laundromat is open from ";
        optNumCustomers = "Set the number of customers the laundromat will serve per day :";
        customers = "customers";
        optPurchase = "Purchase washer and dryer units ";
        showInventoryText = "Currently you have: ";
        optRunSim = "Run this simulation";
        optExit = "Exit laundromat simulator";

        cashUnit = "quarters";
        showPriceText = "Current rates are: \n";
        optSetPrices = "Change the rates for machines in your laundromat";

        dateFormatReminder = "Please enter a date in the format mm-dd-yyyy";
        timeFormatReminder = "Please enter a time in the format h:mm am/pm";
        openTimePrompt = "Enter an opening time for the laundromat :";
        closeTimePrompt = "What time do you want the laundromat to close";
        unitPurchasePrompt = "What type of unit would you like to buy?";
        unitQuantityPrompt = "How many do you need?";
        unitPricePrompt = "Which unit would you like to set a price for?";
        changePricePrompt = "Enter a new price for the unit. Prices are in quarters per cycle for washers" +
                "and quarters per hour for dryers.";
        exitText = "Exiting simulator...";
        newPricePrompt = "What price would you like to set for ";
        optNew = "Create a new simulation";
        optLoad = "Load a saved simulation";
        loadPrompt = "Enter the name of the simulation";
        optOptions = "View Options Menu";
        chooseOptionPrompt = "Please enter a number to choose an option, then press enter";
        invalidInputText = "Invalid input, please try again";

        showAvgDailyProfitTxt = "Average daily profit :";
        showTotalProfitTxt = "Total profit during span of simulation: ";
        showAvgNumTurnsTxt = "Average number of turns per unit: ";
        showAvgWaitTimeTxt = "Average customer wait time: ";
        showLongestWaitTimeTxt = "Longest customer wait time: ";

        optAvProf = "Calculate average daily profit :";
        optTotProf = "Calculate total profit :";
        optAvWaitTime = "Calculate average wait time :";
        optLongWaitTime = "Calculate longest wait time";
        optMainMenu = "Return to Main Menu";
        optData = "View data for this simulation";
        notConfiguredText = "You must load a simulation or run a new one before you can view data";
    }

    public void newSimulation() {
        System.out.println(namePrompt);
        String simName = scanner.next();
        laundromat.setSimName(simName);
        laundromat.newFile();
    }

    public int getInt(){
        while(true) {
            try {
                return scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println(invalidInputText);
                scanner.nextLine();
            }
        }
    }

//    public void displayDataMenu(){
//        System.out.println(showAvgDailyProfitTxt + laundromat.getAvgDailyProfit());
//        System.out.println(showAvgNumTurnsTxt + laundromat.getAvgNumTurns());
//        System.out.println(showTotalProfitTxt + laundromat.getTotalProfit());
//        System.out.println(showAvgWaitTimeTxt + laundromat.getAvgWaitTime());
//        System.out.println(showLongestWaitTimeTxt + laundromat.getLongestWaitTime());
//
//        System.out.println("1. ----------------" + opt + "\n" +
//                "2. ----------------" + opt + "\n" +
//                "3. ----------------" + opt + "\n" +
//                "4. ----------------" + opt + "\n" +
//                "5. ----------------" + opt + "\n";
//    }

    public void displayOptionsMenu() {
        System.out.println("1. ----------------" + optNew + "\n" +
                "2. ----------------" + optLoad + "\n" +
                "3. ----------------" + optData + "\n" +
                "4. ----------------" + optExit);

        switch (getInt()) {
            case 1:
                laundromat = new Laundromat();
                break;
            case 2:
                laundromat = new Laundromat();
                loadConfig();
                break;
            case 3:
                if(laundromat.isConfigured()) {
                    displayDataMenu();
                }else{
                    System.out.println(notConfiguredText);
                }
            case 4:
                exit();
                break;
            default:
                System.out.println(invalidInputText);
                System.out.println(chooseOptionPrompt);
                break;
        }

    }


    public void displayDataMenu(){
        boolean datamenuSelected = true;
        while(datamenuSelected) {
            System.out.println("1. ----------------" + optAvProf + "\n" +
                    "2. ----------------" + optTotProf + "\n" +
                    "3. ----------------" + optAvWaitTime + "\n" +
                    "4. ----------------" + optLongWaitTime + "\n" +
                    "5. ----------------" + optMainMenu + "\n" +
                    "6. ----------------" + optExit);

            switch (getInt()) {
                case 1:
                    System.out.println(showAvgDailyProfitTxt + " " + laundromat.getAvgDailyProfit());
                    break;
                case 2:
                    System.out.println(showTotalProfitTxt + " " + laundromat.getTotalProfit());
                    break;
                case 3:
                    System.out.println(showAvgWaitTimeTxt + " " + laundromat.getAvgWaitTime());
                    break;
                case 4:
                    System.out.println(showLongestWaitTimeTxt + " " + laundromat.getLongestWaitTime());
                    break;
                case 5:
                    System.out.println("Returning to main menu...");
                    return;
                case 6:
                    System.out.println("Exiting...");
                    datamenuSelected = false;
                    exit();
                    break;
                default:
                    break;
            }
        }
    }

    private void loadConfig() {
        System.out.println(loadPrompt);
        String name = scanner.next();
        laundromat.setSimName(name);
        laundromat.readConfigFile();
    }

    public void displayMainMenu() {
        boolean running = true;
        while (running) {
            System.out.println(chooseOptionPrompt);

            System.out.println("1. ----------------" + optDuration + "\n" +
                    "                   " + laundromat.getNumDays() + " " + days + "\n" +
                    "2. ----------------" + optSetDate + "\n" +
                    "                   " + laundromat.getStartDate().format(laundromat.getDateFormatter()) + "\n" +
                    "3. ----------------" + optSetHours + "\n" +
                    "                   " + showHoursText + laundromat.getOpenTime().format(laundromat.getTimeFormatter()) +
                    " - " + laundromat.getCloseTime().format(laundromat.getTimeFormatter()) + "\n" +
                    "4. ----------------" + optNumCustomers + "\n" +
                    "                   " + laundromat.getNumCustomers() + " " + customers + "\n" +
                    "5. ----------------" + optPurchase + "\n" +
                    "                   " + showInventory() + "\n" +
                    "6. ----------------" + optSetPrices + "\n" +
                    "                   " + showPrices() + "\n" +
                    "7. ----------------" + optRunSim + "\n" +
                    "8. ----------------" + optOptions + "\n" +
                    "9. ----------------" + optExit + "\n");


            switch (getInt()) {
                case 1:
                    setDuration();
                    break;
                case 2:
                    setDate();
                    break;
                case 3:
                    setHours();
                    break;
                case 4:
                    setNumCustomers();
                    break;
                case 5:
                    purchaseUnits(); // need to create method that allows user to specify unit type
                    break;
                case 6:
                    setPrices(); ////need to create Type object to include price in map
                    break;
                case 7:
                    runSimulation();
                    break;
                case 8:
                    displayOptionsMenu();
                    break;
                case 9:
                    running = false;
                    exit();
                    break;
                default:
                    System.out.println(invalidInputText);
                    System.out.println(chooseOptionPrompt);
                    break;
            }
        }

    }

    private void setPrices() {
        System.out.println(unitPricePrompt);
        System.out.println(showPrices());
        int unitType = getInt() - 1;
        Type type = laundromat.getTypes().get(unitType);
        System.out.println(newPricePrompt + type.getName() + "s?");
        int price = getInt();
        type.setPrice(price);
    }

    private void exit() {
        System.out.println(exitText);
    }

    private void runSimulation() {
        laundromat.writeConfigFile();
        for(int i=0; i< laundromat.getNumDays(); i++) {
            laundromat.open();
        }
        displayDataMenu();
    }

    private String showPrices() {
        showPriceStringBuilder = new StringBuilder(showPriceText);
        HashMap<Integer, Type> types = laundromat.getTypes();
        for (int type : types.keySet()) {
            showPriceStringBuilder.append("                             ")
                    .append(type + 1)
                    .append(". --------")
                    .append(Unit.typeNames[type])
                    .append(": ")
                    .append(types.get(type).getPrice())
                    .append(" ")
                    .append(cashUnit)
                    .append("\n");
        }

        return showPriceStringBuilder.toString();
    }

    private String showInventory() {
        StringBuilder stringBuilder = new StringBuilder(showInventoryText + "\n");
        HashMap<Integer, Type> types = laundromat.getTypes();
        for (int i = 0; i < types.size(); i++) {
            Type type = types.get(i);
            stringBuilder.append("                             ")
                    .append(i + 1)
                    .append(". --------")
                    .append(type.getName())
                    .append(" ")
                    .append(type.getQuantity())
                    .append("\n");
        }
        return stringBuilder.toString();
    }

    private void purchaseUnits() {
        System.out.println(unitPurchasePrompt);
        System.out.println(showInventory());
        int type = getInt() - 1;
        System.out.println("you chose type " + type );
        while(type < 0 || type >= laundromat.getTypes().size()){
            System.out.println(invalidInputText);
            type = getInt() - 1;
        }
        System.out.println(unitQuantityPrompt);
        int quantity = getInt();
        laundromat.purchaseUnits(type, quantity);
    }

    private void setNumCustomers() {

        System.out.println(optNumCustomers);
        laundromat.setNumCustomers(scanner.nextInt());
    }

    private LocalTime getTime(){
        DateTimeFormatter formatter = laundromat.getTimeFormatter();
        while(true) {
            try {
                return LocalTime.parse(scanner.nextLine().toUpperCase(), formatter);
            } catch (DateTimeParseException e) {
                System.out.println(timeFormatReminder);
            }
        }
    }
    private void setHours() {
        scanner.nextLine();
        System.out.println(openTimePrompt);
        laundromat.setOpenTime(getTime());
        System.out.println(closeTimePrompt);
        laundromat.setCloseTime(getTime());
    }


    private void setDuration() {
        System.out.println(optDuration);
        laundromat.setNumDays(getInt());
    }


    private LocalDate getDate(){
        while (true) {
            try {
                return LocalDate.parse(scanner.nextLine(), laundromat.getDateFormatter());
            } catch (Exception e) {
                System.out.println(dateFormatReminder);
            }
        }
    }

    private void setDate() {
        scanner.nextLine();
        System.out.println(optSetDate);
        laundromat.setStartDate(getDate());
    }

}
