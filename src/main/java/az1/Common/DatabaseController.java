package az1.Common;

//import javax.jws.WebMethod;
//import javax.jws.WebService;
//import javax.jws.soap.SOAPBinding;
import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

//@WebService // Endpoint Interface
//@SOAPBinding(style = SOAPBinding.Style.RPC) // Needed for the WSDL
public interface DatabaseController extends Remote {
//    @WebMethod
    public boolean IsOpened() throws RemoteException;
//    @WebMethod
    public void NewDatabase() throws RemoteException;
//    @WebMethod
    public void LoadDatabase(String path) throws IOException, ClassNotFoundException;
//    @WebMethod
    public void SaveDatabase(String path) throws IOException;
//    @WebMethod
    public void CloseDatabase() throws RemoteException;
//    @WebMethod
    public long[] DatabaseGetTableVersions() throws RemoteException;
//    @WebMethod
    public long DatabaseAddEmptyTable(Scheme scheme, String name) throws RemoteException;
//    @WebMethod
    public boolean DatabaseRemoveTable(long version) throws RemoteException;
//    @WebMethod
    public long[] DatabaseGetTableRowVersions(long version) throws RemoteException;
//    @WebMethod
    public int DatabaseSize() throws RemoteException;
//    @WebMethod
    public int DatabaseTableSize(long version) throws RemoteException;
//    @WebMethod
    public String DatabaseTableName(long version) throws RemoteException;
//    @WebMethod
    public int DatabaseTableRowLength(long version) throws RemoteException;
//    @WebMethod
    public long DatabaseTableAddEmptyRow(long version) throws RemoteException;
//    @WebMethod
    public boolean DatabaseTableRemoveRow(long tableVersion, long rowVersion) throws RemoteException;
//    @WebMethod
    public byte[] DatabaseTableGetField(long tableVersion, long rowVersion, int column) throws RemoteException;
//    @WebMethod
    public String DatabaseTableGetFieldString(long tableVersion, long rowVersion, int column) throws RemoteException;
//    @WebMethod
    public void DatabaseTableSetField(long tableVersion, long rowVersion, int column, byte[] value) throws RemoteException;
//    @WebMethod
    public void DatabaseTableSetFieldStr(long tableVersion, long rowVersion, int column, String strValue) throws RemoteException;
//    @WebMethod
    public long DatabaseTableFind(long tableVersion, String pattern) throws RemoteException;
//	@WebMethod
	public long DatabaseTableDifference(long table1Version, long table2Version) throws RemoteException;
//	@WebMethod
	public long DatabaseTableCartesian(long table1Version, long table2Version) throws RemoteException;
}
