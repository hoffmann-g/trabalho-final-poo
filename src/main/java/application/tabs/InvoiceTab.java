package application.tabs;

import model.entities.CarRental;

import javax.swing.*;
import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
import java.time.format.DateTimeFormatter;

public class InvoiceTab {

    private CarRental carRental;

    private JPanel background;
    private JPanel details;
    private JPanel price;
    private JPanel buttons;

    private JTextArea plate = new JTextArea();
    private JTextArea invoice = new JTextArea();

    public InvoiceTab(){
        background = new JPanel();
        details = new JPanel();
        price = new JPanel();
        buttons = new JPanel();

        background.setLayout(new GridLayout());
        details.setLayout(new GridBagLayout());
        price.setLayout(new GridBagLayout());
        buttons.setLayout(new FlowLayout());

        background.add(details);
        background.add(price);
        background.add(buttons);

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

        JButton generateCode = new JButton("Generate Code");
        generateCode.addActionListener(e -> {
            JFrame frame = new JFrame("test");
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            JLabel jLabel = new JLabel(String.valueOf(carRental.getInvoice().totalPayment()));
            frame.add(jLabel);

            frame.pack();
            frame.setVisible(true);
        });
        buttons.add(generateCode);

        JButton generateFile = new JButton("Export");
        generateFile.addActionListener(e -> {
            try {
                DateTimeFormatter outputformat = DateTimeFormatter.ofPattern("dd-MM-yyyy-HH-mm");
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

                FileWriter fw = new FileWriter(carRental.getVehicle().getModel() + "_" + carRental.getFinish().format(outputformat) + ".txt");
                fw.write("PLATE: " + carRental.getVehicle().getModel());
                fw.write("\nstart: " + carRental.getStart().format(dtf));
                fw.write("\nfinish: " + carRental.getFinish().format(dtf));
                fw.write("\n");
                fw.write("\nbasic payment: " + carRental.getInvoice().getBasicPayment().toString());
                fw.write("\ntax: " + carRental.getInvoice().getTax().toString());
                fw.write("\ntotal: " + carRental.getInvoice().totalPayment().toString());
                fw.close();

                System.out.println("file exported");
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        buttons.add(generateFile);
    }

    public void loadInvoice(CarRental carRental){
        this.carRental = carRental;

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        String time = carRental.getStart().format(dtf) + " - "  + carRental.getFinish().format(dtf);
        //details
        String det = "Plate:\n" + carRental.getVehicle().getModel() + "\n Time:\n" + time;
        plate.setEditable(false);
        plate.setText(det);

        //price and whatever
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
