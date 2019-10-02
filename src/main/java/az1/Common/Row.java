package az1.Common;

import java.util.ArrayList;

public class Row extends VersionedClass {
    private static final long serialVersionUID = 823661899935623L;

    protected ArrayList<byte[]> fields;

    public Row(ArrayList<byte[]> fields) {
        this.fields = fields;
    }

    public int Size() {
        return fields.size();
    }

    public void SetField(int index, byte[] value) {
        fields.set(index, value);
    }

    public byte[] GetField(int index) {
        return fields.get(index);
    }

    public boolean IsEqual(Row row, Scheme scheme) {
        if (row == null || this.Size() != row.Size()) {
            return false;
        }
        for (int col = 0; col < this.Size(); ++col) {
            byte[] d1 = this.GetField(col);
            byte[] d2 = row.GetField(col);
            if (scheme.GetType(col).Compare(d1, d2) != 0) {
                return false;
            }
        }
        return true;
    }
}
