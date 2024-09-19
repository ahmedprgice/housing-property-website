import java.util.ArrayList;
import java.util.List;

public class Project {
    private String projectName;
    private List<Property> properties = new ArrayList<>();

    // Constructor
    public Project(String projectName) {
        this.projectName = projectName;
    }

    // Getter for projectName
    public String getProjectName() {
        return projectName;
    }

    // Add a property to the project
    public void addProperty(Property property) {
        properties.add(property);
    }

    // Get all properties in the project
    public List<Property> getProperties() {
        return properties;
    }

    // Search for properties based on criteria
    public List<Property> searchProperties(int minSqFt, int maxSqFt, double minPrice, double maxPrice, String facilities, String projectName) {
        List<Property> filteredProperties = new ArrayList<>();
    
        // Iterate through all properties and apply filtering conditions
        for (Property property : properties) {
            boolean matchesSqFt = property.getSqFt() >= minSqFt && property.getSqFt() <= maxSqFt;
            boolean matchesPrice = property.getPrice() >= minPrice && property.getPrice() <= maxPrice;
            boolean matchesFacilities = (facilities == null || facilities.isEmpty() || property.getPropertyType().toLowerCase().contains(facilities.toLowerCase()));
            boolean matchesProjectName = (projectName == null || projectName.isEmpty() || property.getScheme().equalsIgnoreCase(projectName));
    
            // Debugging output
            System.out.println("Checking property: " + property.getPropertyType() + ", " + property.getScheme());
            System.out.println("Matches SqFt: " + matchesSqFt + ", Matches Price: " + matchesPrice);
            System.out.println("Matches Facilities: " + matchesFacilities + ", Matches Project Name: " + matchesProjectName);
    
            if (matchesSqFt && matchesPrice && matchesFacilities && matchesProjectName) {
                filteredProperties.add(property);
            }
        }
        return filteredProperties;
    }
    

    // Display all properties in the project
    public void displayProperties() {
        System.out.println("Properties in " + projectName + ":");
        for (Property property : properties) {
            property.displayPropertyDetails();
            System.out.println("-------------------");
        }
    }
}