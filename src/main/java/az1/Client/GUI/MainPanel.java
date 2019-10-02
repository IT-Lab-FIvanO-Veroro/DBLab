package az1.Client.GUI;

import az1.Client.ClientController;
//import com.sun.deploy.panel.ControlPanel;

import javax.swing.*;
import java.awt.*;

public class MainPanel extends JPanel {
    protected ControlPanel controlPanel;
    protected DatabasePanel databasePanel;
    protected ClientController controller;

    public MainPanel(ClientController controller) {
        super(new BorderLayout());

        controlPanel = new ControlPanel(controller);
        databasePanel = new DatabasePanel(controller);
        this.controller = controller;

        add(controlPanel, BorderLayout.PAGE_START);
        add(databasePanel, BorderLayout.CENTER);

        setMinimumSize(new Dimension(1000, 1000));
		setSize(2000, 2000);

        controller.Refresh();
    }
}
