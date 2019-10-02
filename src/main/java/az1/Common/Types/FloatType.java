package az1.Common.Types;

import java.nio.ByteBuffer;

public class FloatType extends AbstractType {
    private static final long serialVersionUID = -3124607231254062L;

    @Override
    public String ToString(byte[] data) {
        if (data == null) {
            return null;
        }

        double value = ToDouble(data);
        return "" + value;
    }

    public Double ToDouble(byte[] data) {
        return ByteBuffer.wrap(data).getDouble();
    }

    @Override
    public byte[] FromString(String str) {
        if (str == null) {
            return null;
        }

        double value = Double.parseDouble(str);
        return ByteBuffer.allocate(8).putDouble(value).array();
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
		return ToDouble(a).compareTo(ToDouble(b));
    }
}
