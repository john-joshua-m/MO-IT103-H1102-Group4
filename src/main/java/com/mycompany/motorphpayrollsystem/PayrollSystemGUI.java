package com.mycompany.motorphpayrollsystem;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File; // For checking image existence

public class PayrollSystemGUI extends JFrame {

    private EmployeeManager employeeManager;
    private DefaultTableModel tableModel;
    private JTable employeeTable;
    private JPanel mainPanel; // Panel to switch views

    // UI Components for various views
    private JPanel welcomePanel;
    private JPanel viewAllEmployeesPanel;
    private JPanel viewSpecificEmployeePanel;
    private JPanel addEmployeePanel;
    private JPanel calculatePayPanel;

    // --- Constructor ---
    public PayrollSystemGUI() {
        setTitle("MotorPH Employee Payroll System");
        setSize(1000, 700); // Increased size for better layout
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window

        employeeManager = EmployeeManager.getInstance(); // Get the Singleton instance

        // Initialize main panel for switching views
        mainPanel = new JPanel(new CardLayout());
        add(mainPanel);

        // Initialize and add all the different panels/views
        setupWelcomePanel();
        setupViewAllEmployeesPanel();
        setupViewSpecificEmployeePanel();
        setupAddEmployeePanel();
        setupCalculatePayPanel();

        // Show welcome panel initially
        CardLayout cl = (CardLayout) (mainPanel.getLayout());
        cl.show(mainPanel, "Welcome");
    }

    // --- Panel Setups ---

    private void setupWelcomePanel() {
        welcomePanel = new JPanel();
        welcomePanel.setLayout(new BorderLayout());
        welcomePanel.setBackground(new Color(240, 248, 255)); // Alice Blue

        // Image Panel
        JPanel imagePanel = new JPanel(new BorderLayout());
        imagePanel.setBackground(new Color(240, 248, 255));

        // Attempt to load the image dynamically
        ImageIcon originalIcon = null;
        try {
            // Try loading from resources first (for JAR deployment)
            originalIcon = new ImageIcon(getClass().getResource("/MotorPH.png"));
            if (originalIcon.getIconWidth() == -1) { // Check if loading failed (e.g., file not found)
                 // Fallback to direct file path if resource loading fails
                 originalIcon = new ImageIcon("./resources/MotorPH.png");
            }
        } catch (Exception e) {
            System.err.println("Error loading image from resources, trying file path: " + e.getMessage());
            originalIcon = new ImageIcon("./resources/MotorPH.png");
        }


        if (originalIcon.getIconWidth() != -1) { // Check if image loaded successfully
            Image image = originalIcon.getImage();
            Image scaledImage = image.getScaledInstance(300, 300, Image.SCALE_SMOOTH); // Scale as needed
            JLabel imageLabel = new JLabel(new ImageIcon(scaledImage));
            imagePanel.add(imageLabel, BorderLayout.CENTER);
            imageLabel.setHorizontalAlignment(SwingConstants.CENTER); // Center the image
        } else {
            imagePanel.add(new JLabel("MotorPH Logo (Image not found)", SwingConstants.CENTER), BorderLayout.CENTER);
        }

        // Welcome Text Panel
        JPanel textPanel = new JPanel(new GridLayout(0, 1, 10, 10)); // Grid layout for stacked text
        textPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        textPanel.setBackground(new Color(240, 248, 255));

        JLabel welcomeLabel = new JLabel("Welcome to MotorPH Payroll System!", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 28));
        welcomeLabel.setForeground(new Color(0, 51, 102)); // Dark Blue

        JLabel instructionLabel = new JLabel("Please select an option from the menu below to get started.", SwingConstants.CENTER);
        instructionLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        instructionLabel.setForeground(new Color(51, 51, 51)); // Dark Gray

        textPanel.add(welcomeLabel);
        textPanel.add(instructionLabel);

        welcomePanel.add(imagePanel, BorderLayout.CENTER);
        welcomePanel.add(textPanel, BorderLayout.NORTH);


        // Buttons for navigation
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        buttonPanel.setBackground(new Color(240, 248, 255));
        JButton viewAllBtn = createStyledButton("View All Employees");
        JButton viewSpecificBtn = createStyledButton("View Specific Employee");
        JButton addEmployeeBtn = createStyledButton("Add New Employee");
        JButton calculatePayBtn = createStyledButton("Calculate Employee Pay");
        JButton exitBtn = createStyledButton("Exit");

        viewAllBtn.addActionListener(e -> showPanel("ViewAll"));
        viewSpecificBtn.addActionListener(e -> showPanel("ViewSpecific"));
        addEmployeeBtn.addActionListener(e -> showPanel("AddEmployee"));
        calculatePayBtn.addActionListener(e -> showPanel("CalculatePay"));
        exitBtn.addActionListener(e -> System.exit(0));

        buttonPanel.add(viewAllBtn);
        buttonPanel.add(viewSpecificBtn);
        buttonPanel.add(addEmployeeBtn);
        buttonPanel.add(calculatePayBtn);
        buttonPanel.add(exitBtn);

        welcomePanel.add(buttonPanel, BorderLayout.SOUTH);

        mainPanel.add(welcomePanel, "Welcome");
    }

    private void setupViewAllEmployeesPanel() {
        viewAllEmployeesPanel = new JPanel(new BorderLayout(10, 10));
        viewAllEmployeesPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        viewAllEmployeesPanel.setBackground(new Color(240, 248, 255));

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
        employeeTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // Only one row selectable

        JScrollPane scrollPane = new JScrollPane(employeeTable);
        viewAllEmployeesPanel.add(scrollPane, BorderLayout.CENTER);

        // Buttons for navigation
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton refreshBtn = createStyledButton("Refresh Records");
        JButton backBtn = createStyledButton("Back to Main Menu");

        refreshBtn.addActionListener(e -> populateEmployeeTable());
        backBtn.addActionListener(e -> showPanel("Welcome"));
        buttonPanel.add(refreshBtn);
        buttonPanel.add(backBtn);
        viewAllEmployeesPanel.add(buttonPanel, BorderLayout.SOUTH);

        mainPanel.add(viewAllEmployeesPanel, "ViewAll");
    }

    private void setupViewSpecificEmployeePanel() {
        viewSpecificEmployeePanel = new JPanel(new BorderLayout(10, 10));
        viewSpecificEmployeePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        viewSpecificEmployeePanel.setBackground(new Color(240, 248, 255));

        JLabel titleLabel = new JLabel("View Specific Employee Record", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(0, 51, 102));
        viewSpecificEmployeePanel.add(titleLabel, BorderLayout.NORTH);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        searchPanel.setBackground(new Color(240, 248, 255));
        JLabel idLabel = new JLabel("Enter Employee ID:");
        JTextField idField = new JTextField(10);
        JButton searchBtn = createStyledButton("Search");
        searchPanel.add(idLabel);
        searchPanel.add(idField);
        searchPanel.add(searchBtn);
        viewSpecificEmployeePanel.add(searchPanel, BorderLayout.WEST); // Place search on left/top

        JTextArea employeeDetailsArea = new JTextArea(15, 40);
        employeeDetailsArea.setEditable(false);
        employeeDetailsArea.setFont(new Font("Monospaced", Font.PLAIN, 14)); // For better formatting
        employeeDetailsArea.setBorder(BorderFactory.createEtchedBorder());
        JScrollPane scrollDetails = new JScrollPane(employeeDetailsArea);
        viewSpecificEmployeePanel.add(scrollDetails, BorderLayout.CENTER);


        searchBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int id = Integer.parseInt(idField.getText().trim());
                    Employee employee = employeeManager.getEmployeeById(id);
                    if (employee != null) {
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
                        details.append(String.format("Current Hours Worked: %.2f%n", employee.getTotalHoursWorked()));
                        details.append(String.format("Current Overtime Hours: %.2f%n", employee.getOvertimeHours()));
                        employeeDetailsArea.setText(details.toString());
                    } else {
                        employeeDetailsArea.setText("Employee with ID " + id + " not found.");
                    }
                } catch (NumberFormatException ex) {
                    employeeDetailsArea.setText("Invalid Employee ID. Please enter a number.");
                }
            }
        });

        // Buttons for navigation
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton backBtn = createStyledButton("Back to Main Menu");
        backBtn.addActionListener(e -> showPanel("Welcome"));
        buttonPanel.add(backBtn);
        viewSpecificEmployeePanel.add(buttonPanel, BorderLayout.SOUTH);

        mainPanel.add(viewSpecificEmployeePanel, "ViewSpecific");
    }

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
        JButton saveBtn = createStyledButton("Save Employee");
        JButton clearBtn = createStyledButton("Clear Form");
        JButton backBtn = createStyledButton("Back to Main Menu");

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

    private void setupCalculatePayPanel() {
        calculatePayPanel = new JPanel(new BorderLayout(10, 10));
        calculatePayPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        calculatePayPanel.setBackground(new Color(240, 248, 255));

        JLabel titleLabel = new JLabel("Calculate Employee Monthly Pay", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(0, 51, 102));
        calculatePayPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        searchPanel.setBackground(new Color(240, 248, 255));
        JLabel idLabel = new JLabel("Enter Employee ID:");
        JTextField idField = new JTextField(10);

        // Month selection (if needed for future monthly hour tracking, otherwise fixed)
        JLabel monthLabel = new JLabel("Month:");
        String[] months = {"January", "February", "March", "April", "May", "June",
                           "July", "August", "September", "October", "November", "December"};
        JComboBox<String> monthComboBox = new JComboBox<>(months);
        monthComboBox.setSelectedItem("June"); // Current month as default

        JLabel hoursLabel = new JLabel("Hours Worked This Month:");
        JTextField hoursField = new JTextField(10);
        hoursField.setToolTipText("Enter total hours worked, including any overtime.");


        JButton calculateBtn = createStyledButton("Calculate Pay");
        searchPanel.add(idLabel);
        searchPanel.add(idField);
        searchPanel.add(monthLabel); // Added month selection
        searchPanel.add(monthComboBox);
        searchPanel.add(hoursLabel);
        searchPanel.add(hoursField);
        searchPanel.add(calculateBtn);
        calculatePayPanel.add(searchPanel, BorderLayout.WEST);


        JTextArea payslipArea = new JTextArea(20, 50);
        payslipArea.setEditable(false);
        payslipArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        payslipArea.setBorder(BorderFactory.createEtchedBorder());
        JScrollPane scrollPayslip = new JScrollPane(payslipArea);
        calculatePayPanel.add(scrollPayslip, BorderLayout.CENTER);

        calculateBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int id = Integer.parseInt(idField.getText().trim());
                    double hoursWorkedInput = Double.parseDouble(hoursField.getText().trim());
                    String selectedMonth = (String) monthComboBox.getSelectedItem(); // Get selected month

                    Employee employee = employeeManager.getEmployeeById(id);
                    if (employee != null) {
                        // Reset hours for a new calculation period to prevent accumulation
                        employee.resetMonthlyHours();
                        // Add hours to the employee; this will separate regular and overtime
                        employee.addRenderedHours(hoursWorkedInput);

                        // Use Motorphpayrollsystem to get calculations and display payslip
                        // The displayPayslip method already prints to console, but for GUI, we want to capture it.
                        // A better approach would be to have Motorphpayrollsystem return a formatted string.
                        // For now, let's create the string here using the calculated values.
                        double basicSalary = employee.getSalary();
                        double hourlyRate = employee.getHourlyRate();
                        double totalRegularHours = employee.getTotalHoursWorked(); // This is regular hours up to threshold
                        double overtimeHours = employee.getOvertimeHours();
                        double grossSalary = Motorphpayrollsystem.calculateGrossSalary(employee);

                        double sssDeduction = Motorphpayrollsystem.calculateSSSContribution(basicSalary);
                        double philhealthDeduction = Motorphpayrollsystem.calculatePhilhealthContribution(basicSalary);
                        double pagibigDeduction = Motorphpayrollsystem.calculatePagibigContribution(basicSalary);
                        double taxableIncome = grossSalary - sssDeduction - philhealthDeduction - pagibigDeduction;
                        double taxDeduction = Motorphpayrollsystem.calculateWithholdingTax(taxableIncome);
                        double totalDeductions = Motorphpayrollsystem.round(sssDeduction + philhealthDeduction + pagibigDeduction + taxDeduction);

                        double netSalary = Motorphpayrollsystem.round(grossSalary - totalDeductions);

                        StringBuilder payslipText = new StringBuilder();
                        payslipText.append(String.format("--- Monthly Payslip for %s (ID: %d) ---\n", employee.getFullName(), employee.getEmployeeId()));
                        payslipText.append("----------------------------------------------------------\n");
                        payslipText.append(String.format("Month: %s%n", selectedMonth)); // Display selected month
                        payslipText.append(String.format("Position: %s%n", employee.getPosition()));
                        payslipText.append(String.format("Monthly Basic Salary: P %.2f%n", basicSalary));
                        payslipText.append(String.format("Hourly Rate: P %.2f%n", hourlyRate));
                        payslipText.append(String.format("Total Hours Worked (Regular): %.2f hours%n", totalRegularHours));
                        payslipText.append(String.format("Overtime Hours: %.2f hours%n", overtimeHours));
                        payslipText.append(String.format("Overtime Pay: P %.2f%n", (overtimeHours * hourlyRate * 1.25)));
                        payslipText.append(String.format("Gross Salary: P %.2f%n", grossSalary));
                        payslipText.append("----------------------------------------------------------\n");
                        payslipText.append("Deductions:\n");
                        payslipText.append(String.format("  SSS Contribution: P %.2f%n", sssDeduction));
                        payslipText.append(String.format("  PhilHealth Contribution: P %.2f%n", philhealthDeduction));
                        payslipText.append(String.format("  Pag-IBIG Contribution: P %.2f%n", pagibigDeduction));
                        payslipText.append(String.format("  Withholding Tax: P %.2f%n", taxDeduction));
                        payslipText.append(String.format("Total Deductions: P %.2f%n", totalDeductions));
                        payslipText.append("----------------------------------------------------------\n");
                        payslipText.append(String.format("NET MONTHLY SALARY: P %.2f%n", netSalary));
                        payslipArea.setText(payslipText.toString());

                    } else {
                        payslipArea.setText("Employee with ID " + id + " not found.");
                    }
                } catch (NumberFormatException ex) {
                    payslipArea.setText("Invalid input. Please enter a valid number for Employee ID and Hours Worked.");
                }
            }
        });


        // Buttons for navigation
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton backBtn = createStyledButton("Back to Main Menu");
        backBtn.addActionListener(e -> showPanel("Welcome"));
        buttonPanel.add(backBtn);
        calculatePayPanel.add(buttonPanel, BorderLayout.SOUTH);

        mainPanel.add(calculatePayPanel, "CalculatePay");
    }


    // --- Helper Methods ---

    private void showPanel(String panelName) {
        CardLayout cl = (CardLayout) (mainPanel.getLayout());
        cl.show(mainPanel, panelName);

        // Special actions when showing specific panels
        if (panelName.equals("ViewAll")) {
            populateEmployeeTable(); // Refresh table when viewing all employees
        }
        // Could add reset forms here for "AddEmployee", "ViewSpecific", "CalculatePay" etc.
    }

    private void populateEmployeeTable() {
        tableModel.setRowCount(0); // Clear existing data
        for (Employee employee : employeeManager.getEmployees()) {
            tableModel.addRow(employee.toCsvArray()); // Use the toCsvArray to get all data
        }
    }

    private void clearForm(JTextField... fields) {
        for (JTextField field : fields) {
            field.setText("");
        }
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(new Color(60, 179, 113)); // Medium Sea Green
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(50, 150, 90)); // Darker on hover
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(60, 179, 113)); // Restore original
            }
        });
        return button;
    }

    // --- Main method ---
    public static void main(String[] args) {
        // Ensure the CSV file exists or can be created on startup
        // This implicitly calls EmployeeManager's constructor which loads/creates the file.
        EmployeeManager.getInstance(); // Initialize EmployeeManager early

        SwingUtilities.invokeLater(() -> {
            new PayrollSystemGUI().setVisible(true);
        });
    }
}