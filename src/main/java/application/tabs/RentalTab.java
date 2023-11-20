package application.tabs;

import model.entities.CarRental;
import model.entities.Vehicle;
import model.services.BrazilTaxService;
import model.services.RentalService;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class RentalTab extends Tab<CarRental>{

    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

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
            if (selectedOption != null) {
                LocalDateTime start = LocalDateTime.parse("11/11/2023 10:30", dtf);
                CarRental cr1 = new CarRental(start, null, new Vehicle(selectedOption));
                insertIntoList(cr1);
            }
        });
        addButton(createRental);

        JButton deleteRental = new JButton("-");
        deleteRental.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (getSelectedValue() != null){
                    removeFromList(getSelectedValue());
                }
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
}
