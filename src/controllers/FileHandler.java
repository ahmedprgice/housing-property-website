package controllers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import models.Property;
import models.Transaction;

public class FileHandler {

    private static final String FILE_PATH = "properties.csv";
    private static final String TRANSACTION_FILE_PATH = "transactions.txt";

    // Singleton instance
    private static volatile FileHandler instance;

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

                    Property property = new Property(sizeSqM, sqFt, propertyType, noOfFloors, address, scheme, price, year, pricePerSqft);
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
    public  void initializeFile(String filePath) {
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
        return transactions;
    }

    private Transaction parseTransaction(String line) {
        try {
            String[] fields = line.split("\t");
            if (fields.length < 10) {
                System.err.println("Skipping improperly formatted transaction line: " + line);
                return null;
            }

            String date = fields[0].trim();
            String address = fields[5].trim();
            int sizeSqFt = Integer.parseInt(fields[2].trim());
            String projectName = fields[6].trim();
            double price = Double.parseDouble(fields[7].trim());

            return new Transaction(projectName, date, price, sizeSqFt, 0, "", 0, address, "", 0, 0.0);

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
