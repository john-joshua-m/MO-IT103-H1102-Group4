package com.mycompany.motorphpayrollsystem;


import java.util.List; 
import java.util.Scanner; // Needed for console inputs and outputs
import java.math.BigDecimal;  // Needed for accurate rounding
import java.math.RoundingMode;  // Needed for round method used in returning values

public class Motorphpayrollsystem {
    private List<Employee> employees;
    private EmployeeManager employeeManager;

public Motorphpayrollsystem() {
    this.employeeManager = new EmployeeManager();
    this.employees = employeeManager.getEmployees();
}
    
    
    

    public Employee getEmployeeById(int employeeId) {
        for (Employee employee : employees) {
            if (employee.getEmployeeId()== employeeId) {
                return employee;
            }
        }
        return null; // Return null if employee is not found
    
    }
    
    public static double round(double value) {
        BigDecimal bd = new BigDecimal(Double.toString(value));
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public void calculateRenderedHours(int employeeId, double renderedHours) {
        Employee employee = getEmployeeById(employeeId);
        if (employee != null) {
            employee.setRenderedHours(renderedHours);
        }
    }

    public double calculateGrossSalary(int employeeId) {
    Employee employee = getEmployeeById(employeeId);
    if (employee != null) {
        double renderedHours = employee.getTotalHoursWorked(); // Get total accumulated hours
        double hourlyRate = employee.getHourlyRate();
        double overtimeHours = employee.getOvertimeHours();
        
        double regularPay = renderedHours * hourlyRate;  
        double overtimePay = overtimeHours * hourlyRate * 1.5;  
        return round(regularPay + overtimePay);
    }
    return 0;
}


    public void addOvertimeHours(int employeeId, double hours) {
        Employee employee = getEmployeeById(employeeId);
        if (employee != null) {
            employee.addOvertimeHours(hours);
        }
    }

 /* Our SSS calculator uses an else if loop, it iterates through each range advised in 
    MotorPH's SSS guide sheet and then it returns the exact amount advised in the file for
    the employee's deduction. 
    
    */
    
    
    
    public double calculateSssDeduction(double grossSalary) {
    double sssDeduction = 0.0; // Initialize sssDeduction

    if (grossSalary < 3250) {
        sssDeduction = 135.00;
    } else if (grossSalary >= 3250 && grossSalary < 3750) {
        sssDeduction = 157.50;
    } else if (grossSalary >= 3750 && grossSalary < 4250) {
        sssDeduction = 180.00;
    } else if (grossSalary >= 4250 && grossSalary < 4750) {
        sssDeduction = 202.50;
    } else if (grossSalary >= 4750 && grossSalary < 5250) {
        sssDeduction = 225.50;
    } else if (grossSalary >= 5250 && grossSalary < 5750) {
        sssDeduction = 247.50;
    } else if (grossSalary >= 5750 && grossSalary < 6250) {
        sssDeduction = 270.00;
    } else if (grossSalary >= 6250 && grossSalary < 6750) {
        sssDeduction = 292.50;
    } else if (grossSalary >= 6750 && grossSalary < 7250) {
        sssDeduction = 315.00;
    } else if (grossSalary >= 7250 && grossSalary < 7750) {
        sssDeduction = 337.50;
    } else if (grossSalary >= 7750 && grossSalary < 8250) {
        sssDeduction = 360.00;
    } else if (grossSalary >= 8250 && grossSalary < 8750) {
        sssDeduction = 382.50;
    } else if (grossSalary >= 8750 && grossSalary < 9250) {
        sssDeduction = 405.00;
    } else if (grossSalary >= 9250 && grossSalary < 9750) {
        sssDeduction = 427.50;
    } else if (grossSalary >= 9750 && grossSalary < 10250) {
        sssDeduction = 450.00;
    } else if (grossSalary >= 10250 && grossSalary < 10750) {
        sssDeduction = 472.50;
    } else if (grossSalary >= 10750 && grossSalary < 11250) {
        sssDeduction = 495.00;
    } else if (grossSalary >= 11250 && grossSalary < 11750) {
        sssDeduction = 517.50;
    } else if (grossSalary >= 11750 && grossSalary < 12250) {
        sssDeduction = 540.00;
    } else if (grossSalary >= 12250 && grossSalary < 12750) {
        sssDeduction = 562.50;
    } else if (grossSalary >= 12750 && grossSalary < 13250) {
        sssDeduction = 585.00;
    } else if (grossSalary >= 13250 && grossSalary < 13750) {
        sssDeduction = 607.50;
    } else if (grossSalary >= 13750 && grossSalary < 14250) {
        sssDeduction = 630.00;
    } else if (grossSalary >= 14250 && grossSalary < 14750) {
        sssDeduction = 652.50;
    } else if (grossSalary >= 14750 && grossSalary < 15250) {
        sssDeduction = 675.00;
    } else if (grossSalary >= 15250 && grossSalary < 15750) {
        sssDeduction = 697.50;
    } else if (grossSalary >= 15750 && grossSalary < 16250) {
        sssDeduction = 720.00;
    } else if (grossSalary >= 16250 && grossSalary < 16750) {
        sssDeduction = 742.50;
    } else if (grossSalary >= 16750 && grossSalary < 17250) {
        sssDeduction = 765.00;
    } else if (grossSalary >= 17250 && grossSalary < 17750) {
        sssDeduction = 787.50;
    } else if (grossSalary >= 17750 && grossSalary < 18250) {
        sssDeduction = 810.00;
    } else if (grossSalary >= 18250 && grossSalary < 18750) {
        sssDeduction = 832.50;
    } else if (grossSalary >= 18750 && grossSalary < 19250) {
        sssDeduction = 855.00;
    } else if (grossSalary >= 19250 && grossSalary < 19750) {
        sssDeduction = 877.50;
    } else if (grossSalary >= 19750 && grossSalary < 20250) {
        sssDeduction = 900.00;
    } else if (grossSalary >= 20250 && grossSalary < 20750) {
        sssDeduction = 922.50;
    } else if (grossSalary >= 20750 && grossSalary < 21250) {
        sssDeduction = 945.00;
    } else if (grossSalary >= 21250 && grossSalary < 21750) {
        sssDeduction = 967.50;
    } else if (grossSalary >= 21750 && grossSalary < 22250) {
        sssDeduction = 990.00;
    } else if (grossSalary >= 22250 && grossSalary < 22750) {
        sssDeduction = 1012.50;
    } else if (grossSalary >= 22750 && grossSalary < 23250) {
        sssDeduction = 1035.00;
    } else if (grossSalary >= 23250 && grossSalary < 23750) {
        sssDeduction = 1057.50;
    } else if (grossSalary >= 23750 && grossSalary < 24250) {
        sssDeduction = 1080.00;
    } else if (grossSalary >= 24250 && grossSalary < 24750) {
        sssDeduction = 1102.50;
    } else {
        sssDeduction = 1125.00;
    }

    return round(sssDeduction); // Return the rounded SSS deduction
}
    
    
    /* Here, we created the Pagibig deduction calculator. We use constants below (with all caps naming convention)
    these contants' values are from Pagibig and we use them with our if-else statement in calculating how 
    much should be deduction from employees' salaries.    
    */

public double calculatePagibigDeduction(double grossSalary) {
    double LOW_SALARY_THRESHOLD = 1500.0;
    double LOW_SALARY_RATE = 0.01; 
    double HIGH_SALARY_RATE = 0.02; 
    double MAXIMUM_SALARY = 5000.0;

    double msc = Math.min(grossSalary, MAXIMUM_SALARY); // Salary cap at 5000

    if (msc <= LOW_SALARY_THRESHOLD) {
        return msc * LOW_SALARY_RATE;
    } else {
        return msc * HIGH_SALARY_RATE;
    }
}

/*
Here, we have the Philhealth calculator, we declared minSalary and maxSalary which contains both minimum and maximum salart
for deductions. The contributionRate then contains the percentage of the deduction which is 3% (0.03 if converted). 
*/

  public double calculatePhilhealthDeduction(double grossSalary) {
    double minSalary = 10000.0;
    double maxSalary = 100000.0;
    double contributionRate = 0.03; 


    double monthlySalary = Math.max(minSalary, Math.min(grossSalary, maxSalary));

  
    double totalContribution = monthlySalary * contributionRate;


    return totalContribution / 2;
}
  
/*
  Here is our tax deduction calculator. It uses an if-else statement to iterate through the different income brackets
  advised by BIR. These brackets then determine the percentage of deductions.
  */

public double calculateWithholdingTax(double monthlyTaxableIncome) {
    double tax = 0.0;

   if (monthlyTaxableIncome <= 20832) {
    tax = 0;
} else if (monthlyTaxableIncome > 20832 && monthlyTaxableIncome < 33333) {
    tax = (monthlyTaxableIncome - 20833) * 0.20;
} else if (monthlyTaxableIncome >= 33333 && monthlyTaxableIncome < 66667) {
    tax = ((monthlyTaxableIncome - 33333) * 0.25) + 2500;
} else if (monthlyTaxableIncome >= 66667 && monthlyTaxableIncome < 166667) {
    tax = ((monthlyTaxableIncome - 66667) * 0.30) + 10833;
} else {
    tax = ((monthlyTaxableIncome - 166667) * 0.32) + 40833.33;
}

    return tax;
}

 /*
 This is our net salary calculator, when used, it prints all the other information
 from the payslip. 
 */

public String calculateNetSalary(int employeeId) {
    Employee employee = getEmployeeById(employeeId);

    if (employee == null) {
        return "Employee not found.";
    }

    double weeklyGrossSalary = round(calculateGrossSalary(employeeId));
    double sssDeduction = round(calculateSssDeduction(weeklyGrossSalary));
    double pagibigDeduction = round(calculatePagibigDeduction(weeklyGrossSalary));

    // Corrected PhilHealth Calculation
    double monthlyGrossSalary = round(weeklyGrossSalary * 4);
    double philhealthMonthlyContribution = Math.max(10000, Math.min(100000, monthlyGrossSalary)) * 0.03;
    double weeklyPhilhealthDeduction = philhealthMonthlyContribution / 2 / 4;

    double weeklyTaxableIncome = weeklyGrossSalary - (sssDeduction + pagibigDeduction + weeklyPhilhealthDeduction);
    double monthlyTaxableIncome = weeklyTaxableIncome * 4;
    double monthlyWithholdingTax = calculateWithholdingTax(monthlyTaxableIncome);
    double weeklyWithholdingTax = round(monthlyWithholdingTax / 4);

    double weeklyNetSalary = round(weeklyGrossSalary - (sssDeduction + pagibigDeduction + weeklyPhilhealthDeduction + weeklyWithholdingTax));

    weeklyNetSalary = Math.max(weeklyNetSalary, 0);

    return "Employee: " + employee.getFullName() + "\n" +
            "Weekly Gross Salary: " + weeklyGrossSalary + "\n" +
            "SSS Deduction: " + sssDeduction + "\n" +
            "Pag-ibig Fund Deduction: " + pagibigDeduction + "\n" +
            "PhilHealth Deduction: " + weeklyPhilhealthDeduction + "\n" +
            "BIR Withholding Tax Amount: " + weeklyWithholdingTax + "\n" +
            "Weekly Net Salary: " + weeklyNetSalary;
}

/*
This is our main entry point for the system. This uses the Scanner class, it helps with
displaying text fields to the console and with scanning inputs from users.
*/

    public static void main(String[] args) {
        EmployeeManager employeeManager = new EmployeeManager();
        Motorphpayrollsystem payrollSystem = new Motorphpayrollsystem();
        new PayrollSystemGUI();
    
        try (Scanner scanner = new Scanner(System.in)) {
            int choice;
            int id;
            String firstName;
            String lastName;
            String birthday;
            String position;
            double hourlyRate;
            double salary;
            
            do {
                System.out.println("***************************************************************************");
                System.out.println("\nPayroll System Menu:");
                System.out.println("1. Calculate Net Salary");
                System.out.println("2. Set Rendered Hours");
                System.out.println("3. Add Overtime Hours");
                System.out.println("4. Add New Employee");
                System.out.println("5. View Employees");
                System.out.println("6. Edit Employee Details");
                System.out.println("7. Delete Employee");
                System.out.println("8. Exit");
                System.out.print("Please press the number of your choice: ");
                choice = scanner.nextInt();
                 switch (choice) {
                    case 1:
                        System.out.println("***************************************************************************");
                        System.out.print("\nEmployee ID: ");
                        id = scanner.nextInt();
                        scanner.nextLine();
                        String deductions = payrollSystem.calculateNetSalary(id);
                        System.out.println(deductions);
                        break;
                        
                        
                    case 2:
                        System.out.println("***************************************************************************");
                        System.out.print("\nEnter Employee ID: ");
                        id = scanner.nextInt();
                        System.out.print("Enter Rendered Hours: ");
                        double hours = scanner.nextDouble();
                        payrollSystem.calculateRenderedHours(id, hours);
                        System.out.println("Rendered hours updated.");
                        break;
                        
                    case 3:
                        System.out.println("***************************************************************************");
                        System.out.print("\nEnter Employee ID: ");
                        id = scanner.nextInt();
                        System.out.print("Enter Overtime Hours: ");
                        hours = scanner.nextDouble();
                        payrollSystem.addOvertimeHours(id, hours);
                        System.out.println("Overtime hours updated.");
                        break;
                        
                    case 4:
                        System.out.println("***************************************************************************");
                        System.out.print("\nEnter Employee ID: ");
                        id = scanner.nextInt();
                        scanner.nextLine();
                        System.out.print("Enter First Name: ");
                        firstName = scanner.nextLine();
                        System.out.print("Enter Last Name: ");
                        lastName = scanner.nextLine();
                        System.out.print("Birthday (MM/DD/YYYY): ");
                        birthday = scanner.nextLine();
                        System.out.print("Enter Position: ");
                        position = scanner.nextLine();
                        System.out.print("Enter Hourly Rate: ");
                        hourlyRate = scanner.nextDouble();
                        System.out.print("Enter Salary: ");
                        salary = scanner.nextDouble();
                        employeeManager.addEmployee(id, firstName, lastName, birthday, position, hourlyRate, salary);
                        break;
                        
                        
                    case 5:
                        System.out.println("***************************************************************************");
                        employeeManager.viewEmployees();
                        break;
                        
                    case 6:
                        System.out.println("***************************************************************************");
                         System.out.print("Enter Employee ID to edit: ");
                    int editId = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Enter new first name (leave blank to keep current): ");
                    String newFirstName = scanner.nextLine();
                    System.out.print("Enter new last name (leave blank to keep current): ");
                    String newLastName = scanner.nextLine();
                    System.out.print("Enter new birthday (leave blank to keep current): ");
                    String newBirthday = scanner.nextLine();
                    System.out.print("Enter new position (leave blank to keep current): ");
                    String newPosition = scanner.nextLine();
                    System.out.print("Enter new hourly rate (-1 to keep current): ");
                    System.out.print("Enter new salary (-1 to keep current): ");
                    double newHourlyRate = scanner.nextDouble();
                    double newSalary = scanner.nextDouble();

                    employeeManager.editEmployee(editId, newFirstName, newLastName, newBirthday, newPosition, newHourlyRate, newSalary);
                    break;
                    
                    case 7:
                        System.out.println("***************************************************************************");
                        System.out.print("Enter Employee ID to delete: ");
                    int deleteId = scanner.nextInt();
                    employeeManager.deleteEmployee(deleteId);
                    break;
                        
                    case 8:
                        System.out.println("***************************************************************************");
                    System.out.println("Exiting...");
                    break;
                    default:
                        System.out.println("***************************************************************************");
                        System.out.println("Invalid choice.");
                }
            } while (choice != 8);
        }
    }
    
}