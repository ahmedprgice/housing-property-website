import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileHandler {

    private static final String FILE_PATH = "properties.csv";

    // Read properties from a file
    public List<Property> readProperties() {
        List<Property> properties = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            br.readLine(); // Skip the header
            while ((line = br.readLine()) != null) {
                String[] details = line.split(",");
                // Assuming the order of data is consistent with the header
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
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }

    // Write a property to a file
    public void writeProperty(Property property) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            String line = property.getSizeSqM() + "," + property.getSqFt() + "," + property.getPropertyType() + ","
                    + property.getNoOfFloors() + "," + property.getAddress() + "," + property.getScheme() + ","
                    + property.getPrice() + "," + property.getYear() + "," + property.getPricePerSqft();
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
}
