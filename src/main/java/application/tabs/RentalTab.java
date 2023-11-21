package application.tabs;

import model.entities.CarRental;
import model.entities.Vehicle;
import model.services.BrazilTaxService;
import model.services.RentalService;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RentalTab extends Tab<CarRental>{

    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

    private CarRental rental = null;

    private final RentalService rs = new RentalService(10., 130., new BrazilTaxService());

    public RentalTab(String name, VehicleTab vehicleTab, InvoiceTab invoiceTab) {
        super(name);

        try {
            readRows();
        } catch (IOException e) {
            throw new RuntimeException("could not read rentals");
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
                try {
                    insertRow(cr1);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }

                System.out.println("Rental created:");
                System.out.println(cr1.getStart().toString());
            }
        });
        addButton(createRental);

        JButton deleteRental = new JButton("-");
        deleteRental.addActionListener(e -> {
            if (getSelectedValue() != null){
                removeFromList(getSelectedValue());
                try {
                    deleteRow(getSelectedValue());
                } catch (IOException ex) {
                    throw new RuntimeException("could not delete rental");
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
                removeFromList(rental);
                try {
                    deleteRow(rental);
                } catch (IOException ex) {
                    throw new RuntimeException("could not delete rental");
                }

            }
        });
        addButton(finalizeRental);
    }

    @Override
    void readRows() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("rentals.csv"));
        String rental;
        while((rental = br.readLine()) != null){
            String[] info = rental.split(",");
            String plate = info[0];
            LocalDateTime time = LocalDateTime.parse(info[1]);

            System.out.println(plate);
            System.out.println(time);

            CarRental cr = new CarRental(time, null, new Vehicle(plate));
            insertIntoList(cr);

            System.out.println("read: " + cr.getVehicle().getModel() + cr.getStart());
        }
        br.close();
    }

    @Override
    void insertRow(CarRental carRental) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter("rentals.csv", true));
        bw.write(carRental.getVehicle().getModel() + "," + carRental.getStart());
        bw.newLine();
        bw.flush();
        bw.close();
    }

    @Override
    void deleteRow(CarRental carRental) throws IOException {

        System.out.println(carRental.getVehicle().getModel());
        System.out.println(carRental.getStart());

        BufferedReader br = new BufferedReader(new FileReader("rentals.csv"));
        List<String[]> rows = new ArrayList<>();
        String rental;
        while((rental = br.readLine()) != null){
            String[] row = rental.split(",");
            System.out.println("read: " + row[0] + " - " + row[1]);
            if(!Objects.equals(row[0], carRental.getVehicle().getModel()) && !Objects.equals(row[1], carRental.getStart().toString())){
                rows.add(row);
            }
        }
        br.close();

        BufferedWriter bw = new BufferedWriter(new FileWriter("rentals.csv", false));
        for(String[] s : rows){
            bw.write(s[0] + "," + s[1]);
            bw.newLine();
        }
        bw.flush();
        bw.close();

    }
}
