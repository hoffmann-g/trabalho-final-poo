package application.tabs;

import model.dao.DataAccessObject;
import model.entities.CarRental;
import model.entities.Vehicle;
import model.services.BrazilTaxService;
import model.services.RentalService;

import javax.swing.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class RentalTab extends Tab<CarRental>{

    private VehicleTab vehicleTab;
    private InvoiceTab invoiceTab;

    private DataAccessObject<CarRental> dao;
    private RentalService rs;

    private CarRental rental;

    public RentalTab(String name, VehicleTab vehicleTab, InvoiceTab invoiceTab, DataAccessObject<CarRental> dao, RentalService rentalService) {
        super(name);
        this.vehicleTab = vehicleTab;
        this.invoiceTab = invoiceTab;
        this.dao = dao;
        this.rs = rentalService;
    }

    public void initUI(){
        for(CarRental cr : dao.readRows()){
            insertIntoList(cr);
        }

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
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
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
                dao.insertRow(cr1);

                System.out.println("Rental created:");
                System.out.println(cr1.getStart().toString());
            }
        });
        addButton(createRental);

        JButton deleteRental = new JButton("-");
        deleteRental.addActionListener(e -> {
            if (getSelectedValue() != null){
                removeFromList(getSelectedValue());
                dao.deleteRow(getSelectedValue());
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
                removeFromList(rental);
                dao.deleteRow(rental);

            }
        });
        addButton(finalizeRental);
    }
}
