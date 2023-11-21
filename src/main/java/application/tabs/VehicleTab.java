package application.tabs;

import model.dao.DataAccessObject;
import model.entities.Vehicle;

import javax.swing.*;
import java.security.InvalidParameterException;

public class VehicleTab extends Tab<Vehicle> {

    private final DataAccessObject<Vehicle> dao;

    public VehicleTab(String name, DataAccessObject<Vehicle> dao) {
        super(name);
        this.dao = dao;
    }
    @SuppressWarnings("")
    public void initUI(){
        for(Vehicle v : dao.readRows()){
            insertIntoList(v);
        }

        JButton createVehicle = new JButton("+");
        createVehicle.addActionListener(e -> {
            String plate = JOptionPane.showInputDialog("License plate:");
            if (plate != null){
                try {
                    insertIntoList(new Vehicle(plate.toUpperCase().replaceAll(" ", "-")));
                    dao.insertRow(new Vehicle(plate.toUpperCase().replaceAll(" ", "-")));
                } catch (InvalidParameterException ex){
                    System.out.println("vehicle already exists");
                    JOptionPane.showMessageDialog(getBackgroundPanel(), "Vehicle already exists!",
                            "Error", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        addButton(createVehicle);

        JButton deleteVehicle = new JButton("-");
        deleteVehicle.addActionListener(e -> {
            if (getSelectedValue() != null){
                removeFromList(getSelectedValue());
                dao.deleteRow(getSelectedValue());
            }
        });
        addButton(deleteVehicle);
    }
}
