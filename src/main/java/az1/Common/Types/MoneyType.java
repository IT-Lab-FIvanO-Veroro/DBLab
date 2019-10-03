package az1.Common.Types;

import java.nio.charset.StandardCharsets;

public class MoneyType extends AbstractType{
    private static final long serialVersionUID = -7667712383263924813L;

    @Override
    public String ToString(byte[] data) {
        if (data == null) {
            return null;
        }

        String value = toString(data) + '$';
        return "" + value;
    }

    public String toString(byte[] data) {
        String str = new String(data, StandardCharsets.UTF_8 );
        return str;
    }

    @Override
    public byte[] FromString(String str) {
        if (str == null) {
            return null;
        }
        byte[] bytes = str.getBytes();

        if (!Supports(bytes)) {
            throw new RuntimeException("Can't parse money.");
        }

        return bytes;
    }

    @Override
    public boolean Supports(byte[] data) {

        if (data == null) {
            return true;
        }

        try {
            String s = String.valueOf(new String(data));
        }
        catch (Exception e) {
            return false;
        }

        return true;
    }

    @Override
    public int Compare(byte[] a, byte[] b) {
        return a.toString().compareToIgnoreCase(b.toString());
    }


}
