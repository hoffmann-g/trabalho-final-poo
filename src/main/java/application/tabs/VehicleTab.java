package application.tabs;

import model.entities.Vehicle;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class VehicleTab extends Tab<Vehicle> {

    public VehicleTab(String name) {
        super(name);

        try {
            readRows();
        } catch (IOException e) {
            throw new RuntimeException("could not load vehicles from garage");
        }

        JButton createVehicle = new JButton("+");
        createVehicle.addActionListener(e -> {
            String plate = JOptionPane.showInputDialog("License plate:").toUpperCase().replaceAll(" ", "-");
            insertIntoList(new Vehicle(plate));
            try {
                insertRow(plate);
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
                    deleteRow(getSelectedValue().getModel());
                } catch (IOException ex) {
                    throw new RuntimeException("could not delete row in csv");
                }
            }
        });
        addButton(deleteVehicle);
    }

    public void readRows() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("garage.csv"));
        String plate;
        while((plate = br.readLine()) != null){
            insertIntoList(new Vehicle(plate.replaceAll(",", "")));
            System.out.println("read: " + plate);
        }
        br.close();
    }

    public void deleteRow(String row) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("garage.csv"));
        List<String> vehicleList = new ArrayList<>();
        String plate;
        while((plate = br.readLine()) != null){
            if (!plate.equals(row)){
                vehicleList.add(plate);
            }
            System.out.println("read: " + plate);
        }
        br.close();

        BufferedWriter bw = new BufferedWriter(new FileWriter("garage.csv", false));
        for(String vehicle: vehicleList){
            bw.write(vehicle);
            bw.newLine();
        }
        bw.flush();
        bw.close();
    }

    public void insertRow(String row) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter("garage.csv", true));
        bw.write(row);
        bw.flush();
        bw.close();
    }
}
