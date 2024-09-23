package models;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Transaction {
    private String projectName;
    private LocalDate transactionDate;
    private double transactionPrice;
    private int transactionSqFt;
    private int sizeSqM;
    private String propertyType;
    private int noOfFloors;
    private String address;
    private String scheme;
    private int year;
    private double pricePerSqft;

    // Constructor
    private Transaction(Builder builder) {
        this.projectName = builder.projectName;
        this.transactionDate = builder.transactionDate;
        this.transactionPrice = builder.transactionPrice;
        this.transactionSqFt = builder.transactionSqFt;
        this.sizeSqM = builder.sizeSqM;
        this.propertyType = builder.propertyType;
        this.noOfFloors = builder.noOfFloors;
        this.address = builder.address;
        this.scheme = builder.scheme;
        this.year = builder.year;
        this.pricePerSqft = builder.pricePerSqft;
    }

    // Getters
    public String getProjectName() { return projectName; }
    public String getTransactionDate() {return transactionDate.format(DateTimeFormatter.ISO_LOCAL_DATE);}
    public double getTransactionPrice() { return transactionPrice; }
    public int getTransactionSqFt() { return transactionSqFt; }
    public int getSizeSqM() { return sizeSqM; }           // Getter for sizeSqM
    public String getPropertyType() { return propertyType; } // Getter for propertyType
    public int getNoOfFloors() { return noOfFloors; }      // Getter for noOfFloors
    public String getAddress() { return address; }          // Getter for address
    public String getScheme() { return scheme; }            // Getter for scheme
    public int getYear() { return year; }                    // Getter for year
    public double getPricePerSqft() { return pricePerSqft; } // Getter for pricePerSqft


    // Builder class
    public static class Builder {
        private String projectName;
        private LocalDate transactionDate;
        private double transactionPrice;
        private int transactionSqFt;
        private int sizeSqM;
        private String propertyType;
        private int noOfFloors;
        private String address;
        private String scheme;
        private int year;
        private double pricePerSqft;

        public Builder setProjectName(String projectName) {
            this.projectName = projectName;
            return this;
        }

        public Builder setTransactionDate(LocalDate transactionDate) {
            this.transactionDate = transactionDate;
            return this;
        }

        public Builder setTransactionPrice(double transactionPrice) {
            this.transactionPrice = transactionPrice;
            return this;
        }

        public Builder setTransactionSqFt(int transactionSqFt) {
            this.transactionSqFt = transactionSqFt;
            return this;
        }

        public Builder setSizeSqM(int sizeSqM) {
            this.sizeSqM = sizeSqM;
            return this;
        }

        public Builder setPropertyType(String propertyType) {
            this.propertyType = propertyType;
            return this;
        }

        public Builder setNoOfFloors(int noOfFloors) {
            this.noOfFloors = noOfFloors;
            return this;
        }

        public Builder setAddress(String address) {
            this.address = address;
            return this;
        }

        public Builder setScheme(String scheme) {
            this.scheme = scheme;
            return this;
        }

        public Builder setYear(int year) {
            this.year = year;
            return this;
        }

        public Builder setPricePerSqft(double pricePerSqft) {
            this.pricePerSqft = pricePerSqft;
            return this;
        }

        public Transaction build() {
            return new Transaction(this);
        }
    }


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