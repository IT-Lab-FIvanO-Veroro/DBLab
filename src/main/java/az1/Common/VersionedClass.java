package az1.Common;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

public class VersionedClass implements Serializable {
    private static final long serialVersionUID = 197112731623L;

    private long version;

    public VersionedClass() {
        version = GetNextVersion();
    }

    public final long GetVersion() {
        return version;
    }

    private void readObject(ObjectInputStream aInputStream
    ) throws ClassNotFoundException, IOException {
        aInputStream.defaultReadObject();
        version = GetNextVersion();
    }

    private static long GetNextVersion() {
        return VersionAssigner.GetInstance().GetNextVersion();
    }
}
