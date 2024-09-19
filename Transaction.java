public class Transaction {
    private String projectName;
    private String address;
    private String size;
    private double price;

    public Transaction(String projectName, String address, String size, double price) {
        this.projectName = projectName;
        this.address = address;
        this.size = size;
        this.price = price;
    }

    // Getters and setters
    public String getProjectName() { return projectName; }
    public void setProjectName(String projectName) { this.projectName = projectName; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getSize() { return size; }
    public void setSize(String size) { this.size = size; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
}
