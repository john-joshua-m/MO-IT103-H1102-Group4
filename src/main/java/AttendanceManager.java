/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author jjosh
 */
package com.mycompany.motorphpayrollsystem;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;
import com.opencsv.exceptions.CsvValidationException;
import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AttendanceManager {
    private static AttendanceManager instance;
    private List<AttendanceRecord> attendanceRecords;
    private static final String attendanceCsv = "attendance.csv";
    
    private AttendanceManager() {
        attendanceRecords = new ArrayList<>();
        try {
            loadAttendanceFromFile();
        } catch (IOException ex) {
            Logger.getLogger(AttendanceManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
     public static synchronized AttendanceManager getInstance() {
        if (instance == null) {
            instance = new AttendanceManager();
        }
       return instance; 
     }
     
    public void loadAttendanceFromFile() throws FileNotFoundException, IOException {
          try (CSVReader reader = new CSVReader(new FileReader(attendanceCsv))) {
              DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("h:mm a"); 
              String[] nextLine;
              reader.readNext(); 
              while ((nextLine = reader.readNext()) != null) {
                    int employeeId = Integer.parseInt(nextLine[0]);
                    String lastName = nextLine[1];
                    String firstName = nextLine[2];
                    LocalDate date = LocalDate.parse(nextLine[3], DateTimeFormatter.ofPattern("MM/dd/yyyy"));
                    LocalTime timeIn = LocalTime.parse(nextLine[4].toUpperCase().trim(), timeFormatter);
                    LocalTime timeOut = LocalTime.parse(nextLine[5].toUpperCase().trim(), timeFormatter);
                    AttendanceRecord record = new AttendanceRecord(employeeId, lastName, firstName, date, timeIn, timeOut);
                    attendanceRecords.add(record);
              }
          } catch (CsvValidationException ex) {
            Logger.getLogger(AttendanceManager.class.getName()).log(Level.SEVERE, null, ex);
        }
      }
    
    public void saveAttendanceRecordToFile() throws IOException {
        String tempFile = "attendance.tmp";
        
        try (CSVWriter writer = new CSVWriter(new FileWriter(tempFile))) {
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("h:mm a"); //using 12-hour format
            // Write header
            String[] header = {"Employee #", "Last Name", "First Name", "Date", "Log In", "Log Out"};
            writer.writeNext(header);

            // Write attendance data
            for (AttendanceRecord record : attendanceRecords) {
                String [] attendanceData = {
                    String.valueOf(record.getEmployeeId()),
                    record.getLastName(),
                    record.getFirstName(),
                    record.getDate().format(DateTimeFormatter.ofPattern("MM/dd/yyyy")),
                    record.getTimeIn().format(timeFormatter),
                    record.getTimeOut().format(timeFormatter)        
                };
                writer.writeNext(attendanceData);
            }   
          }            
            
          File originalFile = new File(attendanceCsv);
          File temp = new File(tempFile);
            
          if (!originalFile.delete()) {
          System.err.println("Failed to delete original CSV file.");
          }

          if (!temp.renameTo(originalFile)) {
              System.err.println("Failed to rename temporary attendance file to original CSV file.");
          } else {
              System.out.println("New attendnace record saved to " + attendanceCsv);
            
        }
    }
    
    //---- Attendance Management Methods-----
    
    public boolean addAttendanceRecord(AttendanceRecord newRecord) {
        try {
            // Check if the date for that employee is already recorded
            for (AttendanceRecord record : attendanceRecords) {
                if (record.getEmployeeId() == newRecord.getEmployeeId() && record.getDate().equals(newRecord.getDate())) {
                    return false;
                }
            }
            
            attendanceRecords.add(newRecord);
            saveAttendanceRecordToFile(); //Save after adding
            return true;
        } catch (IOException ex) {
            Logger.getLogger(AttendanceManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
        
    }
    
    public List<AttendanceRecord> getAttendanceByEmployeeId(int employeeId) {
        
        List<AttendanceRecord> records = new ArrayList<>();
        for(AttendanceRecord record : attendanceRecords) {
            if (record.getEmployeeId() == employeeId){
                records.add(record);
            }
        }
        return records;
    }
    
    public List<AttendanceRecord> getAllAttendanceRecords() {
        return new ArrayList<>(attendanceRecords); // Return a copy to prevent modification
    }

    
}

            


