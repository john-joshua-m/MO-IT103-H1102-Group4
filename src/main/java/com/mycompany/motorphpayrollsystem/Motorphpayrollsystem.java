package com.mycompany.motorphpayrollsystem;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalTime;
import java.util.List;

public class Motorphpayrollsystem {

    public static double calculateSSSContribution(double basicSalary) {
   
        if(basicSalary < 3250) {
          return 135.00f; //Minimum for employee share
      } else if (basicSalary >= 3250 && basicSalary < 3750) {
          return 157.50f;
      } else if (basicSalary >= 3750 && basicSalary < 4250) {
          return 180.00f;
      } else if (basicSalary >= 4250 && basicSalary < 4750) {
          return 202.50f;
      } else if (basicSalary >= 4750 && basicSalary < 5250) {
          return 225.00f;
      } else if (basicSalary >= 5250 && basicSalary < 5750) {
          return 247.50f;
      } else if (basicSalary >= 5750 && basicSalary < 6250) {
          return 270.00f;
      } else if (basicSalary >= 6250 && basicSalary < 6750) {
          return 292.50f;
      } else if (basicSalary >= 6750 && basicSalary < 7250) {
          return 315.00f;
      } else if (basicSalary >= 7250 && basicSalary < 7750) {
          return 337.50f;
      } else if (basicSalary >= 7750 && basicSalary < 8250) {
          return 360.00f;
      } else if (basicSalary >= 8250 && basicSalary < 8750) {
          return 382.50f;
      } else if (basicSalary >= 8750 && basicSalary < 9250) {
          return 405.00f;
      } else if (basicSalary >= 9250 && basicSalary < 9750) {
          return 427.50f;
      } else if (basicSalary >= 9750 && basicSalary < 10250) {
          return 450.00f;
      } else if (basicSalary >= 10250 && basicSalary < 10750) {
          return 472.50f;
      } else if (basicSalary >= 10750 && basicSalary < 11250) {
          return 495.00f;
      } else if (basicSalary >= 11250 && basicSalary < 11750) {
          return 517.50f;
      } else if (basicSalary >= 11750 && basicSalary < 12250) {
          return 540.00f;
      } else if (basicSalary >= 12250 && basicSalary < 12750) {
          return 562.50f;
      } else if (basicSalary >= 12750 && basicSalary < 13250) {
          return 585.00f;
      } else if (basicSalary >= 13250 && basicSalary < 13750) {
          return 607.50f;
      } else if (basicSalary >= 13750 && basicSalary < 14250) {
          return 630.00f;
      } else if (basicSalary >= 14250 && basicSalary < 14750) {
          return 652.50f;
      } else if (basicSalary >= 14750 && basicSalary < 15250) {
          return 675.00f;
      } else if (basicSalary >= 15250 && basicSalary < 15750) {
          return 697.50f;
      } else if (basicSalary >= 15750 && basicSalary < 16250) {
          return 720.00f;
      } else if (basicSalary >= 16250 && basicSalary < 16750) {
          return 742.50f;
      } else if (basicSalary >= 16750 && basicSalary < 17250) {
          return 765.00f;
      } else if (basicSalary >= 17250 && basicSalary < 17750) {
          return 787.50f;
      } else if (basicSalary >= 17750 && basicSalary < 18250) {
          return 810.00f;
      } else if (basicSalary >= 18250 && basicSalary < 18750) {
          return 832.50f;
      } else if (basicSalary >= 18750 && basicSalary < 19250) {
          return 855.00f;
      } else if (basicSalary >= 19250 && basicSalary < 19750) {
          return 877.50f;
      } else if (basicSalary >= 19750 && basicSalary < 20250) {
          return 900.00f;
      } else if (basicSalary >= 20250 && basicSalary < 20750) {
          return 922.50f;
      } else if (basicSalary >= 20750 && basicSalary < 21250) {
          return 945.00f; 
      } else if (basicSalary >= 21250 && basicSalary < 21750) {
          return 967.50f;
      } else if (basicSalary >= 21750 && basicSalary < 22250) {
          return 990.00f;
      } else if (basicSalary >= 22250 && basicSalary < 22750) {
          return 1012.50f;
      } else if (basicSalary >= 22750 && basicSalary < 23250) {
          return 1035.00f;
      } else if (basicSalary >= 23250 && basicSalary < 23750) {
          return 1057.50f;
      } else if (basicSalary >= 23750 && basicSalary < 24750) {
          return 1080.00f;
      } else { 
      return 1125.00f;
      }
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
    
    // Separate methods for total rendered hours and overtime pay for reusability
    public static double calculateTotalRenderedHours(List<AttendanceRecord> attendanceRecords) {
        double totalRegularHours = 0;
        for (AttendanceRecord record : attendanceRecords) {
            double hoursWorked = Duration.between(record.getTimeIn(), record.getTimeOut()).toMinutes()/60.0;
            double finalHoursWorked = hoursWorked - 1.0; // Subtract 1 hour for lunch break
            totalRegularHours += Math.min(finalHoursWorked,8);
        }
        return totalRegularHours;
    }
    
    public static double calculateTotalOvertimeHours(List<AttendanceRecord> attendanceRecords) {
        double totalOvertimeHours = 0;
        
        for (AttendanceRecord record : attendanceRecords) {
            double hoursWorked = Duration.between(record.getTimeIn(), record.getTimeOut()).toMinutes()/60.0;
            double finalHoursWorked = hoursWorked - 1.0; // Subtract 1 hour for lunch break
            
            if(finalHoursWorked > 8) {
                totalOvertimeHours += (finalHoursWorked - 8);
            }
        }
        return totalOvertimeHours;
    }

    public static double calculateTotalTardiness (List<AttendanceRecord> attendanceRecords) {
        LocalTime gracePeriod = LocalTime.of(8, 10); 
        long totalTardyMinutes = 0;
        
        for (AttendanceRecord record : attendanceRecords) {
            
            LocalTime actualTimeIn = record.getTimeIn();
            
            if(actualTimeIn.isAfter(gracePeriod)) {
                Duration tardyDuration = Duration.between(gracePeriod, actualTimeIn);
                totalTardyMinutes += tardyDuration.toMinutes();
             }
            }
        
        return totalTardyMinutes/60.0;
    }
  
     //Calculates the gross salary for an employee based on attendance records        
    public static double calculateGrossSalary(Employee employee, List<AttendanceRecord> attendanceRecords) {
        if (employee == null || attendanceRecords == null) {
            System.err.println("Error: Employee and attendance object is null for gross salary calculation.");
            return 0.0;
        }
        
        double hourlyRate = employee.getHourlyRate();
        double totalRegularHours = calculateTotalRenderedHours(attendanceRecords);
        double overtimeHours = calculateTotalOvertimeHours(attendanceRecords);
        
        double regularPay = totalRegularHours * hourlyRate;
        double overtimePay = overtimeHours * hourlyRate * 1.25;
        
        return round(regularPay + overtimePay);        
        
    }
    
    public static double calculateTotalTardinessDeductions (Employee employee, List<AttendanceRecord> attendanceRecords) {
        if (employee == null || attendanceRecords == null) {
            System.err.println("Error: Employee and attendance object is null for gross salary calculation.");
            return 0.0;
        }
        
        double hourlyRate = employee.getHourlyRate();
        double totalTardinessHours = calculateTotalTardiness(attendanceRecords);
               
        return round(totalTardinessHours * hourlyRate);  
    }
    

    //Calculate PhilHealth Contribution based on Basic Salary
    public static double calculatePhilhealthContribution(double basicSalary) {
        if (basicSalary <= 10000) {
           return 300f/2;
       } else if (basicSalary >= 10000.1f && basicSalary <= 59999.99f) {
           return (basicSalary * 0.03)/2;
       } else {
           return 1800 / 2; //Equally shared by employee and employer
       }
    }

    // Calculates the monthly Pag-IBIG contributio based on Basic Salary
    public static double calculatePagibigContribution(double basicSalary) {
        return Math.min(basicSalary * 0.02, 100);
    }

    //Calculates the total monthly deductions (SSS, PhilHealth, Pag-IBIG, Withholding Tax).
  
    public static double calculateTotalDeductions(double grossSalary, double basicSalary, Employee employee, List<AttendanceRecord> attendanceRecords) {
        double sssDeduction = calculateSSSContribution(basicSalary);
        double philhealthDeduction = calculatePhilhealthContribution(basicSalary);
        double pagibigDeduction = calculatePagibigContribution(basicSalary);
        double tardinessDeduction = calculateTotalTardinessDeductions(employee, attendanceRecords);
        double taxDeduction = calculateWithholdingTax(grossSalary - sssDeduction - philhealthDeduction - pagibigDeduction - tardinessDeduction); // Taxable income after mandatory deductions

        return round(sssDeduction + philhealthDeduction + pagibigDeduction + taxDeduction);
    }

    //Calculates Net Salary based on attendance record.
    public static double calculateNetSalary(Employee employee, List<AttendanceRecord> attendanceRecords) {
        if (employee == null || attendanceRecords == null) {
            System.err.println("Error: Employee object is null for net salary calculation.");
            return 0.0;
        }
        double grossSalary = calculateGrossSalary(employee, attendanceRecords);
        double totalDeductions = calculateTotalDeductions(grossSalary, employee.getSalary(), employee, attendanceRecords);
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
    public static void displayPayslip(Employee employee, List<AttendanceRecord> attendanceRecords) {
        if (employee == null) {
            System.out.println("Employee not found for payslip generation.");
            return;
        }

        double basicSalary = employee.getSalary();
        double hourlyRate = employee.getHourlyRate();
        double totalRenderedHours = calculateTotalRenderedHours(attendanceRecords);
        double overtimeHours = calculateTotalOvertimeHours(attendanceRecords);
        double tardinessHours = calculateTotalTardiness(attendanceRecords);
        double tardinessDeduction = calculateTotalTardinessDeductions(employee, attendanceRecords);

        double grossSalary = calculateGrossSalary(employee, attendanceRecords);

        double sssDeduction = calculateSSSContribution(basicSalary);
        double philhealthDeduction = calculatePhilhealthContribution(basicSalary);
        double pagibigDeduction = calculatePagibigContribution(basicSalary);
        double taxableIncome = grossSalary - sssDeduction - philhealthDeduction - pagibigDeduction - tardinessDeduction;
        double taxDeduction = calculateWithholdingTax(taxableIncome);

        double totalDeductions = round(sssDeduction + philhealthDeduction + pagibigDeduction + taxDeduction + tardinessDeduction);
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
        System.out.printf("  Tardiness Deduction (%.2f hours): P %.2f%n", tardinessHours, tardinessDeduction);
        System.out.printf("Total Deductions: P %.2f%n", totalDeductions);
        System.out.println("----------------------------------------------------------");
        System.out.printf("NET MONTHLY SALARY: P %.2f%n", netSalary);
        System.out.println("----------------------------------------------------------");
    }
}



