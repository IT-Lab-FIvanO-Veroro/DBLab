package az1.Common;

import az1.Common.Types.EnumType;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class EnumTypeTest {
    private static byte[] desiredBytes = new byte[] { 80, 104, 68 };
    private static String desiredString = "PhD";
    private static EnumType instance = new EnumType();

    @Test
    public void testToString() throws Exception {
        assertEquals(desiredString, instance.ToString(desiredBytes));
    }

    @Test
    public void testFromString() throws Exception {
        assertArrayEquals(desiredBytes, instance.FromString(desiredString));
    }

    @Test
    public void testSupports() throws Exception {
        assertTrue(instance.Supports(desiredBytes));
    }

    @Test
    public void testCompare() throws Exception {
        assertTrue(instance.Compare(new byte[] { 80, 104, 68 }, new byte[] { 80, 104, 68 }) == 0);
    }
}