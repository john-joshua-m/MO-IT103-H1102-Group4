
package com.mycompany.motorphpayrollsystem;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;


public class ViewProfile extends JFrame {
    
    private EmployeeManager employeeManager;
    private Employee employee;
    private User loggedInUser;
    private JTextArea employeeDetailsArea;
          
    public ViewProfile(User user, EmployeeManager employeeManager) {
   
        this.loggedInUser = user;
        this.employeeManager = employeeManager;
        this.employee = employeeManager.getEmployeeById(loggedInUser.getEmployeeId());
        
        setTitle("Profile: " + employee.getFirstName() + " " + employee.getLastName());
        setLayout(new BorderLayout());
        
        JLabel titleLabel = new JLabel("View Profile", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(0, 51, 102));
        add(titleLabel, BorderLayout.NORTH);

        employeeDetailsArea = new JTextArea(15, 40);
        employeeDetailsArea.setFont(new Font("Arial", Font.PLAIN, 14));
        employeeDetailsArea.setEditable(false);
        employeeDetailsArea.setLineWrap(true);
        employeeDetailsArea.setWrapStyleWord(true);
        JScrollPane detailsScroll = new JScrollPane(employeeDetailsArea);
        add(detailsScroll, BorderLayout.CENTER);
        
        // Display the details of the employee that logged in
        Employee employee = employeeManager.getEmployeeById(loggedInUser.getEmployeeId());
        if (employee != null){
            displayEmployeeDetails(employee);
        } else {
            employeeDetailsArea.setText("Records not found.");
        }
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton backBtn = new JButton("Back to Main Menu");

        backBtn.addActionListener(e -> {
            dispose();
        });
        
        buttonPanel.add(backBtn);
        add(buttonPanel, BorderLayout.SOUTH);
        
        setSize(1300, 700);
        setResizable(true); // Allow resizing for responsive layout testing
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 
        setLocationRelativeTo(null);
        setVisible(true);
        
    }   
    
    private void displayEmployeeDetails(Employee employee) {
        StringBuilder details = new StringBuilder();
        details.append(String.format("Employee ID: %d%n", loggedInUser.getEmployeeId()));
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

    
    
}
