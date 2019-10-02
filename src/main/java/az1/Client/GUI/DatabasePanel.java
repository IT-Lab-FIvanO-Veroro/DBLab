package az1.Client.GUI;

import az1.Client.ClientController;
import az1.Client.DatabaseUpdateHandler;
import az1.Common.DatabaseController;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;

public class DatabasePanel extends JPanel implements DatabaseUpdateHandler {
    private static final String NULL_STRING = "<NULL>";

    protected ClientController controller;
    protected JTabbedPane tabbedPane;

    public DatabasePanel(ClientController controller) {
        super(new GridLayout(1, 1));

        this.controller = controller;
        this.tabbedPane = new JTabbedPane();

        add(tabbedPane);

        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

        controller.AddUpdateListener(this);
    }

    protected JComponent makeTextPanel(String text) {
        JPanel panel = new JPanel(false);
        JLabel filler = new JLabel(text);
        filler.setHorizontalAlignment(JLabel.CENTER);
        panel.setLayout(new GridLayout(1, 1));
        panel.add(filler);
        return panel;
    }

    @Override
    public void HandleDatabaseUpdate() {
        try {
            ShowDatabase(controller.GetDatabaseController());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void ShowDatabase(DatabaseController databaseController) throws RemoteException {
        tabbedPane.setVisible(databaseController.IsOpened());
        if (!databaseController.IsOpened()) {
            return;
        }

        tabbedPane.removeAll();
        final long[] tableVersions = databaseController.DatabaseGetTableVersions();
        for (int index = 0; index < tableVersions.length; ++index) {
            long tableVersion = tableVersions[index];
            ShowTable(databaseController, tableVersion);
            if (tableVersion == controller.LastUsedTable()) {
                tabbedPane.setSelectedIndex(index);
            }
        }
    }

    private void ShowTable(DatabaseController databaseController, long tableVersion) throws RemoteException {
        tabbedPane.addTab(
                databaseController.DatabaseTableName(tableVersion),
                GetTableComponent(databaseController, tableVersion)
        );
    }

    private JComponent GetTableComponent(DatabaseController databaseController, final long tableVersion) throws RemoteException {
        JPanel panel = new JPanel(new BorderLayout());

        // Table
        final long[] rowVersions = databaseController.DatabaseGetTableRowVersions(tableVersion);
        int numRows = rowVersions.length;
        int numCols = databaseController.DatabaseTableRowLength(tableVersion);
        final JTable table = new JTable(numRows, numCols);

        for (int row = 0; row < numRows; ++row) {
            for (int col = 0; col < numCols; ++col) {
                String value = databaseController.DatabaseTableGetFieldString(tableVersion, rowVersions[row], col);
                if (value == null) {
                    value = NULL_STRING;
                }

                table.setValueAt(value, row, col);
            }
        }

        table.getModel().addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                if (e.getFirstRow() != e.getLastRow() || e.getType() != TableModelEvent.UPDATE) {
                    throw new RuntimeException("Unsupported change to table");
                }

                int row = e.getFirstRow();
                int col = e.getColumn();

                String value = (String) table.getValueAt(row, col);
                if (value.equals(NULL_STRING)) {
                    value = null;
                }

                try {
                    controller.SetTableFieldValue(tableVersion, rowVersions[row], col, value);
                }
                catch (Exception exception) {
                    ClientController.ShowWarning(exception.getMessage());
                    controller.Refresh();
                }
            }
        });

        // Control panel
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.LEADING));

        JButton addEmptyRowButton = new JButton("Add empty row");
        JButton deleteTableButton = new JButton("Delete table");
        JButton deleteRowsButton = new JButton("Delete selected rows");
        JButton findButton = new JButton("Find");
		JButton diffButton = new JButton("Difference");
		JButton cartesianButton = new JButton("Cartesian product");

        addEmptyRowButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.AddEmptyRow(tableVersion);
            }
        });

        deleteTableButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.RemoveTable(tableVersion);
            }
        });

        deleteRowsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int[] rowsToDelete = table.getSelectedRows();
                long[] rowVersionsToDelete = new long[rowsToDelete.length];
                for (int index = 0; index < rowsToDelete.length; ++index) {
                    rowVersionsToDelete[index] = rowVersions[rowsToDelete[index]];
                }

                controller.RemoveTableRows(tableVersion, rowVersionsToDelete);
            }
        });

        findButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.TableFind(tableVersion, numCols);
            }
        });

		diffButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.TableDifference();
			}
		});

		cartesianButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.TableCartesian();
			}
		});

        controlPanel.add(addEmptyRowButton);
        controlPanel.add(deleteTableButton);
        controlPanel.add(deleteRowsButton);
        controlPanel.add(findButton);
		controlPanel.add(diffButton);
		controlPanel.add(cartesianButton);

        // Putting all together
        panel.add(controlPanel, BorderLayout.PAGE_START);
        panel.add(table, BorderLayout.CENTER);
        return panel;
    }
}
