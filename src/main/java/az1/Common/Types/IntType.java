package az1.Common.Types;

import java.nio.ByteBuffer;

public class IntType extends AbstractType {
    private static final long serialVersionUID = -326073292368414062L;

    @Override
    public String ToString(byte[] data) {
        if (data == null) {
            return null;
        }

        int value = ToInt(data);
        return "" + value;
    }

    public Integer ToInt(byte[] data) {
        return ByteBuffer.wrap(data).getInt();
    }

    @Override
    public byte[] FromString(String str) {
        if (str == null) {
            return null;
        }

        int value = Integer.parseInt(str);
        return ByteBuffer.allocate(4).putInt(value).array();
    }

    @Override
    public boolean Supports(byte[] data) {
        return data == null || data.length == 4;
    }

    @Override
    public int Compare(byte[] a, byte[] b) {
		if (a == null) {
			if (b == null) return 0;
			else return -1;
		}
		return ToInt(a).compareTo(ToInt(b));
    }
}
