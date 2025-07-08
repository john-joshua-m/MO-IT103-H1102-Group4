package com.mycompany.motorphpayrollsystem;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Manages user accounts, including loading, saving, adding, and authenticating users.
 * Implements the Singleton pattern.
 * So this class is to manage, add, save user details into csv file natin
 */
public class UserManager {
    private static UserManager instance; // Singleton instance
    private List<User> users;
    private static final String USERS_FILE = "users.csv"; // CSV file for users

    // Private constructor for Singleton pattern
    private UserManager() {
        users = new ArrayList<>();
        loadUsersFromFile(); // Load users when UserManager is initialized
    }

    // Public static method to get the Singleton instance
    public static synchronized UserManager getInstance() {
        if (instance == null) {
            instance = new UserManager();
        }
        return instance;
    }
    
    public User getUserById(int employeeId) {
        for (User user : users) {
            if (user.getEmployeeId() == employeeId) {
                return user;
            }
        }
        return null;
    }
    
    // Syncs employee data (ID & name) from employee.csv into user.csv 
    public void retrieveDataFromEmployees (List<Employee> employees) {
        for (Employee emp : employees) {
            int empId = emp.getEmployeeId();
            String firstName = emp.getFirstName();
            String lastName = emp.getLastName();
            
            // Check if this employee already exists in users.csv
            boolean exists = false;
            for (User user: users) {
                if (user.getEmployeeId() == empId) {
                    exists = true;
                    break;                    
                }
            }
            
            if (!exists){
                //
                users.add(new User("", "", "", firstName, lastName, empId));
            }        
        }
        
        try {
            saveUsersToFile();
            System.out.println("User.csv successfully retrieves data.");
        } catch (IOException e) {
            Logger.getLogger(UserManager.class.getName()).log(Level.SEVERE, "Error retrieving data from employee.csv file.", e);
        }
    }
    
    /**
     * Loads user data from the CSV file into the 'users' list.
     * This method is now PUBLIC to be callable from outside the class (e.g., LoginGUI).
     */
    public void loadUsersFromFile() {
        users.clear(); // Clear existing list before loading to prevent duplicates on successive loads
        File file = new File(USERS_FILE);
        if (!file.exists()) {
            System.out.println("User CSV file not found. Initializing with default users.");
            try {

                users.add(new User("admin", "adminpass", "IT Admin", "IT Admin", "", 0)); // IT Admin
                users.add(new User("employee1", "emppass", "Employee", "Juan","", 10005)); // Link to Juan (Employee ID 10005)
                users.add(new User("hr.motorph", "hrpass", "HR", "Jane","", 0)); // HR
                users.add(new User("manager.motorph", "managerpass", "Manager", "John","", 0)); // Manager
                users.add(new User("employee2", "emppass2", "Employee", "Manuel","", 10001)); // Link to Manuel (Employee ID 10001)

                saveUsersToFile(); // Save these default users to the new file
                System.out.println("Default users created and saved to " + USERS_FILE);
                return; // Exit after initializing default users
            } catch (IOException e) {
                Logger.getLogger(UserManager.class.getName()).log(Level.SEVERE, "Error creating or saving default users on file not found.", e);
                return; // Return to avoid attempting to read non-existent file
            }
        }

        try (CSVReader reader = new CSVReader(new FileReader(USERS_FILE))) {
            List<String[]> allRows = reader.readAll();
            if (allRows.isEmpty()) {
                System.out.println("User CSV file is empty. No users loaded.");
                return;
            }

            List<String[]> dataRows;
            // Check for a header row, robustly handling quotes and case.
            if (!allRows.isEmpty() && allRows.get(0).length >= 1 && allRows.get(0)[0].trim().replace("\"", "").equalsIgnoreCase("username")) {
                dataRows = allRows.subList(1, allRows.size()); // Skip header
            } else {
                dataRows = allRows; // No header, all rows are data
            }

            for (String[] row : dataRows) {
                User user = User.fromCsvArray(row); // User.fromCsvArray now expects 5 fields
                if (user != null) {
                    boolean duplicateFound = false;
                    for (User existingUser : users) {
                        if (existingUser.getUsername().equalsIgnoreCase(user.getUsername())) {
                            duplicateFound = true;
                            System.err.println("Skipping duplicate user during load: " + user.getUsername());
                            break;
                        }
                    }
                    if (!duplicateFound) {
                         users.add(user);
                    }
                } else {
                    // Log malformed rows for debugging
                    System.err.println("Skipping malformed CSV row (User.fromCsvArray returned null or incorrect fields count/format): " + String.join(",", row));
                }
            }
            System.out.println("Users loaded from " + USERS_FILE + ". Total users: " + users.size());
        } catch (IOException | CsvException e) {
            Logger.getLogger(UserManager.class.getName()).log(Level.SEVERE, "Error loading users from " + USERS_FILE, e);
        }
    }

     //saves user info to users.csv   
    public void saveUsersToFile() throws IOException {
        String tempFile = USERS_FILE + ".tmp";
        try (CSVWriter writer = new CSVWriter(new FileWriter(tempFile))) {
            // Write header (updated to include FirstName and EmployeeId)
            String[] header = {"Username", "Password", "Role", "FirstName", "Last Name", "EmployeeId"};
            writer.writeNext(header);

            // Write user data
            for (User user : users) {
                writer.writeNext(user.toCsvArray()); // User.toCsvArray now returns 5 elements
            }
        }

        File originalFile = new File(USERS_FILE);
        File temp = new File(tempFile);

        if (originalFile.exists() && !originalFile.delete()) {
            String msg = "Failed to delete original user CSV file: " + originalFile.getAbsolutePath();
            System.err.println(msg);
            throw new IOException(msg);
        }

        if (!temp.renameTo(originalFile)) {
            String msg = "Failed to rename temp file '" + temp.getAbsolutePath() + "' to original user CSV file: " + originalFile.getAbsolutePath();
            System.err.println(msg);
            throw new IOException(msg);
        } else {
            System.out.println("Users saved to " + USERS_FILE);
        }
    }

    /**
     * This is our newly added functionality, addUser that is only available for IT admin and HR
     * Please note that yung add user na button na to is different from add employee. 
     * This is for the actual payroll system access account
     */
    public boolean addUser(String username, String password, String role, String firstName, String lastName, int employeeId) throws IOException {
        // Check if username already exists (case-insensitive)
        for (User user : users) {
            if (user.getUsername().equalsIgnoreCase(username)) {
                System.out.println("Error: User with username '" + username + "' already exists.");
                return false;
            }
        }
        User newUser = new User(username, password, role, firstName, lastName, employeeId); // Pass employeeId to User constructor
        users.add(newUser);
        saveUsersToFile(); // Save immediately after adding
        System.out.println("User '" + username + "' added successfully.");
        return true;
    }
    
    public boolean editUser (int employeeId, String newUsername, String newPassword, String newRole, String newFirstName, String newLastName) {
        for (User user : users) {
         if (user.getEmployeeId()== employeeId) {
             user.setUsername(newUsername);
             user.setPassword(newPassword);
             user.setRole(newRole);
             user.setFirstName(newFirstName);
             user.setLastName(newLastName);
             
             try {
                 saveUsersToFile();
                 return true;
             } catch (IOException e) {
                 Logger.getLogger(UserManager.class.getName()).log(Level.SEVERE, null, e);
                 return false;
             }  
         }          
       }      
        return false;        
    }

    /**
     * Authenticates a user based on username and password.
     * This is good but we can consider hashing passwords pa to make it cybersec policies compliant
     */
    public User authenticateUser(String username, String password) {
        for (User user : users) {
            if (user.getUsername().equalsIgnoreCase(username) && user.getPassword().equals(password)) {
                return user; // Authentication successful
            }
        }
        return null; // Authentication failed
    }

 
    public List<User> getUsers() {
        return Collections.unmodifiableList(new ArrayList<>(users)); // Return a copy to prevent external modification
    }
}