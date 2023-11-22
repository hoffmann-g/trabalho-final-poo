package application;

import application.tabs.InvoiceTab;
import application.tabs.RentalTab;
import application.tabs.VehicleTab;
import model.dao.DaoFactory;
import model.services.BrazilTaxService;
import model.services.RentalService;

import javax.swing.*;
import java.awt.*;

public class Main {

    public static void main(String[] args){

        RentalService rs = new RentalService(5., 80., new BrazilTaxService());

        DaoFactory.setVehiclePath("garage.csv");
        DaoFactory.setCarRentalPath("rentals.csv");

        InvoiceTab invoiceTab = new InvoiceTab();
        VehicleTab vehicleTab = new VehicleTab("Vehicles", DaoFactory.createVehicleDao());
        RentalTab carRentalTab = new RentalTab
                ("Running Rentals", invoiceTab, DaoFactory.createCarRentalDao(),
                        DaoFactory.createVehicleDao(), rs);

        JFrame window = new JFrame("Parking System Manager");

        JPanel upperBar = new JPanel();
        JPanel lowerBar = new JPanel();

        vehicleTab.initUI();
        carRentalTab.initUI();

        upperBar.setBackground(Color.DARK_GRAY);
        lowerBar.setBackground(Color.DARK_GRAY);

        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setLocationRelativeTo(null);
        window.setExtendedState(JFrame.MAXIMIZED_BOTH);
        window.setLayout(new BorderLayout());

        upperBar.setLayout(new GridLayout(1,2));
        window.add(upperBar, BorderLayout.PAGE_START);

        lowerBar.setLayout(new BorderLayout());
        window.add(lowerBar, BorderLayout.CENTER);

        upperBar.add(vehicleTab.getBackgroundPanel());
        upperBar.add(carRentalTab.getBackgroundPanel());

        lowerBar.add(invoiceTab.getBackground());
        invoiceTab.getBackground().setPreferredSize(upperBar.getPreferredSize());

        // finishing
        window.pack();
        window.setVisible(true);


    }



}
