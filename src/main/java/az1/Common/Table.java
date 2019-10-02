package az1.Common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Table extends VersionedClass {
    private static final long serialVersionUID = 7600220205234L;

    protected Scheme scheme;
    protected List<Row> rows;
    protected String name;

    public Table(Scheme scheme, String name) {
        this.scheme = scheme;
        this.rows = new ArrayList<Row>();
        this.name = name;
    }

    public long[] GetRowVersions() {
        long[] versions = new long[rows.size()];
        for (int index = 0; index < versions.length; ++index) {
            versions[index] = rows.get(index).GetVersion();
        }

        return versions;
    }

    public int Size() {
        return rows.size();
    }

    public int RowLength() {
        return scheme.Size();
    }

    public String Name() {
        return name;
    }

    public long AddEmptyRow() {
        Row row = CreateEmptyRow();
        rows.add(row);
        return row.GetVersion();
    }

    public boolean RemoveRow(long version) {
        for (int index = 0; index < rows.size(); ++index) {
            if (rows.get(index).GetVersion() == version) {
                rows.remove(index);
                return true;
            }
        }

        return false;
    }

    public byte[] GetField(long rowVersion, int column) {
        return GetRow(rowVersion).GetField(column);
    }

    public String GetFieldString(long rowVersion, int column) {
        return scheme.GetType(column).ToString(GetField(rowVersion, column));
    }

    public void SetField(long rowVersion, int column, byte[] value) {
        if (!scheme.GetType(column).Supports(value)) {
            throw new RuntimeException("Unsupported format");
        }

        GetRow(rowVersion).SetField(column, value);
    }

    public void SetField(long rowVersion, int column, String strValue) {
        GetRow(rowVersion).SetField(column, scheme.GetType(column).FromString(strValue));
    }

    protected Row GetRow(long version) {
        for (Row row : rows) {
            if (row.GetVersion() == version) {
                return row;
            }
        }

        return null;
    }

    protected Row CreateEmptyRow() {
        ArrayList<byte[]> data = new ArrayList<byte[]>();
        for (int index = 0; index < scheme.Size(); ++index) {
            data.add(null);
        }

        return new Row(data);
    }

    protected void AddRow(Row row) {
        CheckRowSupport(row);
        rows.add(row);
    }

    private void CheckRowSupport(Row row) {
        if (!scheme.Supports(row)) {
            throw new UnsupportedRowException();
        }
    }

    public void SortByKey(final int column) {
        Collections.sort(rows, new Comparator<Row>() {
            @Override
            public int compare(Row o1, Row o2) {
                byte[] d1 = o1.GetField(column);
                byte[] d2 = o2.GetField(column);

                if (d1 == null) {
                    if (d2 == null) {
                        return 0;
                    }

                    return -1;
                }

                if (d2 == null) {
                    return 1;
                }

                return scheme.GetType(column).Compare(d1, d2);
            }
        });
    }

    public boolean RowMatch(Row row, String pattern) {
        return scheme.RowMatch(row, pattern);
    }

}
