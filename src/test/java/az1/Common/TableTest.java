package az1.Common;

import az1.Common.Types.AbstractType;
import az1.Common.Types.EnumType;
import az1.Common.Types.IntType;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class TableTest {
    private static Table table;
    private static Scheme scheme;
    private static ArrayList<AbstractType> types;
    private static ArrayList<byte[]> fields1;
    private static ArrayList<byte[]> fields2;
    private static String name = "Table";
    private static Row row1;
    private static Row row2;

    @Before
    public void setUp() throws Exception {
        types = new ArrayList<>();
        types.add(new IntType());
        types.add(new EnumType());
        scheme = new Scheme(types);

        table = new Table(scheme, name);

        fields1 = new ArrayList<>();
        fields1.add(new byte[] { 1, 2, 3, 4 });
        fields1.add(new byte[] { 80, 104, 68 });
        row1 = new Row(fields1);

        fields2 = new ArrayList<>();
        fields2.add(new byte[] { 0, 1, 2, 3});
        fields2.add(new byte[] { 80, 104, 68 });
        row2 = new Row(fields2);

        table.AddRow(row1);
        table.AddRow(row2);
    }

    @Test
    public void testGetRowVersions() throws Exception {
        assertArrayEquals(new long[] { row1.GetVersion(), row2.GetVersion() }, table.GetRowVersions());
    }

    @Test
    public void testSize() throws Exception {
        assertEquals(2, table.Size());
    }

    @Test
    public void testRowLength() throws Exception {
        assertEquals(2, table.RowLength());
    }

    @Test
    public void testName() throws Exception {
        assertEquals(name, table.Name());
    }

    @Test
    public void testAddEmptyRow() throws Exception {
        long row = table.AddEmptyRow();
        assertEquals(3, table.Size());
        assertNull(table.GetField(row, 0));
        assertNull(table.GetField(row, 1));
    }

    @Test
    public void testRemoveRow() throws Exception {
        table.RemoveRow(row1.GetVersion());
        assertArrayEquals(new long[] { row2.GetVersion() }, table.GetRowVersions());
        table.RemoveRow(row2.GetVersion());
        assertArrayEquals(new long[] { }, table.GetRowVersions());
    }
}