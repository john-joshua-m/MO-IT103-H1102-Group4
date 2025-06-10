package com.mycompany.motorphpayrollsystem;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class Motorphpayrollsystem {


    // Constants for PhilHealth (based on 2023 rates, 4.0% premium, 50/50 employer/employee)
    private static final double PHILHEALTH_RATE = 0.04; // 4% of monthly basic salary
    private static final double PHILHEALTH_MAX_CONTRIBUTION = 1600.00; // Max contribution for basic salary > P40,000

    // Constants for Pag-IBIG (based on 2023 rates)
    private static final double PAGIBIG_EMPLOYEE_CONTRIBUTION_RATE = 0.02; // 2% of basic salary
    private static final double PAGIBIG_EMPLOYER_CONTRIBUTION_RATE = 0.02; // 2% of basic salary
    private static final double PAGIBIG_MAX_SALARY_FOR_CONTRIBUTION = 5000.00; // Max monthly basic salary for contribution base

  
    public static double calculateSSSContribution(double basicSalary) {
   
        if (basicSalary < 3250) return 135.00; // Min for employee share 
        else if (basicSalary <= 3749.99) return 157.50;
        else if (basicSalary <= 4249.99) return 180.00;
        else if (basicSalary <= 4749.99) return 202.50;
        else if (basicSalary <= 5249.99) return 225.00;
        else if (basicSalary <= 5749.99) return 247.50;
        else if (basicSalary <= 6249.99) return 270.00;
        else if (basicSalary <= 6749.99) return 292.50;
        else if (basicSalary <= 7249.99) return 315.00;
        else if (basicSalary <= 7749.99) return 337.50;
        else if (basicSalary <= 8249.99) return 360.00;
        else if (basicSalary <= 8749.99) return 382.50;
        else if (basicSalary <= 9249.99) return 405.00;
        else if (basicSalary <= 9749.99) return 427.50;
        else if (basicSalary <= 10249.99) return 450.00;
        else if (basicSalary <= 10749.99) return 472.50;
        else if (basicSalary <= 11249.99) return 495.00;
        else if (basicSalary <= 11749.99) return 517.50;
        else if (basicSalary <= 12249.99) return 540.00;
        else if (basicSalary <= 12749.99) return 562.50;
        else if (basicSalary <= 13249.99) return 585.00;
        else if (basicSalary <= 13749.99) return 607.50;
        else if (basicSalary <= 14249.99) return 630.00;
        else if (basicSalary <= 14749.99) return 652.50;
        else if (basicSalary <= 15249.99) return 675.00;
        else if (basicSalary <= 15749.99) return 697.50;
        else if (basicSalary <= 16249.99) return 720.00;
        else if (basicSalary <= 16749.99) return 742.50;
        else if (basicSalary <= 17249.99) return 765.00;
        else if (basicSalary <= 17749.99) return 787.50;
        else if (basicSalary <= 18249.99) return 810.00;
        else if (basicSalary <= 18749.99) return 832.50;
        else if (basicSalary <= 19249.99) return 855.00;
        else if (basicSalary <= 19749.99) return 877.50;
        else return 900.00; // For 20,000 and above 
    }

    
    public static double calculateWithholdingTax(double grossIncome) {

        if (grossIncome <= 20833) { 
            return 0;
        } else if (grossIncome <= 33333) { // Up to 400,000 annually
            return (grossIncome - 20833) * 0.20;
        } else if (grossIncome <= 66667) { // Up to 800,000 annually
            return 2500 + (grossIncome - 33333) * 0.25;
        } else if (grossIncome <= 166667) { // Up to 2,000,000 annually
            return 10833.33 + (grossIncome - 66667) * 0.30;
        } else if (grossIncome <= 666667) { // Up to 8,000,000 annually
            return 40833.33 + (grossIncome - 166667) * 0.32;
        } else { // Over 8,000,000 annually
            return 200833.33 + (grossIncome - 666667) * 0.35;
        }
    }


  
     //Calculates the monthly gross salary for an employee.        
    public static double calculateGrossSalary(Employee employee) {
        if (employee == null) {
            System.err.println("Error: Employee object is null for gross salary calculation.");
            return 0.0;
        }
        double basicSalary = employee.getSalary();
        double hourlyRate = employee.getHourlyRate();
        double overtimePay = employee.getOvertimeHours() * hourlyRate * 1.25; // 125% for overtime
        return round(basicSalary + overtimePay);
    }

    /**
     * Calculates the monthly PhilHealth contribution.
     * @param basicSalary Employee's monthly basic salary
     * @return Monthly PhilHealth contribution
     */
    public static double calculatePhilhealthContribution(double basicSalary) {
        double contribution = basicSalary * PHILHEALTH_RATE / 2; // Employee's share (50%)
        return round(Math.min(contribution, PHILHEALTH_MAX_CONTRIBUTION / 2)); // Cap at max contribution, employee share
    }

    /**
     * Calculates the monthly Pag-IBIG contribution.
     * @param basicSalary Employee's monthly basic salary
     * @return Monthly Pag-IBIG contribution
     */
    public static double calculatePagibigContribution(double basicSalary) {
        double contributionBase = Math.min(basicSalary, PAGIBIG_MAX_SALARY_FOR_CONTRIBUTION);
        return round(contributionBase * PAGIBIG_EMPLOYEE_CONTRIBUTION_RATE);
    }

    /**
     * Calculates the total monthly deductions (SSS, PhilHealth, Pag-IBIG, Withholding Tax).
     * @param grossSalary Employee's monthly gross salary
     * @param basicSalary Employee's monthly basic salary
     * @return Total monthly deductions
     */
    public static double calculateTotalDeductions(double grossSalary, double basicSalary) {
        double sssDeduction = calculateSSSContribution(basicSalary);
        double philhealthDeduction = calculatePhilhealthContribution(basicSalary);
        double pagibigDeduction = calculatePagibigContribution(basicSalary);
        double taxDeduction = calculateWithholdingTax(grossSalary - sssDeduction - philhealthDeduction - pagibigDeduction); // Taxable income after mandatory deductions

        return round(sssDeduction + philhealthDeduction + pagibigDeduction + taxDeduction);
    }

    /**
     * Calculates the monthly net salary.
     * Net Salary = Gross Salary - Total Deductions
     * @param employee The employee object
     * @return Monthly Net Salary
     */
    public static double calculateNetSalary(Employee employee) {
        if (employee == null) {
            System.err.println("Error: Employee object is null for net salary calculation.");
            return 0.0;
        }
        double grossSalary = calculateGrossSalary(employee);
        double totalDeductions = calculateTotalDeductions(grossSalary, employee.getSalary());
        return round(grossSalary - totalDeductions);
    }

    /**
     * Rounds a double value to two decimal places using BigDecimal for precision.
     * @param value The double value to round.
     * @return The rounded double value.
     */
    public static double round(double value) {
        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    // Method to display a detailed monthly payslip
    public static void displayPayslip(Employee employee) {
        if (employee == null) {
            System.out.println("Employee not found for payslip generation.");
            return;
        }

        double basicSalary = employee.getSalary();
        double hourlyRate = employee.getHourlyRate();
        double totalRenderedHours = employee.getTotalHoursWorked();
        double overtimeHours = employee.getOvertimeHours();
        double grossSalary = calculateGrossSalary(employee);

        double sssDeduction = calculateSSSContribution(basicSalary);
        double philhealthDeduction = calculatePhilhealthContribution(basicSalary);
        double pagibigDeduction = calculatePagibigContribution(basicSalary);
        double taxableIncome = grossSalary - sssDeduction - philhealthDeduction - pagibigDeduction;
        double taxDeduction = calculateWithholdingTax(taxableIncome);
        double totalDeductions = round(sssDeduction + philhealthDeduction + pagibigDeduction + taxDeduction);

        double netSalary = round(grossSalary - totalDeductions);


        System.out.println("\n--- Monthly Payslip for " + employee.getFullName() + " (ID: " + employee.getEmployeeId() + ") ---");
        System.out.println("----------------------------------------------------------");
        System.out.printf("Position: %s%n", employee.getPosition());
        System.out.printf("Monthly Basic Salary: P %.2f%n", basicSalary);
        System.out.printf("Hourly Rate: P %.2f%n", hourlyRate);
        System.out.printf("Total Hours Worked (Regular): %.2f hours%n", totalRenderedHours);
        System.out.printf("Overtime Hours: %.2f hours%n", overtimeHours);
        System.out.printf("Overtime Pay: P %.2f%n", (overtimeHours * hourlyRate * 1.25));
        System.out.printf("Gross Salary: P %.2f%n", grossSalary);
        System.out.println("----------------------------------------------------------");
        System.out.println("Deductions:");
        System.out.printf("  SSS Contribution: P %.2f%n", sssDeduction);
        System.out.printf("  PhilHealth Contribution: P %.2f%n", philhealthDeduction);
        System.out.printf("  Pag-IBIG Contribution: P %.2f%n", pagibigDeduction);
        System.out.printf("  Withholding Tax: P %.2f%n", taxDeduction);
        System.out.printf("Total Deductions: P %.2f%n", totalDeductions);
        System.out.println("----------------------------------------------------------");
        System.out.printf("NET MONTHLY SALARY: P %.2f%n", netSalary);
        System.out.println("----------------------------------------------------------");
    }
}


