package az1.Common;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class RowTest {
    private static Row row;
    @Before
    public void setUp() throws Exception {
        ArrayList<byte[]> fields = new ArrayList<byte[]>();
        fields.add(new byte[] { 0, 1, 2, 1 });
        fields.add(new byte[] { 0, 1, 2, 2 });
        row = new Row(fields);
    }

    @Test
    public void testSize() throws Exception {
        assertEquals(2, row.Size());
    }

    @Test
    public void testSetField() throws Exception {
        row.SetField(1, new byte[] { 2 });
        assertArrayEquals(new byte[] { 2 }, row.GetField(1));
    }

    @Test
    public void testGetField() throws Exception {
        assertArrayEquals(new byte[] { 0, 1, 2, 1 }, row.GetField(0));
    }
}