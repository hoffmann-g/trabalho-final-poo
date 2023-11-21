package application.tabs;

import javax.management.openmbean.KeyAlreadyExistsException;
import javax.swing.*;
import java.awt.*;
import java.security.InvalidParameterException;
import java.util.HashSet;

public abstract class Tab<T> {

    private JPanel backgroundPanel;
    private JPanel buttonPanel;
    private T selectedObj;

    private final JList<T> jList = new JList<>();
    private DefaultListModel<T> listModel;
    private final HashSet<T> objSet = new HashSet<>();

    public Tab(String name) {
        // Initialize the components
        initUI(name);
    }

    private void initUI(String name) {
        // background
        backgroundPanel = new JPanel();
        backgroundPanel.setLayout(new BoxLayout(backgroundPanel, BoxLayout.Y_AXIS));

        Dimension fixedSize = new Dimension(200, 150); // Adjust the size as needed
        jList.setMinimumSize(fixedSize);

        // label panel
        JPanel labelPanel = new JPanel();
        JLabel title = new JLabel(name);
        labelPanel.add(title);
        backgroundPanel.add(labelPanel);

        // list
        JPanel listPanel = new JPanel();
        listModel = new DefaultListModel<>();
        jList.setModel(listModel); // Set the model for the JList
        JScrollPane scrollPane = new JScrollPane(jList);

        listPanel.add(scrollPane, BorderLayout.CENTER);
        backgroundPanel.add(listPanel);

        jList.addListSelectionListener(e -> {
            if (!jList.isSelectionEmpty()) {
                selectedObj = jList.getSelectedValue();
                System.out.println(selectedObj);
            }
        });

        // buttonPanel
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        backgroundPanel.add(buttonPanel);
    }

    public void addButton(JButton button){
        buttonPanel.add(button);
    }

    public void insertIntoList(T t){
        if (!objSet.contains(t)) {
            listModel.addElement(t);
            objSet.add(t);
        } else {
            throw new InvalidParameterException();
        }
    }

    public void removeFromList(T t){
        listModel.removeElement(t);
        objSet.remove(t);
    }

    public JPanel getBackgroundPanel() {
        return backgroundPanel;
    }

    public T getSelectedValue(){
        System.out.println("returned:" + selectedObj);
        return selectedObj;
    }
}
