

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

public class ATMInterfaceGUI {

    private static class ATM {
        private Map<String, User> users;
        private User currentUser;

        public ATM() {
            this.users = new HashMap<>();
            // Adding an existing user
            addUser("Venu", "venu", 10000.0);
        }

        public void addUser(String userID, String userPIN, double accountBalance) {
            users.put(userID, new User(userID, userPIN, accountBalance));
        }

        public boolean authenticateUser(String userID, String userPIN) {
            if (users.containsKey(userID)) {
                User user = users.get(userID);
                if (user.getUserPIN().equals(userPIN)) {
                    currentUser = user;
                    return true;
                }
            }
            return false;
        }

        public double getAccountBalance() {
            return currentUser.getAccountBalance();
        }

        public void setAccountBalance(double accountBalance) {
            currentUser.setAccountBalance(accountBalance);
        }
    }

    private static class User {
        private String userID;
        private String userPIN;
        private double accountBalance;

        public User(String userID, String userPIN, double accountBalance) {
            this.userID = userID;
            this.userPIN = userPIN;
            this.accountBalance = accountBalance;
        }

        public String getUserID() {
            return userID;
        }

        public String getUserPIN() {
            return userPIN;
        }

        public double getAccountBalance() {
            return accountBalance;
        }

        public void setAccountBalance(double accountBalance) {
            this.accountBalance = accountBalance;
        }
    }

    public static void main(String[] args) {
        ATM atm = new ATM();

        JFrame frame = new JFrame("ATM Interface");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout());
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        placeComponents(controlPanel, atm);

        mainPanel.add(controlPanel, BorderLayout.CENTER);
        frame.add(mainPanel);
        frame.setVisible(true);
    }

    private static void placeComponents(JPanel panel, ATM atm) {
        panel.setLayout(null);

        JLabel userLabel = new JLabel("User ID:");
        userLabel.setBounds(10, 20, 80, 25);
        panel.add(userLabel);

        JTextField userText = new JTextField(20);
        userText.setBounds(100, 20, 165, 25);
        panel.add(userText);

        JLabel pinLabel = new JLabel("PIN:");
        pinLabel.setBounds(10, 50, 80, 25);
        panel.add(pinLabel);

        JPasswordField pinText = new JPasswordField(20);
        pinText.setBounds(100, 50, 165, 25);
        panel.add(pinText);

        JButton loginButton = new JButton("Login");
        loginButton.setBounds(10, 80, 80, 25);
        panel.add(loginButton);

        JLabel balanceLabel = new JLabel("Balance: $");
        balanceLabel.setBounds(10, 120, 80, 25);
        panel.add(balanceLabel);

        JTextField balanceText = new JTextField();
        balanceText.setBounds(100, 120, 165, 25);
        balanceText.setEditable(false);
        panel.add(balanceText);

        JButton withdrawButton = new JButton("Withdraw");
        withdrawButton.setBounds(10, 160, 100, 25);
        panel.add(withdrawButton);

        JButton depositButton = new JButton("Deposit");
        depositButton.setBounds(120, 160, 100, 25);
        panel.add(depositButton);

        JButton exitButton = new JButton("Exit");
        exitButton.setBounds(230, 160, 80, 25);
        panel.add(exitButton);

        JButton createNewUserButton = new JButton("Create New User");
        createNewUserButton.setBounds(10, 200, 150, 25);
        panel.add(createNewUserButton);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String userID = userText.getText();
                String pin = new String(pinText.getPassword());

                if (atm.authenticateUser(userID, pin)) {
                    balanceText.setText(String.valueOf(atm.getAccountBalance()));
                } else {
                    JOptionPane.showMessageDialog(panel, "Authentication failed. Please check your credentials.");
                }
            }
        });

        withdrawButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String amountStr = JOptionPane.showInputDialog(panel, "Enter withdrawal amount:");
                try {
                    double amount = Double.parseDouble(amountStr);
                    if (amount > 0 && amount <= atm.getAccountBalance()) {
                        atm.setAccountBalance(atm.getAccountBalance() - amount);
                        balanceText.setText(String.valueOf(atm.getAccountBalance()));
                        JOptionPane.showMessageDialog(panel, "Withdrawal successful. Remaining balance: $" + atm.getAccountBalance());
                    } else {
                        JOptionPane.showMessageDialog(panel, "Invalid withdrawal amount or insufficient funds.");
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(panel, "Invalid input. Please enter a valid number.");
                }
            }
        });

        depositButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String amountStr = JOptionPane.showInputDialog(panel, "Enter deposit amount:");
                try {
                    double amount = Double.parseDouble(amountStr);
                    if (amount > 0) {
                        atm.setAccountBalance(atm.getAccountBalance() + amount);
                        balanceText.setText(String.valueOf(atm.getAccountBalance()));
                        JOptionPane.showMessageDialog(panel, "Deposit successful. New balance: $" + atm.getAccountBalance());
                    } else {
                        JOptionPane.showMessageDialog(panel, "Invalid deposit amount.");
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(panel, "Invalid input. Please enter a valid number.");
                }
            }
        });

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        createNewUserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String newUserID = JOptionPane.showInputDialog(panel, "Enter new User ID:");
                String newUserPIN = JOptionPane.showInputDialog(panel, "Enter new PIN:");
                String initialBalanceStr = JOptionPane.showInputDialog(panel, "Enter initial balance:");

                try {
                    double initialBalance = Double.parseDouble(initialBalanceStr);
                    atm.addUser(newUserID, newUserPIN, initialBalance);
                    JOptionPane.showMessageDialog(panel, "New user created successfully!");
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(panel, "Invalid input. Please enter a valid number for initial balance.");
                }
            }
        });
    }
}
