package application;

import model.entities.CarRental;
import model.entities.Vehicle;
import model.services.BrazilTaxService;
import model.services.RentalService;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

public class Main {

    private static CarRental rental = null;

    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private static final RentalService rs = new RentalService(10., 130., new BrazilTaxService());

    private static InvoiceTab invoiceTab = new InvoiceTab();
    private static JFrame window = new JFrame("Car Rental");
    private static Tab<Vehicle> vehicleTab = new Tab<>("Vehicles");
    private static Tab<CarRental> carRentalTab = new Tab<>("Rentals");

    private static JPanel upperBar = new JPanel();
    private static JPanel lowerBar = new JPanel();

    public static void main(String[] args){

        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setLocationRelativeTo(null);
        window.setExtendedState(JFrame.MAXIMIZED_BOTH);

        // Use BorderLayout for the main content
        window.setLayout(new BorderLayout());

        // Set layout for upperBar and add to the top (PAGE_START)
        upperBar.setLayout(new FlowLayout());
        window.add(upperBar, BorderLayout.PAGE_START);

        // Set layout for lowerBar and add to the bottom (PAGE_END)
        lowerBar.setLayout(new FlowLayout());
        window.add(lowerBar, BorderLayout.PAGE_END);

        // vehicle tab
        upperBar.add(vehicleTab.getBackgroundPanel());

        JButton createVehicle = new JButton("+");
        createVehicle.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String plate = JOptionPane.showInputDialog("License plate:");
                vehicleTab.insertIntoList(new Vehicle(plate.toUpperCase().replaceAll(" ", "-")));
            }
        });
        vehicleTab.addButton(createVehicle);

        JButton deleteVehicle = new JButton("-");
        deleteVehicle.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (vehicleTab.getSelectedValue() != null){
                    vehicleTab.removeFromList(vehicleTab.getSelectedValue());
                }
            }
        });
        vehicleTab.addButton(deleteVehicle);

        // car rental Tab
        upperBar.add(carRentalTab.getBackgroundPanel());

        JButton createRental = new JButton("+");
        createRental.addActionListener(e -> {
            List<String> strings = new ArrayList<>();
            for(Vehicle v : vehicleTab.getObjList()){
                strings.add(v.toString());
            }

            String[] stringArray = strings.toArray(new String[0]);

            String selectedOption = (String) JOptionPane.showInputDialog(
                    null,
                    null,
                    "Select a vehicle:",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    stringArray,
                    null
            );
            if (selectedOption != null) {
                LocalDateTime start = LocalDateTime.parse("11/11/2023 10:30", dtf);
                CarRental cr1 = new CarRental(start, null, new Vehicle(selectedOption));
                carRentalTab.insertIntoList(cr1);
            }
        });
        carRentalTab.addButton(createRental);

        JButton deleteRental = new JButton("-");
        deleteRental.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (carRentalTab.getSelectedValue() != null){
                    carRentalTab.removeFromList(carRentalTab.getSelectedValue());
                }
            }
        });
        carRentalTab.addButton(deleteRental);

        JButton finalizeRental = new JButton("Process Invoice");
        finalizeRental.addActionListener(e -> {
            if (carRentalTab.getSelectedValue() != null){
                rental = carRentalTab.getSelectedValue();
                rental.setFinish(LocalDateTime.now());
                rs.processInvoice(rental);
                System.out.println("processed");

                invoiceTab.loadInvoice(rental);

            }
        });
        carRentalTab.addButton(finalizeRental);

        // invoice tab
        lowerBar.add(invoiceTab.getBackground());
        invoiceTab.getBackground().setPreferredSize(upperBar.getPreferredSize());


        // finishing
        window.pack();
        window.setVisible(true);
    }



}
