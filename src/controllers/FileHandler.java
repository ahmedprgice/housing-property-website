package controllers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import models.Property;
import models.Transaction;
import models.User;

public class FileHandler {

    private static final String USER_FILE_PATH = "users.txt";
    private static final String FILE_PATH = "properties.csv";
    private static final String TRANSACTION_FILE_PATH = "transactions.txt";

    private static final DateTimeFormatter[] DATE_FORMATTERS = {
        DateTimeFormatter.ofPattern("d/M/yyyy"),
        DateTimeFormatter.ofPattern("dd/MM/yyyy"),
        DateTimeFormatter.ISO_LOCAL_DATE
    };

    // Singleton instance
    private static volatile FileHandler instance;

    public void saveUser(User user) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(USER_FILE_PATH, true))) {
            String line = String.join(",",
                    user.getUsername(),
                    user.getPassword(),
                    user.getEmail(),
                    user.getRole().name() // Save role as a string
            );
            bw.write(line);
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    // Method to load all users from file
    public List<User> loadUsers() {
        List<User> users = new ArrayList<>();
        File file = new File(USER_FILE_PATH);
        if (file.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(USER_FILE_PATH))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] details = line.split(",");
                    if (details.length == 4) {
                        try {
                            User.Role role = User.Role.valueOf(details[3].toUpperCase().trim());
                            User user = new User(details[0].trim(), details[1].trim(), details[2].trim(), role);
                            users.add(user);
                            System.out.println("Loaded user: " + user.getUsername() + ", Role: " + user.getRole()); // Debug
                        } catch (IllegalArgumentException e) {
                            System.err.println("Invalid role for user: " + details[0] + ". Skipping user.");
                        }
                    } else {
                        System.err.println("Incorrect number of fields for user: " + line + ". Skipping user.");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return users;
    }

    // Method to authenticate user login
    public User authenticateUser(String username, String password) {
        List<User> users = loadUsers();
        System.out.println("Attempting to authenticate: " + username + " / " + password); // Debug

        for (User user : users) {
            System.out.println("Checking user: " + user.getUsername() + " / " + user.getPassword()); // Debug
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                System.out.println("Authenticated: " + username); // Debug
                return user;
            }
        }
        System.out.println("Authentication failed for: " + username); // Debug
        return null; // Return null if no match found
    }

    // Private constructor to prevent instantiation
    private FileHandler() {
        // Optional: Initialize files if they don't exist
        initializeFile(FILE_PATH);
        initializeFile(TRANSACTION_FILE_PATH);
    }

    // Static method to provide access to the single instance
    public static FileHandler getInstance() {
        if (instance == null) {
            synchronized (FileHandler.class) {
                if (instance == null) {
                    instance = new FileHandler();
                }
            }
        }
        return instance;
    }

    // Read properties from the file
    public List<Property> readProperties() {
        List<Property> properties = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            br.readLine(); // Skip the header
            while ((line = br.readLine()) != null) {
                String[] details = line.split(","); // Assuming the data is comma-separated

                if (details.length < 9) {
                    System.out.println("Skipping improperly formatted line: " + line);
                    continue; // Skip this line
                }

                try {
                    int sizeSqM = Integer.parseInt(details[0].trim());
                    int sqFt = Integer.parseInt(details[1].trim());
                    String propertyType = details[2].trim();
                    int noOfFloors = Integer.parseInt(details[3].trim());
                    String address = details[4].trim();
                    String scheme = details[5].trim();
                    double price = Double.parseDouble(details[6].trim());
                    int year = Integer.parseInt(details[7].trim());
                    double pricePerSqft = Double.parseDouble(details[8].trim());

                    // Create Property using Builder
                    Property property = new Property.Builder()
                            .setSizeSqM(sizeSqM)
                            .setSqFt(sqFt)
                            .setPropertyType(propertyType)
                            .setNoOfFloors(noOfFloors)
                            .setAddress(address)
                            .setScheme(scheme)
                            .setPrice(price)
                            .setYear(year)
                            .setPricePerSqft(pricePerSqft)
                            .build();
                    properties.add(property);
                } catch (NumberFormatException e) {
                    System.out.println("Error parsing line: " + line + ". Error: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }

    // Write a property to the file
    public void writeProperty(Property property) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH, true))) { // 'true' to enable append mode
            String line = String.join(",",
                    String.valueOf(property.getSizeSqM()),
                    String.valueOf(property.getSqFt()),
                    property.getPropertyType(),
                    String.valueOf(property.getNoOfFloors()),
                    property.getAddress(),
                    property.getScheme(),
                    String.valueOf(property.getPrice()),
                    String.valueOf(property.getYear()),
                    String.valueOf(property.getPricePerSqft())
            );
            bw.write(line);
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Initialize the file with a header if not present
    public void initializeFile(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
                if (filePath.equals(FILE_PATH)) {
                    // Writing header for properties file
                    bw.write("SizeSqM,SqFt,PropertyType,NoOfFloors,Address,Scheme,Price,Year,PricePerSqft");
                } else if (filePath.equals(TRANSACTION_FILE_PATH)) {
                    // Writing header for transactions file
                    bw.write("Date\tSizeSqM\tSizeSqFt\tPropertyType\tNoOfFloors\tAddress\tScheme\tPrice\tYear\tProjectName\tPricePerSqft");
                }
                bw.newLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Read transactions from the file
    public List<Transaction> loadTransactions() {
        List<Transaction> transactions = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(TRANSACTION_FILE_PATH))) {
            skipHeader(reader);
            String line;
            while ((line = reader.readLine()) != null) {
                Transaction transaction = parseTransaction(line);
                if (transaction != null) {
                    transactions.add(transaction);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading transactions from file: " + e.getMessage());
        }

        // Sort transactions by date
        transactions.sort(Comparator.comparing(Transaction::getTransactionDate, Comparator.nullsLast(Comparator.naturalOrder())));
        return transactions;
    }

    private LocalDate parseTransactionDate(String dateStr) {
        for (DateTimeFormatter formatter : DATE_FORMATTERS) {
            try {
                return LocalDate.parse(dateStr, formatter);
            } catch (DateTimeParseException e) {
                // Continue trying the next format
            }
        }
        throw new DateTimeParseException("Date format is invalid: " + dateStr, dateStr, 0);
    }

    private Transaction parseTransaction(String line) {
        try {
            String[] fields = line.split("\t");
            if (fields.length < 10) {
                System.err.println("Skipping improperly formatted transaction line: " + line);
                return null;
            }

            String dateStr = fields[0].trim();
            String address = fields[5].trim();
            int sizeSqFt = Integer.parseInt(fields[2].trim());
            String projectName = fields[6].trim();
            double price = Double.parseDouble(fields[7].trim());

            LocalDate transactionDate = parseTransactionDate(dateStr);

            // Use the Builder to create a Transaction instance
            return new Transaction.Builder()
                    .setProjectName(projectName)
                    .setTransactionDate(transactionDate)
                    .setTransactionPrice(price)
                    .setTransactionSqFt(sizeSqFt)
                    .setSizeSqM(0) // Set this to an appropriate value if needed
                    .setPropertyType("") // Set to an appropriate value if needed
                    .setNoOfFloors(0) // Set to an appropriate value if needed
                    .setAddress(address)
                    .setScheme("") // Set to an appropriate value if needed
                    .setYear(0) // Set to an appropriate value if needed
                    .setPricePerSqft(0.0) // Set to an appropriate value if needed
                    .build();

        } catch (NumberFormatException e) {
            System.err.println("Error parsing transaction from line: " + line + " - " + e.getMessage());
            return null;
        }
    }

    // Helper method to skip the header line in a file
    private void skipHeader(BufferedReader reader) throws IOException {
        reader.readLine(); // Skip the header
    }

    // Write a transaction to the file
    public void writeTransaction(Transaction transaction) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(TRANSACTION_FILE_PATH, true))) {
            String line = String.join("\t",
                    transaction.getTransactionDate(),
                    String.valueOf(transaction.getSizeSqM()),
                    String.valueOf(transaction.getTransactionSqFt()),
                    transaction.getPropertyType(),
                    String.valueOf(transaction.getNoOfFloors()),
                    transaction.getAddress(),
                    transaction.getScheme(),
                    String.valueOf(transaction.getTransactionPrice()),
                    String.valueOf(transaction.getYear()),
                    transaction.getProjectName(),
                    String.valueOf(transaction.getPricePerSqft())
            );
            bw.write(line);
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Update the properties file with the given list of properties
    public void updatePropertiesFile(List<Property> properties) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH))) {
            // Write the header first
            bw.write("SizeSqM,SqFt,PropertyType,NoOfFloors,Address,Scheme,Price,Year,PricePerSqft");
            bw.newLine();

            // Write each property
            for (Property property : properties) {
                String line = String.join(",",
                        String.valueOf(property.getSizeSqM()),
                        String.valueOf(property.getSqFt()),
                        property.getPropertyType(),
                        String.valueOf(property.getNoOfFloors()),
                        property.getAddress(),
                        property.getScheme(),
                        String.valueOf(property.getPrice()),
                        String.valueOf(property.getYear()),
                        String.valueOf(property.getPricePerSqft())
                );
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getFilePath() {
        return FILE_PATH;
    }
}
