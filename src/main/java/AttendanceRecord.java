/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author jjosh
 */

package com.mycompany.motorphpayrollsystem;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 *
 * @author farha
 */
public class AttendanceRecord {
    
    private int employeeId;
    private String lastName;
    private String firstName;
    private LocalDate date; // MM/DD/YYYY
    private LocalTime timeIn; // HH:mm
    private LocalTime timeOut;// HH:mm
    
    public AttendanceRecord (int employeeId, String lastName, String firstName, LocalDate date, LocalTime timeIn, LocalTime timeOut) {
        this.employeeId = employeeId;
        this.lastName = lastName;
        this.firstName = firstName;
        this.date = date;
        this.timeIn = timeIn;
        this.timeOut = timeOut;
    };
    
    public int getEmployeeId() {
        return employeeId;
    }
    
    public String getLastName() {
        return lastName;
    }
    
    public String getFirstName() {
        return firstName;
    }
    
    public LocalDate getDate() {
        return date;
    }
    
    public LocalTime getTimeIn() {
        return timeIn;
    }
    
    public LocalTime getTimeOut() {
        return timeOut;
    }
    
    public int calculateMinutesWorked() {
        if (timeIn != null && timeOut != null && !timeOut.isBefore(timeIn)){
            return (int) java.time.Duration.between(timeIn, timeOut).toMinutes();
        }
        return 0;
    }
    
    public double getHoursWorked() {
        return calculateMinutesWorked() / 60.0;
    }
}



