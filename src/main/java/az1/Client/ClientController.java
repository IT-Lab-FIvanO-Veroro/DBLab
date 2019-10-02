package az1.Client;

import az1.Common.DatabaseController;
import az1.Common.Scheme;
import az1.Common.Table;

import javax.swing.*;
import java.awt.*;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;

public class ClientController {
    protected ArrayList<DatabaseUpdateHandler> updateListeners;
    protected DatabaseController controller;

    protected long lastUsedTable;

    public ClientController(DatabaseController controller) {
        updateListeners = new ArrayList<DatabaseUpdateHandler>();
        this.controller = controller;
        this.lastUsedTable = -1;
    }

    public long LastUsedTable() {
        return lastUsedTable;
    }

    public DatabaseController GetDatabaseController() {
        return controller;
    }

    public void AddUpdateListener(DatabaseUpdateHandler listener) {
        updateListeners.add(listener);
    }

    public void NewDatabase() {
        System.out.println("new click");
        try {
            controller.NewDatabase();
        } catch (RemoteException e) {
            HandleRemoteException(e);
        }
        RefreshDatabase();
    }

    private void HandleRemoteException(RemoteException e) {
        System.out.printf("Got RemoteException :(");
        e.printStackTrace();
    }

    public void LoadDatabase() {
        System.out.println("load click");
        String path = GetPathToSave();
        if (path == null) {
            return;
        }

        try {
            controller.LoadDatabase(path);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        RefreshDatabase();
    }

    public void SaveDatabase() {
        System.out.println("save click");
        String path = GetPathToSave();
        if (path == null) {
            return;
        }

        try {
            controller.SaveDatabase(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void CloseDatabase() {
        try {
            controller.CloseDatabase();
        } catch (RemoteException e) {
            HandleRemoteException(e);
        }
        RefreshDatabase();
    }

    public void CreateTable() {
        String name = GetTableName();
        if (name == null) {
            return;
        }

        Scheme scheme = GetTableScheme();
        if (scheme == null) {
            return;
        }

        try {
            lastUsedTable = controller.DatabaseAddEmptyTable(scheme, name);
        } catch (RemoteException e) {
            HandleRemoteException(e);
        }
        RefreshDatabase();
    }

    public void AddEmptyRow(long tableVersion) {
        try {
            lastUsedTable = tableVersion;
            controller.DatabaseTableAddEmptyRow(tableVersion);
        } catch (RemoteException e) {
            HandleRemoteException(e);
        }
        RefreshDatabase();
    }

    public void RemoveTableRows(long tableVersion, long[] rowVersions) {
        for (long rowVersion : rowVersions) {
            try {
                lastUsedTable = tableVersion;
                controller.DatabaseTableRemoveRow(tableVersion, rowVersion);
            } catch (RemoteException e) {
                HandleRemoteException(e);
            }
        }

        RefreshDatabase();
    }

    public void RemoveTable(long version) {
        try {
            lastUsedTable = version;
            controller.DatabaseRemoveTable(version);
        } catch (RemoteException e) {
            HandleRemoteException(e);
        }
        RefreshDatabase();
    }

    public void Refresh() {
        OnUpdate();
    }

    private void RefreshDatabase() {
        OnUpdate();
    }

    private void OnUpdate() {
        for (DatabaseUpdateHandler handler : updateListeners) {
            handler.HandleDatabaseUpdate();
        }
    }

    private static String GetPathToSave() {
        return (String) JOptionPane.showInputDialog(
                null,
                "Enter path:",
                "Path request dialog",
                JOptionPane.PLAIN_MESSAGE,
                null,
                null,
                "sample.db"
//                "C:/Users/artem/Dropbox/temp/win.db"
//                "C:/Users/artem/Dropbox/temp/winx.db"
//                "/Users/artemhb/Dropbox/temp/winx.db"
//                "/Users/artemhb/Dropbox/temp/1.db"
        );
    }

    private static String GetTableName() {
        return (String) JOptionPane.showInputDialog(
                null,
                "Enter table name:",
                "Table name request dialog",
                JOptionPane.PLAIN_MESSAGE,
                null,
                null,
                "Table 1"
        );
    }

    private static Scheme GetTableScheme() {
        String line = (String) JOptionPane.showInputDialog(
                null,
                "Enter scheme in a following way. " +
                "Concatenate types, separating with comma to a single string.\n" +
                "Supported types are Int, Float, Char, Enum, Email",
                "Scheme request dialog",
                JOptionPane.PLAIN_MESSAGE,
                null,
                null,
                "Int,Enum,Email"
        );

        if (line == null) {
            return null;
        }

        return Scheme.ParseFromString(line);
    }

    public void SetTableFieldValue(long tableVersion, long rowVersion, int column, String value) {
        try {
            lastUsedTable = tableVersion;
            controller.DatabaseTableSetFieldStr(tableVersion, rowVersion, column, value);
        } catch (RemoteException e) {
            HandleRemoteException(e);
        }
        System.out.println("Set " + rowVersion + "; " + column + " to " + value);
        RefreshDatabase();
    }

    public static void ShowWarning(String text) {
        System.out.println("WARNING: " + text);
    }

//    public void SortTableByKey(long tableVersion) {
//        String key = (String) JOptionPane.showInputDialog(
//                null,
//                "Enter column index (0-based):",
//                "Column request dialog",
//                JOptionPane.PLAIN_MESSAGE,
//                null,
//                null,
//                "0"
//        );
//
//        try {
//            int column = Integer.parseInt(key);
//            try {
//                lastUsedTable = tableVersion;
//                controller.DatabaseTableSortByKey(tableVersion, column);
//            } catch (RemoteException e) {
//                HandleRemoteException(e);
//            }
//        }
//        catch (NumberFormatException e) {
//            ShowWarning("Can't parse column number");
//        }
//
//        RefreshDatabase();
//    }

    public void TableFind(long tableVersion, int nCols) {
        String defaultPattern = "";
        for (int col = 0; col < nCols; ++col) {
            if (col > 0) {
                defaultPattern += "|";
            }

            defaultPattern += ".*";
        }

        String pattern = (String) JOptionPane.showInputDialog(
                null,
                "Enter pattern:",
                "Pattern request dialog",
                JOptionPane.PLAIN_MESSAGE,
                null,
                null,
                defaultPattern
        );

        try {
            lastUsedTable = controller.DatabaseTableFind(tableVersion, pattern);
        } catch (RemoteException e) {
            HandleRemoteException(e);
        }

        RefreshDatabase();
    }

    public void TableInnerJoin(long tableVersion, int nCols) {
        String defaultPattern = "";
        for (int col = 0; col < nCols; ++col) {
            if (col > 0) {
                defaultPattern += "|";
            }

            defaultPattern += "0";
        }

        String pattern_first_table = (String) JOptionPane.showInputDialog(
          null,
          "Enter * on which field you want to join:",
          "Field request dialog",
          JOptionPane.PLAIN_MESSAGE,
          null,
          null,
          defaultPattern
        );

        int first_table_column = 0;
        String[] strs = pattern_first_table.split("|");
        for (String str : strs) {
            if (str.equals("*")) {
                break;
            }

            first_table_column++;
        }

        String second_table_name = (String) JOptionPane.showInputDialog(
          null,
          "Enter name of table to which you want to join:",
          "Table name request dialog",
          JOptionPane.PLAIN_MESSAGE,
          null,
          null,
          ""
        );

        String pattern_second_table = (String) JOptionPane.showInputDialog(
          null,
          "Enter * on which field you want to join on other table:",
          "Field request dialog",
          JOptionPane.PLAIN_MESSAGE,
          null,
          null,
          defaultPattern
        );

        int second_table_column = 0;
        strs = pattern_second_table.split("|");
        for (String str : strs) {
            if (str.equals("*")) {
                break;
            }

            second_table_column++;
        }

        long secondTableVersion = 0;
        try {
            long[] tables_versions = controller.DatabaseGetTableVersions();
            for (long table_version : tables_versions) {
               String table = controller.DatabaseTableName(table_version);
                System.console().writer().println(Arrays.toString(new String[]{"Table name = " + table}));
               if (table.equals(second_table_name)) {
                   secondTableVersion = table_version;
                   break;
               }
            }
        } catch (RemoteException e) {
            HandleRemoteException(e);
        }


        try {
            lastUsedTable = controller.DatabaseTableInnerJoin(tableVersion, secondTableVersion,
              first_table_column, second_table_column);
//            lastUsedTable = controller.DatabaseTableInnerJoin(tableVersion, tableVersion,
//              first_table_column, second_table_column);
        } catch (RemoteException e) {
            HandleRemoteException(e);
        }

        RefreshDatabase();
    }

	public void TableDifference() {
		int table1 = InputTableIndex();
		int table2 = InputTableIndex();
		try {
			lastUsedTable = controller.DatabaseTableDifference(controller.DatabaseGetTableVersions()[table1],
					controller.DatabaseGetTableVersions()[table2]);
		} catch (RemoteException e) {
			HandleRemoteException(e);
		}

		RefreshDatabase();
	}


///	public void TableCartesian() {
//		int table1 = InputTableIndex();
//		int table2 = InputTableIndex();
//		try {
//			lastUsedTable = controller.DatabaseTableCartesian(controller.DatabaseGetTableVersions()[table1],
//					controller.DatabaseGetTableVersions()[table2]);
//		} catch (RemoteException e) {
//			HandleRemoteException(e);
//		}
//
//		RefreshDatabase();
//	}

	private int InputTableIndex() {
		String errorMessage = "";
		int index = -1;
		do {
			// Show input dialog with current error message, if any
			String stringInput = JOptionPane.showInputDialog(errorMessage + "Enter db index:");
			try {
				index = Integer.parseInt(stringInput);
				if (index > controller.DatabaseSize() || index < 0) {
					errorMessage = "That index is not within the allowed range!\n";
				} else {
					errorMessage = "";
				}
			} catch (NumberFormatException e) {
				// The typed text was not an integer
				errorMessage = "The text you typed is not a number.\n";
			} catch (RemoteException e) {
				HandleRemoteException(e);
			}
		} while (!errorMessage.isEmpty());
		return index;
	}

    private static void ShowDialog(Component frame, final Component component) {
        JScrollPane scrollPane = new JScrollPane(component);
        component.addHierarchyListener(new HierarchyListener() {
            public void hierarchyChanged(HierarchyEvent e) {
                Window window = SwingUtilities.getWindowAncestor(component);
                if (window instanceof Dialog) {
                    Dialog dialog = (Dialog) window;
                    if (!dialog.isResizable()) {
                        dialog.setResizable(true);
                    }
                }
            }
        });

        JOptionPane.showMessageDialog(frame, scrollPane);
    }

    public static void DisplayHTML(String value) {
        JEditorPane output = new JEditorPane();
        output.setEditable(false);
        output.setContentType("text/html");
        output.setText(value);

        JPanel myPanel = new JPanel();
        myPanel.add(output);

        ShowDialog(null, myPanel);
    }

    private static int ShowConfirmDialog(Component frame, final Component component) {
        JScrollPane scrollPane = new JScrollPane(component);
        component.addHierarchyListener(new HierarchyListener() {
            public void hierarchyChanged(HierarchyEvent e) {
                Window window = SwingUtilities.getWindowAncestor(component);
                if (window instanceof Dialog) {
                    Dialog dialog = (Dialog) window;
                    if (!dialog.isResizable()) {
                        dialog.setResizable(true);
                    }
                }
            }
        });

        return JOptionPane.showConfirmDialog(frame, scrollPane, "Please", JOptionPane.OK_CANCEL_OPTION);
    }

    public static String GetText() {
        JTextArea input = new JTextArea();
        input.setText("Enter HTML here:");

        JPanel myPanel = new JPanel();
        myPanel.add(input);

        int result = ShowConfirmDialog(null, myPanel);
        if (result == JOptionPane.OK_OPTION) {
            return input.getText();
        }

        return null;
    }
}
