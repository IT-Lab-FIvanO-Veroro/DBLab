package az1.Common;

import java.security.SecureRandom;

public class VersionAssigner {
    private static VersionAssigner ourInstance = new VersionAssigner();
    private SecureRandom random;

    public static VersionAssigner GetInstance() {
        return ourInstance;
    }

    public long GetNextVersion() {
        return random.nextLong();
    }

    private VersionAssigner() {
        random = new SecureRandom();
    }
}
