package com.company;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.*;
import java.util.*;

public class Laundromat implements Serializable {

    private HashMap<Integer, Washer> washers = new HashMap<>();
    private HashMap<Integer, Dryer> dryers = new HashMap<>();
    private HashMap<Integer, Type> types = new HashMap<>();
    private PriorityQueue<Dryer> dryersRunning = new PriorityQueue<>(Comparator.comparing(Unit::getMinutesLeft));
    private LinkedList<Dryer> dryersAvailable = new LinkedList<>();
    private PriorityQueue<Washer> washersRunning = new PriorityQueue<>(Comparator.comparing(Unit::getMinutesLeft));
    private LinkedList<Washer> washersAvailable = new LinkedList<>();
    private LinkedList<Customer> dryerQueue = new LinkedList<>();
    private LinkedList<Customer> washerQueue = new LinkedList<>();
    private LinkedList<Customer> customers = new LinkedList<>();
    private PriorityQueue<Customer> entryQueue = new PriorityQueue<>(Comparator.comparing(Customer::getEntryTime));
    private int washersAdded;
    private int dryersAdded;
    private LocalTime openTime = LocalTime.parse("08:00");
    private LocalTime closeTime = LocalTime.parse("20:00");
    private LocalTime currentTime;
    private LocalDate startDate = LocalDate.now();
    private LocalDate currentDate;
    private double dailyTotal = 0;
    private int numCustomers = 80;
    private int numDays = 1;
    private int updateRate = 1;
    private long eof = 0;
    private String rafFileLocation;
    private boolean configured = false;
    private boolean dataWritten = false;
//    private final LaundromatData laundromatData;
    private static final long serialVersionUID = 24973L;
//    public static class SerializableUnitComparator implements Serializable, Comparator<Washer> {
//
//        @Override
//        public int compare(Washer o1, Washer o2) {
//            return o1.getMinutesLeft() - o2.getMinutesLeft();
//        }
//    }
//    public static class SerializableCustomerComparator implements Serializable, Comparator<Customer> {
//
//        @Override
//        public int compare(Customer o1, Customer o2) {
//            return o1.getEntryTime().compareTo(o2.getEntryTime());
//        }
//    }
    private DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("h:mm a");
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
    private final String[] nameArray = new String[]{"Fred", "Bob", "Lisa", "Georgina", "Hilary", "Greg"};
    private String simName;


//    public LaundromatData getLaundromatData() {
//        return laundromatData;
//    }

    public void setNumCustomers(int numCustomers) {
        this.numCustomers = numCustomers;
    }




    public Laundromat() {
        washersAdded = 0;
        dryersAdded = 0;
//        laundromatData = LaundromatData.getInstance();
        currentDate = LocalDate.now();
    }
    {
        types.put(Unit.STANDARD_WASHER, new Type(Unit.STANDARD_WASHER, 20, "Standard Washer", 0 ));
        types.put(Unit.STANDARD_DRYER, new Type(Unit.STANDARD_DRYER, 10, "Standard Dryer", 0));
    }


    private void generateCustomers() {
        Random random = new Random();
        for (int i = 0; i < numCustomers; i++) {
//            randomly generate entry time
            int minutes = random.nextInt((int) openTime.until(closeTime, ChronoUnit.MINUTES));
            LocalTime entryTime = openTime.plusMinutes(minutes);
            int nameIndex = random.nextInt(nameArray.length);
            String name = nameArray[nameIndex];
//            keeping it simple for now and just giving each customer one regular load
//            later will randomize quantity and type of laundry
            int multipleLoads = random.nextInt(1);
            int loads;
            if(multipleLoads == 0){
                loads = 1;
            }else{
                loads = random.nextInt(5) + 1;
            }
            Customer customer = new Customer(name, i, entryTime, loads);
            customers.add(customer);
        }
        entryQueue.addAll(customers);
    }

    public void addWasher(Washer washer) {
        washersAdded += 1;
        washer.setWasherID(washersAdded);
        washers.put(washersAdded, washer);
        Type type = types.get(washer.getType());
//        System.out.println("Added washer of type " + type.getName());
        type.setQuantity(type.getQuantity() + 1);
    }

    public void addDryer(Dryer dryer) {
        dryersAdded += 1;
        dryer.setDryerID(dryersAdded);
        dryers.put(dryersAdded, dryer);
//        System.out.println("Dryer should be of type " + dryer.getType());
        Type type = types.get(dryer.getType());
//        System.out.println("Added dryer of type " + type.getName());
        type.setQuantity(type.getQuantity() + 1);
    }

    public void open() {
        currentTime = LocalTime.from(openTime);
        washersAvailable.addAll(washers.values());
        dryersAvailable.addAll(dryers.values());
        generateCustomers();

        System.out.println(currentDate.toString() + " Opening Laundromat with " + washersAvailable.size() + " washers and " + dryersAvailable.size() +
                " dryers available. Open from " + getOpenTime().toString() + " to " + getCloseTime().toString());

        while (currentTime.isBefore(closeTime)) {

            updateDryers(updateRate);
            updateDryerQueue();
            updateWashers(updateRate);
            updateDryerQueue();
            updateWasherQueue();
            updateEntryQueue();
            updateWasherQueue();
            currentTime = currentTime.plusMinutes(updateRate);
        }
        while (washersRunning.size() > 0 || dryersRunning.size() > 0) {
            updateDryers(1);
            updateDryerQueue();
            updateWashers(1);
            updateDryerQueue();
//            updateWasherQueue();
        }
        close();
    }

    public void close() {
        washersAvailable.clear();
        dryersAvailable.clear();
        calculateDailyTotal();
        writeDay();
        prepareNextDay();
    }

    public void prepareNextDay(){
        currentDate = currentDate.plusDays(1);
        currentTime = LocalTime.from(openTime);
        for(Washer washer : washers.values()){
            washer.reset();
        }
        for (Dryer dryer : dryers.values()){
            dryer.reset();
        }
        customers.clear();
        entryQueue.clear();
        washerQueue.clear();
    }

    private void calculateDailyTotal(){
        dailyTotal = 0;
        for (Washer washer : washers.values()) {
            dailyTotal += washer.getCash();
        }
        for (Dryer dryer : dryers.values()) {
            dailyTotal += dryer.getCash();
        }
        System.out.println( "Calculated daily total of " + dailyTotal + " for date " + currentDate.toString());
    }

    private void updateEntryQueue() {

        while (!entryQueue.isEmpty() && entryQueue.peek().getEntryTime().equals(currentTime)) {
            Customer customer = entryQueue.poll();
            assert customer != null;
            washerQueue.add(customer);
            customer.setWasherQueueEntry(currentTime);
//            System.out.println("Customer " + customer.getName() + " " + customer.getId() + " added to washer queue");
        }
    }

    private void updateWasherQueue() {
        //            If any customers are waiting to use a washer, and any washers are available, have them run the washer
        while (!washerQueue.isEmpty() && !washersAvailable.isEmpty()) {
            Washer washer = washersAvailable.poll();
            Customer customer = washerQueue.poll();
            assert customer != null;
            customer.setWasherQueueExit(currentTime);
            washer.run(customer, types.get(washer.getType()).getPrice());
            washersRunning.add(washer);
        }
    }

    private void updateWashers(int minutesPassed) {
        // Updates timer on all washers that are currently running, and cue them to process any changes

        for (Washer washer : washersRunning) {
            washer.update(minutesPassed);
        }
//      If any washers in the washersRunning queue have stopped running, remove them from the running queue and add
//        them to the available queue and nullify their customer field
        while (!washersRunning.isEmpty() && !washersRunning.peek().isRunning()) {
            Washer washer = washersRunning.poll();
            assert washer != null;
            Customer customer = washer.getCustomer();
            washersAvailable.add(washer);
//            washer.setCustomer(null);
            dryerQueue.add(customer);
            customer.setDryerQueueEntry(currentTime);
        }

    }

    private void updateDryerQueue() {
        //            If any customers are waiting to use a dryer, and any dryers are available, have them run the dryer
        while (!dryerQueue.isEmpty() && !dryersAvailable.isEmpty()) {
            Dryer dryer = dryersAvailable.poll();
            Customer customer = dryerQueue.poll();
            dryer.run(customer, types.get(dryer.getType()).getPrice());
            assert customer != null;
            customer.setDryerQueueExit(currentTime);
//            System.out.println("Customer " + customer.getName() + " " + customer.getId() + " started dryer " +
//                    dryer.getDryerID());
            dryersRunning.add(dryer);
        }
    }

    private void updateDryers(int minutesPassed) {
        //              Updates timer on all dryers that are currently running, and cue them to process any changes
        for (Dryer dryer : dryersRunning) {
            dryer.update(minutesPassed);
        }
//        if there are any dryers in the dryersRunning cue that have stopped running, remove them from the queue, then
//        nullify the customer field and add them to the dryersAvailable queue;
        while (!dryersRunning.isEmpty() && !dryersRunning.peek().isRunning()) {
            Dryer dryer = dryersRunning.poll();
            assert dryer != null;
            Customer customer = dryer.getCustomer();
            customer.setExitTime(currentTime);
            dryersAvailable.add(dryer);
//            dryer.setCustomer(null);
        }

    }

    public HashMap<Integer, Washer> getWashers() {
        return washers;
    }

    public HashMap<Integer, Dryer> getDryers() {
        return dryers;
    }

    public LocalTime getOpenTime() {
        return openTime;
    }

    public void setOpenTime(String openTime) {
        this.openTime = LocalTime.parse(openTime, timeFormatter);
    }

    public void setOpenTime(LocalTime openTime){
        this.openTime = openTime;
    }
    public LocalTime getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(String closeTime) {
        this.closeTime = LocalTime.parse(closeTime, timeFormatter);
    }

    public void setCloseTime(LocalTime closeTime){
        this.closeTime = closeTime;
    }

//    public void setWasherRate(int pricePerLoad) {
//        for (Washer washer : washers.values()) {
//            washer.setQuartersPerLoad(pricePerLoad);
//        }
//    }
//    public void setWasherRate(int type, int quartersPerLoad) {
//        for (Washer washer : washers.values()) {
//            if(washer.getType() == type) {
//                washer.setQuartersPerLoad(quartersPerLoad);
//            }
//        }
//    }
//
//    public void setDryerRate(int quartersPerHour) {
//        for (Dryer dryer : dryers.values()) {
//            dryer.setQuartersPerHour(quartersPerHour);
//        }
//    }
//    public void setDryerRate(int type, int quartersPerHour) {
//        for (Dryer dryer : dryers.values()) {
//            if(dryer.getType() == type) {
//                dryer.setQuartersPerHour(quartersPerHour);
//            }
//        }
//    }

    public double getDailyTotal() {
        return dailyTotal;
    }

    public void setCurrentDate(String currentDate) {
            this.currentDate = LocalDate.parse(currentDate, dateFormatter);
    }

    public LocalDate getCurrentDate() {
        return currentDate;
    }

    //    Change how often, in minutes, to check the status of units and queues
    public void setUpdateRate(int updateRate) {
        this.updateRate = updateRate;
    }

    public Washer findWasherById(int id) {
        return washers.get(id);
    }

    public Dryer findDryerById(int id) {
        return dryers.get(id);
    }

    public int getNumCustomers() {
        return numCustomers;
    }

    public void setNumDays(int numDays) {
        this.numDays = numDays;
    }

    public int getNumDays() {
        return numDays;
    }

    public HashMap<Integer, Type> getTypes() {
        return types;
    }

    public DateTimeFormatter getTimeFormatter() {
        return timeFormatter;
    }

    public void setTimeFormatter(DateTimeFormatter timeFormatter) {
        this.timeFormatter = timeFormatter;
    }

    public DateTimeFormatter getDateFormatter() {
        return dateFormatter;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
        currentDate = LocalDate.from(startDate);
    }

    public void setDateFormatter(DateTimeFormatter dateFormatter) {
        this.dateFormatter = dateFormatter;
    }


//    write data for each day
//    when Day objects are added # of Days at bytes 4-7 must be updated, end of file curser could be saved or
//      calculated from #Days and DaysStartByte

//                                                                              DaysStartAt: 5 * #types + 21
//    Day contents:
                        //    int Day#                                          4bytes
                        //    int dailyTotal                                    4bytes
//    for each unit an ID, a type #, a number of turns today, a cash total      16bytes * num units
//    for each Customer an id, an entry time, a washerQueue exit time, a dryer queue entry time, a dryer queue exit time
//    an exit time, a number of loads                                           28 bytes * num customers
//
//    num bytes in day = 8 + (16 * num units) + (28 * num customers)

   public void writeDay(){
       try(RandomAccessFile raf = new RandomAccessFile(rafFileLocation, "rwd")){
           raf.seek(eof);
           System.out.println("Writing simulation data for " + currentDate.toString() + "to file...");
           int dayNumber = startDate.until(currentDate).getDays();
           System.out.println("writing day number " + dayNumber + " @byte " + raf.getFilePointer());
           raf.writeInt(dayNumber); // Day number, bytes 0-3
           System.out.println("writing daily total " + dailyTotal + " @byte " + raf.getFilePointer());
           raf.writeInt(  (int) dailyTotal * 100); // Daily total, bytes 4-7

//           8
           System.out.println("Writing washer data starting @byte " + raf.getFilePointer());
           for(int i = 1; i <= washers.size(); i++){ // Washer data, 16 bytes for each washer, starts at 8
               int type = findWasherById(i).getType();// 8-23, 24-39, 40-55 etc until
               int turns = findWasherById(i).getTurns();
               int cash = (int) findWasherById(i).getCash() * 100;
               raf.writeInt(i);
               raf.writeInt(type);
               raf.writeInt(turns);
               raf.writeInt(cash);
           }
//           16 * washers.size() + 8
           System.out.println("Writing dryer data starting @byte " + raf.getFilePointer());
           for(int i = 1; i <= dryers.size(); i++) { // Dryer data, 16 bytes for each dryer
               int type = findDryerById(i).getType();
               int turns = findDryerById(i).getTurns();
               int cash = (int) findDryerById(i).getCash() * 100;
               raf.writeInt(i);
               raf.writeInt(type);
               raf.writeInt(turns);
               raf.writeInt(cash);
           }
           System.out.println("Writing customer data starting @byte " + raf.getFilePointer());
           for(Customer customer : customers){ // Customer data, 28 bytes each customer
               int id = customer.getId();
               int entryTime = customer.getEntryTime().get(ChronoField.MINUTE_OF_DAY);
               int wqEntryTime = customer.getWasherQueueEntry().get(ChronoField.MINUTE_OF_DAY);
               int wqExitTime = customer.getWasherQueueExit().get(ChronoField.MINUTE_OF_DAY);
               int dqEntryTime = customer.getDryerQueueEntry().get(ChronoField.MINUTE_OF_DAY);
               int dqExitTime = customer.getDryerQueueExit().get(ChronoField.MINUTE_OF_DAY);
               int exitTime = customer.getExitTime().get(ChronoField.MINUTE_OF_DAY);

               raf.writeInt(id);
               raf.writeInt(entryTime);
               raf.writeInt(wqEntryTime);
               raf.writeInt(wqExitTime);
               raf.writeInt(dqEntryTime);
               raf.writeInt(dqExitTime);
               raf.writeInt(exitTime);
           }
        eof = raf.getFilePointer();
           System.out.println("Finished writing today's data @byte " + raf.getFilePointer());
       }catch(FileNotFoundException e){
            System.out.println("File not found");
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }
    }


    public int getAvgWaitTime() {
        int customerSize = 28;
        int unitSize = 16;
        int numUnits = washers.size() + dryers.size();
        int startLoc = 8 + unitSize * numUnits;
        int daySize = 8 + (16 * numUnits) + (28 * numCustomers);
        int totalWaitTime = 0;
        try(RandomAccessFile raf = new RandomAccessFile(rafFileLocation, "rwd")) {
            raf.seek(startLoc);
            for (int i = 0; i < getNumDays(); i++) {
                System.out.println("Reading Customer data for Day " + i);
                for (int j = 0; j < numCustomers; j++) {
                    int id = raf.readInt();
                    int entryTime =raf.readInt();
                    int wqEntryTime = raf.readInt();
                    int wqExitTime = raf.readInt();
                    int dqEntryTime = raf.readInt();
                    int dqExitTime = raf.readInt();
                    int exitTime = raf.readInt();
                    System.out.println("Customer " + id + " Entered at " + entryTime + " started waiting at " + wqEntryTime + " used a washer at " + wqExitTime
                    + " finished using the washer at " + dqEntryTime + " then used a dryer at " + dqExitTime + " then exited " +
                            " at " + exitTime);
                    if(wqExitTime > 0) {
                        totalWaitTime += wqExitTime - wqEntryTime;
                        totalWaitTime += dqEntryTime - dqExitTime;
                    }
                }
                raf.seek(raf.getFilePointer() + daySize - (customerSize * numCustomers));
            }
        } catch( IOException e){
            e.printStackTrace();
        }
       int avgTotalWaitTime = totalWaitTime / (numCustomers * numDays);
       System.out.println("Average total wait time is " + avgTotalWaitTime);
       return avgTotalWaitTime;
    }

    public int getLongestWaitTime(){
        int customerSize = 28;
        int unitSize = 16;
        int numUnits = washers.size() + dryers.size();
        int startLoc = 8 + unitSize * numUnits;
        int daySize = 8 + (16 * numUnits) + (28 * numCustomers);
        int longestWaitTime = 0;
        try(RandomAccessFile raf = new RandomAccessFile(rafFileLocation, "rwd")) {
            raf.seek(startLoc);
            for (int i = 0; i < getNumDays(); i++) {
                System.out.println("Reading Customer data for Day " + i);
                for (int j = 0; j < numCustomers; j++) {
                    int id = raf.readInt();
                    int entryTime =raf.readInt();
                    int wqEntryTime = raf.readInt();
                    int wqExitTime = raf.readInt();
                    int dqEntryTime = raf.readInt();
                    int dqExitTime = raf.readInt();
                    int exitTime = raf.readInt();
                    System.out.println("Customer " + id + " Entered at " + entryTime + " started waiting at " + wqEntryTime + " used a washer at " + wqExitTime
                            + " finished using the washer at " + dqEntryTime + " then used a dryer at " + dqExitTime + " then exited " +
                            " at " + exitTime);
                    if(wqExitTime > 0) {
                        int waitTime = wqExitTime - wqEntryTime + dqEntryTime - dqExitTime;
                        if (waitTime > longestWaitTime) {
                            longestWaitTime = waitTime;
                        }
                    }
                }
                raf.seek(raf.getFilePointer() + daySize - (customerSize * numCustomers));
            }
        } catch( IOException e){
            e.printStackTrace();
        }

        System.out.println("Longest wait time is " + longestWaitTime);
        return longestWaitTime;
    }

    public double getAvgDailyProfit() {
        try(RandomAccessFile raf = new RandomAccessFile(rafFileLocation, "rwd")){
            int startLoc = 4;
            int numUnits = washers.size() + dryers.size();
            int dayLength = 8 + (16 * numUnits) + (28 * numCustomers);
            raf.seek(startLoc);
            int runningTotal = 0;
            for(int i = 0; i < numDays; i++){
                System.out.println("Reading daily total @byte " + raf.getFilePointer());
                dailyTotal = raf.readInt();
                runningTotal += dailyTotal;
                System.out.println("Daily total is " + dailyTotal);
                System.out.println("Running total is now " + runningTotal + " and file pointer is @byte" + raf.getFilePointer());
                raf.seek(raf.getFilePointer() + dayLength - 4);
                System.out.println("File pointer jumped ahead to @byte" + raf.getFilePointer());
            }
            return runningTotal/numDays/100;
        } catch(IOException e){
           e.printStackTrace();
           return -1;
        }
    }

    public double getTotalProfit() {
        try(RandomAccessFile raf = new RandomAccessFile(rafFileLocation, "rwd")){
            int startLoc = 4;
            int numUnits = washers.size() + dryers.size();
            int dayLength = 8 + (16 * numUnits) + (28 * numCustomers);
            raf.seek(startLoc);
            int runningTotal = 0;
            for(int i = 0; i < numDays; i++){
                System.out.println("Reading daily total @byte " + raf.getFilePointer());
                dailyTotal = raf.readInt() / 100;
                runningTotal += dailyTotal;
                System.out.println("Daily total is " + dailyTotal);
                System.out.println("Running total is now " + runningTotal + " and file pointer is @byte" + raf.getFilePointer());
                raf.seek(raf.getFilePointer() + dayLength - 4);
                System.out.println("File pointer jumped ahead to @byte" + raf.getFilePointer());
            }
            return runningTotal;
        } catch(IOException e){
            e.printStackTrace();
            return -1;
        }
    }

    public int getAvgTurnsPerUnitPerDay(){
        try(RandomAccessFile raf = new RandomAccessFile(rafFileLocation, "rwd")){
            int unitSize = 16;
            int startLoc = 16;
            int numUnits = washers.size() + dryers.size();
            int daySize = 8 + (16 * numUnits) + (28 * numCustomers);
            int totalTurns = 0;
            int turns;
            raf.seek(startLoc);
            for(int i = 0; i< numDays; i++) {
                for (int j = 0; j < numUnits; j++) {
                    turns = raf.readInt();
                    totalTurns += turns;
                    System.out.println("Read " + turns + "turns, for a total of " + totalTurns + "turns today");
                    raf.seek(raf.getFilePointer() + unitSize - 4);
                }
                raf.seek(raf.getFilePointer() + daySize - (unitSize * numUnits));
                System.out.println("Total turns for day " + i + " = " + totalTurns );
            }
            System.out.println("Total turns :" + totalTurns);
            int avgTurnsPerUnitPerDay = totalTurns/(numUnits * numDays);
            System.out.println("Average turns per day is " + avgTurnsPerUnitPerDay);
            return avgTurnsPerUnitPerDay;
        }catch(IOException e){
            e.printStackTrace();
            return -1;
        }

    }

    public void newFile(){
        try{
            Path path = Files.createDirectories(Paths.get(simName));
        }catch (IOException e){
            e.printStackTrace();
        }

    }

    public void writeConfigFile(){
        Charset charset = StandardCharsets.UTF_8;
        Path file = Paths.get(simName + "/ " + "config.txt");
        try (BufferedWriter writer = Files.newBufferedWriter(file, charset)) {
            writer.write(numDays);
            writer.write(currentDate.getLong(ChronoField.EPOCH_DAY) + "\n");
            writer.write(openTime.get(ChronoField.MINUTE_OF_DAY));
            writer.write(closeTime.get(ChronoField.MINUTE_OF_DAY));
            writer.write(numCustomers);
            writer.write(types.size());
            for(int i = 0; i < types.size(); i++){
                int quantity = types.get(i).getQuantity();
                int price = types.get(i).getPrice();
                System.out.println("Writing type " + i + " with quantity " + quantity + " and price " + price);
                writer.write(i);
                writer.write(quantity);
                writer.write(price);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        rafFileLocation = simName + "/ " + "records.dat";
        configured = true;
    }

    public void readConfigFile(){
        Path file = Paths.get(simName + "/ " + "config.txt");
        try(BufferedReader reader = Files.newBufferedReader(file, StandardCharsets.UTF_8)){
            numDays = reader.read();
            startDate = LocalDate.ofEpochDay(Long.parseLong(reader.readLine()));
            openTime = LocalTime.ofSecondOfDay(reader.read() * 60);
            closeTime = LocalTime.ofSecondOfDay(reader.read() * 60);
            numCustomers = reader.read();
            System.out.println("Reading configuration data into memory: " + "\n" +
                    "Number of days = " + numDays + "\n" +
                    "current date is " + currentDate.toString() + "\n" +
                    "hours are " + openTime.toString() + " - " + closeTime.toString() + "\n" +
                    "Number of Customers : " + numCustomers);
            int numTypes = reader.read();
            System.out.println("Reading " + numTypes + " types");
            for(int i = 0; i < numTypes; i++){
                int type = reader.read();
                int quantity = reader.read();
                int price = reader.read();
                System.out.println("Reading type " + type + " with quantity " + quantity + " and price " + price );
                types.get(i).setPrice(price);
                purchaseUnits(type, quantity);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        rafFileLocation = simName + "/ " + "records.dat";
        configured = true;
    }

    public void purchaseUnits(int type, int quantity) {
        System.out.println("purchasing " + quantity + " of type " + type);
        switch(type){
            case Unit.STANDARD_WASHER:
                purchaseStandardWashers(quantity);
                break;
            case Unit.STANDARD_DRYER:
                purchaseStandardDryers(quantity);
                break;
            default:
                break;
        }

    }

    public void purchaseStandardWashers(int quantity){
        for(int i = 0; i<quantity; i++){
            addWasher(new StandardWasher());
        }

    }

    public void purchaseStandardDryers(int quantity){
        for(int i = 0; i<quantity; i++){
            addDryer(new StandardDryer());
        }
    }

    public void setSimName(String simName) {
        this.simName = simName;
    }

    public boolean isConfigured() {
        return configured;
    }
}


