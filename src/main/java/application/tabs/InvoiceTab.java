package application.tabs;

import model.entities.CarRental;

import javax.swing.*;
import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.Flow;

public class InvoiceTab {

    private CarRental carRental;

    private JPanel background;

    private JTextPane plate = new JTextPane();

    public InvoiceTab(){
        initUI();
    }

    private void initUI(){
        // instantiate
        background = new JPanel();
        JPanel details = new JPanel();
        JPanel buttons = new JPanel();
        JPanel labelPanel = new JPanel();
        JLabel title = new JLabel("Invoice");

        // define layouts
        background.setLayout(new BorderLayout());
        details.setLayout(new GridBagLayout());
        buttons.setLayout(new FlowLayout());

        // adds
        labelPanel.add(title);
        details.add(plate);

        // add to bg
        background.add(labelPanel, BorderLayout.PAGE_START);
        background.add(details, BorderLayout.CENTER);
        background.add(buttons, BorderLayout.PAGE_END);

        // color
        background.setBackground(Color.DARK_GRAY);
        buttons.setBackground(Color.white);
        details.setBackground(Color.GRAY);
        plate.setBackground(Color.gray);

        // font
        title.setFont(new Font("Tahoma", Font.PLAIN, 16));
        plate.setFont(new Font("Serif", Font.PLAIN, 16));
        //
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
                fw.write("\nTOTAL: " + carRental.getInvoice().totalPayment().toString());
                fw.close();

                System.out.println("file exported");
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        buttons.add(generateFile);

        generateFile.setBackground(Color.white);
        generateCode.setBackground(Color.white);
    }

    public void loadInvoice(CarRental carRental){
        this.carRental = carRental;

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        String time = carRental.getStart().format(dtf) + "\n    "  + carRental.getFinish().format(dtf);

        //details
        String invoice = "PLATE: " + carRental.getVehicle().getModel() +
                            "\nstart: " + carRental.getStart().format(dtf) +
                            "\nfinish: " + carRental.getFinish().format(dtf) +
                            "\n" +
                            "\nbasic payment: R$" + carRental.getInvoice().getBasicPayment() +
                            "\ntax: R$" + carRental.getInvoice().getTax() +
                            "\nTOTAL: R$" + carRental.getInvoice().totalPayment();
        plate.setBackground(Color.white);
        plate.setText(invoice);

    }

    public JPanel getBackground() {
        return background;
    }
}
