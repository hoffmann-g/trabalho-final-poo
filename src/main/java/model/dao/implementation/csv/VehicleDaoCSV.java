package model.dao.implementation.csv;

import model.dao.DataAccessObject;
import model.entities.Vehicle;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class VehicleDaoCSV implements DataAccessObject<Vehicle> {

    private String path;

    public VehicleDaoCSV(String path){
        this.path = path;
    }

    @Override
    public List<Vehicle> readRows() {
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            List<Vehicle> vehicleList = new ArrayList<>();
            String plate;
            while ((plate = br.readLine()) != null) {
                vehicleList.add(new Vehicle(plate));
            }
            br.close();

            return vehicleList;
        } catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteRow(Vehicle vehicle) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            List<String> vehicleList = new ArrayList<>();
            String plate;
            while ((plate = br.readLine()) != null) {
                if (!plate.equals(vehicle.getModel().toUpperCase().replaceAll(" ", "-"))) {
                    vehicleList.add(plate);
                }
            }
            br.close();

            BufferedWriter bw = new BufferedWriter(new FileWriter(path, false));
            for (String v : vehicleList) {
                bw.write(v);
                bw.newLine();
            }
            bw.flush();
            bw.close();
        } catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public void insertRow(Vehicle vehicle) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(path, true));
            bw.write(vehicle.getModel().toUpperCase().replaceAll(" ", "-"));
            bw.newLine();
            bw.flush();
            bw.close();
        } catch (IOException e){
            throw new RuntimeException(e);
        }
    }
}
