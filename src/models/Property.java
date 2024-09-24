package models;


public class Property {
   
    private int sizeSqM;
    private int sqFt;
    private String propertyType;
    private int noOfFloors;
    private String address;
    private String scheme;
    private double price;
    private int year;
    private double pricePerSqft;

    // Constructor
    private Property(Builder builder) {
        this.sizeSqM = builder.sizeSqM;
        this.sqFt = builder.sqFt;
        this.propertyType = builder.propertyType;
        this.noOfFloors = builder.noOfFloors;
        this.address = builder.address;
        this.scheme = builder.scheme;
        this.price = builder.price;
        this.year = builder.year;
        this.pricePerSqft = builder.pricePerSqft;
    }


    // Getters and setters
    public int getSizeSqM() {
        return sizeSqM;
    }

    public int getSqFt() {
        return sqFt;
    }

    public String getPropertyType() {
        return propertyType;
    }

    public int getNoOfFloors() {
        return noOfFloors;
    }

    public String getAddress() {
        return address;
    }

    public String getScheme() {
        return scheme;
    }

    public double getPrice() {
        return price;
    }

    public int getYear() {
        return year;
    }

    public double getPricePerSqft() {
        return pricePerSqft;
    }

    // Builder class
    public static class Builder {
        private int sizeSqM;
        private int sqFt;
        private String propertyType;
        private int noOfFloors;
        private String address;
        private String scheme;
        private double price;
        private int year;
        private double pricePerSqft;

        public Builder setSizeSqM(int sizeSqM) {
            this.sizeSqM = sizeSqM;
            return this;
        }

        public Builder setSqFt(int sqFt) {
            this.sqFt = sqFt;
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

        public Builder setPrice(double price) {
            this.price = price;
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

        public Property build() {
            return new Property(this);
        }
    }

    // Method to display property details
    public void displayPropertyDetails() {
        System.out.println("Property Type: " + propertyType);
        System.out.println("Size: " + sqFt + " SqFt (" + sizeSqM + " SqM)");
        System.out.println("Number of Floors: " + noOfFloors);
        System.out.println("Address: " + address);
        System.out.println("Scheme: " + scheme);
        System.out.println("Price: $" + price);
        System.out.println("Year: " + year);
        System.out.println("Price per Sq Ft: $" + pricePerSqft);
    }
    public String getDetails() {
        return String.format(
            "Property Type: %s\nSize: %d SqFt (%d SqM)\nNumber of Floors: %d\nAddress: %s\nScheme: %s\nPrice: $%.1f\nYear: %d\nPrice per Sq Ft: $%.1f",
            propertyType, 
            sqFt, 
            sizeSqM, 
            noOfFloors, 
            address, 
            scheme, 
            price, 
            year, 
            pricePerSqft
        );
    }
}