package views;

import controllers.FileHandler;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import models.Property;
import models.Transaction;

public class HomeFinderApp extends JFrame {

    private JTextField minPriceField;
    private JTextField maxPriceField;
    private JTextField minSqFtField;
    private JTextField maxSqFtField;
    private JTextField propertyTypeField;
    private JTextField projectNameField;
    private JTextArea resultArea;
    private JComboBox<String> projectNameComboBox; // Dropdown for project names

    private FileHandler fileHandler;
    private List<Property> allProperties;

    public HomeFinderApp() {
        // call FileHandler instance and read properties
        fileHandler = FileHandler.getInstance();
        fileHandler.initializeFile("properties.csv");
        fileHandler.initializeFile("transactions.txt");

        allProperties = fileHandler.readProperties();

        // Set up the main frame
        setTitle("Home Finder");
        setSize(600, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridBagLayout());
        getContentPane().setBackground(new Color(5, 33, 67)); // Background color

        // Create title label
        JLabel titleLabel = new JLabel("Home Finder", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 36));
        titleLabel.setForeground(Color.WHITE);

        // Create buttons
        JButton searchButton = new JButton("Search for Properties");
        JButton addButton = new JButton("Add New Property");
        JButton displayButton = new JButton("Display All Properties");
        JButton showTransactionButton = new JButton("Show Last 5 Transactions");
        JButton exitButton = new JButton("Exit");

        // Customize button appearance
        Color buttonColor = new Color(23, 76, 124);
        Font buttonFont = new Font("Arial", Font.BOLD, 18);
        JButton[] buttons = {searchButton, addButton, displayButton, showTransactionButton, exitButton};

        for (JButton button : buttons) {
            button.setBackground(buttonColor);
            button.setForeground(Color.WHITE);
            button.setFont(buttonFont);
            button.setPreferredSize(new Dimension(250, 40));
            button.setFocusPainted(false);
            button.setBorder(BorderFactory.createEmptyBorder());
        }

        // Add Action Listeners
        searchButton.addActionListener(e -> openPropertySearchUI());
        addButton.addActionListener(e -> openAddPropertyDialog());
        displayButton.addActionListener(e -> displayAllProperties());
        showTransactionButton.addActionListener(e -> showLastFiveTransactionsUI());
        exitButton.addActionListener(e -> System.exit(0));

        // Add components to the frame
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(titleLabel, gbc);

        gbc.gridy++;
        for (JButton button : buttons) {
            add(button, gbc);
            gbc.gridy++;
        }

        setLocationRelativeTo(null); // Center the frame
        setVisible(true);
    }

    private void openPropertySearchUI() {
        // Create and display the Property Search UI
        JDialog searchDialog = new JDialog(this, "Property Search", true);
        searchDialog.setSize(400, 500);

        searchDialog.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Create input fields
        minPriceField = new JTextField();
        maxPriceField = new JTextField();
        minSqFtField = new JTextField();
        maxSqFtField = new JTextField();
        propertyTypeField = new JTextField();
        projectNameComboBox = new JComboBox<>(getAllProjectNames()); // Initialize the ComboBox with project names

        // Add components to the dialog
        addInputField(searchDialog, gbc, "Minimum Price:", minPriceField, 0);
        addInputField(searchDialog, gbc, "Maximum Price:", maxPriceField, 1);
        addInputField(searchDialog, gbc, "Minimum SqFt:", minSqFtField, 2);
        addInputField(searchDialog, gbc, "Maximum SqFt:", maxSqFtField, 3);
        addInputField(searchDialog, gbc, "Property Type:", propertyTypeField, 4);
        addInputField(searchDialog, gbc, "Project Name:", projectNameComboBox, 5); // Use ComboBox instead of TextField

        gbc.gridx = 1;
        gbc.gridy = 6;
        JButton searchButton = new JButton("Search");
        searchDialog.add(searchButton, gbc);

        // Add action listener to search button
        searchButton.addActionListener(e -> searchProperties());

        searchDialog.setLocationRelativeTo(this);
        searchDialog.setVisible(true);
    }

    private void addInputField(JDialog dialog, GridBagConstraints gbc, String label, JTextField field, int row) {
        gbc.gridx = 0;
        gbc.gridy = row;
        dialog.add(new JLabel(label), gbc);
        gbc.gridx = 1;
        dialog.add(field, gbc);
    }

// Method to get all unique project names for the ComboBox
    private String[] getAllProjectNames() {
        Set<String> projectNames = new HashSet<>();
        for (Property property : allProperties) {
            projectNames.add(property.getScheme());
        }
        return projectNames.toArray(new String[0]);
    }

// Overloaded method for adding JComboBox as input field
    private void addInputField(JDialog dialog, GridBagConstraints gbc, String label, JComboBox<String> comboBox, int row) {
        gbc.gridx = 0;
        gbc.gridy = row;
        dialog.add(new JLabel(label), gbc);
        gbc.gridx = 1;
        dialog.add(comboBox, gbc);
    }

    private void searchProperties() {
        try {
            // Set default values
            double minPrice = Double.MIN_VALUE;
            double maxPrice = Double.MAX_VALUE;
            int minSqFt = Integer.MIN_VALUE;
            int maxSqFt = Integer.MAX_VALUE;

            String minPriceInput = minPriceField.getText().trim();
            if (!minPriceInput.isEmpty()) {
                minPrice = Double.parseDouble(minPriceInput);
            }

            String maxPriceInput = maxPriceField.getText().trim();
            if (!maxPriceInput.isEmpty()) {
                maxPrice = Double.parseDouble(maxPriceInput);
            }

            String minSqFtInput = minSqFtField.getText().trim();
            if (!minSqFtInput.isEmpty()) {
                minSqFt = Integer.parseInt(minSqFtInput);
            }

            String maxSqFtInput = maxSqFtField.getText().trim();
            if (!maxSqFtInput.isEmpty()) {
                maxSqFt = Integer.parseInt(maxSqFtInput);
            }

            String propertyType = propertyTypeField.getText().trim();
            String projectName = (String) projectNameComboBox.getSelectedItem(); // Get selected project name

            List<Property> results = filterProperties(allProperties, minSqFt, maxSqFt, minPrice, maxPrice, propertyType, projectName);

            if (results.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No properties found that match your criteria.", "No Results", JOptionPane.INFORMATION_MESSAGE);
            } else {
                displaySearchResults(results);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter valid numbers for price and square footage.", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buyProperty(Property property, JDialog resultsDialog) {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to buy this property?\n" + property.getDetails(),
                "Confirm Purchase",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            // Create a transaction record
            Transaction transaction = new Transaction(
                    property.getScheme(), // projectName
                    java.time.LocalDate.now(), // transactionDate
                    property.getPrice(), // transactionPrice
                    property.getSqFt(), // transactionSqFt
                    property.getSizeSqM(), // sizeSqM
                    property.getPropertyType(), // propertyType
                    property.getNoOfFloors(), // noOfFloors
                    property.getAddress(), // address
                    property.getScheme(), // scheme
                    property.getYear(), // year
                    property.getPricePerSqft() // pricePerSqft
            );

            try {
                // Record the transaction
                fileHandler.writeTransaction(transaction);

                // Remove the purchased property from the list
                allProperties.remove(property);

                // Update the properties file
                fileHandler.updatePropertiesFile(allProperties);

                JOptionPane.showMessageDialog(this, "Property purchased successfully!\nTransaction recorded.", "Success", JOptionPane.INFORMATION_MESSAGE);

                // Close the results dialog if it's not null
                if (resultsDialog != null) {
                    resultsDialog.dispose();
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Failed to record the transaction: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

// Method to display the search results
    private void displaySearchResults(List<Property> results) {
        JDialog resultsDialog = new JDialog(this, "Search Results", true);
        resultsDialog.setSize(750, 600);
        resultsDialog.setLayout(new BorderLayout());
        resultsDialog.setLocationRelativeTo(this);

        // Container for all property panels
        JPanel resultsPanel = new JPanel();
        resultsPanel.setLayout(new BoxLayout(resultsPanel, BoxLayout.Y_AXIS));
        resultsPanel.setBackground(new Color(210, 225, 240)); // Light blue background

        for (Property property : results) {
            JPanel propertyPanel = createPropertyPanel(property);
            resultsPanel.add(propertyPanel);
            resultsPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Spacer between panels
        }

        // Scroll pane for the results panel
        JScrollPane scrollPane = new JScrollPane(resultsPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        // Back button
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(5, 33, 67)); // Dark blue background
        JButton backButton = new JButton("Back");
        backButton.setFont(new Font("Arial", Font.BOLD, 16));
        backButton.setPreferredSize(new Dimension(100, 40));
        backButton.setBackground(new Color(23, 76, 124));
        backButton.setForeground(Color.WHITE);
        backButton.setFocusPainted(false);
        backButton.addActionListener(e -> resultsDialog.dispose());
        buttonPanel.add(backButton);

        resultsDialog.add(scrollPane, BorderLayout.CENTER);
        resultsDialog.add(buttonPanel, BorderLayout.SOUTH);

        resultsDialog.setVisible(true);
    }

    // Helper method to create a property panel for display
    private JPanel createPropertyPanel(Property property) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setPreferredSize(new Dimension(800, 250)); // Adjusted size for the panel
        panel.setBorder(BorderFactory.createLineBorder(new Color(5, 33, 67), 2)); // Dark blue border
        panel.setBackground(new Color(230, 240, 255)); // Light blue background for property panel

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Increase insets for better spacing
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;

        // Load image for property
        JLabel imageLabel = new JLabel();
        ImageIcon propertyImage = loadImage(property.getScheme());
        imageLabel.setIcon(propertyImage);
        imageLabel.setPreferredSize(new Dimension(200, 200)); // Increase image size
        gbc.gridheight = 6; // Span image over multiple rows
        panel.add(imageLabel, gbc);

        gbc.gridheight = 1; // Reset grid height
        gbc.gridx = 1; // Move to the next column

        // Project Name
        JLabel projectNameLabel = new JLabel("Project: " + property.getScheme());
        projectNameLabel.setFont(new Font("Arial", Font.BOLD, 18)); // Increase font size
        panel.add(projectNameLabel, gbc);

        // Location
        gbc.gridy++;
        JLabel locationLabel = new JLabel("Location: " + property.getAddress());
        locationLabel.setFont(new Font("Arial", Font.PLAIN, 16)); // Increase font size
        panel.add(locationLabel, gbc);

        // Size
        gbc.gridy++;
        JLabel sizeLabel = new JLabel("Size: " + property.getSqFt() + " SqFt (" + property.getSizeSqM() + " SqM)");
        sizeLabel.setFont(new Font("Arial", Font.PLAIN, 16)); // Increase font size
        panel.add(sizeLabel, gbc);

        // Number of Floors
        gbc.gridy++;
        JLabel floorsLabel = new JLabel("Number of Floors: " + property.getNoOfFloors());
        floorsLabel.setFont(new Font("Arial", Font.PLAIN, 16)); // Increase font size
        panel.add(floorsLabel, gbc);

        // Price
        gbc.gridy++;
        JLabel priceLabel = new JLabel("Price: $" + String.format("%.1f", property.getPrice()));
        priceLabel.setFont(new Font("Arial", Font.PLAIN, 16)); // Increase font size
        panel.add(priceLabel, gbc);

        // Year
        gbc.gridy++;
        JLabel yearLabel = new JLabel("Year: " + property.getYear());
        yearLabel.setFont(new Font("Arial", Font.PLAIN, 16)); // Increase font size
        panel.add(yearLabel, gbc);

        // Price per SqFt
        gbc.gridy++;
        JLabel pricePerSqFtLabel = new JLabel("Price per Sq Ft: $" + String.format("%.1f", property.getPricePerSqft()));
        pricePerSqFtLabel.setFont(new Font("Arial", Font.PLAIN, 16)); // Increase font size
        panel.add(pricePerSqFtLabel, gbc);

        // Add Buy Button
        gbc.gridy++;
        JButton buyButton = new JButton("Buy");
        buyButton.setPreferredSize(new Dimension(100, 30)); // Increase button size
        buyButton.setBackground(new Color(46, 204, 113));
        buyButton.setForeground(Color.WHITE);
        buyButton.setFont(new Font("Arial", Font.BOLD, 16)); // Increase font size for button
        buyButton.addActionListener(e -> buyProperty(property, null)); // Pass null for now, can handle dialog refresh if needed
        panel.add(buyButton, gbc);

        return panel;
    }

    // Method to load an image for the property based on the scheme or project name
    private ImageIcon loadImage(String scheme) {
        String imagePath = "images/" + scheme.toLowerCase().replace(" ", "_") + ".jpg"; // Image path based on scheme name
        File imageFile = new File(imagePath);
        if (imageFile.exists()) {
            ImageIcon icon = new ImageIcon(imagePath);
            Image img = icon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH); // Resize the image
            return new ImageIcon(img);
        } else {
            // Return a placeholder image if the image is not found
            return new ImageIcon(new ImageIcon("pexels-binyamin-mellish-106399.jpg").getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH));
        }
    }

    public static List<Property> filterProperties(List<Property> properties, int minSqFt, int maxSqFt, double minPrice, double maxPrice, String propertyType, String projectName) {
        List<Property> filteredProperties = new ArrayList<>();

        for (Property property : properties) {
            boolean matchesSqFt = property.getSqFt() >= minSqFt && property.getSqFt() <= maxSqFt;
            boolean matchesPrice = property.getPrice() >= minPrice && property.getPrice() <= maxPrice;
            boolean matchesType = propertyType.isEmpty() || property.getPropertyType().equalsIgnoreCase(propertyType);
            boolean matchesProjectName = projectName.isEmpty() || property.getScheme().equalsIgnoreCase(projectName);

            if (matchesSqFt && matchesPrice && matchesType && matchesProjectName) {
                filteredProperties.add(property);
            }
        }
        return filteredProperties;
    }

    private void openAddPropertyDialog() {
        JDialog addDialog = new JDialog(this, "Add New Property", true);
        addDialog.setSize(400, 500);

        addDialog.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Create input fields
        JTextField sizeSqMField = new JTextField();
        JTextField sqFtField = new JTextField();
        JTextField propertyTypeField = new JTextField();
        JTextField noOfFloorsField = new JTextField();
        JTextField addressField = new JTextField();
        JTextField schemeField = new JTextField();
        JTextField priceField = new JTextField();
        JTextField yearField = new JTextField();
        JTextField pricePerSqFtField = new JTextField();

        // Add fields to the dialog
        addInputField(addDialog, gbc, "Size (sqm):", sizeSqMField, 0);
        addInputField(addDialog, gbc, "Size (sqft):", sqFtField, 1);
        addInputField(addDialog, gbc, "Property Type:", propertyTypeField, 2);
        addInputField(addDialog, gbc, "Number of Floors:", noOfFloorsField, 3);
        addInputField(addDialog, gbc, "Address:", addressField, 4);
        addInputField(addDialog, gbc, "Project Name:", schemeField, 5);
        addInputField(addDialog, gbc, "Price:", priceField, 6);
        addInputField(addDialog, gbc, "Year:", yearField, 7);
        addInputField(addDialog, gbc, "Price per sqft:", pricePerSqFtField, 8);

        gbc.gridx = 1;
        gbc.gridy = 9;
        JButton addButton = new JButton("Add Property");
        addDialog.add(addButton, gbc);

        // Action listener for the add button
        addButton.addActionListener(e -> {
            try {
                // Gather input data
                int sizeSqM = Integer.parseInt(sizeSqMField.getText());
                int sqFt = Integer.parseInt(sqFtField.getText());
                String propertyType = propertyTypeField.getText().trim();
                int noOfFloors = Integer.parseInt(noOfFloorsField.getText());
                String address = addressField.getText().trim();
                String scheme = schemeField.getText().trim();
                double price = Double.parseDouble(priceField.getText());
                int year = Integer.parseInt(yearField.getText());
                double pricePerSqFt = Double.parseDouble(pricePerSqFtField.getText());
                        // Create new Property using Builder
                Property newProperty = new Property.Builder()
                    .setSizeSqM(sizeSqM)
                    .setSqFt(sqFt)
                    .setPropertyType(propertyType)
                    .setNoOfFloors(noOfFloors)
                    .setAddress(address)
                    .setScheme(scheme)
                    .setPrice(price)
                    .setYear(year)
                    .setPricePerSqft(pricePerSqFt)
                    .build();
                allProperties.add(newProperty);
                fileHandler.writeProperty(newProperty);

                JOptionPane.showMessageDialog(addDialog, "Property added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                addDialog.dispose(); // Close the dialog
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(addDialog, "Please enter valid numbers.", "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        addDialog.setLocationRelativeTo(this);
        addDialog.setVisible(true);
    }

    private void displayAllProperties() {
        if (allProperties.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No properties available.", "All Properties", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JDialog propertiesDialog = new JDialog(this, "All Properties", true);
            propertiesDialog.setSize(750, 600);
            propertiesDialog.setLayout(new BorderLayout());
            propertiesDialog.setLocationRelativeTo(this);

            // Container for all property panels
            JPanel propertiesPanel = new JPanel();
            propertiesPanel.setLayout(new BoxLayout(propertiesPanel, BoxLayout.Y_AXIS));
            propertiesPanel.setBackground(new Color(210, 225, 240)); // Light blue background

            for (Property property : allProperties) {
                JPanel propertyPanel = createPropertyPanel(property);
                propertiesPanel.add(propertyPanel);
                propertiesPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Spacer between panels
            }

            // Scroll pane for the properties panel
            JScrollPane scrollPane = new JScrollPane(propertiesPanel);
            scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

            // Back button
            JPanel buttonPanel = new JPanel();
            buttonPanel.setBackground(new Color(5, 33, 67)); // Dark blue background
            JButton backButton = new JButton("Back");
            backButton.setFont(new Font("Arial", Font.BOLD, 16));
            backButton.setPreferredSize(new Dimension(100, 40));
            backButton.setBackground(new Color(23, 76, 124));
            backButton.setForeground(Color.WHITE);
            backButton.setFocusPainted(false);
            backButton.addActionListener(e -> propertiesDialog.dispose());
            buttonPanel.add(backButton);

            propertiesDialog.add(scrollPane, BorderLayout.CENTER);
            propertiesDialog.add(buttonPanel, BorderLayout.SOUTH);

            propertiesDialog.setVisible(true);
        }
    }

    public static List<Transaction> filterTransactionsByProjectName(List<Transaction> transactions, String projectName) {
        List<Transaction> filteredTransactions = new ArrayList<>();
        for (Transaction transaction : transactions) {
            if (transaction.getProjectName().equalsIgnoreCase(projectName)) {
                filteredTransactions.add(transaction);
            }
        }
        return filteredTransactions;
    }

    public static void displayTransactions(List<Transaction> transactions, String projectName) {
        System.out.println("Transactions for project: " + projectName);
        for (Transaction transaction : transactions) {
            transaction.displayTransactionDetails();
        }
    }

    // Updated method to show last 5 transactions in UI
    private void showLastFiveTransactionsUI() {
        String projectName = JOptionPane.showInputDialog(this, "Enter the project name to show the last 5 transactions:");
        if (projectName == null || projectName.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a valid project name.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        List<Transaction> allTransactions = fileHandler.loadTransactions();
        List<Transaction> projectTransactions = filterTransactionsByProjectName(allTransactions, projectName);

        if (projectTransactions.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No transactions found for the project: " + projectName, "No Results", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JDialog transactionsDialog = new JDialog(this, "Last 5 Transactions for " + projectName, true);
            transactionsDialog.setSize(800, 400);
            transactionsDialog.setLayout(new BorderLayout());

            // Table model and table to display transactions
            String[] columnNames = {"Date", "Size SqFt", "Price", "Address"};
            DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
            JTable table = new JTable(tableModel);

            // Show only last 5 transactions
            int startIndex = Math.max(0, projectTransactions.size() - 5);
            for (int i = startIndex; i < projectTransactions.size(); i++) {
                Transaction transaction = projectTransactions.get(i);
                tableModel.addRow(new Object[]{
                    transaction.getTransactionDate(),
                    transaction.getTransactionSqFt(),
                    transaction.getTransactionPrice(),
                    transaction.getAddress()
                });
            }

            // Add table to scroll pane
            JScrollPane scrollPane = new JScrollPane(table);
            transactionsDialog.add(scrollPane, BorderLayout.CENTER);

            // Close button
            JPanel buttonPanel = new JPanel();
            JButton closeButton = new JButton("Close");
            closeButton.addActionListener(e -> transactionsDialog.dispose());
            buttonPanel.add(closeButton);
            transactionsDialog.add(buttonPanel, BorderLayout.SOUTH);

            transactionsDialog.setLocationRelativeTo(this);
            transactionsDialog.setVisible(true);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new HomeFinderApp().setVisible(true));
    }
}
