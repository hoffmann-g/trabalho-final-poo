package application;

import application.tabs.InvoiceTab;
import application.tabs.RentalTab;
import application.tabs.VehicleTab;
import model.dao.DaoFactory;
import model.dao.DataAccessObject;
import model.entities.Vehicle;

import javax.swing.*;
import java.awt.*;

public class Main {

    public static void main(String[] args){

        JFrame window = new JFrame("Car Rental");

        InvoiceTab invoiceTab = new InvoiceTab();
        VehicleTab vehicleTab = new VehicleTab("Vehicles");
        RentalTab carRentalTab = new RentalTab("Rentals", vehicleTab, invoiceTab);

        DaoFactory.setVehiclePath("garage.csv");
        DaoFactory.setCarRentalPath("rentals.csv");
        vehicleTab.setDao(DaoFactory.createVehicleDao());
        carRentalTab.setDao(DaoFactory.createCarRentalDao());

        JPanel upperBar = new JPanel();
        JPanel lowerBar = new JPanel();

        vehicleTab.initUI();
        carRentalTab.initUI();

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
