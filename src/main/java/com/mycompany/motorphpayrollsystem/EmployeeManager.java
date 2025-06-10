package com.mycompany.motorphpayrollsystem;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class EmployeeManager {
    private static EmployeeManager instance; // Singleton instance
    private List<Employee> employees;
    private static final String csvFile = "employees.csv"; // CSV file name

    // Private constructor for Singleton pattern
    private EmployeeManager() {
        employees = new ArrayList<>();
        loadEmployeesFromFile(); // Load employees 
    }

    // Public method to get the Singleton instance
    public static synchronized EmployeeManager getInstance() {
        if (instance == null) {
            instance = new EmployeeManager();
        }
        return instance;
    }

    public List<Employee> getEmployees() {
        return Collections.unmodifiableList(employees); // Return unmodifiable list
    }

    public Employee getEmployeeById(int employeeId) {
        for (Employee employee : employees) {
            if (employee.getEmployeeId() == employeeId) {
                return employee;
            }
        }
        return null;
    }

    // --- CSV File Operations using OpenCSV ---

    private void loadEmployeesFromFile() {
        employees.clear(); // Clear existing list before loading
        try (CSVReader reader = new CSVReader(new FileReader(csvFile))) {
            List<String[]> allRows = reader.readAll();
            if (allRows.isEmpty()) {
                System.out.println("CSV file is empty or headers are missing. No employees loaded.");
                return;
            }

            // Skip header row 
            List<String[]> dataRows;
            if (allRows.get(0)[0].equals("Employee ID")) { 
                dataRows = allRows.subList(1, allRows.size());
            } else {
                dataRows = allRows; // No header, all rows are data
            }

            for (String[] row : dataRows) {
                Employee employee = Employee.fromCsvArray(row);
                if (employee != null) {
                    employees.add(employee);
                }
                reader.close();
            }
            System.out.println("Employees loaded from " + csvFile);
        } catch (IOException e) {
            System.err.println("Could not read " + csvFile + ". Creating new file on save. Error: " + e.getMessage());
            
        } catch (CsvException e) {
            System.err.println("Error parsing CSV file: " + e.getMessage());
        }
    }

    private void saveEmployeesToFile() throws IOException {
        
        String tempFile = "employees.tmp";
        
        try (CSVWriter writer = new CSVWriter(new FileWriter(tempFile))) {
            // Write header
            String[] header = {"Employee ID", "First Name", "Last Name", "Birthday", "Position",
                               "Hourly Rate", "Monthly Salary", "SSS No", "PhilHealth No", "TIN", "Pag-IBIG No"};
            writer.writeNext(header);

            // Write employee data
            for (Employee employee : employees) {
                String [] employeeData = (employee.toCsvArray());
                
                for (int i = 0; i < employeeData.length; i++) {
                employeeData[i] = employeeData[i].replace(",", ";"); // Replaces commas with semicolons
            }
             writer.writeNext(employeeData);   
            }        
            
          }            
            
          File originalFile = new File(csvFile);
          File temp = new File(tempFile);
            
          if (!originalFile.delete()) {
          System.err.println("Failed to delete original CSV file.");
          }

          if (!temp.renameTo(originalFile)) {
              System.err.println("Failed to rename temp file to original CSV file.");
          } else {
              System.out.println("Employees saved to " + csvFile);
            
        }
    }



    // --- Employee Management Methods ---

    public boolean addEmployee(int employeeId, String firstName, String lastName, String birthday, String position,
                               double hourlyRate, double salary, String sssNo, String philhealthNo, String tin, String pagibigNo) throws IOException {
        if (getEmployeeById(employeeId) != null) {
            System.out.println("Error: Employee with ID " + employeeId + " already exists.");
            return false;
        }
        Employee newEmployee = new Employee(employeeId, firstName, lastName, birthday, position,
                                            hourlyRate, salary, sssNo, philhealthNo, tin, pagibigNo);
        employees.add(newEmployee);
        saveEmployeesToFile(); // Save changes after adding
        System.out.println("Employee " + newEmployee.getFullName() + " added successfully.");
        return true;
    }

    public boolean editEmployee(int employeeId, String newFirstName, String newLastName, String newBirthday,
                                String newPosition, double newHourlyRate, double newSalary,
                                String newSssNo, String newPhilhealthNo, String newTin, String newPagibigNo) throws IOException {
        Employee employee = getEmployeeById(employeeId);
        if (employee != null) {
            if (!newFirstName.isEmpty()) employee.setFirstName(newFirstName);
            if (!newLastName.isEmpty()) employee.setLastName(newLastName);
            if (!newBirthday.isEmpty()) employee.setBirthday(newBirthday);
            if (!newPosition.isEmpty()) employee.setPosition(newPosition);
            if (newHourlyRate >= 0) employee.setHourlyRate(newHourlyRate); // Use >=0 to allow 0 or specific valid rates
            if (newSalary >= 0) employee.setSalary(newSalary); // Use >=0 to allow 0 or specific valid salaries
            if (!newSssNo.isEmpty()) employee.setSssNo(newSssNo);
            if (!newPhilhealthNo.isEmpty()) employee.setPhilhealthNo(newPhilhealthNo);
            if (!newTin.isEmpty()) employee.setTin(newTin);
            if (!newPagibigNo.isEmpty()) employee.setPagibigNo(newPagibigNo);

            saveEmployeesToFile(); // Save changes after editing
            System.out.println("Employee " + employeeId + " details updated.");
            return true;
        } else {
            System.out.println("Employee " + employeeId + " not found.");
            return false;
        }
    }

    public boolean deleteEmployee(int employeeId) throws IOException {
        boolean removed = employees.removeIf(e -> e.getEmployeeId() == employeeId);
        if (removed) {
            saveEmployeesToFile(); // Save changes after deleting
            System.out.println("Employee " + employeeId + " deleted successfully.");
        } else {
            System.out.println("Employee " + employeeId + " not found.");
        }
        return removed;
    }

    public void viewEmployees() {
        if (employees.isEmpty()) {
            System.out.println("No employees found.");
            return;
        }
        System.out.println("\n--- All Employee Records ---");
        System.out.printf("%-10s %-20s %-15s %-15s %-15s %-12s %-12s%n",
                          "ID", "Name", "Position", "SSS No", "PhilHealth", "TIN", "Pag-IBIG");
        System.out.println("--------------------------------------------------------------------------------------------------");
        for (Employee employee : employees) {
            System.out.printf("%-10d %-20s %-15s %-15s %-15s %-12s %-12s%n",
                              employee.getEmployeeId(),
                              employee.getFullName(),
                              employee.getPosition(),
                              employee.getSssNo(),
                              employee.getPhilhealthNo(),
                              employee.getTin(),
                              employee.getPagibigNo());
        }
        System.out.println("--------------------------------------------------------------------------------------------------");
    }
}


