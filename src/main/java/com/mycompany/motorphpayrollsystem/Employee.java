package com.mycompany.motorphpayrollsystem;

/**
 * Employee class to store and manage MotorPH employee information.
 */
public class Employee {
    private int employeeId;
    private double hourlyRate;
    private double totalHoursWorked;
    private double overtimeHours;
    private String firstName;
    private String lastName;

    private static final double MONTHLY_OVERTIME_THRESHOLD = 160.0; 

    public Employee(int employeeId, double hourlyRate, String firstName, String lastName) {
        this.employeeId = employeeId;
        this.hourlyRate = hourlyRate;
        this.totalHoursWorked = 0;
        this.overtimeHours = 0;
        this.firstName = firstName;
        this.lastName = lastName;
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

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }
    
    public void setRenderedHours(double hours) {
    this.totalHoursWorked = hours;
    }

    public void addOvertimeHours(double hours) {
        this.overtimeHours += hours;
    }
    
      
    
    public void addRenderedHours(double hours) {
        if (hours <= 0) {
            System.out.println("Invalid input: Hours should be positive.");
            return;
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

