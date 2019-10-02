package az1.Common;

import az1.Common.Types.AbstractType;
import az1.Common.Types.EnumType;
import az1.Common.Types.IntType;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SchemeTest {
    private static Scheme scheme;
    private static Row row;
    private static ArrayList<AbstractType> types;
    private static ArrayList<byte[]> fields;
    private static String stringValue;


    @Before
    public void setUp() throws Exception {
        types = new ArrayList<AbstractType>();
        types.add(new IntType());
        types.add(new EnumType());
        scheme = new Scheme(types);

        fields = new ArrayList<byte[]>();
        fields.add(new byte[] { 1, 2, 3, 4 });
        fields.add(new byte[] { 80, 104, 68 });
        row = new Row(fields);

        stringValue = "Int,Enum";
    }

    @Test
    public void testSize() throws Exception {
        assertEquals(2, scheme.Size());
    }

    @Test
    public void testGetType() throws Exception {
        assertEquals(types.get(0), scheme.GetType(0));
        assertEquals(types.get(1), scheme.GetType(1));
    }

    @Test
    public void testSupports() throws Exception {
        assertTrue(scheme.Supports(row));
    }

    @Test
    public void testParseFromString() throws Exception {
        assertTrue(Scheme.ParseFromString(stringValue).GetType(0) instanceof IntType);
        assertTrue(Scheme.ParseFromString(stringValue).GetType(1) instanceof EnumType);
    }
}