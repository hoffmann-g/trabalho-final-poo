package application.tabs;

import application.utility.QRCodeManager;
import model.entities.CarRental;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.format.DateTimeFormatter;

public class InvoiceTab {

    private CarRental carRental;

    private JPanel background;

    private final JTextPane plate = new JTextPane();

    private String outputQRCodePath;

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
            try {
                String message = "https://payment-link/(value: " + carRental.getInvoice().totalPayment().toString() + ")";

                QRCodeManager.createQRImage(new File(outputQRCodePath),
                        message, 250, "png");

                System.out.println("QR Code created");

                showQRCode(outputQRCodePath);
            } catch (Exception ex) {
                throw new RuntimeException("could not create QR code");
            }

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

    private void showQRCode(String path) throws IOException {
        JDialog paymentDialog = new JDialog();
        paymentDialog.setTitle("QR Code");

        JPanel panel = new JPanel(new BorderLayout());

        ImageIcon icon = new ImageIcon(path);
        JLabel imageLabel = new JLabel(icon);

        panel.add(imageLabel, BorderLayout.CENTER);

        paymentDialog.add(panel);

        paymentDialog.setSize(new Dimension(icon.getIconWidth(), icon.getIconHeight()));
        paymentDialog.setLocationRelativeTo(background);
        paymentDialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        paymentDialog.setVisible(true);

        Files.delete(Path.of(path));
    }

    public JPanel getBackground() {
        return background;
    }

    public void setOutputQRCodePath(String outputQRCodePath) {
        this.outputQRCodePath = outputQRCodePath;
    }
}
