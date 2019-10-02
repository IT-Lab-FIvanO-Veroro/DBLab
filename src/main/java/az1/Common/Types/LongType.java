package az1.Common.Types;

import java.nio.ByteBuffer;

public class LongType extends AbstractType {
    private static final long serialVersionUID = -25745645L;

    @Override
    public String ToString(byte[] data) {
        if (data == null) {
            return null;
        }

        long value = ToLong(data);
        return "" + value;
    }

    public Long ToLong(byte[] data) {
        return ByteBuffer.wrap(data).getLong();
    }

    @Override
    public byte[] FromString(String str) {
        if (str == null) {
            return null;
        }

        long value = Long.parseLong(str);
        return ByteBuffer.allocate(8).putLong(value).array();
    }

    @Override
    public boolean Supports(byte[] data) {
        return data == null || data.length == 8;
    }

    @Override
    public int Compare(byte[] a, byte[] b) {
		if (a == null) {
			if (b == null) return 0;
			else return -1;
		}
		return ToLong(a).compareTo(ToLong(b));
    }
}
