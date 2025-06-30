package com.mycompany.motorphpayrollsystem;
import com.mycompany.motorphpayrollsystem.AttendanceRecord;
import com.mycompany.motorphpayrollsystem.AttendanceManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
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
    private JPanel mainPanel;

    private User loggedInUser;
    private JLabel welcomeTitleLabel;
    private JLabel imageLabel;
    private JPanel buttonPanel;
    private JLabel userRoleLabel;

    // UI Components for different views
    private JPanel welcomePanel;
    private JPanel viewAllEmployeesPanel;
    private JPanel viewSpecificEmployeePanel;
    private JPanel addEmployeePanel;
    private JPanel updateEmployeePanel;
    private JPanel calculatePayPanel;
    private JPanel attendancePanel;
    private JTextArea employeeDetailsArea;

    private JPanel addUserPanel; // Panel for adding new users
    private JButton addUserAccountBtn; // Button for adding new users (controlled by RBAC)

    // Declare employee management buttons as instance variables for RBAC
    private JButton addEmployeeBtn;
    private JButton updateEmployeeBtn;
    private JButton deleteBtn;
    
    // Declare button for employee's own attendance
    private JButton recordMyAttendanceBtn;
    // Make the general attendance button an instance variable too
    private JButton attendanceBtn;

    // Declare viewAllBtn as an instance variable for RBAC
    private JButton viewAllBtn;


    // --- Constructor ---
    public PayrollSystemGUI() {
        setTitle("MotorPH Employee Payroll System");
        setSize(1300, 700);
        setResizable(true); // Allow resizing for responsive layout testing
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        employeeManager = EmployeeManager.getInstance();
        // Ensure UserManager is also initialized if it's a Singleton
        UserManager.getInstance(); // Initialize UserManager to load existing users

        mainPanel = new JPanel(new CardLayout());
        add(mainPanel);

        // Initialize and add all the different panels
        setupWelcomePanel();
        setupViewAllEmployeesPanel();
        setupAddEmployeePanel();
        setupAddUserPanel(); 

        this.setVisible(false); // Keep main GUI hidden until login is successful

        SwingUtilities.invokeLater(() -> {
            LoginGUI loginFrame = new LoginGUI(this);
            loginFrame.setVisible(true);
        });
    }

    public void setLoggedInUser(User user) {
        this.loggedInUser = user;
        System.out.println("DEBUG: PayrollSystemGUI - setLoggedInUser called. User object received. User object toString: " + (user != null ? user.getUsername() : "null user object"));
        System.out.println("DEBUG: PayrollSystemGUI - setLoggedInUser. FirstName in User object (at this point): '" + (user != null ? user.getFirstName() : "null user object") + "'");

        this.setVisible(true);

        updateWelcomePanel(); // Call the method to refresh the UI and display the welcome message

        CardLayout cl = (CardLayout) (mainPanel.getLayout());
        cl.show(mainPanel, "Welcome");

        applyRoleBasedAccess(user.getRole()); // Apply permissions based on role
    }

    private void logout() {
        this.loggedInUser = null;
        System.out.println("DEBUG: PayrollSystemGUI - User logged out. Clearing loggedInUser.");

        this.setVisible(false);

        updateWelcomePanel(); // Reset welcome message and role label

        SwingUtilities.invokeLater(() -> {
            LoginGUI loginFrame = new LoginGUI(this);
            loginFrame.setVisible(true);
        });
    }

    // Role-Based Access Control
    private void applyRoleBasedAccess(String role) {
        boolean isAdmin = "IT Admin".equalsIgnoreCase(role); 
        boolean isHR = "HR".equalsIgnoreCase(role);
        boolean isEmployee = "Employee".equalsIgnoreCase(role);
        boolean isManager = "Manager".equalsIgnoreCase(role);

        System.out.println("DEBUG: PayrollSystemGUI - Applying role-based access for role: " + role);

        // Control visibility of the Add User Account button (IT Admin & HR)
        if (addUserAccountBtn != null) {
            addUserAccountBtn.setVisible(isAdmin || isHR); 
        }

        // Control visibility of Employee Management buttons (IT Admin, HR, Manager)
        boolean canManageEmployees = isAdmin || isHR || isManager;

        if (addEmployeeBtn != null) {
            addEmployeeBtn.setVisible(canManageEmployees);
        }
        if (updateEmployeeBtn != null) {
            updateEmployeeBtn.setVisible(canManageEmployees);
        }
        if (deleteBtn != null) {
            deleteBtn.setVisible(canManageEmployees);
        }

        // Control visibility for "Record My Attendance" button (only for Employee role)
        if (recordMyAttendanceBtn != null) {
            recordMyAttendanceBtn.setVisible(isEmployee);
        }

        // Control visibility for the general "Record Attendance" button (for other employees)
        // This button is in the View All Employees panel and allows managing others' attendance
        if (attendanceBtn != null) { 
            attendanceBtn.setVisible(canManageEmployees);
        }

        if (viewAllBtn != null) {
            viewAllBtn.setVisible(!isEmployee); // Visible for all roles EXCEPT Employee
        }
    }


    // --- Panel Setups ---

    private void setupWelcomePanel() {
        welcomePanel = new JPanel();
        welcomePanel.setLayout(new BorderLayout());

        JPanel imagePanel = new JPanel(new BorderLayout());

        ImageIcon icon = new ImageIcon("./resources/MotorPH.png");
        setIconImage(icon.getImage());

        ImageIcon originalIcon = null;
        try {
            originalIcon = new ImageIcon(getClass().getResource("/bg3.png"));
            if (originalIcon.getIconWidth() == -1) {
                originalIcon = new ImageIcon("./resources/bg3.png");
            }
        } catch (Exception e) {
            originalIcon = new ImageIcon("./resources/bg3.png");
        }

        if (originalIcon.getIconWidth() != -1) {
            Image image = originalIcon.getImage();

            imagePanel.addComponentListener(new ComponentAdapter() {
                @Override
                public void componentResized(ComponentEvent e) {
                    JPanel sourcePanel = (JPanel)e.getSource();
                    int newWidth = sourcePanel.getWidth();
                    int newHeight = sourcePanel.getHeight();
                    if (newWidth > 0 && newHeight > 0 && image != null) {
                        Image scaledImage = image.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
                        if (imageLabel != null) {
                            imageLabel.setIcon(new ImageIcon(scaledImage));
                            imageLabel.revalidate();
                            imageLabel.repaint();
                        }
                    }
                }
            });

            Image scaledImageInitial = originalIcon.getImage().getScaledInstance(1300, 700, Image.SCALE_SMOOTH);
            imageLabel = new JLabel(new ImageIcon(scaledImageInitial));
            imagePanel.add(imageLabel, BorderLayout.CENTER);
            imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        } else {
            imageLabel = new JLabel("MotorPH Logo (Image not found)", SwingConstants.CENTER);
            imagePanel.add(imageLabel, BorderLayout.CENTER);
        }
        imageLabel.setLayout(null);


        // Welcome Title Label
        welcomeTitleLabel = new JLabel("<html>WELCOME to<br>MotorPH Payroll System</html>", SwingConstants.LEFT);
        welcomeTitleLabel.setBounds(70, 210, 650, 100);
        welcomeTitleLabel.setFont(new Font("Arial", Font.BOLD, 40));
        welcomeTitleLabel.setForeground(Color.WHITE);
        imageLabel.add(welcomeTitleLabel);

        // User Role / Slogan Label
        userRoleLabel = new JLabel("Please select an option to get started.", SwingConstants.LEFT);
        userRoleLabel.setBounds(70, 320, 600, 35);
        userRoleLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        userRoleLabel.setForeground(Color.WHITE);
        imageLabel.add(userRoleLabel);


        // Header Panel
        ImageIcon headerIcon = new ImageIcon("./resources/MotorPH.png");
        Image headerLogo = headerIcon.getImage();
        Image scaledIcon = headerLogo.getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        headerIcon = new ImageIcon(scaledIcon);

        JLabel label = new JLabel(headerIcon, SwingConstants.LEFT);

        JLabel titleLabel = new JLabel("MOTORPH PAYROLL SYSTEM", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(new Color(0, 0, 128));

        JPanel whitePanel = new JPanel();
        whitePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 5));
        whitePanel.setBackground(Color.WHITE);
        whitePanel.add(label);
        whitePanel.add(titleLabel);

        // Footer Panel
        JPanel redPanel = new JPanel();
        redPanel.setLayout(null);
        redPanel.setBackground(new Color(185, 0, 0));
        redPanel.setPreferredSize(new Dimension(1000, 40));

        JLabel footerLabel = new JLabel("© 2025 MotorPH Payroll", SwingConstants.LEFT);
        footerLabel.setForeground(Color.WHITE);
        footerLabel.setBounds(10, 0, 600, 30);
        footerLabel.setFont(new Font("Arial",Font.BOLD, 10));
        redPanel.add(footerLabel);


        welcomePanel.add(imagePanel, BorderLayout.CENTER);
        welcomePanel.add(whitePanel, BorderLayout.NORTH);
        welcomePanel.add(redPanel, BorderLayout.SOUTH);

        // --- BUTTON PANEL SETUP WITH RESPONSIVE POSITIONING ---
        buttonPanel = new JPanel();
        // Now 8 rows for 8 buttons
        buttonPanel.setLayout(new GridLayout(8, 1, 10, 10)); 
        buttonPanel.setOpaque(false);
        buttonPanel.setPreferredSize(new Dimension(280, 400)); // Adjusted height for 8 buttons

        imageLabel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int rightMargin = 50;
                int buttonPanelWidth = buttonPanel.getPreferredSize().width;
                int buttonPanelHeight = buttonPanel.getPreferredSize().height;
                int newX = imageLabel.getWidth() - buttonPanelWidth - rightMargin;
                int newY = 150;
                if (newX < 0) newX = 0;
                buttonPanel.setBounds(newX, newY, buttonPanelWidth, buttonPanelHeight);
                buttonPanel.revalidate();
                buttonPanel.repaint();
            }
        });

        // Create the buttons and assign them to instance variables
        viewAllBtn = createStyledButton("View All Employees"); // Assigned to instance variable
        addEmployeeBtn = createStyledButton("Add New Employee"); // Assigned to instance variable
        updateEmployeeBtn = createStyledButton("Update Employee"); // Assigned to instance variable
        deleteBtn = createStyledButton("Delete Employee"); // Assigned to instance variable
        addUserAccountBtn = createStyledButton("Add User Account"); // Declared as class field
        recordMyAttendanceBtn = createStyledButton("Record My Attendance"); // NEW Button
        JButton logoutBtn = createStyledButton("Logout");
        JButton exitBtn = createStyledButton("Exit");


        // Add ActionListeners
        viewAllBtn.addActionListener(e -> showPanel("ViewAll"));
        addEmployeeBtn.addActionListener(e -> showPanel("AddEmployee"));
        updateEmployeeBtn.addActionListener (e -> {
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
        deleteBtn.addActionListener(e -> {
            try {
                delete();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Error deleting employee: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        addUserAccountBtn.addActionListener(e -> showPanel("AddUser"));
        
        // NEW Action Listener for "Record My Attendance"
        recordMyAttendanceBtn.addActionListener(e -> {
            if (loggedInUser != null && "Employee".equalsIgnoreCase(loggedInUser.getRole())) {
                // Directly use the employeeId from the loggedInUser object
                int empId = loggedInUser.getEmployeeId(); 
                if (empId > 0) { // Check if a valid employeeId is linked
                    Employee employee = employeeManager.getEmployeeById(empId);
                    if (employee != null) { 
                         setupAttendancePanel(empId, employee.getFirstName(), employee.getLastName());
                         showPanel("Attendance Record");
                    } else {
                        // This case implies an employeeId is linked to the user account
                        // but no matching employee record is found in employees.csv
                        JOptionPane.showMessageDialog(this, "Your linked Employee ID (" + empId + ") was not found in the employee records. Please check your user account configuration.", "Data Mismatch Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                     JOptionPane.showMessageDialog(this, "Your user account is not linked to an Employee ID. Please contact HR/IT Admin to link your account to an employee record.", "Missing Employee ID", JOptionPane.WARNING_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "This function is only for Employees.", "Access Denied", JOptionPane.WARNING_MESSAGE);
            }
        });

        logoutBtn.addActionListener(e -> logout());
        exitBtn.addActionListener(e -> System.exit(0));

        // Add buttons to the button panel
        buttonPanel.add(viewAllBtn);
        buttonPanel.add(addEmployeeBtn);
        buttonPanel.add(updateEmployeeBtn);
        buttonPanel.add(deleteBtn);
        buttonPanel.add(recordMyAttendanceBtn); 
        buttonPanel.add(addUserAccountBtn);
        buttonPanel.add(logoutBtn);
        buttonPanel.add(exitBtn);

        imageLabel.add(buttonPanel); // Place buttons directly on top of the background image

        mainPanel.add(welcomePanel, "Welcome");
    }

    private void updateWelcomePanel() {
        if (loggedInUser != null) {
            if (welcomeTitleLabel != null) {
                welcomeTitleLabel.setText("<html>Welcome, " + loggedInUser.getFirstName() + "!</html>");
                System.out.println("DEBUG: PayrollSystemGUI - updateWelcomePanel. User is NOT null. Displaying firstName: '" + loggedInUser.getFirstName() + "' and role: '" + loggedInUser.getRole() + "'");
            } else {
                System.err.println("ERROR: PayrollSystemGUI - welcomeTitleLabel is null! Cannot update welcome message.");
                setTitle("MotorPH Employee Payroll System - Logged in as: " + loggedInUser.getUsername() + " (" + loggedInUser.getRole() + ")");
            }

            if (userRoleLabel != null) {
                userRoleLabel.setText("(" + loggedInUser.getRole() + ")");
            } else {
                System.err.println("ERROR: PayrollSystemGUI - userRoleLabel is null! Cannot update user role.");
            }

        } else {
            if (welcomeTitleLabel != null) {
                 welcomeTitleLabel.setText("<html>WELCOME to<br>MotorPH Payroll System</html>");
            } else {
                System.err.println("ERROR: PayrollSystemGUI - welcomeTitleLabel is null for generic welcome!");
            }

            if (userRoleLabel != null) {
                userRoleLabel.setText("Please select an option to get started.");
            } else {
                System.err.println("ERROR: PayrollSystemGUI - userRoleLabel is null for slogan!");
            }
            System.out.println("DEBUG: PayrollSystemGUI - updateWelcomePanel. loggedInUser is null, displaying generic welcome.");
        }
    }


    private void setupViewAllEmployeesPanel() {
        viewAllEmployeesPanel = new JPanel(new BorderLayout(10, 10));
        viewAllEmployeesPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        viewAllEmployeesPanel.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel("All Employee Records", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(0, 51, 102));
        viewAllEmployeesPanel.add(titleLabel, BorderLayout.NORTH);

        String[] columnNames = {"Employee ID", "First Name", "Last Name", "Birthday", "Position",
                                 "Hourly Rate", "Monthly Salary", "SSS No", "PhilHealth No", "TIN", "Pag-IBIG No"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        employeeTable = new JTable(tableModel);
        employeeTable.setFont(new Font("Arial", Font.PLAIN, 12));
        employeeTable.setRowHeight(25);
        employeeTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        employeeTable.getTableHeader().setBackground(new Color(173, 216, 230));
        employeeTable.getTableHeader().setReorderingAllowed(false);
        employeeTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(employeeTable);
        viewAllEmployeesPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton refreshBtn = createStyledButton2("Refresh Records");
        JButton backBtn = createStyledButton2("Back to Main Menu");
        JButton viewEmployee = createStyledButton2("View Employee");
        JButton updateEmployee = createStyledButton2("Update Details");
        
        // Assign to instance variable so applyRoleBasedAccess can control it
        attendanceBtn = createStyledButton2("Record Attendance"); 

        refreshBtn.addActionListener(e -> populateEmployeeTable());
        backBtn.addActionListener(e -> showPanel("Welcome"));
        buttonPanel.add(refreshBtn);
        buttonPanel.add(backBtn);
        buttonPanel.add(viewEmployee);
        buttonPanel.add(updateEmployee);
        buttonPanel.add(attendanceBtn); // Add the general attendance button

         viewEmployee.addActionListener(e -> {
            int selectedRow = employeeTable.getSelectedRow();
            if (selectedRow != -1) {
                int empId = Integer.parseInt(employeeTable.getValueAt(selectedRow, 0).toString());
                Employee selectedEmp = employeeManager.getEmployeeById(empId);
                if (selectedEmp != null) {
                    List<AttendanceRecord> allRecords = AttendanceManager.getInstance().getAllAttendanceRecords();
                    List<AttendanceRecord> employeeRecords = allRecords.stream()
                           .filter(r -> r.getEmployeeId() == selectedEmp.getEmployeeId())
                           .toList();

                    setupViewSpecificEmployeePanel(selectedEmp, employeeRecords);
                    showPanel("ViewSpecific");
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

        attendanceBtn.addActionListener(e -> { // This is the general attendance button
           int selectedRow = employeeTable.getSelectedRow();
           if (selectedRow != -1) {
               int empId = Integer.parseInt(employeeTable.getValueAt (selectedRow, 0). toString());
               Employee selectedEmp = employeeManager.getEmployeeById(empId);
               String firstName = employeeTable.getValueAt(selectedRow, 1).toString();
               String lastName = employeeTable.getValueAt(selectedRow, 2).toString();

               if (selectedEmp != null) {
                   setupAttendancePanel(empId,firstName, lastName);
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

    private void setupViewSpecificEmployeePanel(Employee employee, List<AttendanceRecord> attendanceRecords) {


             viewSpecificEmployeePanel = new JPanel(new BorderLayout(10, 10));
             viewSpecificEmployeePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
             viewSpecificEmployeePanel.setBackground(Color.WHITE);

             JLabel titleLabel = new JLabel("Employee Details", SwingConstants.CENTER);
             titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
             titleLabel.setForeground(new Color(0, 51, 102));
             viewSpecificEmployeePanel.add(titleLabel, BorderLayout.NORTH);

             employeeDetailsArea = new JTextArea(15, 40);
             employeeDetailsArea.setFont(new Font("Arial", Font.PLAIN, 14));
             employeeDetailsArea.setEditable(false);
             employeeDetailsArea.setLineWrap(true);
             employeeDetailsArea.setWrapStyleWord(true);
             JScrollPane detailsScroll = new JScrollPane(employeeDetailsArea);

             displayEmployeeDetails(employee);

             JPanel payCalculatorPanel = new JPanel(new BorderLayout(10, 10));
             payCalculatorPanel.setBackground(new Color(240, 248, 255));

             JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
             inputPanel.setBackground(new Color(240, 248, 255));

             JLabel startDate = new JLabel("Enter Start Date and End Date of Payroll Period (MM/dd/yyyy) :");
             JTextField startDateField = new JTextField(10);
             startDateField.setToolTipText("Enter start date");

             JLabel endDate = new JLabel();
             JTextField endDateField = new JTextField(10);
             endDateField.setToolTipText("Enter end date");

             JButton calculateBtn = createStyledButton2("Calculate Pay");

             inputPanel.add(startDate);
             inputPanel.add(endDate);
             inputPanel.add(startDateField);
             inputPanel.add(endDateField);
             inputPanel.add(calculateBtn);

             payCalculatorPanel.add(inputPanel, BorderLayout.NORTH);

             JTextArea payslipArea = new JTextArea(20, 40);
             payslipArea.setEditable(false);
             payslipArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
             payslipArea.setBorder(BorderFactory.createEtchedBorder());
             JScrollPane payslipScroll = new JScrollPane(payslipArea);
             payCalculatorPanel.add(payslipScroll, BorderLayout.CENTER);

             calculateBtn.addActionListener(e -> {
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
                     payslipArea.setText("Please use MM/dd/yyyy format properly for Start Date");
                     return;
                 }

                 try {
                     end = LocalDate.parse(endDateInput, formatter);
                 } catch (DateTimeParseException dtpe) {
                    Logger.getLogger(PayrollSystemGUI.class.getName()).log(Level.SEVERE, "Invalid date format for End Date: " + endDateInput, dtpe);
                    payslipArea.setText("Invalid date format for End Date. Please use MM/dd/yyyy.");
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
                             .toList();

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

    private void setupAddEmployeePanel() {
        addEmployeePanel = new JPanel(new BorderLayout(10, 10));
        addEmployeePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        addEmployeePanel.setBackground(new Color(240, 248, 255));

        JLabel titleLabel = new JLabel("Add New Employee Record", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(0, 51, 102));
        addEmployeePanel.add(titleLabel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));
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
                    }

                    if (!lastName.matches("[a-zA-Z ]+")) {
                        JOptionPane.showMessageDialog(addEmployeePanel, "Last Name should only contain alphabets and spaces.", "Input Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

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
                    }

                    if (!position.matches("[a-zA-Z ]+")) {
                        JOptionPane.showMessageDialog(addEmployeePanel, "Position should only contain alphabets and spaces.", "Input Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    if (!sssNo.matches("\\d+")) {
                        JOptionPane.showMessageDialog(addEmployeePanel, "SSS Number should only contain digits.", "Input Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    if (!philhealthNo.matches("\\d+")) {
                        JOptionPane.showMessageDialog(addEmployeePanel, "PhilHealth Number should only contain digits.", "Input Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }


                    if (!tin.matches("\\d+")) {
                        JOptionPane.showMessageDialog(addEmployeePanel, "TIN should only contain digits.", "Input Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }


                    if (!pagibigNo.matches("\\d+")) {
                        JOptionPane.showMessageDialog(addEmployeePanel, "Pag-IBIG Number should only contain digits.", "Input Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    if (!sssNo.matches("\\d{10}")) {
                        JOptionPane.showMessageDialog(addEmployeePanel, "SSS Number must be exactly 10 digits.", "Input Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    if (!philhealthNo.matches("\\d{12}")) {
                        JOptionPane.showMessageDialog(addEmployeePanel, "PhilHealth Number must be exactly 12 digits.", "Input Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }


                    if (!tin.matches("\\d{9}")) {
                        JOptionPane.showMessageDialog(addEmployeePanel, "TIN must be exactly 9 digits.", "Input Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }


                    if (!pagibigNo.matches("\\d{12}")) {
                        JOptionPane.showMessageDialog(addEmployeePanel, "Pag-IBIG Number must be exactly 12 digits.", "Input Error", JOptionPane.ERROR_MESSAGE);
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

    private void setupUpdateEmployeePanel (Employee employeeToEdit) {
       updateEmployeePanel = new JPanel(new BorderLayout(10, 10));
       updateEmployeePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
       updateEmployeePanel.setBackground(new Color(240,248,255));

        JLabel  titleLabel = new JLabel("Update Existing Employee Record", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Ariel", Font.BOLD, 24));
        titleLabel.setForeground(new Color(0, 51, 102));
        updateEmployeePanel.add(titleLabel, BorderLayout.NORTH);

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


    private void setupAddUserPanel() {
        addUserPanel = new JPanel(new BorderLayout(10, 10));
        addUserPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        addUserPanel.setBackground(new Color(240, 248, 255));

        JLabel titleLabel = new JLabel("Add New User Account", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(0, 51, 102));
        addUserPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10)); // Layout for input fields
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        formPanel.setBackground(new Color(240, 248, 255));

        JTextField usernameField = new JTextField(15);
        JPasswordField passwordField = new JPasswordField(15);
        JTextField firstNameField = new JTextField(15); // For associating a first name with the user account
        JTextField employeeIdField = new JTextField(15); // Important, para ma record ni employee on his/her own yung attendance. Employee field

        // Dropdown for Role Selection
        String[] roles = {"Employee", "Manager", "HR", "IT Admin"}; 
        JComboBox<String> roleComboBox = new JComboBox<>(roles);
        roleComboBox.setSelectedIndex(0); // Default to "Employee"

        // Listener to enable/disable employeeIdField based on selected role
        roleComboBox.addActionListener(e -> {
            String selectedRole = (String) roleComboBox.getSelectedItem();
            employeeIdField.setEnabled("Employee".equalsIgnoreCase(selectedRole));
            if (!employeeIdField.isEnabled()) {
                employeeIdField.setText("0"); // Set to 0 if not an employee
            } else {
                employeeIdField.setText(""); // Clear if it's an employee
            }
        });

        formPanel.add(new JLabel("Username:"));
        formPanel.add(usernameField);
        formPanel.add(new JLabel("Password:"));
        formPanel.add(passwordField);
        formPanel.add(new JLabel("First Name (for welcome):")); //Display name to ng user
        formPanel.add(firstNameField);
        formPanel.add(new JLabel("Role:"));
        formPanel.add(roleComboBox);
        formPanel.add(new JLabel("Employee ID (for Employee role):")); //Important to, since this is needed if may add si employee ng hours on his/her own
        formPanel.add(employeeIdField); //

        // Initialize employeeIdField state based on default role
        employeeIdField.setEnabled("Employee".equalsIgnoreCase((String) roleComboBox.getSelectedItem()));
        if (!employeeIdField.isEnabled()) {
            employeeIdField.setText("0"); // Default for non-employees
        }


        addUserPanel.add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(new Color(240, 248, 255));
        JButton saveUserBtn = createStyledButton2("Save User Account");
        JButton clearUserFormBtn = createStyledButton2("Clear Form");
        JButton backBtn = createStyledButton2("Back to Main Menu");

        saveUserBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText().trim();
                String password = new String(passwordField.getPassword()).trim();
                String firstName = firstNameField.getText().trim();
                String role = (String) roleComboBox.getSelectedItem();
                String employeeIdText = employeeIdField.getText().trim(); // Get employee ID text

                // Input validation for user account creation
                if (username.isEmpty() || password.isEmpty() || firstName.isEmpty() || role.isEmpty()) {
                    JOptionPane.showMessageDialog(addUserPanel, "All fields are required!", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (!username.matches("^[a-zA-Z0-9._-]+$")) {
                    JOptionPane.showMessageDialog(addUserPanel, "Username can only contain letters, numbers, dots, underscores, and hyphens.", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (password.length() < 6) {
                    JOptionPane.showMessageDialog(addUserPanel, "Password must be at least 6 characters long.", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                 if (!firstName.matches("[a-zA-Z ]+")) {
                    JOptionPane.showMessageDialog(addUserPanel, "First Name should only contain alphabets and spaces.", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int employeeId = 0; // Default employeeId
                if ("Employee".equalsIgnoreCase(role)) {
                    if (employeeIdText.isEmpty()) {
                        JOptionPane.showMessageDialog(addUserPanel, "Employee ID is required for Employee roles.", "Input Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    try {
                        employeeId = Integer.parseInt(employeeIdText);
                        if (employeeId <= 0) {
                            JOptionPane.showMessageDialog(addUserPanel, "Employee ID must be a positive number.", "Input Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }

                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(addUserPanel, "Invalid Employee ID format. Please enter a number.", "Input Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }


                try {
                    boolean success = UserManager.getInstance().addUser(username, password, role, firstName, employeeId); // Pass employeeId
                    if (success) {
                        JOptionPane.showMessageDialog(addUserPanel, "User account for '" + username + "' created successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                        // Clear fields after successful addition
                        usernameField.setText("");
                        passwordField.setText("");
                        firstNameField.setText("");
                        employeeIdField.setText("0"); // Reset employeeId field
                        employeeIdField.setEnabled(false); // Disable again
                        roleComboBox.setSelectedIndex(0); // Reset to default role
                    } else {
                        JOptionPane.showMessageDialog(addUserPanel, "Failed to add user account. Username might already exist.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (IOException ex) {
                    Logger.getLogger(PayrollSystemGUI.class.getName()).log(Level.SEVERE, "Error adding user account", ex);
                    JOptionPane.showMessageDialog(addUserPanel, "An error occurred while saving the user account: " + ex.getMessage(), "File Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        clearUserFormBtn.addActionListener(e -> {
            usernameField.setText("");
            passwordField.setText("");
            firstNameField.setText("");
            employeeIdField.setText("0"); // Clear employeeId field
            employeeIdField.setEnabled(false); // Disable again
            roleComboBox.setSelectedIndex(0);
        });
        backBtn.addActionListener(e -> showPanel("Welcome"));

        buttonPanel.add(saveUserBtn);
        buttonPanel.add(clearUserFormBtn);
        buttonPanel.add(backBtn);
        addUserPanel.add(buttonPanel, BorderLayout.SOUTH);

        mainPanel.add(addUserPanel, "AddUser"); // Add the new panel to the CardLayout
    }


    //Panel for recording time in and time out
    private void setupAttendancePanel(int employeeId, String firstName, String lastName) {
             attendancePanel = new JPanel (new BorderLayout());

             //Formatters for parsing date and time
             DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
             DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("h:mm a"); //dapat 12-hour format

             JLabel titleLabel = new JLabel("Record Attendance for " + firstName + " " + lastName, SwingConstants.CENTER);
             titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
             attendancePanel.add(titleLabel, BorderLayout.NORTH);

             // Form Panel
             JPanel formPanel = new JPanel(new GridLayout(10, 2, 10, 10));
             formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

             JTextField dateField = new JTextField(); 
             dateField.setPreferredSize(new Dimension(100, 20));
             JTextField timeInField = new JTextField(); 
             timeInField.setPreferredSize(new Dimension(100, 20));
             JTextField timeOutField = new JTextField();
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
