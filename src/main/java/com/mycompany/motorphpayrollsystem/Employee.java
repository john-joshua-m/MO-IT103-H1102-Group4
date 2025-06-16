package com.mycompany.motorphpayrollsystem;

import java.io.*;
import java.util.*;

public class Employee {
    private int employeeId;
    private String firstName;
    private String lastName;
    private String birthday;
    private String position;
    private double hourlyRate;
    private double salary;

    private String sssNo;
    private String philhealthNo;
    private String tin;
    private String pagibigNo;

    private double totalHoursWorked;
    private double overtimeHours;

    private static final double MONTHLY_OVERTIME_THRESHOLD = 160.0;

    public Employee(int employeeId, String firstName, String lastName, String birthday, String position,
                    double hourlyRate, double salary, String sssNo, String philhealthNo, String tin, String pagibigNo) {
        this.employeeId = employeeId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthday = birthday;
        this.position = position;
        this.hourlyRate = hourlyRate;
        this.salary = salary;
        this.sssNo = sssNo;
        this.philhealthNo = philhealthNo;
        this.tin = tin;
        this.pagibigNo = pagibigNo;
        this.totalHoursWorked = 0;
        this.overtimeHours = 0;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public String getFirstName() {
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

    public double getHourlyRate() {
        return hourlyRate;
    }

    public double getSalary() {
        return salary;
    }

    public String getSssNo() {
        return sssNo;
    }

    public String getPhilhealthNo() {
        return philhealthNo;
    }

    public String getTin() {
        return tin;
    }

    public String getPagibigNo() {
        return pagibigNo;
    }

    public double getTotalHoursWorked() {
        return totalHoursWorked;
    }

    public double getOvertimeHours() {
        return overtimeHours;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
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

    public void setSssNo(String sssNo) {
        this.sssNo = sssNo;
    }

    public void setPhilhealthNo(String philhealthNo) {
        this.philhealthNo = philhealthNo;
    }

    public void setTin(String tin) {
        this.tin = tin;
    }

    public void setPagibigNo(String pagibigNo) {
        this.pagibigNo = pagibigNo;
    }

    public void addRenderedHours(double hours) {
        if (hours <= 0) {
            System.out.println("Invalid input: Hours should be positive.");
            return;
        }

        double potentialTotalHours = totalHoursWorked + hours;

        if (potentialTotalHours > MONTHLY_OVERTIME_THRESHOLD) {
            double regularHoursToAdd = MONTHLY_OVERTIME_THRESHOLD - totalHoursWorked;
            if (regularHoursToAdd > 0) {
                totalHoursWorked += regularHoursToAdd;
            }

            double remainingHours = hours - regularHoursToAdd;
            if (remainingHours > 0) {
                overtimeHours += remainingHours;
            }
        } else {
            totalHoursWorked += hours;
        }
    }

    public void resetMonthlyHours() {
        this.totalHoursWorked = 0;
        this.overtimeHours = 0;
    }

    public String[] toCsvArray() {
        return new String[]{
            String.valueOf(employeeId),
            firstName,
            lastName,
            birthday,
            position,
            String.valueOf(hourlyRate),
            String.valueOf(salary),
            sssNo,
            philhealthNo,
            tin,
            pagibigNo
        };
    }

    public static Employee fromCsvArray(String[] parts) {
        if (parts.length != 11) {
            System.err.println("Error: Invalid number of fields for employee record: " + Arrays.toString(parts));
            return null;
        }
        try {
            int id = Integer.parseInt(parts[0]);
            String firstName = parts[1];
            String lastName = parts[2];
            String birthday = parts[3];
            String position = parts[4];
            double hourlyRate = Double.parseDouble(parts[5]);
            double salary = Double.parseDouble(parts[6]);
            String sssNo = parts[7];
            String philhealthNo = parts[8];
            String tin = parts[9];
            String pagibigNo = parts[10];

            return new Employee(id, firstName, lastName, birthday, position, hourlyRate, salary,
                                sssNo, philhealthNo, tin, pagibigNo);
        } catch (NumberFormatException e) {
            System.err.println("Error parsing numeric data in employee record: " + Arrays.toString(parts) + " - " + e.getMessage());
            return null;
        } catch (Exception e) {
            System.err.println("Unknown error creating employee from CSV: " + Arrays.toString(parts) + " - " + e.getMessage());
            return null;
        }
    }

    public void displayInfo() {
        System.out.println("***************************************************************************");
        System.out.println("ID: " + employeeId + " | Name: " + firstName + " " + lastName);
        System.out.println("Birthday: " + birthday + " | Position: " + position);
        System.out.println("Hourly Rate: " + String.format("%.2f", hourlyRate) + " | Monthly Salary: " + String.format("%.2f", salary));
        System.out.println("SSS No: " + sssNo + " | PhilHealth No: " + philhealthNo + " | TIN: " + tin + " | Pag-IBIG No: " + pagibigNo);
        System.out.println("Total Hours Worked (current period): " + String.format("%.2f", totalHoursWorked));
        System.out.println("Overtime Hours (current period): " + String.format("%.2f", overtimeHours));
        System.out.println("***************************************************************************");
    }
}



