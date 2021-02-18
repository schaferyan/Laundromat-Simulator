package com.company;


import java.io.*;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.time.LocalDate;

public class LaundromatData implements Serializable {

    private HashMap<LocalDate, Day> days;
    private String dataFileName;
    private static final LaundromatData instance = new LaundromatData();
    private static final long serialVersionUID = 35989035L;

    public static LaundromatData getInstance() {
        return instance;
    }

    public void setDataFileName(String dataFileName) {
        this.dataFileName = dataFileName;
    }


//    Each time laundromat.close() is called we write the date, and all relevant data, and store the date, startByte
//    and length in a Record object. We add the Date and Record object to a HashMap

//    When the simulation is over, we loop through the Records in the map and write them to the index section of the
//    random access file.

//    RAF contents:
//    offset where Days begin
//    Simulation configuration: start date, unit types - quantity and price rate of each
//    int # of Days
//    int size of each Day in bytes (calculated depending on num customers, washers, dryers)

//    write data for each day
//    when Day objects are added # of Days must be updated

//    Day contents:
//    Date in int form
//    dailyTotal
//    for each unit an ID, a type, a number of turns, a cash total
//    for each Customer an entry time, a washerQueue exit time, a dryer queue entry time, a dryer queue exit time,
//    an exit time, a number of loads
    public void saveDate(Day day){

    }


//    public void saveDate(Day day){
//        Path datePath = FileSystems.getDefault().getPath(dataFileName);
//        if(Files.exists(datePath)){
//            try(ObjectOutputStream dateFile = new ObjectOutputStream(new BufferedOutputStream(Files.newOutputStream(datePath)))){
//                dateFile.writeObject(day);
//            }catch(IOException e){
//                e.printStackTrace();
//            }
//        }
//        try(AppendObjectOutputStream dateFile = new AppendObjectOutputStream(new BufferedOutputStream(Files.newOutputStream(datePath)))){
//                dateFile.writeObject(day);
//        }catch(IOException e){
//            e.printStackTrace();
//        }
//    }

////    for appending to file once it is created
//    public class AppendObjectOutputStream extends ObjectOutputStream{
//
//        public AppendObjectOutputStream(OutputStream out) throws IOException {
//            super(out);
//        }
//
//        protected AppendObjectOutputStream() throws IOException, SecurityException {
//        }
//
//        @Override
//        protected void writeStreamHeader() throws IOException {
////            don't write the stream header
//        }
//    }
}
