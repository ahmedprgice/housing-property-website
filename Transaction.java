

public class Transaction {
    private String projectName;
    private String transactionDate;
    private double transactionPrice;
    private int transactionSqFt;
    private int sizeSqM;
    private String propertyType;
    private int noOfFloors;
    private String address;
    private String scheme;
    private int year;
    private double pricePerSqft;

    public Transaction(String projectName, String transactionDate, double transactionPrice, int transactionSqFt,
                       int sizeSqM, String propertyType, int noOfFloors, String address, String scheme, int year, double pricePerSqft) {
        this.projectName = projectName;
        this.transactionDate = transactionDate;
        this.transactionPrice = transactionPrice;
        this.transactionSqFt = transactionSqFt;
        this.sizeSqM = sizeSqM;
        this.propertyType = propertyType;
        this.noOfFloors = noOfFloors;
        this.address = address;
        this.scheme = scheme;
        this.year = year;
        this.pricePerSqft = pricePerSqft;
    }

    // Getters
    public String getProjectName() { return projectName; }
    public String getTransactionDate() { return transactionDate; }
    public double getTransactionPrice() { return transactionPrice; }
    public int getTransactionSqFt() { return transactionSqFt; }
    public int getSizeSqM() { return sizeSqM; }           // Getter for sizeSqM
    public String getPropertyType() { return propertyType; } // Getter for propertyType
    public int getNoOfFloors() { return noOfFloors; }      // Getter for noOfFloors
    public String getAddress() { return address; }          // Getter for address
    public String getScheme() { return scheme; }            // Getter for scheme
    public int getYear() { return year; }                    // Getter for year
    public double getPricePerSqft() { return pricePerSqft; } // Getter for pricePerSqft

    // Display transaction details
    public void displayTransactionDetails() {
        System.out.println("Transaction Date: " + transactionDate);
        System.out.println("Property Type: " + propertyType);
        System.out.println("Size: " + sizeSqM + " SqM, " + transactionSqFt + " SqFt");
        System.out.println("Number of Floors: " + noOfFloors);
        System.out.println("Address: " + address);
        System.out.println("Scheme: " + scheme);
        System.out.println("Transaction Price: $" + transactionPrice);
        System.out.println("Year: " + year);
        System.out.println("Price per Sq Ft: $" + pricePerSqft);
        System.out.println("--------------------");
    }
    public String getDetails() {
        return String.format(
            "Project: %s, Address: %s, Property Type: %s, Size: %d SqM (%d SqFt), Floors: %d, Year: %d, Price: $%.2f, Price per SqFt: $%.2f, Date: %s",
            projectName, address, propertyType, sizeSqM, transactionSqFt, noOfFloors, year, transactionPrice, pricePerSqft, transactionDate
        );
    }
}