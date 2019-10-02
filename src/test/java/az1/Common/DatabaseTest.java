package az1.Common;

import az1.Common.Types.AbstractType;
import az1.Common.Types.EnumType;
import az1.Common.Types.IntType;
import org.junit.Before;
import org.junit.Test;
//import org.junit.Before;
////import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class DatabaseTest {
    private static Database database;
    private static long table1;
    private static long table2;
    private static Scheme scheme;
    private static ArrayList<AbstractType> types;
    private static String name = "Table";

    @Before
    public void setUp() throws Exception {
        types = new ArrayList<AbstractType>();
        types.add(new IntType());
        types.add(new EnumType());
        scheme = new Scheme(types);

        database = new Database();
        table1 = database.AddEmptyTable(scheme, name);
        table2 = database.AddEmptyTable(scheme, name);
    }

    @Test
    public void testGetTableVersions() throws Exception {
        assertArrayEquals(new long[] { table1, table2 }, database.GetTableVersions());
    }

    @Test
    public void testAddEmptyTable() throws Exception {
        long table3 = database.AddEmptyTable(scheme, name);
        assertArrayEquals(new long[] { table1, table2, table3 }, database.GetTableVersions());
    }

    @Test
    public void testRemoveTable() throws Exception {
        database.RemoveTable(table1);
        assertArrayEquals(new long[] { table2 }, database.GetTableVersions());
    }

    @Test
    public void testSize() throws Exception {
        assertEquals(2, database.Size());
    }
}