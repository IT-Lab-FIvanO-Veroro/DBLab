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
//    private static long table3;
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
//        table11 = database.AddEmptyTable(scheme, name);
//        table12 = database.AddEmptyTable(scheme, name);
//        database.TableAddEmptyRow(table11);
//        database.TableAddEmptyRow(table12);
//        table3 = database.TableInnerJoin(table11, table12, 1, 1);
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

    @Test(expected = AssertionError.class)
    public void tableInnerJoinShouldThrowAssertErrorWhenTableLengthDiffer() {
        int firstCol = 2;
        int secCol = 1;
        assertArrayEquals(new long[] { table1, table2, firstCol, secCol }, database.GetTableVersions());
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