package application;

import model.entities.CarRental;
import model.entities.Vehicle;
import model.services.BrazilTaxService;
import model.services.RentalService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Program {

    public static void main(String[] args){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        LocalDateTime start = LocalDateTime.parse("25/06/2018 10:30", dtf);
        LocalDateTime finish = LocalDateTime.parse("27/06/2018 11:40", dtf);

        CarRental cr = new CarRental(start, finish, new Vehicle("Civic"));
        RentalService rs = new RentalService(10., 130., new BrazilTaxService());
        rs.processInvoice(cr);

        System.out.println("FATURA: ");
        System.out.println("basic payment");
        System.out.println(cr.getInvoice().getBasicPayment());
        System.out.println("tax");
        System.out.println(cr.getInvoice().getTax());
        System.out.println("total payment");
        System.out.println(cr.getInvoice().totalPayment());

    }
}
