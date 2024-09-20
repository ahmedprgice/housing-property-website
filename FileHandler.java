import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileHandler {

    private static final String FILE_PATH = "properties.csv";
    private static final String TRANSACTION_FILE_PATH = "transactions.txt";

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
    public void initializeFile() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH))) {
                // Writing header
                bw.write("SizeSqM,SqFt,PropertyType,NoOfFloors,Address,Scheme,Price,Year,PricePerSqft");
                bw.newLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Read transactions from the file
    public List<Transaction> readTransactions() {
        List<Transaction> transactions = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(TRANSACTION_FILE_PATH))) {
            String line;
            br.readLine(); // Skip the header
            while ((line = br.readLine()) != null) {
                String[] details = line.split("\t"); // Assuming the data is tab-separated

                if (details.length < 11) {
                    continue;
                }

                try {
                    String transactionDate = details[0].trim();
                    double transactionPrice = Double.parseDouble(details[1].trim());
                    int transactionSqFt = Integer.parseInt(details[2].trim());
                    String projectName = details[3].trim();
                    int sizeSqM = Integer.parseInt(details[4].trim());
                    String propertyType = details[5].trim();
                    int noOfFloors = Integer.parseInt(details[6].trim());
                    String address = details[7].trim();
                    String scheme = details[8].trim();
                    int year = Integer.parseInt(details[9].trim());
                    double pricePerSqft = Double.parseDouble(details[10].trim());

                    Transaction transaction = new Transaction(
                            projectName,
                            transactionDate,
                            transactionPrice,
                            transactionSqFt,
                            sizeSqM,
                            propertyType,
                            noOfFloors,
                            address,
                            scheme,
                            year,
                            pricePerSqft
                    );
                    transactions.add(transaction);
                } catch (NumberFormatException e) {
                    System.out.println("Error parsing number in file: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return transactions;
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
