package application.tabs;

import model.entities.CarRental;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public abstract class Tab<T> {

    private JPanel backgroundPanel;
    private JPanel labelPanel;
    private JScrollPane scrollPane;
    private JPanel buttonPanel;
    private JPanel listPanel;
    private T selectedObj;

    private JList<T> jList = new JList<>();
    private DefaultListModel<T> listModel;
    private List<T> objList = new ArrayList<>();

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
        labelPanel = new JPanel();
        JLabel title = new JLabel(name);
        labelPanel.add(title);
        backgroundPanel.add(labelPanel);

        // list
        listPanel = new JPanel();
        listModel = new DefaultListModel<>();
        jList.setModel(listModel); // Set the model for the JList
        scrollPane = new JScrollPane(jList);

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
        listModel.addElement(t);
        objList.add(t);
    }

    public void removeFromList(T t){
        listModel.removeElement(t);
        objList.remove(t);
    }

    public JPanel getBackgroundPanel() {
        return backgroundPanel;
    }

    public List<T> getObjList() {
        return objList;
    }

    public T getSelectedValue(){
        System.out.println("returned:" + selectedObj);
        return selectedObj;
    }

    abstract void readRows() throws Exception;
    abstract void insertRow(T t) throws Exception;
    abstract void deleteRow(T t) throws Exception;

    abstract void loadPath(String string);

}
