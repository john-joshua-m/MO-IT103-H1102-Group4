package com.mycompany.motorphpayrollsystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class PayrollSystemGUI extends JFrame {
    private Motorphpayrollsystem payrollSystem;
    private EmployeeManager manageEmployee;

    JButton buttonCalculate;
    JButton buttonRenderHours;
    JButton buttonOvertime;
    JButton buttonAddEmployee;
    JButton buttonView;
    JButton buttonEdit;
    JButton buttonDelete;

    JPanel menuPanel; // Declare menuPanel at the class level

    public PayrollSystemGUI() {
        // Initialize backend systems
        this.payrollSystem = new Motorphpayrollsystem();
        this.manageEmployee = new EmployeeManager();

        // Frame setup
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setTitle("MotorPH Payroll System");
        setSize(600, 400);
        setLayout(null); // Using null layout
        setLocationRelativeTo(null);

        // Set icon image
        ImageIcon image = new ImageIcon("samplelogo.jpg");
        setIconImage(image.getImage());

        // Background color
        getContentPane().setBackground(new Color(128, 128, 128));

        // Header
        JLabel label = new JLabel("MotorPH Payroll System", SwingConstants.CENTER);
        label.setForeground(Color.GRAY);
        label.setBounds(0, 0, 600, 20);

        JPanel whitePanel = new JPanel();
        whitePanel.setLayout(null);
        whitePanel.setBackground(Color.WHITE);
        whitePanel.setBounds(0, 0, 600, 20);
        whitePanel.add(label);
        add(whitePanel);

     // Menu panel with GridLayout for buttons
        menuPanel = new JPanel(); // Initialize menuPanel here
        menuPanel.setLayout(new GridLayout(7, 1, 5, 5)); // 7 rows, 1 column, 5px gaps
        menuPanel.setBackground(Color.WHITE);
        menuPanel.setSize(240, 300); // Set the initial size

        // Initialize buttons
        buttonCalculate = new JButton("Calculate Net Salary");
        buttonRenderHours = new JButton("Set Rendered Hours");
        buttonOvertime = new JButton("Add Overtime Hours");
        buttonAddEmployee = new JButton("Add Employee");
        buttonView = new JButton("View Employees");
        buttonEdit = new JButton("Edit Employee Details");
        buttonDelete = new JButton("Delete Employee");

        // Add buttons to menu panel
        menuPanel.add(buttonCalculate);
        menuPanel.add(buttonRenderHours);
        menuPanel.add(buttonOvertime);
        menuPanel.add(buttonAddEmployee);
        menuPanel.add(buttonView);
        menuPanel.add(buttonEdit);
        menuPanel.add(buttonDelete);

        add(menuPanel);

        // Button styles & listeners
        JButton[] buttons = {buttonCalculate, buttonRenderHours, buttonOvertime, buttonAddEmployee, buttonView, buttonEdit, buttonDelete};
        for (JButton btn : buttons) {
            btn.setFocusable(false);
            btn.setForeground(Color.BLACK);
            btn.setHorizontalTextPosition(SwingConstants.CENTER);
        }

        buttonCalculate.addActionListener(e -> {
            try {
                String input = JOptionPane.showInputDialog("Enter Employee ID:");
                if (input == null || input.trim().isEmpty()) return;
                int id = Integer.parseInt(input);
                String result = payrollSystem.calculateNetSalary(id);
                JOptionPane.showMessageDialog(null, result);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Invalid ID entered.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        buttonRenderHours.addActionListener(e -> {
            try {
                String inputId = JOptionPane.showInputDialog("Enter Employee ID:");
                if (inputId == null || inputId.trim().isEmpty()) return;
                int id = Integer.parseInt(inputId);
                String inputHours = JOptionPane.showInputDialog("Enter Rendered Hours:");
                if (inputHours == null || inputHours.trim().isEmpty()) return;
                double hours = Double.parseDouble(inputHours);
                payrollSystem.calculateRenderedHours(id, hours);
                JOptionPane.showMessageDialog(null, "Rendered hours updated.");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Invalid input: " + ex.getMessage());
            }
        });

        buttonOvertime.addActionListener(e -> {
            try {
                String inputId = JOptionPane.showInputDialog("Enter Employee ID:");
                if (inputId == null || inputId.trim().isEmpty()) return;
                int id = Integer.parseInt(inputId);
                String inputOvertime = JOptionPane.showInputDialog("Enter Overtime Hours:");
                if (inputOvertime == null || inputOvertime.trim().isEmpty()) return;
                double overtime = Double.parseDouble(inputOvertime);
                payrollSystem.addOvertimeHours(id, overtime);
                JOptionPane.showMessageDialog(null, "Overtime hours added.");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Invalid input: " + ex.getMessage());
            }
        });

        buttonAddEmployee.addActionListener(e -> {
            JTextField idField = new JTextField(5);
            JTextField firstNameField = new JTextField(15);
            JTextField lastNameField = new JTextField(15);
            JTextField birthdayField = new JTextField(10);
            JTextField positionField = new JTextField(15);
            JTextField hourlyRateField = new JTextField(10);
            JTextField salaryField = new JTextField(10);

            JPanel panel = new JPanel(new GridLayout(0, 1));
            panel.add(new JLabel("Employee ID:"));
            panel.add(idField);
            panel.add(new JLabel("First Name:"));
            panel.add(firstNameField);
            panel.add(new JLabel("Last Name:"));
            panel.add(lastNameField);
            panel.add(new JLabel("Birthday (MM/DD/YYYY):"));
            panel.add(birthdayField);
            panel.add(new JLabel("Position:"));
            panel.add(positionField);
            panel.add(new JLabel("Hourly Rate:"));
            panel.add(hourlyRateField);
            panel.add(new JLabel("Salary:"));
            panel.add(salaryField);

            int result = JOptionPane.showConfirmDialog(null, panel,
                    "Enter New Employee Details", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                try {
                    int id = Integer.parseInt(idField.getText());
                    String firstName = firstNameField.getText();
                    String lastName = lastNameField.getText();
                    String birthday = birthdayField.getText();
                    String position = positionField.getText();
                    double hourlyRate = Double.parseDouble(hourlyRateField.getText());
                    double salary = Double.parseDouble(salaryField.getText());
                    manageEmployee.addEmployee(id, firstName, lastName, birthday, position, hourlyRate, salary);
                    JOptionPane.showMessageDialog(null, "Employee added successfully.");
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Invalid input.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        buttonView.addActionListener(e -> {
            List<Employee> employees = manageEmployee.getEmployees();
            if (employees.isEmpty()) {
                JOptionPane.showMessageDialog(null, "No employees to view.");
                return;
            }
            StringBuilder employeeList = new StringBuilder("List of Employees:\n");
            for (Employee emp : employees) {
                employeeList.append(emp.getFullName()).append(" (ID: ").append(emp.getEmployeeId()).append(")\n");
            }
            JOptionPane.showMessageDialog(null, employeeList.toString(), "Employee List", JOptionPane.INFORMATION_MESSAGE);
        });

        buttonEdit.addActionListener(e -> {
            String inputId = JOptionPane.showInputDialog("Enter Employee ID to edit:");
            if (inputId == null || inputId.trim().isEmpty()) return;
            try {
                int id = Integer.parseInt(inputId);
                Employee employeeToEdit = manageEmployee.getEmployeeById(id);
                if (employeeToEdit == null) {
                    JOptionPane.showMessageDialog(null, "Employee not found.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                JTextField firstNameField = new JTextField(employeeToEdit.getName());
                JTextField lastNameField = new JTextField(employeeToEdit.getLastName());
                JTextField birthdayField = new JTextField(employeeToEdit.getBirthday());
                JTextField positionField = new JTextField(employeeToEdit.getPosition());
                JTextField hourlyRateField = new JTextField(String.valueOf(employeeToEdit.getHourlyRate()));
                JTextField salaryField = new JTextField(String.valueOf(employeeToEdit.getSalary()));

                JPanel panel = new JPanel(new GridLayout(0, 1));
                panel.add(new JLabel("First Name:"));
                panel.add(firstNameField);
                panel.add(new JLabel("Last Name:"));
                panel.add(lastNameField);
                panel.add(new JLabel("Birthday (MM/DD/YYYY):"));
                panel.add(birthdayField);
                panel.add(new JLabel("Position:"));
                panel.add(positionField);
                panel.add(new JLabel("Hourly Rate:"));
                panel.add(hourlyRateField);
                panel.add(new JLabel("Salary:"));
                panel.add(salaryField);

                int result = JOptionPane.showConfirmDialog(null, panel,
                        "Edit Employee Details", JOptionPane.OK_CANCEL_OPTION);
                if (result == JOptionPane.OK_OPTION) {
                    try {
                        String newFirstName = firstNameField.getText();
                        String newLastName = lastNameField.getText();
                        String newBirthday = birthdayField.getText();
                        String newPosition = positionField.getText();
                        double newHourlyRate = Double.parseDouble(hourlyRateField.getText());
                        double newSalary = Double.parseDouble(salaryField.getText());
                        manageEmployee.editEmployee(id, newFirstName, newLastName, newBirthday, newPosition, newHourlyRate, newSalary);
                        JOptionPane.showMessageDialog(null, "Employee details updated.");
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(null, "Invalid input.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Invalid ID entered.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        buttonDelete.addActionListener(e -> {
            String inputId = JOptionPane.showInputDialog("Enter Employee ID to delete:");
            if (inputId == null || inputId.trim().isEmpty()) return;
            try {
                int id = Integer.parseInt(inputId);
                int confirmation = JOptionPane.showConfirmDialog(null,
                        "Are you sure you want to delete employee ID " + id + "?",
                        "Confirm Deletion", JOptionPane.YES_NO_OPTION);
                if (confirmation == JOptionPane.YES_OPTION) {
                    manageEmployee.deleteEmployee(id);
                    JOptionPane.showMessageDialog(null, "Employee deleted successfully.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Invalid ID entered.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Add a ComponentListener to adjust the menuPanel's position when the frame is shown
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                int frameWidth = getWidth();
                int panelWidth = menuPanel.getWidth();
                int centerX = (frameWidth - panelWidth) / 2;
                menuPanel.setBounds(centerX, 40, 240, 300); // Adjusted y to 40
            }

            @Override
            public void componentResized(ComponentEvent e) {
                int frameWidth = getWidth();
                int panelWidth = menuPanel.getWidth();
                int centerX = (frameWidth - panelWidth) / 2;
                menuPanel.setBounds(centerX, 40, 240, 300); // Adjusted y to 40
            }
        });

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            PayrollSystemGUI gui = new PayrollSystemGUI();
        });
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

