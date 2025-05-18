package com.mycompany.motorphpayrollsystem;

import java.io.*;
import java.util.*;

class Employee { // Not public, because EmployeeManager is public in another file.
    private int employeeId;
    private double hourlyRate;
    private double totalHoursWorked;
    private double overtimeHours;
    private String firstName;
    private String lastName;
    private String position;
    private String birthday;
    private double salary;
    private static final double MONTHLY_OVERTIME_THRESHOLD = 160.0;

    public Employee(int employeeId, String firstName, String lastName, String birthday, String position, double hourlyRate, double salary) {
        this.totalHoursWorked = 0;
        this.overtimeHours = 0;
        this.employeeId = employeeId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthday = birthday;
        this.position = position;
        this.hourlyRate = hourlyRate;
        this.salary = salary;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public double getHourlyRate() {
        return hourlyRate;
    }

    public double getTotalHoursWorked() {
        return totalHoursWorked;
    }

    public double getOvertimeHours() {
        return overtimeHours;
    }

    public String getName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getPosition() {
        return position;
    }

    public double getSalary() {
        return salary;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public void setHourlyRate(double hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public void setRenderedHours(double hours) {
        this.totalHoursWorked = hours;
    }

    public void addOvertimeHours(double hours) {
        this.overtimeHours += hours;
    }

    public String toFileString() {
        return employeeId + "," + firstName + "," + lastName + "," + birthday + "," + position + "," + hourlyRate + "," + salary;
    }

    public static Employee fromFileString(String line) {
        String[] parts = line.split(",");

        // Check if the line contains exactly 7 parts
        if (parts.length != 7) {
            System.out.println("\nSkipping invalid employee record: " + line);
            return null; // Return null if the data format is incorrect
        }

        try {
            int employeeId = Integer.parseInt(parts[0]);
            String firstName = parts[1];
            String lastName = parts[2];
            String birthday = parts[3];
            String position = parts[4];
            double hourlyRate = Double.parseDouble(parts[5]);
            double salary = Double.parseDouble(parts[6]);
            return new Employee(employeeId, firstName, lastName, birthday, position, hourlyRate, salary);
        } catch (NumberFormatException e) {
            System.out.println("\nError parsing employee record: " + line);
            return null;
        }
    }

    public void displayInfo() {
        System.out.println("***************************************************************************");
        System.out.println("ID: " + employeeId + " | Name: " + firstName + " | Last Name: " + lastName
                + " | Birthday: " + birthday + " | Position: " + position
                + " | Hourly Rate: " + hourlyRate + " | Salary: " + salary);
        System.out.println("***************************************************************************");
    }

    public void addRenderedHours(double hours) {
        if (hours <= 0) {
            System.out.println("\nInvalid input: Hours should be positive.");
            return;
        }

        totalHoursWorked += hours;

        if (totalHoursWorked > MONTHLY_OVERTIME_THRESHOLD) {
            overtimeHours = totalHoursWorked - MONTHLY_OVERTIME_THRESHOLD;
        }

        double totalAfterAdding = totalHoursWorked + hours;

        if (totalAfterAdding > MONTHLY_OVERTIME_THRESHOLD) {
            double overtimeToAdd = totalAfterAdding - MONTHLY_OVERTIME_THRESHOLD;
            overtimeHours += overtimeToAdd;
            totalHoursWorked = MONTHLY_OVERTIME_THRESHOLD;
        } else {
            totalHoursWorked = totalAfterAdding;
        }
    }

    public void resetMonthlyHours() {
        totalHoursWorked = 0;
        overtimeHours = 0;
    }
}