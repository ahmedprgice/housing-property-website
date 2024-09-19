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
    public Property(int sizeSqM, int sqFt, String propertyType, int noOfFloors, String address, String scheme, double price, int year, double pricePerSqft) {
        this.sizeSqM = sizeSqM;
        this.sqFt = sqFt;
        this.propertyType = propertyType;
        this.noOfFloors = noOfFloors;
        this.address = address;
        this.scheme = scheme;
        this.price = price;
        this.year = year;
        this.pricePerSqft = pricePerSqft;
    }

    public Property(int sqFt2, double price2, String propertyType2, String address2, String scheme2, int year2,
            double pricePerSqft2) {
        //TODO Auto-generated constructor stub
    }

    // Getters and setters
    public int getSizeSqM() { return sizeSqM; }
    public void setSizeSqM(int sizeSqM) { this.sizeSqM = sizeSqM; }

    public int getSqFt() { return sqFt; }
    public void setSqFt(int sqFt) { this.sqFt = sqFt; }

    public String getPropertyType() { return propertyType; }
    public void setPropertyType(String propertyType) { this.propertyType = propertyType; }

    public int getNoOfFloors() { return noOfFloors; }
    public void setNoOfFloors(int noOfFloors) { this.noOfFloors = noOfFloors; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getScheme() { return scheme; }
    public void setScheme(String scheme) { this.scheme = scheme; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }

    public double getPricePerSqft() { return pricePerSqft; }
    public void setPricePerSqft(double pricePerSqft) { this.pricePerSqft = pricePerSqft; }

    // Method to display property details
    public void displayPropertyDetails() {
        System.out.println("Property Type: " + propertyType);
        System.out.println("Size: " + sqFt + " sq ft (" + sizeSqM + " SqM)");
        System.out.println("Number of Floors: " + noOfFloors);
        System.out.println("Address: " + address);
        System.out.println("Scheme: " + scheme);
        System.out.println("Year: " + year);
        System.out.println("Price: $" + price);
        System.out.println("Price per Sq Ft: $" + pricePerSqft);
    }
}