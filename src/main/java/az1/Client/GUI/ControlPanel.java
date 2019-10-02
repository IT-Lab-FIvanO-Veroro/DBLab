package az1.Client.GUI;

import az1.Client.ClientController;
import az1.Client.DatabaseUpdateHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;

public class ControlPanel extends JPanel implements DatabaseUpdateHandler {
    protected JButton newButton;
    protected JButton loadButton;
    protected JButton saveButton;
    protected JButton closeButton;

    protected JButton addTableButton;
//    protected JButton deleteTableButton;

    protected ClientController controller;

    public ControlPanel(ClientController controller1) {
        super(new GridLayout(1, 2));

        newButton = new JButton("New");
        loadButton = new JButton("Load");
        saveButton = new JButton("Save");
        closeButton = new JButton("Close");

        addTableButton = new JButton("Add table");
//        deleteTableButton = new JButton("Delete table");

        this.controller = controller1;

        newButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.NewDatabase();
            }
        });

        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.LoadDatabase();
            }
        });

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.SaveDatabase();
            }
        });

        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.CloseDatabase();
            }
        });

        addTableButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.CreateTable();
            }
        });

//        deleteTableButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                controller.RemoveTable();
//            }
//        });

        JPanel leftSide = new JPanel();
        leftSide.setLayout(new FlowLayout(FlowLayout.LEADING));
        leftSide.add(newButton);
        leftSide.add(loadButton);
        leftSide.add(saveButton);
        leftSide.add(closeButton);

        JPanel rightSide = new JPanel();
        rightSide.setLayout(new FlowLayout(FlowLayout.TRAILING));
        rightSide.add(addTableButton);
//        rightSide.add(deleteTableButton);

        add(leftSide);
        add(rightSide);

        controller.AddUpdateListener(this);
    }

    @Override
    public void HandleDatabaseUpdate() {
        try {
            SetTableButtonsVisibility(controller.GetDatabaseController().IsOpened());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void SetTableButtonsVisibility(boolean visible) {
        saveButton.setEnabled(visible);
        closeButton.setEnabled(visible);
        addTableButton.setEnabled(visible);
//        deleteTableButton.setEnabled(visible);
    }
}
