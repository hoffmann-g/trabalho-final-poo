package application;

import model.entities.CarRental;

import javax.swing.*;
import java.awt.*;
import java.time.format.DateTimeFormatter;

public class InvoiceTab {

    private CarRental carRental;

    private JPanel background;
    private JPanel details;
    private JPanel price;

    private JTextArea plate = new JTextArea();
    private JTextArea invoice = new JTextArea();

    public InvoiceTab(){
        background = new JPanel();
        details = new JPanel();
        price = new JPanel();

        background.setLayout(new BoxLayout(background, BoxLayout.X_AXIS));
        details.setLayout(new GridBagLayout());
        price.setLayout(new GridBagLayout());

        background.add(details);
        background.add(price);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.CENTER;

        GridBagConstraints gbc2 = new GridBagConstraints();
        gbc2.gridx = 0;
        gbc2.gridy = 0;
        gbc2.weightx = 1.0;
        gbc2.weighty = 1.0;
        gbc2.anchor = GridBagConstraints.CENTER;

        details.add(plate, gbc);
        price.add(invoice, gbc2);
    }

    public void loadInvoice(CarRental carRental){


        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        String time = carRental.getStart().format(dtf) + " - "  + carRental.getFinish().format(dtf);
        //details
        String det = "Plate:\n" + carRental.getVehicle().getModel() + "\n Time:\n" + time;
        plate.setEditable(false);
        plate.setText(det);

        //details

        String inv = "Value: -----\n" + carRental.getInvoice().getBasicPayment() +
                "\n Tax: -----\n"+ carRental.getInvoice().getTax() +
                "\n TOTAL: -----\n" + carRental.getInvoice().totalPayment();
        invoice.setEditable(false);
        invoice.setText(inv);

    }

    public JPanel getBackground() {
        return background;
    }
}
