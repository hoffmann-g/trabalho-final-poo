package application.tabs;

import model.entities.Vehicle;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class VehicleTab extends Tab<Vehicle> {

    public VehicleTab(String name) {
        super(name);

        JButton createVehicle = new JButton("+");
        createVehicle.addActionListener(e -> {
            String plate = JOptionPane.showInputDialog("License plate:");
            insertIntoList(new Vehicle(plate.toUpperCase().replaceAll(" ", "-")));
        });
        addButton(createVehicle);

        JButton deleteVehicle = new JButton("-");
        deleteVehicle.addActionListener(e -> {
            if (getSelectedValue() != null){
                removeFromList(getSelectedValue());
            }
        });
        addButton(deleteVehicle);
    }
}
