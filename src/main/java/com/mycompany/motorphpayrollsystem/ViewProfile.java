package com.mycompany.motorphpayrollsystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.text.MessageFormat; // Import for MessageFormat
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class ViewProfile extends JFrame {

    private User loggedInUser;
    private Employee employee;
    private EmployeeManager employeeManager;

    private JTextArea employeeDetailsArea;
    private JTextArea payslipArea; // New: Text area for payslip
    private JTextField startDateField; // New: Start date input
    private JTextField endDateField;   // New: End date input

    public ViewProfile(User loggedInUser, EmployeeManager employeeManager) {
        this.loggedInUser = loggedInUser;
        this.employeeManager = employeeManager;
        this.employee = employeeManager.getEmployeeById(loggedInUser.getEmployeeId());

        if (this.employee == null) {
            JOptionPane.showMessageDialog(this, "Employee data not found for your user account. Please contact HR.", "Data Error", JOptionPane.ERROR_MESSAGE);
            dispose();
            return;
        }

        setTitle("My Profile - " + employee.getFullName());
        setSize(1400,800); // Increased size to accommodate payslip
        setResizable(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(240, 248, 255));
        
        JLabel titleLabel = new JLabel("My Profile and Payslip", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(0, 51, 102));
        add(titleLabel, BorderLayout.NORTH);

        // Employee Details Panel
        JPanel detailsPanel = new JPanel(new BorderLayout(5, 5));
        detailsPanel.setBorder(BorderFactory.createTitledBorder("Personal & Employment Details"));
        detailsPanel.setBackground(Color.WHITE);

        employeeDetailsArea = new JTextArea(10, 30);
        employeeDetailsArea.setFont(new Font("Arial", Font.PLAIN, 14));
        employeeDetailsArea.setEditable(false);
        employeeDetailsArea.setLineWrap(true);
        employeeDetailsArea.setWrapStyleWord(true);
        JScrollPane detailsScrollPane = new JScrollPane(employeeDetailsArea);
        detailsPanel.add(detailsScrollPane, BorderLayout.CENTER);
        displayEmployeeDetails();

        // Payslip Calculation Panel
        JPanel payslipCalculatorPanel = new JPanel(new BorderLayout(10, 10));
        payslipCalculatorPanel.setBackground(new Color(240, 248, 255));
        payslipCalculatorPanel.setBorder(BorderFactory.createTitledBorder("Payslip Calculation"));

        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        inputPanel.setBackground(new Color(240, 248, 255));

        JLabel startDateLabel = new JLabel("Start Date (MM/dd/yyyy):");
        startDateField = new JTextField(10);
        startDateField.setToolTipText("Enter start date");

        JLabel endDateLabel = new JLabel("End Date (MM/dd/yyyy):");
        endDateField = new JTextField(10);
        endDateField.setToolTipText("Enter end date");

        JButton calculatePayslipBtn = createStyledButton2("Calculate Payslip");
        JButton printPayslipBtn = createStyledButton2("Print Payslip"); // New: Print button

        inputPanel.add(startDateLabel);
        inputPanel.add(startDateField);
        inputPanel.add(endDateLabel);
        inputPanel.add(endDateField);
        inputPanel.add(calculatePayslipBtn);
        inputPanel.add(printPayslipBtn); // Add the print button

        payslipCalculatorPanel.add(inputPanel, BorderLayout.NORTH);

        payslipArea = new JTextArea(20, 40);
        payslipArea.setEditable(false);
        payslipArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        payslipArea.setBorder(BorderFactory.createEtchedBorder());
        JScrollPane payslipScrollPane = new JScrollPane(payslipArea);
        payslipCalculatorPanel.add(payslipScrollPane, BorderLayout.CENTER);

        // Action Listener for Calculate Payslip Button
        calculatePayslipBtn.addActionListener(e -> calculateAndDisplayPayslip());

        // Action Listener for Print Payslip Button
        printPayslipBtn.addActionListener(e -> {
            try {
                // Create a header for the printout
                MessageFormat header = new MessageFormat("Payslip for {0} - Period: {1} to {2}");
                MessageFormat footer = new MessageFormat("- Page {0} -");

                // Get dynamic values for the header
                String fullName = employee.getFullName();
                String startDate = startDateField.getText().trim();
                String endDate = endDateField.getText().trim();

                // Set arguments for the header MessageFormat
                Object[] headerArgs = {fullName, startDate, endDate};
                header.setFormatByArgumentIndex(0, new MessageFormat(fullName));
                header.setFormatByArgumentIndex(1, new MessageFormat(startDate));
                header.setFormatByArgumentIndex(2, new MessageFormat(endDate));


                boolean complete = payslipArea.print(header, footer, true, null, null, true);
                if (complete) {
                    JOptionPane.showMessageDialog(this, "Printing Complete!", "Print Result", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Printing Cancelled or Failed.", "Print Result", JOptionPane.WARNING_MESSAGE);
                }
            } catch (java.awt.print.PrinterException pe) {
                JOptionPane.showMessageDialog(this, "Error during printing: " + pe.getMessage(), "Print Error", JOptionPane.ERROR_MESSAGE);
                Logger.getLogger(ViewProfile.class.getName()).log(Level.SEVERE, "Error printing payslip", pe);
            }
        });


        // Split Pane to combine details and payslip calculator
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, detailsPanel, payslipCalculatorPanel);
        splitPane.setResizeWeight(0.5); // Gives equal space to both panels initially
        add(splitPane, BorderLayout.CENTER);

        // Close button
        JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton closeButton = createStyledButton2("Close");
        closeButton.addActionListener(e -> dispose());
        southPanel.add(closeButton);
        add(southPanel, BorderLayout.SOUTH);
    }

    private void displayEmployeeDetails() {
        StringBuilder details = new StringBuilder();
        details.append(String.format("Employee ID: %d%n", employee.getEmployeeId()));
        details.append(String.format("Name: %s %s%n", employee.getFirstName(), employee.getLastName()));
        details.append(String.format("Birthday: %s%n", employee.getBirthday()));
        details.append(String.format("Position: %s%n", employee.getPosition()));
        details.append(String.format("Hourly Rate: P%.2f%n", employee.getHourlyRate()));
        details.append(String.format("Monthly Salary: P%.2f%n", employee.getSalary()));
        details.append(String.format("SSS No: %s%n", employee.getSssNo()));
        details.append(String.format("PhilHealth No: %s%n", employee.getPhilhealthNo()));
        details.append(String.format("TIN: %s%n", employee.getTin()));
        details.append(String.format("Pag-IBIG No: %s%n", employee.getPagibigNo()));
        employeeDetailsArea.setText(details.toString());
    }

    private void calculateAndDisplayPayslip() {
        String startDateInput = startDateField.getText().trim();
        String endDateInput = endDateField.getText().trim();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

        if (startDateInput.isEmpty() || endDateInput.isEmpty()) {
            payslipArea.setText("Please fill in both start and end dates.");
            return;
        }

        LocalDate start;
        LocalDate end;

        try {
            start = LocalDate.parse(startDateInput, formatter);
        } catch (DateTimeParseException dtpe) {
            payslipArea.setText("Please use MM/dd/yyyy format for Start Date.");
            return;
        }

        try {
            end = LocalDate.parse(endDateInput, formatter);
        } catch (DateTimeParseException dtpe) {
            payslipArea.setText("Please use MM/dd/yyyy format for End Date.");
            return;
        }

        if (end.isBefore(start)) {
            payslipArea.setText("End Date must be after or equal to Start Date.");
            return;
        }

        try {
            var allAttendance = AttendanceManager.getInstance().getAllAttendanceRecords();

            var filteredRecords = allAttendance.stream()
                    .filter(r -> r.getEmployeeId() == employee.getEmployeeId()
                            && !r.getDate().isBefore(start)
                            && !r.getDate().isAfter(end))
                    .collect(Collectors.toList());

            if (filteredRecords.isEmpty()) {
                payslipArea.setText("No attendance records found for the selected period.");
                return;
            }

            double basicSalary = employee.getSalary();
            double hourlyRate = employee.getHourlyRate();
            double totalRegularHours = Motorphpayrollsystem.calculateTotalRenderedHours(filteredRecords);
            double overtimeHours = Motorphpayrollsystem.calculateTotalOvertimeHours(filteredRecords);
            double grossSalary = Motorphpayrollsystem.calculateGrossSalary(employee, filteredRecords);

            double tardinessHours = Motorphpayrollsystem.calculateTotalTardiness(filteredRecords);
            double tardinessDeduction = Motorphpayrollsystem.calculateTotalTardinessDeductions(employee, filteredRecords);
            double sssDeduction = Motorphpayrollsystem.calculateSSSContribution(basicSalary);
            double philhealthDeduction = Motorphpayrollsystem.calculatePhilhealthContribution(basicSalary);
            double pagibigDeduction = Motorphpayrollsystem.calculatePagibigContribution(basicSalary);
            double taxableIncome = grossSalary - sssDeduction - philhealthDeduction - pagibigDeduction - tardinessDeduction;
            double taxDeduction = Motorphpayrollsystem.calculateWithholdingTax(taxableIncome);
            double totalDeductions = Motorphpayrollsystem.round(tardinessDeduction + sssDeduction + philhealthDeduction + pagibigDeduction + taxDeduction);

            double netSalary = Motorphpayrollsystem.round(grossSalary - totalDeductions);
            
            // Display payslip
            StringBuilder payslipText = new StringBuilder();
            payslipText.append(String.format("--- Payslip for %s (ID: %d) ---%n", employee.getFullName(), employee.getEmployeeId()));
            payslipText.append(String.format("Period: %s to %s%n", startDateInput, endDateInput));
            payslipText.append(String.format("Position: %s%n", employee.getPosition()));
            payslipText.append(String.format("Monthly Basic Salary: P %.2f%n", basicSalary));
            payslipText.append(String.format("Hourly Rate: P %.2f%n", hourlyRate));
            payslipText.append(String.format("Total Hours Worked (Regular): %.2f hours%n", totalRegularHours));
            payslipText.append(String.format("Overtime Hours: %.2f hours%n", overtimeHours));
            payslipText.append(String.format("Overtime Pay: P %.2f%n", (overtimeHours * hourlyRate * 1.25)));
            payslipText.append(String.format("Gross Salary: P %.2f%n", grossSalary));
            payslipText.append("----------------------------------------------------------\n");
            payslipText.append("Deductions:\n");
            payslipText.append(String.format("  Tardiness Hours: %.2f hours%n", tardinessHours));
            payslipText.append(String.format("  Tardiness Deduction: P %.2f%n", tardinessDeduction));
            payslipText.append(String.format("  SSS Contribution: P %.2f%n", sssDeduction));
            payslipText.append(String.format("  PhilHealth Contribution: P %.2f%n", philhealthDeduction));
            payslipText.append(String.format("  Pag-IBIG Contribution: P %.2f%n", pagibigDeduction));
            payslipText.append(String.format("  Withholding Tax: P %.2f%n", taxDeduction));
            payslipText.append(String.format("Total Deductions: P %.2f%n", totalDeductions));
            payslipText.append(String.format("NET SALARY: P %.2f%n", netSalary));

            // Display attendance breakdown after the payslip display
            payslipText.append("\n----------------------------------------------------------\n");
            payslipText.append("Attendance Records:\n");
            payslipText.append(String.format("%-12s %-10s %-10s %-10s %-10s %-10s%n", 
                        "Date", "Log In", "Log Out", "Hours", "Mins", "Total Time"));
            payslipText.append("----------------------------------------------------------\n");

            DateTimeFormatter dateFormatterOutput = DateTimeFormatter.ofPattern("MM/dd/yyyy");
            DateTimeFormatter timeFormatterOutput = DateTimeFormatter.ofPattern("h:mm a");

            for (AttendanceRecord record : filteredRecords) {
                long hours = java.time.Duration.between(record.getTimeIn(), record.getTimeOut()).toHours();
                long mins = java.time.Duration.between(record.getTimeIn(), record.getTimeOut()).toMinutesPart();
                String date = record.getDate().format(dateFormatterOutput);
                String in = record.getTimeIn().format(timeFormatterOutput);
                String out = record.getTimeOut().format(timeFormatterOutput);
                payslipText.append(String.format("%-12s %-10s %-10s %-10d %-10d %s%n",
                        date, in, out, hours, mins, String.format("%d hrs %d mins", hours, mins)));
            }
            payslipArea.setText(payslipText.toString());

        } catch (Exception ex) {
            payslipArea.setText("Error calculating payslip: " + ex.getMessage() + "\nPlease ensure all data is valid.");
            Logger.getLogger(ViewProfile.class.getName()).log(Level.SEVERE, "Error calculating payslip", ex);
        }
    }

    private JButton createStyledButton2(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 15));
        button.setBackground(new Color(0, 0, 90));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(Color.WHITE); 
                button.setForeground(new Color(0, 0, 128));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(0, 0, 90)); 
                button.setForeground(Color.WHITE);
            }
        });
        return button;
    }
}