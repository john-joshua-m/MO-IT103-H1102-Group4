package com.mycompany.motorphpayrollsystem;

import java.io.*;
import java.util.*;

public class EmployeeManager { // public class
    private static final String FILE_NAME = "employees.txt";
    private List<Employee> employees;

    public EmployeeManager() {
        if (this.employees == null) {
            this.employees = new ArrayList<>();
        }
        loadEmployeesFromFile();
    }

    private void loadEmployeesFromFile() {
        employees.clear();
        File file = new File(FILE_NAME);
        if (!file.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                Employee emp = Employee.fromFileString(line);
                if (emp != null) {
                    employees.add(emp);
                }
            }
        } catch (IOException e) {
            System.out.println("\nError loading employees: " + e.getMessage());
        }
    }

    private void saveEmployeesToFile() {
        if (employees == null) {
            System.out.println("\nError: Employees list is null! Creating a new list.");
            employees = new ArrayList<>();
            return;
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Employee emp : employees) {
                if (emp != null) {
                    bw.write(emp.toFileString());
                    bw.newLine();
                }
            }
        } catch (IOException e) {
            System.out.println("\nError saving employees: " + e.getMessage());
        }
    }

    public void addEmployee(int employeeId, String firstName, String lastName, String birthday, String position, double hourlyRate, double salary) {
        employees.add(new Employee(employeeId, firstName, lastName, birthday, position, hourlyRate, salary));
        saveEmployeesToFile();
        System.out.println("\nEmployee added successfully.");
    }

    public void viewEmployees() {
        if (employees.isEmpty()) {
            System.out.println("\nNo employees found.");
            return;
        }
        for (Employee emp : employees) {
            emp.displayInfo();
        }
    }

    public void editEmployee(int employeeId, String newfirstName, String newlastName, String newBirthday, String newPosition, double newhourlyRate, double newSalary) {
        for (Employee emp : employees) {
            if (emp.getEmployeeId() == employeeId) {
                if (!newfirstName.isEmpty()) emp.setFirstName(newfirstName);
                if (!newlastName.isEmpty()) emp.setLastName(newlastName);
                if (!newBirthday.isEmpty()) emp.setBirthday(newBirthday);
                if (!newPosition.isEmpty()) emp.setPosition(newPosition);
                if (newhourlyRate >= 0) emp.setHourlyRate(newhourlyRate);
                if (newSalary >= 0) emp.setSalary(newSalary);

                saveEmployeesToFile();
                System.out.println("\nEmployee updated successfully.");
                return;
            }
        }
        System.out.println("\nEmployee not found.");
    }

    public void deleteEmployee(int employeeId) {
        Iterator<Employee> iterator = employees.iterator();
        while (iterator.hasNext()) {
            Employee emp = iterator.next();
            if (emp.getEmployeeId() == employeeId) {
                iterator.remove();
                saveEmployeesToFile();
                System.out.println("\nEmployee deleted successfully.");
                return;
            }
        }
        System.out.println("\nEmployee not found.");
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public Employee getEmployeeById(int id) {
        for (Employee employee : employees) {
            if (employee.getEmployeeId() == id) {
                return employee;
            }
        }
        return null; // Employee not found
    }
}