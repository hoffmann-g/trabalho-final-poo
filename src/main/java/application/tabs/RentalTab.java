package application.tabs;

import model.dao.DataAccessObject;
import model.entities.CarRental;
import model.entities.Vehicle;
import model.services.RentalService;

import javax.swing.*;
import java.security.InvalidParameterException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class RentalTab extends Tab<CarRental>{

    private final InvoiceTab invoiceTab;

    private final DataAccessObject<CarRental> carRentalDao;
    private final DataAccessObject<Vehicle> vehicleDao;
    private final RentalService rentalService;

    private CarRental rental;

    public RentalTab(String name, InvoiceTab invoiceTab, DataAccessObject<CarRental> carRentalDao, DataAccessObject<Vehicle> vehicleDao, RentalService rentalService) {
        super(name);
        this.invoiceTab = invoiceTab;
        this.carRentalDao = carRentalDao;
        this.vehicleDao = vehicleDao;
        this.rentalService = rentalService;
    }

    public void initUI(){
        for(CarRental cr : carRentalDao.readRows()){
            insertIntoList(cr);
        }

        JButton createRental = new JButton("+");
        createRental.addActionListener(e -> {

            List<CarRental> carRentalList = carRentalDao.readRows();
            List<Vehicle> vehicles = vehicleDao.readRows();

            carRentalList.forEach(x -> vehicles.remove(x.getVehicle()));

            System.out.println("available vehicles");
            System.out.println(vehicles);

            Vehicle selectedVehicle = (Vehicle) JOptionPane.showInputDialog(
                    null,
                    null,
                    "Select a vehicle:",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    vehicles.toArray(),
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

            if (selectedVehicle != null) {
                CarRental cr1;
                if (selectedTime.length() < 1){
                    cr1 = new CarRental(LocalDateTime.now(), null, selectedVehicle);
                }
                else {
                    selectedTime = selectedTime.replaceAll("/", "-");
                    selectedTime = selectedTime.replaceAll(" ", "T");

                    cr1 = new CarRental(LocalDateTime.parse(selectedTime), null, selectedVehicle);
                }
                try {
                    insertIntoList(cr1);
                    carRentalDao.insertRow(cr1);
                } catch (InvalidParameterException ex){
                    System.out.println("rental already exists");
                    JOptionPane.showMessageDialog(getBackgroundPanel(), "A rental with the same vehicle and start time already exists!",
                            "Error", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        addButton(createRental);

        JButton deleteRental = new JButton("-");
        deleteRental.addActionListener(e -> {
            if (getSelectedValue() != null){
                removeFromList(getSelectedValue());
                carRentalDao.deleteRow(getSelectedValue());
            }
        });
        addButton(deleteRental);

        JButton finalizeRental = new JButton("Process Invoice");
        finalizeRental.addActionListener(e -> {
            if (getSelectedValue() != null){
                rental = getSelectedValue();
                rental.setFinish(LocalDateTime.now());
                rentalService.processInvoice(rental);
                System.out.println("processed");

                invoiceTab.loadInvoice(rental);
                removeFromList(rental);
                carRentalDao.deleteRow(rental);
            }
        });
        addButton(finalizeRental);
    }
}
