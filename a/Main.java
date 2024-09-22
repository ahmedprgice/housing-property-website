package a;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        FileHandler fileHandler = new FileHandler();
        fileHandler.initializeFile();

        // Read existing properties from the file
        List<Property> allProperties = fileHandler.readProperties();

        Scanner scanner = new Scanner(System.in);
        boolean continueProgram = true;

        while (continueProgram) {
            System.out.println("Choose an option:");
            System.out.println("1. Search for properties");
            System.out.println("2. Add a new property");
            System.out.println("3. Display all properties");
            System.out.println("4. Show last 5 transactions for a project");
            System.out.println("5. Exit");

            int option = scanner.nextInt();
            scanner.nextLine(); // Consume the newline

            switch (option) {
                case 1:
                    searchProperties(scanner, allProperties, fileHandler);
                    break;
                case 2:
                    addNewProperty(scanner, allProperties, fileHandler);
                    break;
                case 3:
                    displayAllProperties(allProperties);
                    break;
                case 4:
                    showLast5Transactions(scanner, fileHandler);
                    break;
                case 5:
                    continueProgram = false;
                    System.out.println("Exiting the program.");
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
                    break;
            }
        }

        scanner.close();
    }

    // Method to search for properties
    private static void searchProperties(Scanner scanner, List<Property> allProperties, FileHandler fileHandler) {
        System.out.println("Enter minimum square footage:");
        int minSqFt = scanner.nextInt();
        System.out.println("Enter maximum square footage:");
        int maxSqFt = scanner.nextInt();
        System.out.println("Enter minimum price:");
        double minPrice = scanner.nextDouble();
        System.out.println("Enter maximum price:");
        double maxPrice = scanner.nextDouble();
        scanner.nextLine(); // consume the leftover newline

        System.out.println("Property type:");
        String facilities = scanner.nextLine().trim();
        System.out.println("Enter project name:");
        String projectName = scanner.nextLine().trim();

        List<Property> results = filterProperties(allProperties, minSqFt, maxSqFt, minPrice, maxPrice, facilities, projectName);

        if (results.isEmpty()) {
            System.out.println("No properties found that match your criteria.");
        } else {
            System.out.println("Matching properties:");
            for (Property prop : results) {
                prop.displayPropertyDetails();
                System.out.println("-------------------");
            }
            System.out.println("Enter the ID of the property you want to buy, or 0 to return to the main menu:");
            int propertyId = scanner.nextInt();
            scanner.nextLine(); // Consume the newline

            if (propertyId == 0) {
                return; // Return to the main menu
            }

            if (propertyId > 0 && propertyId <= results.size()) {
                buyProperty(scanner, results.get(propertyId - 1), fileHandler, allProperties);
            } else {
                System.out.println("Invalid property ID. Returning to the main menu.");
            }
        }
    }

    // Helper method to filter properties based on the criteria
    public static List<Property> filterProperties(List<Property> properties, int minSqFt, int maxSqFt, double minPrice, double maxPrice, String facilities, String projectName) {
        List<Property> filteredProperties = new ArrayList<>();
        
        for (Property property : properties) {
            boolean matchesSqFt = property.getSqFt() >= minSqFt && property.getSqFt() <= maxSqFt;
            boolean matchesPrice = property.getPrice() >= minPrice && property.getPrice() <= maxPrice;
            boolean matchesFacilities = facilities.isEmpty() || property.getPropertyType().equalsIgnoreCase(facilities);
            boolean matchesProjectName = projectName.isEmpty() || property.getScheme().equalsIgnoreCase(projectName);

            if (matchesSqFt && matchesPrice && matchesFacilities && matchesProjectName) {
                filteredProperties.add(property);
            }
        }
        return filteredProperties;
    }

    // Method to add a new property
    private static void addNewProperty(Scanner scanner, List<Property> allProperties, FileHandler fileHandler) {
        System.out.println("Enter property size in square meters:");
        int sizeSqM = scanner.nextInt();
        System.out.println("Enter property size in square feet:");
        int sqFt = scanner.nextInt();
        scanner.nextLine(); // Consume the newline

        System.out.println("Enter property type (e.g., Condominium, Apartment):");
        String propertyType = scanner.nextLine();

        System.out.println("Enter the number of floors:");
        int noOfFloors = scanner.nextInt();
        scanner.nextLine(); // Consume the newline

        System.out.println("Enter property address:");
        String address = scanner.nextLine();

        System.out.println("Enter property scheme/project name:");
        String scheme = scanner.nextLine();

        System.out.println("Enter property price:");
        double price = scanner.nextDouble();

        System.out.println("Enter year of construction:");
        int year = scanner.nextInt();

        System.out.println("Enter price per square foot:");
        double pricePerSqft = scanner.nextDouble();
        scanner.nextLine(); // Consume the newline

        Property newProperty = new Property(sizeSqM, sqFt, propertyType, noOfFloors, address, scheme, price, year, pricePerSqft);
        allProperties.add(newProperty);
        fileHandler.writeProperty(newProperty);

        System.out.println("Property added successfully and saved to the file!");
    }

    // Method to display all properties
    private static void displayAllProperties(List<Property> allProperties) {
        if (allProperties.isEmpty()) {
            System.out.println("No properties available.");
        } else {
            System.out.println("All Properties:");
            for (Property property : allProperties) {
                property.displayPropertyDetails();
                System.out.println("-------------------");
            }
        }
    }

    // Method to show the last 5 transactions for a selected project
    private static void showLast5Transactions(Scanner scanner, FileHandler fileHandler) {
        System.out.println("Enter the project name to show the last 5 transactions:");
        String projectName = scanner.nextLine().trim(); // Ensure trimming any extra spaces

        List<Transaction> allTransactions = fileHandler.readTransactions();
        List<Transaction> projectTransactions = new ArrayList<>();

        for (Transaction transaction : allTransactions) {
            if (transaction.getProjectName().equalsIgnoreCase(projectName)) {
                projectTransactions.add(transaction);
            }
        }

        int transactionCount = projectTransactions.size();
        if (transactionCount == 0) {
            System.out.println("No transactions found for the project: " + projectName);
        } else {
            System.out.println("Last 5 transactions for project: " + projectName);
            for (int i = Math.max(transactionCount - 5, 0); i < transactionCount; i++) {
                projectTransactions.get(i).displayTransactionDetails();
                System.out.println("----------------------------------");
            }
        }
    }

    private static void buyProperty(Scanner scanner, Property selectedProperty, FileHandler fileHandler, List<Property> allProperties) {
        System.out.println("Are you sure you want to buy this property?");
        selectedProperty.displayPropertyDetails();
        System.out.println("Enter 'yes' to confirm, or any other key to cancel:");

        String confirmation = scanner.nextLine();
        if (confirmation.equalsIgnoreCase("yes")) {
            System.out.println("You have successfully bought the property at: " + selectedProperty.getAddress());

            // Create a transaction record
            Transaction transaction = new Transaction(
                selectedProperty.getScheme(), // projectName
                java.time.LocalDate.now().toString(), // transactionDate
                selectedProperty.getPrice(), // transactionPrice
                selectedProperty.getSqFt(), // transactionSqFt
                selectedProperty.getSizeSqM(), // sizeSqM
                selectedProperty.getPropertyType(), // propertyType
                selectedProperty.getNoOfFloors(), // noOfFloors
                selectedProperty.getAddress(), // address
                selectedProperty.getScheme(), // scheme
                selectedProperty.getYear(), // year
                selectedProperty.getPricePerSqft() // pricePerSqft
            );

            try {
                fileHandler.writeTransaction(transaction);
                System.out.println("Transaction recorded successfully.");
            } catch (Exception e) {
                System.out.println("Failed to record transaction: " + e.getMessage());
            }

            // Update the properties file to remove the sold property
            allProperties.remove(selectedProperty);
            fileHandler.updatePropertiesFile(allProperties);
            System.out.println("The property has been removed from the available list.");
        } else {
            System.out.println("Purchase cancelled. Returning to the main menu.");
        }
    }
}