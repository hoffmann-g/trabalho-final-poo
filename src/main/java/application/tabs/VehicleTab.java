package application.tabs;

import model.dao.DaoFactory;
import model.dao.DataAccessObject;
import model.entities.Vehicle;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class VehicleTab extends Tab<Vehicle> {

    private String path;

    private DataAccessObject<Vehicle> dao;

    public VehicleTab(String name) {
        super(name);
    }

    public void setDao(DataAccessObject<Vehicle> dao){
        this.dao = dao;
    }

    public void initUI(){
        for(Vehicle v : dao.readRows()){
            insertIntoList(v);
        }

        JButton createVehicle = new JButton("+");
        createVehicle.addActionListener(e -> {
            String plate = JOptionPane.showInputDialog("License plate:").toUpperCase().replaceAll(" ", "-");
            insertIntoList(new Vehicle(plate));
            dao.insertRow(new Vehicle(plate));
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
