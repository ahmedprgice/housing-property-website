public class Property {
    private String size;
    private double price;
    private String facilities;
    private String projectName;
    private String address;

    // Constructor
    public Property(String size, double price, String facilities, String projectName, String address) {
        this.size = size;
        this.price = price;
        this.facilities = facilities;
        this.projectName = projectName;
        this.address = address;
    }

    // Getters and Setters
    public String getSize() { return size; }
    public void setSize(String size) { this.size = size; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public String getFacilities() { return facilities; }
    public void setFacilities(String facilities) { this.facilities = facilities; }

    public String getProjectName() { return projectName; }
    public void setProjectName(String projectName) { this.projectName = projectName; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    // Display property details
    public void displayPropertyDetails() {
        System.out.println("Property Details:");
        System.out.println("Size: " + size);
        System.out.println("Price: " + price);
        System.out.println("Facilities: " + facilities);
        System.out.println("Project Name: " + projectName);
        System.out.println("Address: " + address);
    }
}
