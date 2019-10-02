package az1.Common.Types;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailType extends AbstractType {
    private static final long serialVersionUID = 3856522808513372069L;
//    private static final long serialVersionUID = 3856522808513372069L;


    @Override
    public String ToString(byte[] data) {
        if (data == null) {
            return null;
        }

        String value = toString(data);
        return "" + value;
    }

    public String toString(byte[] data) {
//        ByteBuffer.flip();
//        ByteBuffer original = ByteBuffer.wrap(data.getBytes("UTF-8"));
//        byte[] bytes = .getBytes( StandardCharsets.UTF_8 );
        String str = new String(data, StandardCharsets.UTF_8 );
        return str;
//        charBuffer = StandardCharsets.UTF_16.decode(paramByteBuffer);
//        text = charBuffer.toString();
//        System.out.println("UTF_16"+text);
    }

    @Override
    public byte[] FromString(String str) {
        if (str == null) {
            return null;
        }

        Pattern email_regex =
                Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = email_regex.matcher(str);
        if (!matcher.find()) {
            throw new IllegalArgumentException("Email is invalid!");
        }

        byte[] bytes = str.getBytes();
//        return ByteBuffer.allocate(16).put(bytes).array();
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
