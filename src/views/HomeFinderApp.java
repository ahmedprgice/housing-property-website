package views;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.AbstractDocument;

import controllers.FileHandler;
import models.Property;
import models.Transaction;
import models.User;

public class HomeFinderApp extends JFrame {
    private User loggedInUser;
    private FileHandler fileHandler;
    private List<Property> allProperties;

    public HomeFinderApp(User user) {
        this.loggedInUser = user;
        this.fileHandler = FileHandler.getInstance();
        this.allProperties = fileHandler.readProperties();

        // Set up the main frame
        setTitle("Home Finder - " + (loggedInUser.getRole() == User.Role.SELLER ? "Seller" : "Buyer"));
        setSize(600, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridBagLayout());
        getContentPane().setBackground(new Color(5, 33, 67)); // Background color

        // Create title label
        JLabel titleLabel = new JLabel("Welcome, " + loggedInUser.getUsername(), SwingConstants.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);

        // Buttons common to both roles
        JButton logoutButton = new JButton("Logout");

        // Customize button appearance
        Color buttonColor = new Color(23, 76, 124);
        Font buttonFont = new Font("Arial", Font.BOLD, 18);
        logoutButton.setBackground(buttonColor);
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setFont(buttonFont);
        logoutButton.setPreferredSize(new Dimension(250, 40));
        logoutButton.setFocusPainted(false);
        logoutButton.setBorder(BorderFactory.createEmptyBorder());

        // Action listeners
        logoutButton.addActionListener(e -> {
            this.dispose();
            new LoginPage().setVisible(true);
        });

        // GridBagConstraints settings
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(titleLabel, gbc);

        // Set up the UI based on user role
        if (loggedInUser.getRole() == User.Role.SELLER) {
            setupSellerUI(gbc);
        } else if (loggedInUser.getRole() == User.Role.BUYER) {
            setupBuyerUI(gbc);
        }

        // Add Logout button
        gbc.gridy++;
        add(logoutButton, gbc);

        setLocationRelativeTo(null); // Center the frame
        setVisible(true);
    }

    private void setupSellerUI(GridBagConstraints gbc) {
        JButton addPropertyButton = new JButton("Add New Property");
    
        // Customize button appearance
        Color buttonColor = new Color(23, 76, 124);
        Font buttonFont = new Font("Arial", Font.BOLD, 18);
        JButton[] sellerButtons = {addPropertyButton}; // Removed viewPropertiesButton
    
        for (JButton button : sellerButtons) {
            button.setBackground(buttonColor);
            button.setForeground(Color.WHITE);
            button.setFont(buttonFont);
            button.setPreferredSize(new Dimension(250, 40));
            button.setFocusPainted(false);
            button.setBorder(BorderFactory.createEmptyBorder());
        }
    
        // Action listeners for Seller buttons
        addPropertyButton.addActionListener(e -> openAddPropertyDialog());
    
        // Add Seller-specific buttons to the frame
        gbc.gridy++;
        add(addPropertyButton, gbc);
    
      
    }
    
    private void setupBuyerUI(GridBagConstraints gbc) {
        JButton searchPropertiesButton = new JButton("Search for Properties");
        JButton viewTransactionsButton = new JButton("View Project Transactions");
        JButton displayAllPropertiesButton = new JButton("Display All Properties"); // New button
    
        // Customize button appearance
        Color buttonColor = new Color(23, 76, 124);
        Font buttonFont = new Font("Arial", Font.BOLD, 18);
        JButton[] buyerButtons = {searchPropertiesButton, viewTransactionsButton, displayAllPropertiesButton};
    
        for (JButton button : buyerButtons) {
            button.setBackground(buttonColor);
            button.setForeground(Color.WHITE);
            button.setFont(buttonFont);
            button.setPreferredSize(new Dimension(250, 40));
            button.setFocusPainted(false);
            button.setBorder(BorderFactory.createEmptyBorder());
        }
    
        // Action listeners for Buyer buttons
        searchPropertiesButton.addActionListener(e -> openPropertySearchUI());
        viewTransactionsButton.addActionListener(e -> viewBuyerTransactions());
        displayAllPropertiesButton.addActionListener(e -> displayAllProperties()); // New action listener
    
        // Add Buyer-specific buttons to the frame
        gbc.gridy++;
        add(searchPropertiesButton, gbc);
    
        gbc.gridy++;
        add(viewTransactionsButton, gbc);
    
        gbc.gridy++;
        add(displayAllPropertiesButton, gbc); // Add the button to the layout
    }
    // Method to display all properties
private void displayAllProperties() {
    JDialog propertiesDialog = new JDialog(this, "All Properties", true);
    propertiesDialog.setSize(750, 600);
    propertiesDialog.setLayout(new BorderLayout());
    propertiesDialog.setLocationRelativeTo(this);

    // Container for all property panels
    JPanel propertiesPanel = new JPanel();
    propertiesPanel.setLayout(new BoxLayout(propertiesPanel, BoxLayout.Y_AXIS));
    propertiesPanel.setBackground(new Color(210, 225, 240)); // Light blue background

    // Add each property to the propertiesPanel
    for (Property property : allProperties) {
        JPanel propertyPanel = createPropertyPanel(property);
        propertiesPanel.add(propertyPanel);
        propertiesPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Spacer between panels
    }

    // Scroll pane for the properties panel
    JScrollPane scrollPane = new JScrollPane(propertiesPanel);
    scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

    // Add a close button at the bottom
    JPanel buttonPanel = new JPanel();
    buttonPanel.setBackground(new Color(5, 33, 67));
    JButton closeButton = new JButton("Close");
    closeButton.setFont(new Font("Arial", Font.BOLD, 16));
    closeButton.setPreferredSize(new Dimension(100, 40));
    closeButton.setBackground(new Color(23, 76, 124));
    closeButton.setForeground(Color.WHITE);
    closeButton.setFocusPainted(false);
    closeButton.addActionListener(e -> propertiesDialog.dispose());
    buttonPanel.add(closeButton);

    // Add scroll pane and button panel to the dialog
    propertiesDialog.add(scrollPane, BorderLayout.CENTER);
    propertiesDialog.add(buttonPanel, BorderLayout.SOUTH);

    propertiesDialog.setVisible(true);
}

    
    // Seller functionality: Open dialog to add a new property
    private void openAddPropertyDialog() {
        JDialog addDialog = new JDialog(this, "Add New Property", true);
        addDialog.setSize(400, 600);
        addDialog.setLayout(new GridBagLayout());
        addDialog.getContentPane().setBackground(new Color(5, 33, 67)); // Background color
        addDialog.setResizable(false); // Fixed size

        JLabel titleLabel = new JLabel("Add New Property", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        addDialog.add(titleLabel, gbc);

        gbc.gridwidth = 1;

        JTextField sizeSqMField = createNumericInputField("Size (sqm):", gbc, addDialog, 1);
        JTextField sqFtField = createNumericInputField("Size (sqft):", gbc, addDialog, 2);
        JTextField propertyTypeField = createInputField("Property Type:", gbc, addDialog, 3);
        JTextField noOfFloorsField = createNumericInputField("Number of Floors:", gbc, addDialog, 4);
        JTextField addressField = createInputField("Address:", gbc, addDialog, 5);
        JTextField schemeField = createInputField("Project Name:", gbc, addDialog, 6);
        JTextField priceField = createNumericInputField("Price:", gbc, addDialog, 7);
        JTextField yearField = createNumericInputField("Year:", gbc, addDialog, 8);
        JTextField pricePerSqFtField = createNumericInputField("Price per sqft:", gbc, addDialog, 9);

        gbc.gridx = 0;
        gbc.gridy = 10;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        JButton addButton = new JButton("Add Property");
        addButton.setBackground(new Color(23, 76, 124));
        addButton.setForeground(Color.WHITE);
        addButton.setFont(new Font("Arial", Font.BOLD, 18));
        addButton.setPreferredSize(new Dimension(250, 40));
        addButton.setFocusPainted(false);
        addButton.setBorder(BorderFactory.createEmptyBorder());
        addDialog.add(addButton, gbc);

        addButton.addActionListener(e -> {
            try {
                int sizeSqM = Integer.parseInt(sizeSqMField.getText());
                int sqFt = Integer.parseInt(sqFtField.getText());
                String propertyType = propertyTypeField.getText().trim();
                int noOfFloors = Integer.parseInt(noOfFloorsField.getText());
                String address = addressField.getText().trim();
                String scheme = schemeField.getText().trim();
                double price = Double.parseDouble(priceField.getText());
                int year = Integer.parseInt(yearField.getText());
                double pricePerSqFt = Double.parseDouble(pricePerSqFtField.getText());

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
                addDialog.dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(addDialog, "Please enter valid numbers in the numeric fields.", "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        addDialog.setLocationRelativeTo(this);
        addDialog.setVisible(true);
    }

    // Buyer functionality: Open property search UI
    private void openPropertySearchUI() {
        JDialog searchDialog = new JDialog(this, "Property Search", true);
        searchDialog.setSize(400, 600);
        searchDialog.setLayout(new GridBagLayout());
        searchDialog.getContentPane().setBackground(new Color(5, 33, 67));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel infoLabel = new JLabel("Search for a property", SwingConstants.CENTER);
        infoLabel.setForeground(Color.WHITE);
        infoLabel.setFont(new Font("Arial", Font.BOLD, 24));

        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = 0;
        searchDialog.add(infoLabel, gbc);

        gbc.gridwidth = 1;
        gbc.gridy++;

        JTextField minPriceField = createNumericInputField("Minimum Price:", gbc, searchDialog, gbc.gridy);
        gbc.gridy++;
        JTextField maxPriceField = createNumericInputField("Maximum Price:", gbc, searchDialog, gbc.gridy);
        gbc.gridy++;
        JTextField minSqFtField = createNumericInputField("Minimum SqFt:", gbc, searchDialog, gbc.gridy);
        gbc.gridy++;
        JTextField maxSqFtField = createNumericInputField("Maximum SqFt:", gbc, searchDialog, gbc.gridy);
        gbc.gridy++;
        JTextField propertyTypeField = createInputField("Property Type:", gbc, searchDialog, gbc.gridy);
        gbc.gridy++;

        // Project Name ComboBox
        JComboBox<String> projectNameComboBox = new JComboBox<>(getAllProjectNames());
        projectNameComboBox.setPreferredSize(new Dimension(200, 30));
        projectNameComboBox.setBackground(Color.WHITE);
        projectNameComboBox.setForeground(Color.BLACK);
        projectNameComboBox.setFont(new Font("Arial", Font.PLAIN, 16));
        addInputField(searchDialog, gbc, "Project Name:", projectNameComboBox, gbc.gridy);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        JButton searchButton = new JButton("Search");
        searchButton.setPreferredSize(new Dimension(200, 40));
        searchButton.setBackground(new Color(23, 76, 124));
        searchButton.setForeground(Color.WHITE);
        searchButton.setFont(new Font("Arial", Font.BOLD, 18));
        searchDialog.add(searchButton, gbc);

        searchButton.addActionListener(e -> {
            try {
                double minPrice = Double.parseDouble(minPriceField.getText().isEmpty() ? "0" : minPriceField.getText());
                double maxPrice = Double.parseDouble(maxPriceField.getText().isEmpty() ? String.valueOf(Double.MAX_VALUE) : maxPriceField.getText());
                int minSqFt = Integer.parseInt(minSqFtField.getText().isEmpty() ? "0" : minSqFtField.getText());
                int maxSqFt = Integer.parseInt(maxSqFtField.getText().isEmpty() ? String.valueOf(Integer.MAX_VALUE) : maxSqFtField.getText());
                String propertyType = propertyTypeField.getText().trim();
                String projectName = (String) projectNameComboBox.getSelectedItem();

                List<Property> results = filterProperties(allProperties, minSqFt, maxSqFt, minPrice, maxPrice, propertyType, projectName);
                displaySearchResults(results);
                searchDialog.dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter valid numbers for price and square footage.", "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        searchDialog.setLocationRelativeTo(this);
        searchDialog.setVisible(true);
    }

    // Buyer functionality: Display search results
    private void displaySearchResults(List<Property> results) {
        JDialog resultsDialog = new JDialog(this, "Search Results", true);
        resultsDialog.setSize(750, 600);
        resultsDialog.setLayout(new BorderLayout());
        resultsDialog.setLocationRelativeTo(this);

        JPanel resultsPanel = new JPanel();
        resultsPanel.setLayout(new BoxLayout(resultsPanel, BoxLayout.Y_AXIS));
        resultsPanel.setBackground(new Color(210, 225, 240)); // Light blue background

        for (Property property : results) {
            JPanel propertyPanel = createPropertyPanel(property);
            resultsPanel.add(propertyPanel);
            resultsPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Spacer between panels
        }

        JScrollPane scrollPane = new JScrollPane(resultsPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(5, 33, 67));
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

    // Buyer functionality: View transaction history
    private void viewBuyerTransactions() {
        List<Transaction> allTransactions = fileHandler.loadTransactions();
        String projectName = JOptionPane.showInputDialog(this, "Enter the project name to view your transactions:");
    
        if (projectName == null || projectName.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a valid project name.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
    
        List<Transaction> projectTransactions = filterTransactionsByProjectName(allTransactions, projectName);
    
        // Sort transactions by date in descending order
        projectTransactions.sort(Comparator.comparing(Transaction::getTransactionDate).reversed());
    
        // Get the last 5 transactions, if available
        List<Transaction> lastFiveTransactions = projectTransactions.size() > 5 ?
                projectTransactions.subList(0, 5) : projectTransactions;
    
        if (lastFiveTransactions.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No transactions found for the project: " + projectName, "No Results", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JDialog transactionsDialog = new JDialog(this, "Last 5 Transactions for " + projectName, true);
            transactionsDialog.setSize(800, 400);
            transactionsDialog.setLayout(new BorderLayout());
    
            String[] columnNames = {"Date", "Size SqFt", "Price", "Address"};
            DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
            JTable table = new JTable(tableModel);
    
            for (Transaction transaction : lastFiveTransactions) {
                tableModel.addRow(new Object[]{
                        transaction.getTransactionDate(),
                        transaction.getTransactionSqFt(),
                        transaction.getTransactionPrice(),
                        transaction.getAddress()
                });
            }
    
            JScrollPane scrollPane = new JScrollPane(table);
            transactionsDialog.add(scrollPane, BorderLayout.CENTER);
    
            JPanel buttonPanel = new JPanel();
            JButton closeButton = new JButton("Close");
            closeButton.addActionListener(e -> transactionsDialog.dispose());
            buttonPanel.add(closeButton);
            transactionsDialog.add(buttonPanel, BorderLayout.SOUTH);
    
            transactionsDialog.setLocationRelativeTo(this);
            transactionsDialog.setVisible(true);
        }
    }
    
    // Seller functionality: Display properties added by seller
    private void displaySellerProperties() {
        List<Property> sellerProperties = new ArrayList<>(); // Replace with properties added by this seller

        JDialog propertiesDialog = new JDialog(this, "My Properties", true);
        propertiesDialog.setSize(750, 600);
        propertiesDialog.setLayout(new BorderLayout());
        propertiesDialog.setLocationRelativeTo(this);

        JPanel propertiesPanel = new JPanel();
        propertiesPanel.setLayout(new BoxLayout(propertiesPanel, BoxLayout.Y_AXIS));
        propertiesPanel.setBackground(new Color(210, 225, 240));

        for (Property property : sellerProperties) {
            JPanel propertyPanel = createPropertyPanel(property);
            propertiesPanel.add(propertyPanel);
            propertiesPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        }

        JScrollPane scrollPane = new JScrollPane(propertiesPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(5, 33, 67));
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

    // Helper method to create labeled input fields
    private JTextField createInputField(String label, GridBagConstraints gbc, JDialog dialog, int row) {
        gbc.gridx = 0;
        gbc.gridy = row;
        JLabel jLabel = new JLabel(label, SwingConstants.RIGHT);
        jLabel.setForeground(Color.WHITE);
        dialog.add(jLabel, gbc);

        gbc.gridx = 1;
        JTextField field = new JTextField();
        field.setPreferredSize(new Dimension(160, 25));
        dialog.add(field, gbc);

        return field;
    }

    // Helper method to create numeric input fields
    private JTextField createNumericInputField(String label, GridBagConstraints gbc, JDialog dialog, int row) {
        gbc.gridx = 0;
        gbc.gridy = row;
        JLabel jLabel = new JLabel(label, SwingConstants.RIGHT);
        jLabel.setForeground(Color.WHITE);
        dialog.add(jLabel, gbc);

        gbc.gridx = 1;
        JTextField field = new JTextField();
        field.setPreferredSize(new Dimension(160, 25));
        ((AbstractDocument) field.getDocument()).setDocumentFilter(new NumericDocumentFilter());
        dialog.add(field, gbc);

        return field;
    }

    private void addInputField(JDialog dialog, GridBagConstraints gbc, String label, JComboBox<String> comboBox, int row) {
        gbc.gridx = 0;
        gbc.gridy = row;
        JLabel jLabel = new JLabel(label, SwingConstants.RIGHT);
        jLabel.setForeground(Color.WHITE);
        jLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        dialog.add(jLabel, gbc);

        gbc.gridx = 1;
        dialog.add(comboBox, gbc);
    }

    // Helper method to create a property panel
    private JPanel createPropertyPanel(Property property) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setPreferredSize(new Dimension(800, 250));
        panel.setBorder(BorderFactory.createLineBorder(new Color(5, 33, 67), 2));
        panel.setBackground(new Color(230, 240, 255));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;

        JLabel imageLabel = new JLabel();
        ImageIcon propertyImage = loadImage(property.getScheme());
        imageLabel.setIcon(propertyImage);
        imageLabel.setPreferredSize(new Dimension(200, 200));
        gbc.gridheight = 6;
        panel.add(imageLabel, gbc);

        gbc.gridheight = 1;
        gbc.gridx = 1;

        JLabel projectNameLabel = new JLabel("Project: " + property.getScheme());
        projectNameLabel.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(projectNameLabel, gbc);

        gbc.gridy++;
        JLabel locationLabel = new JLabel("Location: " + property.getAddress());
        locationLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        panel.add(locationLabel, gbc);

        gbc.gridy++;
        JLabel sizeLabel = new JLabel("Size: " + property.getSqFt() + " SqFt (" + property.getSizeSqM() + " SqM)");
        sizeLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        panel.add(sizeLabel, gbc);

        gbc.gridy++;
        JLabel floorsLabel = new JLabel("Number of Floors: " + property.getNoOfFloors());
        floorsLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        panel.add(floorsLabel, gbc);

        gbc.gridy++;
        JLabel priceLabel = new JLabel("Price: $" + String.format("%.1f", property.getPrice()));
        priceLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        panel.add(priceLabel, gbc);

        gbc.gridy++;
        JLabel yearLabel = new JLabel("Year: " + property.getYear());
        yearLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        panel.add(yearLabel, gbc);

        gbc.gridy++;
        JLabel pricePerSqFtLabel = new JLabel("Price per Sq Ft: $" + String.format("%.1f", property.getPricePerSqft()));
        pricePerSqFtLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        panel.add(pricePerSqFtLabel, gbc);

        gbc.gridy++;
        JButton buyButton = new JButton("Buy");
        buyButton.setPreferredSize(new Dimension(100, 30));
        buyButton.setBackground(new Color(46, 204, 113));
        buyButton.setForeground(Color.WHITE);
        buyButton.setFont(new Font("Arial", Font.BOLD, 16));
        buyButton.addActionListener(e -> buyProperty(property, null));
        panel.add(buyButton, gbc);

        return panel;
    }

    // Helper method to filter properties
    private List<Property> filterProperties(List<Property> properties, int minSqFt, int maxSqFt, double minPrice, double maxPrice, String propertyType, String projectName) {
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

    // Helper method to buy a property
    private void buyProperty(Property property, JDialog resultsDialog) {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to buy this property?\n" + property.getDetails(),
                "Confirm Purchase",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            Transaction transaction = new Transaction.Builder()
                    .setProjectName(property.getScheme())
                    .setTransactionDate(LocalDate.now())
                    .setTransactionPrice(property.getPrice())
                    .setTransactionSqFt(property.getSqFt())
                    .setSizeSqM(property.getSizeSqM())
                    .setPropertyType(property.getPropertyType())
                    .setNoOfFloors(property.getNoOfFloors())
                    .setAddress(property.getAddress())
                    .setScheme(property.getScheme())
                    .setYear(property.getYear())
                    .setPricePerSqft(property.getPricePerSqft())
                    .build();

            try {
                fileHandler.writeTransaction(transaction);
                allProperties.remove(property);
                fileHandler.updatePropertiesFile(allProperties);

                JOptionPane.showMessageDialog(this, "Property purchased successfully!\nTransaction recorded.", "Success", JOptionPane.INFORMATION_MESSAGE);
                if (resultsDialog != null) {
                    resultsDialog.dispose();
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Failed to record the transaction: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Helper method to get all unique project names for the ComboBox
    private String[] getAllProjectNames() {
        Set<String> projectNames = new HashSet<>();
        for (Property property : allProperties) {
            projectNames.add(property.getScheme());
        }
        return projectNames.toArray(new String[0]);
    }

    // Helper method to load an image for the property based on the scheme or project name
    private ImageIcon loadImage(String scheme) {
        String imagePath = "images/" + scheme.toLowerCase().replace(" ", "_") + ".jpg";
        File imageFile = new File(imagePath);
        if (imageFile.exists()) {
            ImageIcon icon = new ImageIcon(imagePath);
            Image img = icon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
            return new ImageIcon(img);
        } else {
            return new ImageIcon(new ImageIcon("pexels-binyamin-mellish-106399.jpg").getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH));
        }
    }

    // Helper method to filter transactions by project name
    public static List<Transaction> filterTransactionsByProjectName(List<Transaction> transactions, String projectName) {
        List<Transaction> filteredTransactions = new ArrayList<>();
        for (Transaction transaction : transactions) {
            if (transaction.getProjectName().equalsIgnoreCase(projectName)) {
                filteredTransactions.add(transaction);
            }
        }
        return filteredTransactions;
    }

    // NumericDocumentFilter class to allow only numeric input
    class NumericDocumentFilter extends javax.swing.text.DocumentFilter {
        @Override
        public void insertString(javax.swing.text.DocumentFilter.FilterBypass fb, int offset, String string, javax.swing.text.AttributeSet attr) throws javax.swing.text.BadLocationException {
            if (string.matches("\\d*")) {
                super.insertString(fb, offset, string, attr);
            } else {
                Toolkit.getDefaultToolkit().beep();
            }
        }

        @Override
        public void replace(javax.swing.text.DocumentFilter.FilterBypass fb, int offset, int length, String text, javax.swing.text.AttributeSet attrs) throws javax.swing.text.BadLocationException {
            if (text.matches("\\d*")) {
                super.replace(fb, offset, length, text, attrs);
            } else {
                Toolkit.getDefaultToolkit().beep();
            }
        }

        @Override
        public void remove(javax.swing.text.DocumentFilter.FilterBypass fb, int offset, int length) throws javax.swing.text.BadLocationException {
            super.remove(fb, offset, length);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginPage().setVisible(true));
    }
}
