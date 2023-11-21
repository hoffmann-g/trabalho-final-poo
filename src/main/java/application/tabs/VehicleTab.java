package application.tabs;

import model.entities.Vehicle;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class VehicleTab extends Tab<Vehicle> {

    private String path;

    public VehicleTab(String name) {
        super(name);
    }

    public void initUI(){
        try {
            readRows();
        } catch (IOException e) {
            throw new RuntimeException("could not load vehicles from path");
        }

        JButton createVehicle = new JButton("+");
        createVehicle.addActionListener(e -> {
            String plate = JOptionPane.showInputDialog("License plate:").toUpperCase().replaceAll(" ", "-");
            insertIntoList(new Vehicle(plate));
            try {
                insertRow(new Vehicle(plate));
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        addButton(createVehicle);

        JButton deleteVehicle = new JButton("-");
        deleteVehicle.addActionListener(e -> {
            if (getSelectedValue() != null){
                removeFromList(getSelectedValue());
                try {
                    deleteRow(getSelectedValue());
                } catch (IOException ex) {
                    throw new RuntimeException("could not delete row in csv");
                }
            }
        });
        addButton(deleteVehicle);
    }

    public void readRows() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(path));
        String plate;
        while((plate = br.readLine()) != null){
            insertIntoList(new Vehicle(plate.replaceAll(",", "")));
            System.out.println("read: " + plate);
        }
        br.close();
    }

    public void deleteRow(Vehicle vehicle) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(path));
        List<String> vehicleList = new ArrayList<>();
        String plate;
        while((plate = br.readLine()) != null){
            if (!plate.equals(vehicle.getModel().toUpperCase().replaceAll(" ", "-"))){
                vehicleList.add(plate);
            }
            System.out.println("read: " + plate);
        }
        br.close();

        BufferedWriter bw = new BufferedWriter(new FileWriter(path, false));
        for(String v : vehicleList){
            bw.write(v);
            bw.newLine();
        }
        bw.flush();
        bw.close();
    }

    @Override
    public void loadPath(String string) {
        path = string;
    }

    public void insertRow(Vehicle vehicle) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter(path, true));
        bw.write(vehicle.getModel().toUpperCase().replaceAll(" ", "-"));
        bw.newLine();
        bw.flush();
        bw.close();
    }
}
