package model.dao.implementation.csv;

import model.dao.DataAccessObject;
import model.entities.CarRental;
import model.entities.Vehicle;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CarRentalDaoCSV implements DataAccessObject<CarRental> {

    private String path;

    public CarRentalDaoCSV(String path){
        this.path = path;
    }

    @Override
    public List<CarRental> readRows() {
        try {
            BufferedReader br = new BufferedReader(new FileReader("rentals.csv"));
            List<CarRental> carRentalList = new ArrayList<>();

            String rental;
            while ((rental = br.readLine()) != null) {
                String[] info = rental.split(",");
                String plate = info[0];
                LocalDateTime time = LocalDateTime.parse(info[1]);

                CarRental cr = new CarRental(time, null, new Vehicle(plate));
                carRentalList.add(cr);
            }
            br.close();

            return carRentalList;
        } catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public void insertRow(CarRental carRental) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(path, true));
            bw.write(carRental.getVehicle().getModel() + "," + carRental.getStart());
            bw.newLine();
            bw.flush();
            bw.close();
        } catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteRow(CarRental carRental) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            List<String[]> rows = new ArrayList<>();
            String rental;
            while ((rental = br.readLine()) != null) {
                String[] row = rental.split(",");
                if (!Objects.equals(row[0], carRental.getVehicle().getModel()) && !Objects.equals(row[1], carRental.getStart().toString())) {
                    rows.add(row);
                }
            }
            br.close();

            BufferedWriter bw = new BufferedWriter(new FileWriter(path, false));
            for (String[] s : rows) {
                bw.write(s[0] + "," + s[1]);
                bw.newLine();
            }
            bw.flush();
            bw.close();
        } catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean contains(CarRental carRental) {
        return readRows().contains(carRental);
    }
}
