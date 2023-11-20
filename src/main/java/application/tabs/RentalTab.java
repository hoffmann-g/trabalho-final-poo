package application.tabs;

import model.entities.CarRental;
import model.entities.Vehicle;
import model.services.BrazilTaxService;
import model.services.RentalService;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class RentalTab extends Tab<CarRental>{

    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

    private CarRental rental = null;

    private final RentalService rs = new RentalService(10., 130., new BrazilTaxService());

    public RentalTab(String name, VehicleTab vehicleTab, InvoiceTab invoiceTab) {
        super(name);

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

            String now = LocalDateTime.now().format(dtf);
            String selectedTime = (String)JOptionPane.showInputDialog(
                    null,
                    "PATTERN: yyyy/MM/dd HH:mm:ss",
                    "Select start time",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    null,
                    now
                    );

            if (selectedOption != null) {
                CarRental cr1;
                if (selectedTime.length() < 1){
                    cr1 = new CarRental(LocalDateTime.now(), null, new Vehicle(selectedOption));
                }
                else {
                    System.out.println("time: " + selectedTime);

                    selectedTime = selectedTime.replaceAll("/", "-");

                    System.out.println("time: " + selectedTime);

                    selectedTime = selectedTime.replaceAll(" ", "T");

                    System.out.println("time: " + selectedTime);

                    cr1 = new CarRental(LocalDateTime.parse(selectedTime), null, new Vehicle(selectedOption));
                }
                insertIntoList(cr1);

                System.out.println("Rental created:");
                System.out.println(cr1.getStart().toString());
            }
        });
        addButton(createRental);

        JButton deleteRental = new JButton("-");
        deleteRental.addActionListener(e -> {
            if (getSelectedValue() != null){
                removeFromList(getSelectedValue());
            }
        });
        addButton(deleteRental);

        JButton finalizeRental = new JButton("Process Invoice");
        finalizeRental.addActionListener(e -> {
            if (getSelectedValue() != null){
                rental = getSelectedValue();
                rental.setFinish(LocalDateTime.now());
                rs.processInvoice(rental);
                System.out.println("processed");

                invoiceTab.loadInvoice(rental);

            }
        });
        addButton(finalizeRental);
    }

    @Override
    void readRows() {

    }

    @Override
    void insertRow(String row) {

    }

    @Override
    void deleteRow(String row) {

    }
}
