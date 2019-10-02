package az1.Common.Types;

import java.io.Serializable;

public abstract class AbstractType implements Serializable {
    private static final long serialVersionUID = -2725819304874234605L;

    public final void Serialize() {
        throw new RuntimeException("Not implemented");
    }

    public final static AbstractType DeSerialize() {
        AbstractType result = null;
        throw new RuntimeException("Not implemented");
    }

    public abstract String ToString(byte[] data);
    public abstract byte[] FromString(String str);
    public abstract boolean Supports(byte[] data);
    public abstract int Compare(byte[] a, byte[] b);
}
