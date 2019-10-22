package az1.Server;

import az1.Common.Database;
import az1.Common.DatabaseController;
import az1.Common.Scheme;

import java.io.*;
import java.rmi.RemoteException;

public class ServerController implements DatabaseController {
    Database database;
    public ServerController() {
        database = null;
    }

    @Override
    public boolean IsOpened() {
        return database != null;
    }

    @Override
    public void NewDatabase() {
        database = new Database();
    }

    @Override
    public void LoadDatabase(String path) throws IOException, ClassNotFoundException {
        InputStream inputStream = new FileInputStream(path);
        BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
        ObjectInputStream objectInputStream = new ObjectInputStream(bufferedInputStream);
        database = (Database) objectInputStream.readObject();
        objectInputStream.close();
    }

    @Override
    public void SaveDatabase(String path) throws IOException {
        OutputStream outputStream = new FileOutputStream(path);
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(bufferedOutputStream);
        objectOutputStream.writeObject(database);
        objectOutputStream.close();
    }

    @Override
    public void CloseDatabase() {
        database = null;
    }

    @Override
    public long[] DatabaseGetTableVersions() {
        return database.GetTableVersions();
    }

    @Override
    public long DatabaseAddEmptyTable(Scheme scheme, String name) {
        return database.AddEmptyTable(scheme, name);
    }

    @Override
    public boolean DatabaseRemoveTable(long version) {
        return database.RemoveTable(version);
    }

    @Override
    public long[] DatabaseGetTableRowVersions(long version) {
        return database.GetTableRowVersions(version);
    }

    @Override
    public int DatabaseSize() {
        return database.Size();
    }

    @Override
    public int DatabaseTableSize(long version) {
        return database.TableSize(version);
    }

    @Override
    public String DatabaseTableName(long version) {
        return database.TableName(version);
    }

    @Override
    public int DatabaseTableRowLength(long version) {
        return database.TableRowLength(version);
    }

    @Override
    public long DatabaseTableAddEmptyRow(long version) {
        return database.TableAddEmptyRow(version);
    }

    @Override
    public boolean DatabaseTableRemoveRow(long tableVersion, long rowVersion) {
        return database.TableRemoveRow(tableVersion, rowVersion);
    }

    @Override
    public byte[] DatabaseTableGetField(long tableVersion, long rowVersion, int column) {
        return database.TableGetField(tableVersion, rowVersion, column);
    }

    @Override
    public String DatabaseTableGetFieldString(long tableVersion, long rowVersion, int column) {
        return database.TableGetFieldString(tableVersion, rowVersion, column);
    }

    @Override
    public void DatabaseTableSetField(long tableVersion, long rowVersion, int column, byte[] value) {
        database.TableSetField(tableVersion, rowVersion, column, value);
    }

    @Override
    public void DatabaseTableSetFieldStr(long tableVersion, long rowVersion, int column, String strValue) {
        database.TableSetField(tableVersion, rowVersion, column, strValue);
    }

    @Override
    public long DatabaseTableFind(long tableVersion, String pattern) throws RemoteException {
        return database.TableFind(tableVersion, pattern);
    }

	@Override
	public long DatabaseTableDifference(long table1Version, long table2Version) throws RemoteException {
		return database.TableDifference(table1Version, table2Version);
	}

//	@Override
//	public long DatabaseTableCartesian(long table1Version, long table2Version) throws RemoteException {
//		return database.TableCartesian(table1Version, table2Version);
//	}

    @Override
    public long DatabaseTableInnerJoin(long firstTableVersion, long secondTableVersion,
                                       int firstTableColumn, int secondTableColumn) throws RemoteException {
        return database.TableInnerJoin(firstTableVersion, secondTableVersion, firstTableColumn, secondTableColumn);
    }

  @Override
  public long DatabaseTableProjection(long tableVersion, String pattern) {
    return database.TableProjection(tableVersion, pattern);
  }
}
