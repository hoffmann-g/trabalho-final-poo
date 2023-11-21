package model.dao;

import model.dao.implementation.csv.CarRentalDaoCSV;
import model.dao.implementation.csv.VehicleDaoCSV;
import model.entities.CarRental;
import model.entities.Vehicle;

public class DaoFactory {

    private static String vehiclePath;
    private static String carRentalPath;

    public static DataAccessObject<Vehicle> createVehicleDao(){
        return new VehicleDaoCSV(vehiclePath);
    }

    public static DataAccessObject<CarRental> createCarRentalDao(){
        return new CarRentalDaoCSV(carRentalPath);
    }

    public static void setCarRentalPath(String path){
        DaoFactory.carRentalPath = path;
    }

    public static void setVehiclePath(String path){
        DaoFactory.vehiclePath = path;
    }

}
