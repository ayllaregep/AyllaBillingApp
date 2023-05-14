package main;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Panel extends JFrame{
    private JPanel cards;
    private CardLayout cardLayout;
    private UserData userData;
    private Factura tempFac;

    public Panel() {
        userData = new UserData();
        tempFac = new Factura();
        setTitle("Billing App");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 480);

        // Create a panel for the card layout
        cards = new JPanel();
        cardLayout = new CardLayout();
        cards.setLayout(cardLayout);

        // Create and add the registration panel
        JPanel registrationPanel = createRegistrationPanel();
        cards.add(registrationPanel, "Registration");

        // Create and add the login panel
        JPanel loginPanel = createLoginPanel();
        cards.add(loginPanel, "Login");

        JPanel appPanel = createAppPanel();
        cards.add(appPanel, "App");

        JPanel createBillsPanel = createBill();
        cards.add(createBillsPanel, "CreateBill");

        // Set the default visible panel
        cardLayout.show(cards, "Login");

        // Add the cards panel to the JFrame
        add(cards);

        setVisible(true);
    }

    private JPanel createRegistrationPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(11, 1));
        panel.setBackground(new Color(228, 241, 254)); // Set background color

        JLabel titleLabel = new JLabel("Aylla's Billing App");
        titleLabel.setFont(titleLabel.getFont().deriveFont(24f).deriveFont(Font.BOLD)); // Increase font size and set bold
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setForeground(new Color(102, 102, 255)); // Set text color

        JLabel nameLabel = new JLabel("Name:");
        JTextField nameField = new JTextField();

        JLabel surnameLabel = new JLabel("Surname:");
        JTextField surnameField = new JTextField();

        JLabel emailLabel = new JLabel("Email:");
        JTextField emailField = new JTextField();

        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField();

        JButton registerButton = new JButton("Register");
        registerButton.setBackground(new Color(102, 102, 255)); // Set button background color
        registerButton.setForeground(Color.WHITE); // Set button text color
        registerButton.setFont(registerButton.getFont().deriveFont(16f)); // Increase button text size

        JButton goToLogInButton = new JButton("Already have an account? Log in");
        goToLogInButton.setForeground(new Color(102, 102, 255));
        goToLogInButton.setBorderPainted(false); // Remove button border
        goToLogInButton.setOpaque(false); // Make button transparent
        goToLogInButton.setContentAreaFilled(false); // Make button content area transparent
        goToLogInButton.setFont(goToLogInButton.getFont().deriveFont(12f).deriveFont(Font.BOLD));

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Perform registration logic here
                String name = nameField.getText();
                String email = emailField.getText();
                String surname = surnameField.getText();
                String password = new String(passwordField.getPassword());

                Database.registerUser(name, surname, email, password);
                nameField.setText("");
                surnameLabel.setText("");
                emailField.setText("");
                passwordField.setText("");

                cardLayout.show(cards, "Login");
            }
        });

        goToLogInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cards, "Login");
            }
        });

        panel.add(titleLabel);
        panel.add(nameLabel);
        panel.add(nameField);
        panel.add(surnameLabel);
        panel.add(surnameField);
        panel.add(emailLabel);
        panel.add(emailField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(registerButton);
        panel.add(goToLogInButton);

        return panel;
    }

    private JPanel createLoginPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(7, 1));
        panel.setBackground(new Color(228, 241, 254));

        JLabel titleLabel = new JLabel("Aylla's Billing App");
        titleLabel.setFont(titleLabel.getFont().deriveFont(24f).deriveFont(Font.BOLD));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setForeground(new Color(102, 102, 255));

        JLabel emailLabel = new JLabel("Email:");
        JTextField emailField = new JTextField();

        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField();

        JButton loginButton = new JButton("Login");
        loginButton.setBackground(new Color(102, 102, 255));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFont(loginButton.getFont().deriveFont(16f));

        JButton goToRegister = new JButton("Don't have an account, register here!");
        goToRegister.setForeground(new Color(102, 102, 255));
        goToRegister.setBorderPainted(false);
        goToRegister.setOpaque(false);
        goToRegister.setContentAreaFilled(false);
        goToRegister.setFont(goToRegister.getFont().deriveFont(12f).deriveFont(Font.BOLD));

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = emailField.getText();
                String password = new String(passwordField.getPassword());

                if (Database.loginUser(email, password, userData)) {
                    cardLayout.show(cards, "App");
                }
            }
        });

        goToRegister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cards, "Registration");
            }
        });

        panel.add(titleLabel);
        panel.add(emailLabel);
        panel.add(emailField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(loginButton);
        panel.add(goToRegister);

        return panel;
    }

    private JPanel createAppPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 1));
        panel.setBackground(new Color(228, 241, 254));

        JLabel nameLabel = new JLabel("Aylla's Billing App");
        nameLabel.setFont(nameLabel.getFont().deriveFont(24f).deriveFont(Font.BOLD));
        nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        nameLabel.setForeground(new Color(102, 102, 255));

        JButton goToBillsButton = new JButton("Billing History");
        goToBillsButton.setBackground(new Color(102, 102, 255));
        goToBillsButton.setForeground(Color.WHITE);
        goToBillsButton.setFont(goToBillsButton.getFont().deriveFont(16f));

        JButton createNewBillButton = new JButton("Register New Bill");
        createNewBillButton.setBackground(new Color(102, 102, 255));
        createNewBillButton.setForeground(Color.WHITE);
        createNewBillButton.setFont(createNewBillButton.getFont().deriveFont(16f));

        goToBillsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JPanel viewBillsPanel = viewBills();
                cards.add(viewBillsPanel, "ViewBills");
                cardLayout.show(cards, "ViewBills");
            }
        });

        createNewBillButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cards, "CreateBill");
            }
        });

        panel.add(nameLabel);
        panel.add(goToBillsButton);
        panel.add(createNewBillButton);

        return panel;
    }

    private JPanel viewBills() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(228, 241, 254));
        List<Factura> listaFactura = Database.getBillingStatements(userData.id);
        System.out.println(listaFactura.size());

        panel.setLayout(new BorderLayout());

        JLabel nameLabel = new JLabel("Aylla's Billing App");
        nameLabel.setFont(nameLabel.getFont().deriveFont(24f).deriveFont(Font.BOLD));
        nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        nameLabel.setForeground(new Color(102, 102, 255));

        panel.add(nameLabel, BorderLayout.NORTH);

        JPanel facturaPanel = new JPanel();
        facturaPanel.setBackground(new Color(228, 241, 254));
        facturaPanel.setLayout(new GridLayout(listaFactura.size(), 1));

        for (Factura factura : listaFactura) {
            JPanel facturaItemPanel = factura.toPanel();
            facturaPanel.add(facturaItemPanel);
        }

        JScrollPane scrollPane = new JScrollPane(facturaPanel);
        panel.add(scrollPane, BorderLayout.CENTER);

        JButton goBackButton = new JButton("Back");
        goBackButton.setBackground(new Color(102, 102, 255));
        goBackButton.setForeground(Color.WHITE);
        goBackButton.setFont(goBackButton.getFont().deriveFont(16f));

        goBackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cards, "App");
            }
        });

        panel.add(goBackButton, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createBill() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(228, 241, 254));
        panel.setLayout(new GridLayout(7, 1));

        JLabel nameLabel = new JLabel("Aylla's Billing App");
        nameLabel.setFont(nameLabel.getFont().deriveFont(24f).deriveFont(Font.BOLD));
        nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        nameLabel.setForeground(new Color(102, 102, 255));
        JLabel electricLabel = new JLabel("Consumed kW:");
        electricLabel.setFont(electricLabel.getFont().deriveFont(16f));
        JTextField electricField = new JTextField();

        JLabel addressLabel = new JLabel("Address:");
        addressLabel.setFont(addressLabel.getFont().deriveFont(16f));
        JTextField addressField = new JTextField();

        JButton submitButton = new JButton("Submit");
        submitButton.setBackground(new Color(102, 102, 255));
        submitButton.setForeground(Color.WHITE);
        submitButton.setFont(submitButton.getFont().deriveFont(16f));

        JButton backButton = new JButton("Back");
        backButton.setBackground(new Color(102, 102, 255));
        backButton.setForeground(Color.WHITE);
        backButton.setFont(backButton.getFont().deriveFont(16f));

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                Date date = new Date();
                double kW = Double.parseDouble(electricField.getText());
                double amount = kW * 0.80;
                String fullName = userData.name + " " + userData.surname;
                tempFac = new Factura(userData.id, fullName, addressField.getText(), kW, amount, date);

                JPanel payBillPanel = payBill();
                cards.add(payBillPanel, "PayBill");

                Database.registerBill(userData.id, date, amount, kW, addressField.getText());
                electricField.setText("");
                addressField.setText("");
                cardLayout.show(cards, "PayBill");
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cards, "App");
            }
        });

        panel.add(nameLabel);
        panel.add(electricLabel);
        panel.add(electricField);
        panel.add(addressLabel);
        panel.add(addressField);
        panel.add(submitButton);
        panel.add(backButton);

        return panel;
    }

    private JPanel payBill() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(228, 241, 254));
        panel.setLayout(new GridLayout(3, 1));

        JLabel nameLabel = new JLabel("Aylla's Billing App");
        nameLabel.setFont(nameLabel.getFont().deriveFont(24f).deriveFont(Font.BOLD));
        nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        nameLabel.setForeground(new Color(102, 102, 255));

        JButton payButton = new JButton("Pay Now");
        payButton.setBackground(new Color(102, 102, 255));
        payButton.setForeground(Color.WHITE);
        payButton.setFont(payButton.getFont().deriveFont(16f));

        JPanel facturaPanel = tempFac.toPanel();

        payButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cards, "App");
            }
        });

        panel.add(nameLabel);
        panel.add(facturaPanel);
        panel.add(payButton);

        return panel;
    }
}


