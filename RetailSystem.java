package com.retail.billing;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

public class RetailSystem extends JFrame {

    // Inventory data
    HashMap<String, Double> inventory = new HashMap<>();
    JTextArea billArea;
    JTextField quantityField, discountField, billNoField, customerNameField, phoneField;
    JComboBox<String> categoryComboBox, productComboBox;
    double totalAmount = 0;

    public RetailSystem() {
        // Frame settings
        setTitle("Retail Billing System");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Initialize inventory data
        inventory.put("lemon Cinthol", 45.0);
        inventory.put("Dove Soap", 50.0);
        inventory.put("Pears Soap", 55.0);
        inventory.put("Dettol Soap", 25.0);

        // Top Panel for Customer Details
        JPanel topPanel = new JPanel(new GridLayout(2, 4, 10, 10));
        topPanel.add(new JLabel("Bill No:"));
        billNoField = new JTextField();
        topPanel.add(billNoField);
        topPanel.add(new JLabel("Customer Name:"));
        customerNameField = new JTextField();
        topPanel.add(customerNameField);
        topPanel.add(new JLabel("Phone No:"));
        phoneField = new JTextField();
        topPanel.add(phoneField);

        // Middle Panel for Product Selection and Calculator
        JPanel middlePanel = new JPanel(new GridLayout(1, 2));

        // Left Panel for Product Selection
        JPanel leftPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        categoryComboBox = new JComboBox<>(new String[]{"Cosmetics", "Groceries", "Electronics"});
        leftPanel.add(new JLabel("Select Category:"));
        leftPanel.add(categoryComboBox);
        productComboBox = new JComboBox<>(new String[]{"lemon Cinthol", "Dove Soap", "Pears Soap", "Dettol Soap"});
        leftPanel.add(new JLabel("Select Product:"));
        leftPanel.add(productComboBox);
        leftPanel.add(new JLabel("Quantity:"));
        quantityField = new JTextField();
        leftPanel.add(quantityField);
        leftPanel.add(new JLabel("Discount (%):"));
        discountField = new JTextField();
        leftPanel.add(discountField);

        JButton addToCartButton = new JButton("Add to Cart");
        leftPanel.add(addToCartButton);
        addToCartButton.addActionListener(new AddToCartListener());

        // Right Panel for Calculator (Simple)
        JPanel rightPanel = new JPanel(new GridLayout(4, 4, 5, 5));
        JTextField calculatorField = new JTextField();
        rightPanel.add(calculatorField);
        for (int i = 1; i <= 9; i++) {
            JButton button = new JButton(String.valueOf(i));
            button.addActionListener(e -> calculatorField.setText(calculatorField.getText() + button.getText()));
            rightPanel.add(button);
        }
        JButton button0 = new JButton("0");
        button0.addActionListener(e -> calculatorField.setText(calculatorField.getText() + button0.getText()));
        rightPanel.add(button0);
        JButton addButton = new JButton("+");
        addButton.addActionListener(e -> calculatorField.setText(calculatorField.getText() + "+"));
        rightPanel.add(addButton);
        JButton subButton = new JButton("-");
        subButton.addActionListener(e -> calculatorField.setText(calculatorField.getText() + "-"));
        rightPanel.add(subButton);
        JButton mulButton = new JButton("*");
        mulButton.addActionListener(e -> calculatorField.setText(calculatorField.getText() + "*"));
        rightPanel.add(mulButton);
        JButton divButton = new JButton("/");
        divButton.addActionListener(e -> calculatorField.setText(calculatorField.getText() + "/"));
        rightPanel.add(divButton);
        JButton eqButton = new JButton("=");
        eqButton.addActionListener(e -> calculatorField.setText(String.valueOf(eval(calculatorField.getText()))));
        rightPanel.add(eqButton);

        middlePanel.add(leftPanel);
        middlePanel.add(rightPanel);

        // Bottom Panel for Bill Display
        JPanel bottomPanel = new JPanel(new BorderLayout());
        billArea = new JTextArea();
        bottomPanel.add(new JScrollPane(billArea), BorderLayout.CENTER);
        JButton generateBillButton = new JButton("Generate Bill");
        bottomPanel.add(generateBillButton, BorderLayout.SOUTH);
        generateBillButton.addActionListener(new GenerateBillListener());

        // Adding all panels to the frame
        add(topPanel, BorderLayout.NORTH);
        add(middlePanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    // Listener to add product to cart
    class AddToCartListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String product = (String) productComboBox.getSelectedItem();
            int quantity = Integer.parseInt(quantityField.getText());
            double price = inventory.get(product);
            double discount = 0;
            if (!discountField.getText().isEmpty()) {
                discount = Double.parseDouble(discountField.getText());
            }
            double amount = quantity * price * (1 - discount / 100);
            totalAmount += amount;
            billArea.append(product + "\t" + quantity + "\t" + amount + "\n");
        }
    }

    // Listener to generate the final bill
    class GenerateBillListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            billArea.append("\nTotal Amount: " + totalAmount);
        }
    }

    // Simple eval function to evaluate calculator expressions
    public double eval(final String str) {
        return new Object() {
            int pos = -1, ch;

            void nextChar() {
                ch = (++pos < str.length()) ? str.charAt(pos) : -1;
            }

            boolean eat(int charToEat) {
                while (ch == ' ') nextChar();
                if (ch == charToEat) {
                    nextChar();
                    return true;
                }
                return false;
            }

            double parse() {
                nextChar();
                double x = parseExpression();
                if (pos < str.length()) throw new RuntimeException("Unexpected: " + (char) ch);
                return x;
            }

            double parseExpression() {
                double x = parseTerm();
                for (; ; ) {
                    if (eat('+')) x += parseTerm(); // addition
                    else if (eat('-')) x -= parseTerm(); // subtraction
                    else return x;
                }
            }

            double parseTerm() {
                double x = parseFactor();
                for (; ; ) {
                    if (eat('*')) x *= parseFactor(); // multiplication
                    else if (eat('/')) x /= parseFactor(); // division
                    else return x;
                }
            }

            double parseFactor() {
                if (eat('+')) return +parseFactor(); // unary plus
                if (eat('-')) return -parseFactor(); // unary minus

                double x;
                int startPos = this.pos;
                if (eat('(')) { // parentheses
                    x = parseExpression();
                    eat(')');
                } else if ((ch >= '0' && ch <= '9') || ch == '.') { // numbers
                    while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
                    x = Double.parseDouble(str.substring(startPos, this.pos));
                } else {
                    throw new RuntimeException("Unexpected: " + (char) ch);
                }

                return x;
            }
        }.parse();
    }

    public static void main(String[] args) {
        new RetailSystem();
    }
}
