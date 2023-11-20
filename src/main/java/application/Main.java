package application;

import application.tabs.InvoiceTab;
import application.tabs.RentalTab;
import application.tabs.VehicleTab;

import javax.swing.*;
import java.awt.*;

public class Main {

    private static JFrame window = new JFrame("Car Rental");

    private static InvoiceTab invoiceTab = new InvoiceTab();
    private static VehicleTab vehicleTab = new VehicleTab("Vehicles");
    private static RentalTab carRentalTab = new RentalTab("Rentals", vehicleTab, invoiceTab);

    private static JPanel upperBar = new JPanel();
    private static JPanel lowerBar = new JPanel();

    public static void main(String[] args){

        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setLocationRelativeTo(null);
        window.setExtendedState(JFrame.MAXIMIZED_BOTH);
        window.setLayout(new BorderLayout());

        upperBar.setLayout(new FlowLayout());
        window.add(upperBar, BorderLayout.PAGE_START);

        lowerBar.setLayout(new FlowLayout());
        window.add(lowerBar, BorderLayout.PAGE_END);

        upperBar.add(vehicleTab.getBackgroundPanel());
        upperBar.add(carRentalTab.getBackgroundPanel());

        lowerBar.add(invoiceTab.getBackground());
        invoiceTab.getBackground().setPreferredSize(upperBar.getPreferredSize());

        // finishing
        window.pack();
        window.setVisible(true);
    }



}
