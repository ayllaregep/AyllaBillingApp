package main;

import javax.swing.*;
import java.awt.*;
import java.util.Date;

public class Factura {
    private int user_id;
    private String name;
    private String address;
    private double kW;
    private double amount;
    private Date date;

    public Factura() {
        this.user_id = 0;
        this.name = "NULL";
        this.address = "NULL";
        this.kW = 0;
        this.amount = 0;
        this.date = new Date();
    }

    public Factura(int user_id, String name, String address, double kW, double amount, Date date){
        this.user_id = user_id;
        this.name = name;
        this.address = address;
        this.kW = kW;
        this.amount = amount;
        this.date = date;
    }

    public JPanel toPanel() {
        JPanel panel = new JPanel();
        String tempDate = date.toString();
        panel.setBorder(BorderFactory.createEtchedBorder());
        panel.setBackground(new Color(240, 240, 240));
        panel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.insets = new Insets(5, 10, 5, 10);

        Font labelFont = new Font(Font.DIALOG, Font.PLAIN, 18); // Set the desired font size

        JLabel nameLabel = new JLabel(name);
        nameLabel.setFont(labelFont);
        nameLabel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));

        JLabel addressLabel = new JLabel(address);
        addressLabel.setFont(labelFont);
        addressLabel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));

        JLabel kWLabel = new JLabel(Double.toString(kW) + "kW");
        kWLabel.setFont(labelFont);
        kWLabel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));

        JLabel amountLabel = new JLabel(Double.toString(amount) + " RON");
        amountLabel.setFont(labelFont);
        amountLabel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));

        JLabel dateLabel = new JLabel(tempDate);
        dateLabel.setFont(labelFont);
        dateLabel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));

        panel.add(nameLabel, gbc);
        panel.add(addressLabel, gbc);
        panel.add(kWLabel, gbc);
        panel.add(amountLabel, gbc);
        panel.add(dateLabel, gbc);

        return panel;
    }

    public boolean isOwnedBy(int user_id){
        if(user_id == this.user_id)
            return true;
        return false;
    }
}
