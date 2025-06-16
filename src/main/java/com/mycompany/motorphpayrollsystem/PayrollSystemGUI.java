package com.mycompany.motorphpayrollsystem;
import com.mycompany.motorphpayrollsystem.AttendanceRecord;
import com.mycompany.motorphpayrollsystem.AttendanceManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File; // For checking image 
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;


public class PayrollSystemGUI extends JFrame {

    private EmployeeManager employeeManager;
    private DefaultTableModel tableModel;
    private JTable employeeTable;
    private JPanel mainPanel; // Panel to switch views

    // UI Components for different views
    private JPanel welcomePanel;
    private JPanel viewAllEmployeesPanel;
    private JPanel viewSpecificEmployeePanel;
    private JPanel addEmployeePanel;
    private JPanel updateEmployeePanel;
    private JPanel calculatePayPanel;
    private JPanel attendancePanel;
    private JTextArea employeeDetailsArea;

    // --- Constructor ---
    public PayrollSystemGUI() {
        setTitle("MotorPH Employee Payroll System");
        setSize(1000, 700); // Increased size for better layout
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window

        employeeManager = EmployeeManager.getInstance(); // Get the Singleton instance

        // Initialize main panel for switching views
        mainPanel = new JPanel(new CardLayout());
        add(mainPanel);

        // Initialize and add all the different panels
        setupWelcomePanel();
        setupViewAllEmployeesPanel();
        setupAddEmployeePanel();
      

        // Show welcome panel initially
        CardLayout cl = (CardLayout) (mainPanel.getLayout());
        cl.show(mainPanel, "Welcome");
    }

    // --- Panel Setups ---

    private void setupWelcomePanel() {
        welcomePanel = new JPanel();
        welcomePanel.setLayout(new BorderLayout());
        
        // Image Panel
        JPanel imagePanel = new JPanel(new BorderLayout());
        
        //Change Logo        
        ImageIcon icon = new ImageIcon("./resources/MotorPH.png"); //create image icon
        setIconImage(icon.getImage());

        // Attempt to load the image for the background
        ImageIcon originalIcon = null;
        try {
            // Try loading from resources first 
            originalIcon = new ImageIcon(getClass().getResource("/bg3.png"));
            if (originalIcon.getIconWidth() == -1) { // Check if loading failed 
                 // Fallback to direct file path if resource loading fails
                 originalIcon = new ImageIcon("./resources/bg3.png");
            }
        } catch (Exception e) {
            originalIcon = new ImageIcon("./resources/bg3.png");
        }

        JLabel imageLabel = null;
        if (originalIcon.getIconWidth() != -1) { // Check if image loaded successfully
            Image image = originalIcon.getImage();
            Image scaledImage = image.getScaledInstance(1000, 700, Image.SCALE_SMOOTH); // Scale as needed
            imageLabel = new JLabel(new ImageIcon(scaledImage));
            imagePanel.add(imageLabel, BorderLayout.CENTER);
            imageLabel.setHorizontalAlignment(SwingConstants.CENTER); // Center the image 
        } else {
            imagePanel.add(new JLabel("MotorPH Logo (Image not found)", SwingConstants.CENTER), BorderLayout.CENTER);
        }
        imageLabel.setLayout(null);
        
               
        JLabel titleLine1 = new JLabel("WELCOME!", SwingConstants.LEFT);
        titleLine1.setBounds(70, 210, 1000, 100);
        titleLine1.setFont(new Font("Arial", Font.BOLD, 50));
        titleLine1.setForeground(Color.WHITE);

        JLabel titleLine3 = new JLabel("Please select an option to get started.", SwingConstants.LEFT);
        titleLine3.setBounds(70, 280, 600, 35);
        titleLine3.setFont(new Font("Arial", Font.PLAIN, 20));
        titleLine3.setForeground(Color.WHITE);
        
        imageLabel.add(titleLine1);
        imageLabel.add(titleLine3);

        //White header with logo
        ImageIcon headerIcon = new ImageIcon("./resources/MotorPH.png"); //To get picture from resources folder
        Image headerLogo = headerIcon.getImage();
        Image scaledIcon = headerLogo.getScaledInstance(20, 20, Image.SCALE_SMOOTH); //To scale the image
        headerIcon = new ImageIcon(scaledIcon);        
               
        JLabel label = new JLabel(headerIcon, SwingConstants.LEFT); //Creates logo label
        
        JLabel titleLabel = new JLabel("MOTORPH PAYROLL SYSTEM", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(new Color(0, 0, 128));

        JPanel whitePanel = new JPanel();
        whitePanel.setLayout(new BorderLayout());
        whitePanel.setBackground(Color.WHITE);
        whitePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 5));
        whitePanel.add(label); //Put logo at the top left corner
        whitePanel.add(titleLabel);
        
        //Footer
        JPanel redPanel = new JPanel();
        redPanel.setLayout(null);
        redPanel.setBackground(new Color(185, 0, 0)); // Red color 
        redPanel.setPreferredSize(new Dimension(1000, 40));
        
        
        JLabel footerLabel = new JLabel("© 2025 MotorPH Payroll", SwingConstants.LEFT);
        footerLabel.setForeground(Color.WHITE);
        footerLabel.setBounds(10, 0, 600, 30); // To position 10px from the left
        footerLabel.setFont(new Font("Arial",Font.BOLD, 10));
        redPanel.add(footerLabel);
       

        welcomePanel.add(imagePanel, BorderLayout.CENTER); //Adds the picture for the background
        welcomePanel.add(whitePanel, BorderLayout.NORTH); //Adds the header
        welcomePanel.add(redPanel, BorderLayout.SOUTH); //Adds the footer        

        // Buttons for navigation on the right-side 
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(5, 1, 10, 10)); // 5 rows, 1 column, spacing
        buttonPanel.setBounds(650, 150, 280, 300);
        buttonPanel.setOpaque(false); // Transparent background
        buttonPanel.setPreferredSize(new Dimension(300, 300)); 
      
        JButton viewAllBtn = createStyledButton("View All Employees");        
        JButton addEmployeeBtn = createStyledButton("Add New Employee");  
        JButton updateEmployee = createStyledButton("Update Employee");
        JButton deleteBtn = createStyledButton("Delete Employee");
        JButton exitBtn = createStyledButton("Exit");
        
        viewAllBtn.addActionListener(e -> showPanel("ViewAll"));       
        addEmployeeBtn.addActionListener(e -> showPanel("AddEmployee"));    
        updateEmployee.addActionListener (e -> {
            
        });
        deleteBtn.addActionListener(e -> {
            try {
                delete();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Error deleting employee: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        exitBtn.addActionListener(e -> System.exit(0));        
               
        buttonPanel.add(viewAllBtn);        
        buttonPanel.add(addEmployeeBtn);  
        buttonPanel.add(deleteBtn);
        buttonPanel.add(exitBtn);

        imageLabel.add(buttonPanel); // Place buttons directly on top of the background image
       

        mainPanel.add(welcomePanel, "Welcome");
    }

    //Shows the table in a scrollable panel with buttons
    private void setupViewAllEmployeesPanel() {
        viewAllEmployeesPanel = new JPanel(new BorderLayout(10, 10));
        viewAllEmployeesPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        viewAllEmployeesPanel.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel("All Employee Records", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(0, 51, 102));
        viewAllEmployeesPanel.add(titleLabel, BorderLayout.NORTH);

        // Table setup
        String[] columnNames = {"Employee ID", "First Name", "Last Name", "Birthday", "Position",
                                "Hourly Rate", "Monthly Salary", "SSS No", "PhilHealth No", "TIN", "Pag-IBIG No"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make all cells non-editable
            }
        };
        employeeTable = new JTable(tableModel);
        employeeTable.setFont(new Font("Arial", Font.PLAIN, 12));
        employeeTable.setRowHeight(25);
        employeeTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        employeeTable.getTableHeader().setBackground(new Color(173, 216, 230)); // Light Blue
        employeeTable.getTableHeader().setReorderingAllowed(false);
        employeeTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // Only one row selectable

        JScrollPane scrollPane = new JScrollPane(employeeTable);
        viewAllEmployeesPanel.add(scrollPane, BorderLayout.CENTER);

        // Buttons for navigation
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton refreshBtn = createStyledButton2("Refresh Records");
        JButton backBtn = createStyledButton2("Back to Main Menu");
        JButton viewEmployee = createStyledButton2("View Employee");  
        JButton updateEmployee = createStyledButton2("Update Details");
        JButton attendanceBtn = createStyledButton2("Record Attendance");

        refreshBtn.addActionListener(e -> populateEmployeeTable());
        backBtn.addActionListener(e -> showPanel("Welcome"));         
        buttonPanel.add(refreshBtn);
        buttonPanel.add(backBtn);
        buttonPanel.add(viewEmployee);      
        buttonPanel.add(updateEmployee);
        buttonPanel.add(attendanceBtn);
        
         viewEmployee.addActionListener(e -> { // To view the employee details based on the row selected
            int selectedRow = employeeTable.getSelectedRow();
            if (selectedRow != -1) {
                int empId = Integer.parseInt(employeeTable.getValueAt(selectedRow, 0).toString()); // Column 0 is Employee ID
                Employee selectedEmp = employeeManager.getEmployeeById(empId);
                if (selectedEmp != null) {
                     List<AttendanceRecord> allRecords = AttendanceManager.getInstance().getAllAttendanceRecords();
                     List<AttendanceRecord> employeeRecords = allRecords.stream()
                         .filter(r -> r.getEmployeeId() == selectedEmp.getEmployeeId())
                         .toList();
                     
                    setupViewSpecificEmployeePanel(selectedEmp, employeeRecords); // Shows the details
                    showPanel("ViewSpecific"); // Then show the panel
                } else {
                    JOptionPane.showMessageDialog(this, "Employee not found.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select an employee first.", "Warning", JOptionPane.WARNING_MESSAGE);
            }
        });
         
        updateEmployee.addActionListener(e -> {
           int selectedRow = employeeTable.getSelectedRow();
           if (selectedRow != -1) {
               int empId = Integer.parseInt(employeeTable.getValueAt (selectedRow, 0). toString());
               Employee employeeToEdit = employeeManager.getEmployeeById(empId);
               if (employeeToEdit != null) {
                   setupUpdateEmployeePanel (employeeToEdit);
                   showPanel("UpdateEmployee");
               } else {
                  JOptionPane.showMessageDialog(this, "Employee not found.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select an employee first.", "Warning", JOptionPane.WARNING_MESSAGE);
            }
        });
        
        attendanceBtn.addActionListener(e -> {
           int selectedRow = employeeTable.getSelectedRow();
           if (selectedRow != -1) {
               int empId = Integer.parseInt(employeeTable.getValueAt (selectedRow, 0). toString());
               Employee selectedEmp = employeeManager.getEmployeeById(empId);
               String firstName = employeeTable.getValueAt(selectedRow, 1).toString();
               String lastName = employeeTable.getValueAt(selectedRow, 2).toString();
               
               if (selectedEmp != null) {
                    setupAttendancePanel(empId,firstName, lastName); // Shows the details
                    showPanel("Attendance Record"); 
                } else {
                    JOptionPane.showMessageDialog(this, "Employee not found.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select an employee first.", "Warning", JOptionPane.WARNING_MESSAGE);
            }
        });
        
        viewAllEmployeesPanel.add(buttonPanel, BorderLayout.SOUTH);
        mainPanel.add(viewAllEmployeesPanel, "ViewAll");
    }

    //Panel that shows after selecting an employee from the table
    private void setupViewSpecificEmployeePanel(Employee employee, List<AttendanceRecord> attendanceRecords) {
        

             viewSpecificEmployeePanel = new JPanel(new BorderLayout(10, 10));
             viewSpecificEmployeePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
             viewSpecificEmployeePanel.setBackground(Color.WHITE);

             JLabel titleLabel = new JLabel("Employee Details", SwingConstants.CENTER);
             titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
             titleLabel.setForeground(new Color(0, 51, 102));
             viewSpecificEmployeePanel.add(titleLabel, BorderLayout.NORTH);

             // Employee Details Area that is NOT editable
             employeeDetailsArea = new JTextArea(15, 40);
             employeeDetailsArea.setFont(new Font("Arial", Font.PLAIN, 14));
             employeeDetailsArea.setEditable(false);
             employeeDetailsArea.setLineWrap(true);
             employeeDetailsArea.setWrapStyleWord(true);
             JScrollPane detailsScroll = new JScrollPane(employeeDetailsArea);

             displayEmployeeDetails(employee);

             // Pay Calculator Panel
             JPanel payCalculatorPanel = new JPanel(new BorderLayout(10, 10));
             payCalculatorPanel.setBackground(new Color(240, 248, 255));

             JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
             inputPanel.setBackground(new Color(240, 248, 255));
             
             //Input data range to calculate pay within that payroll period only
             //Attendance file only contains records from 2024
             JLabel startDate = new JLabel("Start Date (MM/dd/yyyy) :");
             JTextField startDateField = new JTextField(10);
             startDateField.setToolTipText("Enter start date");

             JLabel endDate = new JLabel("End Date (MM/dd/yyyy):");
             JTextField endDateField = new JTextField(10);
             endDateField.setToolTipText("Enter end date");

             JButton calculateBtn = createStyledButton2("Calculate Pay");

             inputPanel.add(startDate);
             inputPanel.add(endDate);
             inputPanel.add(startDateField);
             inputPanel.add(endDateField);
             inputPanel.add(calculateBtn);

             payCalculatorPanel.add(inputPanel, BorderLayout.NORTH);

             //To display generated payslip (NOT editable)
             JTextArea payslipArea = new JTextArea(20, 40);
             payslipArea.setEditable(false);
             payslipArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
             payslipArea.setBorder(BorderFactory.createEtchedBorder());
             JScrollPane payslipScroll = new JScrollPane(payslipArea);
             payCalculatorPanel.add(payslipScroll, BorderLayout.CENTER);

             calculateBtn.addActionListener(e -> {
                 //Get the date inputs
                 String startDateInput = startDateField.getText().trim();
                 String endDateInput = endDateField.getText().trim();
                 DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
                 
                 //Check if all fields are filled in
                 if (startDateInput.isEmpty() || endDateInput.isEmpty()) {
                     payslipArea.setText("Please fill in both start and end dates.");
                     return;
                 }
                 
                 //Initialized start and end dates so I can properly put try-catch blocks
                 LocalDate start;
                 LocalDate end;
                 
                 //Show error when parsing fails
                 try {
                     start = LocalDate.parse(startDateInput, formatter);
                 } catch (DateTimeParseException dtpe) {
                     payslipArea.setText("Please use MM/dd/yyyy format properly for Start Date");
                     return;
                 }
                 
                 //Show error when parsing fails
                 try {
                     end = LocalDate.parse(endDateInput, formatter);
                 } catch (DateTimeParseException dtpe) {
                     payslipArea.setText("Please use MM/dd/yyyy format properly for End Date");
                     return;
                 }
                     
                 //Check if end date is not before start date
                 if (end.isBefore(start)) {
                         payslipArea.setText("End Date must be after or equal to Start Date.");
                         return;
                 }
                      
                 try { 
                      //Get all attendance records from AttendanceManager
                      var allAttendance = AttendanceManager.getInstance().getAllAttendanceRecords();
                      
                      //Filter the records for the employee and within the specific date
                      var filteredRecords = allAttendance.stream()
                            .filter(r -> r.getEmployeeId() == employee.getEmployeeId()
                                      && !r.getDate().isBefore(start)
                                      && !r.getDate().isAfter(end))
                            .toList();
                      
                      //Check if there are available records
                      if (filteredRecords.isEmpty()) {
                        payslipArea.setText("No attendance records found for the selected period.");
                        return;
                      }
                      
                      //Call on methods and getters for pay calculations
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
                      
                      
                      //Formatted text 
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

                      payslipArea.setText(payslipText.toString());

                  } catch (Exception ex) {
                      payslipArea.setText("Invalid input. Please enter valid dates in MM/dd/yyyy format.");
                  }
              });

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, detailsScroll, payCalculatorPanel);
        splitPane.setResizeWeight(0.5);
        viewSpecificEmployeePanel.add(splitPane, BorderLayout.CENTER);

        JButton backButton = createStyledButton2("Back to All Employees");
        backButton.addActionListener(e -> showPanel("ViewAll"));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(backButton);
        viewSpecificEmployeePanel.add(buttonPanel, BorderLayout.SOUTH);

        mainPanel.add(viewSpecificEmployeePanel, "ViewSpecific");
    }   
    
    //Panel for adding new employees
    private void setupAddEmployeePanel() {
        addEmployeePanel = new JPanel(new BorderLayout(10, 10));
        addEmployeePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        addEmployeePanel.setBackground(new Color(240, 248, 255));

        JLabel titleLabel = new JLabel("Add New Employee Record", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(0, 51, 102));
        addEmployeePanel.add(titleLabel, BorderLayout.NORTH);

        // Form Panel
        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10)); // Rows, 2 columns, gaps
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        formPanel.setBackground(new Color(240, 248, 255));

        JTextField idField = new JTextField(15);
        JTextField firstNameField = new JTextField(15);
        JTextField lastNameField = new JTextField(15);
        JTextField birthdayField = new JTextField(15);
        JTextField positionField = new JTextField(15);
        JTextField hourlyRateField = new JTextField(15);
        JTextField monthlySalaryField = new JTextField(15);
        JTextField sssNoField = new JTextField(15);
        JTextField philhealthNoField = new JTextField(15);
        JTextField tinField = new JTextField(15);
        JTextField pagibigNoField = new JTextField(15);

        formPanel.add(new JLabel("Employee ID:"));
        formPanel.add(idField);
        formPanel.add(new JLabel("First Name:"));
        formPanel.add(firstNameField);
        formPanel.add(new JLabel("Last Name:"));
        formPanel.add(lastNameField);
        formPanel.add(new JLabel("Birthday (MM/DD/YYYY):"));
        formPanel.add(birthdayField);
        formPanel.add(new JLabel("Position:"));
        formPanel.add(positionField);
        formPanel.add(new JLabel("Hourly Rate:"));
        formPanel.add(hourlyRateField);
        formPanel.add(new JLabel("Monthly Basic Salary:"));
        formPanel.add(monthlySalaryField);
        formPanel.add(new JLabel("SSS No:"));
        formPanel.add(sssNoField);
        formPanel.add(new JLabel("PhilHealth No:"));
        formPanel.add(philhealthNoField);
        formPanel.add(new JLabel("TIN:"));
        formPanel.add(tinField);
        formPanel.add(new JLabel("Pag-IBIG No:"));
        formPanel.add(pagibigNoField);

        addEmployeePanel.add(formPanel, BorderLayout.CENTER);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(new Color(240, 248, 255));
        JButton saveBtn = createStyledButton2("Save Employee");
        JButton clearBtn = createStyledButton2("Clear Form");
        JButton backBtn = createStyledButton2("Back to Main Menu");

        saveBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int id = Integer.parseInt(idField.getText().trim());
                    String firstName = firstNameField.getText().trim();
                    String lastName = lastNameField.getText().trim();
                    String birthday = birthdayField.getText().trim();
                    String position = positionField.getText().trim();
                    double hourlyRate = Double.parseDouble(hourlyRateField.getText().trim());
                    double monthlySalary = Double.parseDouble(monthlySalaryField.getText().trim());
                    String sssNo = sssNoField.getText().trim();
                    String philhealthNo = philhealthNoField.getText().trim();
                    String tin = tinField.getText().trim();
                    String pagibigNo = pagibigNoField.getText().trim();

                    if (firstName.isEmpty() || lastName.isEmpty() || birthday.isEmpty() || position.isEmpty() ||
                        sssNo.isEmpty() || philhealthNo.isEmpty() || tin.isEmpty() || pagibigNo.isEmpty()) {
                        JOptionPane.showMessageDialog(addEmployeePanel, "All fields are required!", "Input Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    
                     if (!firstName.matches("[a-zA-Z ]+")) { 
                        JOptionPane.showMessageDialog(addEmployeePanel, "Please enter only alphabets and spaces for the first name.", "Input Error", JOptionPane.ERROR_MESSAGE);
                        return; 
                    } // Added this block para mag throw ng error if hindi alphabets or string ang data input
                     
                     if (!lastName.matches("[a-zA-Z ]+")) { // Allows letters and spaces
                        JOptionPane.showMessageDialog(addEmployeePanel, "Last Name should only contain alphabets and spaces.", "Input Error", JOptionPane.ERROR_MESSAGE);
                        return; // Stop processing if invalid
                    } // Added this block para mag throw ng error if hindi alphabets or string ang data input

                     try {
                        java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("MM/dd/yyyy");
                        LocalDate.parse(birthday, formatter);
                    } catch (java.time.format.DateTimeParseException dtpe) {
                        JOptionPane.showMessageDialog(addEmployeePanel, "Invalid Birthday format. Please use MM/DD/YYYY date format.", "Input Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    if (hourlyRate < 0 || monthlySalary < 0) {
                         JOptionPane.showMessageDialog(addEmployeePanel, "Please enter the correct value. Hourly Rate and Monthly Salary cannot be negative.", "Input Error", JOptionPane.ERROR_MESSAGE);
                         return;
                    } //This is for DOBs format validation as well, added this for the same purpose of checking data input
                
                    if (!position.matches("[a-zA-Z ]+")) {
                        JOptionPane.showMessageDialog(addEmployeePanel, "Position should only contain alphabets and spaces.", "Input Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    } //Newly added error message, this will check kung letters and spaces lang ang input, will throw error message pag ibang data type ang na enter sa field

                  
                    if (!sssNo.matches("\\d+")) { // \\d+ means one or more digits
                        JOptionPane.showMessageDialog(addEmployeePanel, "SSS Number should only contain digits.", "Input Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    } //Newly added error message, this will check kung numbers lang ang input, will throw error message pag ibang data type ang na enter sa field

                    
                    if (!philhealthNo.matches("\\d+")) {
                        JOptionPane.showMessageDialog(addEmployeePanel, "PhilHealth Number should only contain digits.", "Input Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    } //Newly added error message, this will check kung numbers lang ang input, will throw error message pag ibang data type ang na enter sa field

                   
                    if (!tin.matches("\\d+")) {
                        JOptionPane.showMessageDialog(addEmployeePanel, "TIN should only contain digits.", "Input Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    } //Newly added error message, this will check kung numbers lang ang input, will throw error message pag ibang data type ang na enter sa field

                  
                    if (!pagibigNo.matches("\\d+")) {
                        JOptionPane.showMessageDialog(addEmployeePanel, "Pag-IBIG Number should only contain digits.", "Input Error", JOptionPane.ERROR_MESSAGE);
                        return;
                        } //Newly added error message, this will check kung numbers lang ang input, will throw error message pag ibang data type ang na enter sa field

                    // Additional field checks for SSS, Pagibig, Philhealth and TIN. I checheck nito if tama ba yung bilang ng digits entered
                    // Digit requirements are from my research lang, depending on  SSS, Pagibig, Philhealth and TIN, baka these will change in the future
                    //Per research, adding only minimum number of digits to allow for future changes in issued digits is not advisable for data protection so I made it 
                    // check for exact number of digits entered sa field
                    
                    if (!sssNo.matches("\\d{10}")) {
                        JOptionPane.showMessageDialog(addEmployeePanel, "SSS Number must be exactly 10 digits.", "Input Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    } //Should check for exact 10 digits entered 

                  
                    if (!philhealthNo.matches("\\d{12}")) {
                        JOptionPane.showMessageDialog(addEmployeePanel, "PhilHealth Number must be exactly 12 digits.", "Input Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    } //Should check for exact 12 digits entered 

                    

                    if (!tin.matches("\\d{9}")) {
                        JOptionPane.showMessageDialog(addEmployeePanel, "TIN must be exactly 9 digits.", "Input Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    } //Should check for exact 9  digits entered 

              
                    if (!pagibigNo.matches("\\d{12}")) {
                        JOptionPane.showMessageDialog(addEmployeePanel, "Pag-IBIG Number must be exactly 12 digits.", "Input Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    } //Should check for exact 12 digits entered

                        boolean success = employeeManager.addEmployee(id, firstName, lastName, birthday, position,
                                                                     hourlyRate, monthlySalary, sssNo, philhealthNo, tin, pagibigNo);
                        if (success) {
                            JOptionPane.showMessageDialog(addEmployeePanel, "Employee added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                            clearForm(idField, firstNameField, lastNameField, birthdayField, positionField, hourlyRateField,
                                      monthlySalaryField, sssNoField, philhealthNoField, tinField, pagibigNoField);
                        } else {
                            JOptionPane.showMessageDialog(addEmployeePanel, "Failed to add employee. ID might already exist.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(addEmployeePanel, "Invalid number format for ID, Hourly Rate, or Monthly Salary.", "Input Error", JOptionPane.ERROR_MESSAGE);
                    } catch (IOException ex) {
                        Logger.getLogger(PayrollSystemGUI.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });

        clearBtn.addActionListener(e -> clearForm(idField, firstNameField, lastNameField, birthdayField, positionField, hourlyRateField,
                                                 monthlySalaryField, sssNoField, philhealthNoField, tinField, pagibigNoField));
        backBtn.addActionListener(e -> showPanel("Welcome"));

        buttonPanel.add(saveBtn);
        buttonPanel.add(clearBtn);
        buttonPanel.add(backBtn);
        addEmployeePanel.add(buttonPanel, BorderLayout.SOUTH);

        mainPanel.add(addEmployeePanel, "AddEmployee");
    }
    
    //Panel for updating employee details
    private void setupUpdateEmployeePanel (Employee employeeToEdit) {
       updateEmployeePanel = new JPanel(new BorderLayout(10, 10));
       updateEmployeePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
       updateEmployeePanel.setBackground(new Color(240,248,255));
        
        JLabel  titleLabel = new JLabel("Update Existing Employee Record", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Ariel", Font.BOLD, 24));
        titleLabel.setForeground(new Color(0, 51, 102));
        updateEmployeePanel.add(titleLabel, BorderLayout.NORTH);

        //Form 
        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        formPanel.setBackground(new Color(240, 248, 255));

        JTextField firstNameField = new JTextField(employeeToEdit.getFirstName());
        JTextField lastNameField = new JTextField(employeeToEdit.getLastName());
        JTextField birthdayField = new JTextField(employeeToEdit.getBirthday());
        JTextField positionField = new JTextField(employeeToEdit.getPosition());
        JTextField hourlyRateField = new JTextField(String.valueOf(employeeToEdit.getHourlyRate()));
        JTextField monthlySalaryField = new JTextField(String.valueOf(employeeToEdit.getSalary()));
        JTextField sssNoField = new JTextField(employeeToEdit.getSssNo());
        JTextField philhealthNoField = new JTextField(employeeToEdit.getPhilhealthNo());
        JTextField tinField = new JTextField(employeeToEdit.getTin());
        JTextField pagibigNoField = new JTextField(employeeToEdit.getPagibigNo());
        
        formPanel.add(new JLabel("First Name:"));
        formPanel.add(firstNameField);
        formPanel.add(new JLabel("Last Name:"));
        formPanel.add(lastNameField);
        formPanel.add(new JLabel("Birthday (MM/DD/YYYY):"));
        formPanel.add(birthdayField);
        formPanel.add(new JLabel("Position:"));
        formPanel.add(positionField);
        formPanel.add(new JLabel("Hourly Rate:"));
        formPanel.add(hourlyRateField);
        formPanel.add(new JLabel("Monthly Basic Salary:"));
        formPanel.add(monthlySalaryField);
        formPanel.add(new JLabel("SSS No:"));
        formPanel.add(sssNoField);
        formPanel.add(new JLabel("PhilHealth No:"));
        formPanel.add(philhealthNoField);
        formPanel.add(new JLabel("TIN:"));
        formPanel.add(tinField);
        formPanel.add(new JLabel("Pag-IBIG No:"));
        formPanel.add(pagibigNoField);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        JButton backButton = createStyledButton2("Back to All Employees");
        backButton.addActionListener(e -> showPanel("ViewAll"));
        
        JButton clearBtn = createStyledButton2("Clear Form");
        clearBtn.addActionListener(e -> clearForm(firstNameField, lastNameField, birthdayField, positionField, hourlyRateField,
                                                 monthlySalaryField, sssNoField, philhealthNoField, tinField, pagibigNoField));

        //To save the updated information
        JButton updateBtn = createStyledButton2("Update");
        updateBtn.addActionListener (e -> {
           
                    try {
                        String newFirstName = firstNameField.getText();
                        String newLastName = lastNameField.getText();
                        String newBirthday = birthdayField.getText();
                        String newPosition = positionField.getText();
                        double newHourlyRate = Double.parseDouble(hourlyRateField.getText());
                        double newSalary = Double.parseDouble(monthlySalaryField.getText());
                        String newSssNo = sssNoField.getText();
                        String newPhilhealthNo = philhealthNoField.getText();
                        String newTin = tinField.getText();
                        String newPagibigNo = pagibigNoField.getText();
                        try {
                            employeeManager.editEmployee(employeeToEdit.getEmployeeId(), newFirstName, newLastName, newBirthday, newPosition, newHourlyRate,
                                    newSalary, newSssNo, newPhilhealthNo, newTin, newPagibigNo);
                        } catch (IOException ex) {
                            Logger.getLogger(PayrollSystemGUI.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        JOptionPane.showMessageDialog(null, "Employee " + employeeToEdit.getEmployeeId() + " details updated.");
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(null, "Invalid input.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
        });
        updateEmployeePanel.add(formPanel, BorderLayout.CENTER);
        buttonPanel.add(updateBtn);
        buttonPanel.add(backButton);
        buttonPanel.add(clearBtn);
        updateEmployeePanel.add(buttonPanel, BorderLayout.SOUTH);
        
        mainPanel.add(updateEmployeePanel, "UpdateEmployee");

    }      
    
    //Panel for recording time in and time out
    private void setupAttendancePanel(int employeeId, String firstName, String lastName) {
            attendancePanel = new JPanel (new BorderLayout());
            
            //Formatters for parsing date and time
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("h:mm a"); //using 12-hour format
        
            JLabel titleLabel = new JLabel("Record Attendance for " + firstName + " " + lastName, SwingConstants.CENTER);
            titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
            attendancePanel.add(titleLabel, BorderLayout.NORTH);

            // Form Panel
            JPanel formPanel = new JPanel(new GridLayout(10, 2, 10, 10));
            formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            
            JTextField dateField = new JTextField(); // Format: MM/DD/YYYY
            dateField.setPreferredSize(new Dimension(100, 20));
            JTextField timeInField = new JTextField(); // Format: HH:mm
            timeInField.setPreferredSize(new Dimension(100, 20));
            JTextField timeOutField = new JTextField(); // Format: HH:mm
            timeOutField.setPreferredSize(new Dimension(100, 20));

            formPanel.add(new JLabel("Date (MM/DD/YYYY):"));
            formPanel.add(dateField);
            formPanel.add(new JLabel("Time In (HH:mm AM):"));
            formPanel.add(timeInField);
            formPanel.add(new JLabel("Time Out (HH:mm PM):"));
            formPanel.add(timeOutField);
            
            //Font for inputs
            dateField.setFont(new Font("Arial", Font.PLAIN, 16));
            timeInField.setFont(new Font("Arial", Font.PLAIN, 16));
            timeOutField.setFont(new Font("Arial", Font.PLAIN, 16));

            attendancePanel.add(formPanel, BorderLayout.CENTER);
            
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            JButton saveButton = createStyledButton2("Save");
            JButton backButton = createStyledButton2("Back to Employee List");
            
            saveButton.addActionListener(e -> {
                try {
                    String dateInput = dateField.getText().trim();
                    String timeInInput = timeInField.getText().toUpperCase().trim();
                    String timeOutInput = timeOutField.getText().toUpperCase().trim();
                    
                    //Check if user inputs all necessary details
                    if (dateInput.isEmpty() || timeInInput.isEmpty() || timeOutInput.isEmpty()) {
                        JOptionPane.showMessageDialog(attendancePanel,"Please fill in all fields.","Missing Input", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    
                    //Parse Date
                    LocalDate date;
                    try {
                        date = LocalDate.parse(dateInput, DateTimeFormatter.ofPattern("MM/dd/yyyy"));
                    } catch (DateTimeParseException dtpe) {
                        JOptionPane.showMessageDialog(attendancePanel, "Invalid date format. Please use MM/DD/YYYY.", "Date Format Error",JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    
                     // Parse Time In
                    LocalTime timeIn;
                    try {
                        timeIn = LocalTime.parse(timeInInput, timeFormatter);
                    } catch (DateTimeParseException dtpe) {
                        JOptionPane.showMessageDialog(attendancePanel, "Invalid Time In format. Please use h:mm AM", "Time In Format Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    
                    // Parse Time Out
                    LocalTime timeOut;
                    try {
                        timeOut = LocalTime.parse(timeOutInput, timeFormatter);
                    } catch (DateTimeParseException dtpe) {
                        JOptionPane.showMessageDialog(attendancePanel, "Invalid Time Out format. Please use h:mm PM format", "Time Out Format Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    
                    //Check if the user inputs Time In that is later than Time Out
                    if (timeIn.isAfter(timeOut)) {
                        JOptionPane.showMessageDialog(attendancePanel, "Time In cannot be later than Time Out.", "Invalid Time Entry", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    

                    AttendanceRecord record = new AttendanceRecord(employeeId, lastName, firstName, date, timeIn, timeOut);
                    boolean added = AttendanceManager.getInstance().addAttendanceRecord(record);
                    if (added) {
                        JOptionPane.showMessageDialog(attendancePanel, "Attendance recorded successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                        showPanel("ViewAll"); // Go back to employee list
                    } else {
                        JOptionPane.showMessageDialog(attendancePanel, "Attendance record for this employee on the specified date already exists.", "Record Already Exists", JOptionPane.WARNING_MESSAGE);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(attendancePanel, "Error: " + ex.getMessage(), "Input Error", JOptionPane.ERROR_MESSAGE);
                }
            });

            backButton.addActionListener(e -> showPanel("ViewAll")); 
            buttonPanel.add(saveButton);
            buttonPanel.add(backButton);

            attendancePanel.add(buttonPanel, BorderLayout.SOUTH);

            mainPanel.add(attendancePanel, "Attendance Record");
    }

    // --- Helper Methods ---

    private void showPanel(String panelName) {
        CardLayout cl = (CardLayout) (mainPanel.getLayout());
        cl.show(mainPanel, panelName);

        // Special actions when showing specific panels
        if (panelName.equals("ViewAll")) {
            populateEmployeeTable(); // Refresh table when viewing all employees
        }        
    }

    private void populateEmployeeTable() {
        tableModel.setRowCount(0); // Clear existing data
        for (Employee employee : employeeManager.getEmployees()) {
            tableModel.addRow(employee.toCsvArray()); // Use the toCsvArray to get all data
        }
    }
    
    private void delete () throws IOException {
            String inputId = JOptionPane.showInputDialog("Enter Employee ID to delete:");
            if (inputId == null || inputId.trim().isEmpty()) return;
            try {
                int id = Integer.parseInt(inputId);
                int confirmation = JOptionPane.showConfirmDialog(null,
                        "Are you sure you want to delete employee ID " + id + "?",
                        "Confirm Deletion", JOptionPane.YES_NO_OPTION);
                if (confirmation == JOptionPane.YES_OPTION) {
                    employeeManager.deleteEmployee(id);
                    JOptionPane.showMessageDialog(null, "Employee deleted successfully.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Invalid ID entered.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    
    private void displayEmployeeDetails(Employee employee) {
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
        
    private void clearForm(JTextField... fields) {
        for (JTextField field : fields) {
            field.setText("");
        }
    }    
    
    // Common style of all buttons
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 20));
        button.setBackground(Color.WHITE); 
        button.setForeground(new Color(0, 0, 128));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(0, 0, 90)); // Darker on hover
                button.setForeground(Color.WHITE);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(Color.WHITE); // Restore original
                button.setForeground(new Color(0, 0, 128));
            }
        });
        return button;
    }
    
    //This is for buttons inside the table
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
                button.setBackground(Color.WHITE); // Lighter on hover
                button.setForeground(new Color(0, 0, 128));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(0, 0, 90)); // Restore original
                button.setForeground(Color.WHITE);
            }
        });
        return button;
    }

    // --- Main method ---
    public static void main(String[] args) {
        EmployeeManager.getInstance(); // Initialize EmployeeManager and Attendance Manager early to check if data is available
        AttendanceManager.getInstance();
        SwingUtilities.invokeLater(() -> {
            new PayrollSystemGUI().setVisible(true);
        });
    }
}



