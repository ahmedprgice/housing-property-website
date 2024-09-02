import java.util.ArrayList;
import java.util.List;

public class Project {
    private String projectName;
    private List<Property> properties;

    // Constructor
    public Project(String projectName) {
        this.projectName = projectName;
        this.properties = new ArrayList<>();
    }

    // Add a property
    public void addProperty(Property property) {
        properties.add(property);
    }

    // Get properties based on search criteria
    public List<Property> getPropertiesByCriteria(String size, double minPrice, double maxPrice, String facilities) {
        List<Property> result = new ArrayList<>();
        for (Property property : properties) {
            if ((size == null || property.getSize().equals(size)) &&
                (facilities == null || property.getFacilities().contains(facilities)) &&
                property.getPrice() >= minPrice && property.getPrice() <= maxPrice) {
                result.add(property);
            }
        }
        return result;
    }

    // Get historical transactions
    public List<Transaction> getHistoricalTransactions(List<Transaction> allTransactions) {
        List<Transaction> result = new ArrayList<>();
        for (Transaction transaction : allTransactions) {
            if (transaction.getProjectName().equals(this.projectName)) {
                result.add(transaction);
            }
        }
        return result.size() > 5 ? result.subList(0, 5) : result;
    }
}
