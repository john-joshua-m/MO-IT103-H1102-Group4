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
    private int employeeId; 


    public User(String username, String password, String role, String firstName, int employeeId) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.firstName = firstName;
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

    public int getEmployeeId() { // NEW: Getter for employeeId
        return employeeId;
    }

    /**
     * This creates a User object from the CSV.
     * So lahat ng stored payroll access accounts are also saved sa users.csv na nasa main folder
     */
    public static User fromCsvArray(String[] csvArray) {
        if (csvArray == null || csvArray.length != 5) { // Updated to expect 5 fields
            System.err.println("Malformed CSV array for User: Expected 5 fields, found " + (csvArray != null ? csvArray.length : "null"));
            return null;
        }
        try {
            String username = csvArray[0].trim().replace("\"", ""); // Remove potential quotes
            String password = csvArray[1].trim().replace("\"", "");
            String role = csvArray[2].trim().replace("\"", "");
            String firstName = csvArray[3].trim().replace("\"", "");
            
            int employeeId = 0; // Default to 0 for non-employee users
            String employeeIdStr = csvArray[4].trim().replace("\"", "");
            if (employeeIdStr.matches("\\d+")) { // Check if it's a valid number
                employeeId = Integer.parseInt(employeeIdStr);
            } else {
                System.err.println("Warning: Invalid employeeId format in CSV for user " + username + ": '" + employeeIdStr + "'. Defaulting to 0.");
            }

            return new User(username, password, role, firstName, employeeId);
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
            String.valueOf(employeeId) // Convert int to String for CSV
        };
    }
}
