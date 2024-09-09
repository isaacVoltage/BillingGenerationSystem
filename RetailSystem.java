package com.retail.billing;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Vector;

public class RetailSystem extends JFrame {

    HashMap<String, HashMap<String, Double>> inventory = new HashMap<>();
    JTextArea billArea;
    JTextField quantityField, discountField, billNoField, customerNameField, phoneField;
    JComboBox<String> categoryComboBox, productComboBox;
    double totalAmount = 0;

    public RetailSystem() {
        // Frame settings
        setTitle("Retail Billing System");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Initialize inventory data with categories and products
        HashMap<String, Double> cosmetics = new HashMap<>();
        cosmetics.put("lemon Cinthol", 45.0);
        cosmetics.put("Dove Soap", 50.0);
        cosmetics.put("Pears Soap", 55.0);
        cosmetics.put("Dettol Soap", 25.0);

        HashMap<String, Double> groceries = new HashMap<>();
        groceries.put("Rice", 60.0);
        groceries.put("Wheat", 40.0);
        groceries.put("Oil", 120.0);
        groceries.put("Sugar", 45.0);

        HashMap<String, Double> electronics = new HashMap<>();
        electronics.put("Mobile", 15000.0);
        electronics.put("Laptop", 45000.0);
        electronics.put("Headphones", 1500.0);
        electronics.put("Charger", 500.0);

        inventory.put("Cosmetics", cosmetics);
        inventory.put("Groceries", groceries);
        inventory.put("Electronics", electronics);

        // Top Panel for Customer Details (Aligned using GridBagLayout)
        JPanel topPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Padding

        gbc.gridx = 0;
        gbc.gridy = 0;
        topPanel.add(new JLabel("Bill No:"), gbc);
        gbc.gridx = 1;
        billNoField = new JTextField(15);
        topPanel.add(billNoField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        topPanel.add(new JLabel("Customer Name:"), gbc);
        gbc.gridx = 1;
        customerNameField = new JTextField(15);
        topPanel.add(customerNameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        topPanel.add(new JLabel("Phone No:"), gbc);
        gbc.gridx = 1;
        phoneField = new JTextField(15);
        topPanel.add(phoneField, gbc);

        // Middle Panel for Product Selection
        JPanel middlePanel = new JPanel(new GridLayout(5, 2, 10, 10));

        categoryComboBox = new JComboBox<>(new String[]{"Cosmetics", "Groceries", "Electronics"});
        categoryComboBox.addActionListener(new CategorySelectionListener());
        middlePanel.add(new JLabel("Select Category:"));
        middlePanel.add(categoryComboBox);

        productComboBox = new JComboBox<>(new Vector<>(inventory.get("Cosmetics").keySet()));
        middlePanel.add(new JLabel("Select Product:"));
        middlePanel.add(productComboBox);

        middlePanel.add(new JLabel("Quantity:"));
        quantityField = new JTextField();
        middlePanel.add(quantityField);

        middlePanel.add(new JLabel("Discount (%):"));
        discountField = new JTextField();
        middlePanel.add(discountField);

        JButton addToCartButton = new JButton("Add to Cart");
        middlePanel.add(addToCartButton);
        addToCartButton.addActionListener(new AddToCartListener());

        // Bottom Panel for Bill Display
        JPanel bottomPanel = new JPanel(new BorderLayout());
        billArea = new JTextArea();
        billArea.setFont(new Font("Monospaced", Font.PLAIN, 14));  // Setting a monospaced font for better alignment
        bottomPanel.add(new JScrollPane(billArea), BorderLayout.CENTER);
        JButton generateBillButton = new JButton("Generate Bill");
        bottomPanel.add(generateBillButton, BorderLayout.SOUTH);
        generateBillButton.addActionListener(new GenerateBillListener());

        // Adjusting the area of bill output
        billArea.setRows(15);

        // Adding all panels to the frame
        add(topPanel, BorderLayout.NORTH);
        add(middlePanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    // Listener to handle category selection and update product combo box
    class CategorySelectionListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String selectedCategory = (String) categoryComboBox.getSelectedItem();
            if (selectedCategory != null) {
                productComboBox.removeAllItems();
                for (String product : inventory.get(selectedCategory).keySet()) {
                    productComboBox.addItem(product);
                }
            }
        }
    }

    // Listener to add product to cart
    class AddToCartListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String product = (String) productComboBox.getSelectedItem();
            if (product != null) {
                int quantity = Integer.parseInt(quantityField.getText());
                String selectedCategory = (String) categoryComboBox.getSelectedItem();
                double price = inventory.get(selectedCategory).get(product);
                double discount = 0;
                if (!discountField.getText().isEmpty()) {
                    discount = Double.parseDouble(discountField.getText());
                }
                double amount = quantity * price * (1 - discount / 100);
                totalAmount += amount;
                billArea.append(product + "\t" + quantity + "\t" + amount + "\n");
            }
        }
    }

    // Listener to generate the final bill
    class GenerateBillListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            billArea.append("\nCustomer Name: " + customerNameField.getText());
            billArea.append("\nBill No: " + billNoField.getText());
            billArea.append("\n\nTotal Amount: " + totalAmount);
        }
    }

    public static void main(String[] args) {
        new RetailSystem();
    }
}
