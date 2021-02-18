package com.company;


import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Scanner;

public class Simulator {

//    public static Laundromat laundromat;
    public static Scanner scanner;
//    private int numDays;
    public static UI userInterface;



    public static void main(String[] args) {
        userInterface = new UI();
        userInterface.englishStrings();
        userInterface.newSimulation();
        userInterface.displayMainMenu();



//        scanner = new Scanner(System.in);
//        laundromat = new Laundromat();
//
//        System.out.println("Enter a name for this simulation");
//        String dataFileName = scanner.next() + ".dat";
//
//        System.out.println("Enter number of days to simulate: ");
//        int numDays = scanner.nextInt();
//        scanner.nextLine();
//
//        System.out.println("Enter date for the simulation to start");
//        while(true) {
//            try {
//                String startDate = scanner.nextLine();
//                laundromat.setCurrentDate(startDate);
//                break;
//            } catch (DateTimeParseException e) {
//                System.out.println("Please enter date in MM-dd-yyyy format.");
//            }
//        }
//
//
//        System.out.println("Enter number of washers to purchase");
//        int washersBought = scanner.nextInt();
//        purchaseStandardWashers(washersBought);
//
//        System.out.println("Enter number of dryers to purchase");
//        int dryersBought = scanner.nextInt();
//        purchaseStandardDryers(washersBought);
//
//        System.out.println("What time will the laundromat open?");
//        scanner.nextLine();
//        String openTimeStr = scanner.nextLine();
//        laundromat.setOpenTime(openTimeStr);
//
//        System.out.println("What time will it close?");
//        String closeTimeStr = scanner.nextLine();
//        laundromat.setCloseTime(closeTimeStr);
//
//        System.out.println("How many customers will the laundromat serve, daily?");
//        int numCustomers = scanner.nextInt();
//        laundromat.setNumCustomers(numCustomers);
//
//        System.out.println("How much will you charge for each cycle of a washer?");
//        int quartersPerLoad = scanner.nextInt();
//        laundromat.setWasherRate(quartersPerLoad);
//
//        System.out.println("How much will you charge per minute of drying?");
//        int quartersPerMinute = scanner.nextInt();
//        laundromat.setDryerRate(quartersPerMinute);
//
//
//        laundromat.newFile(dataFileName);
//        for(int i = 0; i < numDays; i++) {
//            laundromat.open();
//            System.out.println("Today's cash total was $" + laundromat.getDailyTotal() +
//                    " --------------------------------------------------------------");
//            laundromat.close();
//            laundromat.prepareNextDay();
//        }


    }

//    need a purchase method for each type of unit.


//    public static void purchaseStandardWashers(int quantity){
//        for(int i = 0; i<quantity; i++){
//            laundromat.addWasher(new StandardWasher());
//        }
//    }
//
//    public static void purchaseStandardDryers(int quantity){
//        for(int i = 0; i<quantity; i++){
//            laundromat.addDryer(new StandardDryer());
//        }
//    }

}
