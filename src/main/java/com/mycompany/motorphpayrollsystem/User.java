package com.mycompany.motorphpayrollsystem;

/**
This is our User class, this is for the actual MotorPH system accounts that can only be created by
* HR and IT Admin only (from mga user types na dineclare natin)
 */
public class User {
    private String username;
    private String password;
    private String role;
    private String firstName;
    private String lastName;
    private int employeeId; 


    public User(String username, String password, String role, String firstName, String lastName, int employeeId) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.firstName = firstName;
        this.lastName = lastName;
        this.employeeId = employeeId; // Initialize new field
    }

    // Getters
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    public String getFirstName() {
        return firstName;
    }
    
    public String getLastName() {
        return lastName;
    }

    public int getEmployeeId() { // NEW: Getter for employeeId
        return employeeId;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public void setRole(String role){
        this.role = role;
    }
    
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }
    

    /**
     * This creates a User object from the CSV.
     * So lahat ng stored payroll access accounts are also saved sa users.csv na nasa main folder
     */
    public static User fromCsvArray(String[] csvArray) {
        if (csvArray == null || csvArray.length != 6) { // Updated to expect 5 fields
            System.err.println("Malformed CSV array for User: Expected 6 fields, found " + (csvArray != null ? csvArray.length : "null"));
            return null;
        }
        try {
            String username = csvArray[0].trim();
            String password = csvArray[1].trim();
            String role = csvArray[2].trim();
            String firstName = csvArray[3].trim();
            String lastName = csvArray[4].trim();
            int employeeId = Integer.parseInt(csvArray[5].trim());                    
                       

            return new User(username, password, role, firstName, lastName, employeeId);
        } catch (NumberFormatException e) {
            System.err.println("Error parsing employeeId from CSV for row: " + String.join(",", csvArray) + " - " + e.getMessage());
            return null; // Return null if employeeId cannot be parsed
        }
    }

    /**
     * Converts the User object to a String array for CSV writing.
     * Ang order nito sa csv is username,password,role,display name sa system,then yung employeeId
     */
    public String[] toCsvArray() {
        return new String[]{
                    
            username,
            password,
            role,
            firstName,
            lastName,
            String.valueOf(employeeId) // Convert int to String for CSV     
            
        };
    }    
}
