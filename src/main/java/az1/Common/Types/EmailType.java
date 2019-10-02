package az1.Common.Types;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailType extends AbstractType {
    private static final long serialVersionUID = 3856522808513372069L;

    @Override
    public String ToString(byte[] data) {
        if (data == null) {
            return null;
        }

        String value = data.toString();
        return "" + value;
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

        return bytes;
    }

    @Override
    public boolean Supports(byte[] data) {
        return data == null || data.length == 8;
    }

    @Override
    public int Compare(byte[] a, byte[] b) {
        return a.toString().compareToIgnoreCase(b.toString());
    }
}
