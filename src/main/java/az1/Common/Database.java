package az1.Common;

import az1.Common.Types.AbstractType;
import az1.Common.Types.*;
import java.util.ArrayList;

public class Database extends VersionedClass {
    private static final long serialVersionUID = 19773186723L;

    protected ArrayList<Table> tables;

    public Database() {
        tables = new ArrayList<Table>();
    }

    public long[] GetTableVersions() {
        long[] versions = new long[tables.size()];
        for (int index = 0; index < versions.length; ++index) {
            versions[index] = tables.get(index).GetVersion();
        }

        return versions;
    }

    public long AddEmptyTable(Scheme scheme, String name) {
        Table table = new Table(scheme, name);
        tables.add(table);
        return table.GetVersion();
    }

    public boolean RemoveTable(long version) {
        for (int index = 0; index < tables.size(); ++index) {
            if (tables.get(index).GetVersion() == version) {
                tables.remove(index);
                return true;
            }
        }

        return false;
    }

    public long[] GetTableRowVersions(long version) {
        return GetTable(version).GetRowVersions();
    }

    public int Size() {
        return tables.size();
    }

    public int TableSize(long version) {
        return GetTable(version).Size();
    }

    public String TableName(long version) {
        return GetTable(version).Name();
    }

    public int TableRowLength(long version) {
        return GetTable(version).RowLength();
    }

    public long TableAddEmptyRow(long version) {
        return GetTable(version).AddEmptyRow();
    }

    public boolean TableRemoveRow(long tableVersion, long rowVersion) {
        return GetTable(tableVersion).RemoveRow(rowVersion);
    }

    public byte[] TableGetField(long tableVersion, long rowVersion, int column) {
        return GetTable(tableVersion).GetField(rowVersion, column);
    }

    public String TableGetFieldString(long tableVersion, long rowVersion, int column) {
        return GetTable(tableVersion).GetFieldString(rowVersion, column);
    }

    public void TableSetField(long tableVersion, long rowVersion, int column, byte[] value) {
        GetTable(tableVersion).SetField(rowVersion, column, value);
    }

    public void TableSetField(long tableVersion, long rowVersion, int column, String strValue) {
        GetTable(tableVersion).SetField(rowVersion, column, strValue);
    }

    protected Table GetTable(long version) {
        for (Table table : tables) {
            if (table.GetVersion() == version) {
                return table;
            }
        }

        return null;
    }

    public void TableSortByKey(long tableVersion, int column) {
        GetTable(tableVersion).SortByKey(column);
    }

    public long TableFind(long tableVersion, String pattern) {
        Table source = GetTable(tableVersion);
        Table result = new Table(source.scheme, "Search in " + source.name);
        source.rows.stream().filter(row -> source.RowMatch(row, pattern)).forEach(result::AddRow);
        tables.add(result);
        return result.GetVersion();
    }

    public long TableDifference(long table1Version, long table2Version) {
        Table first = GetTable(table1Version);
        Table second = GetTable(table2Version);
        Table result = new Table(first.scheme, first.name + " - " + second.name);
        boolean canDiff = true;

        if (first.RowLength() != second.RowLength()) {
            canDiff = false;
        }

        if (!first.scheme.IsEqual(second.scheme)) {
            canDiff = false;
        }

        if (!canDiff) {
            first.rows.forEach(result::AddRow);
        } else {
            for (Row row1 : first.rows) {
                boolean isUnique = true;
                for (Row row2 : second.rows) {
                    boolean isEqual = row1.IsEqual(row2, first.scheme);
                    if (isEqual) {
                        isUnique = false;
                        break;
                    }
                }
                if (isUnique) {
                    result.AddRow(row1);
                }
            }
        }

        tables.add(result);
        return result.GetVersion();
    }

//    public long TableCartesian(long table1Version, long table2Version) {
//        Table first = GetTable(table1Version);
//        Table second = GetTable(table2Version);
//
//        ArrayList<AbstractType> types = new ArrayList<>();
//        for (AbstractType type : first.scheme.types) {
//            types.add(type);
//        }
//        for (AbstractType type : second.scheme.types) {
//            types.add(type);
//        }
//        Scheme scheme = new Scheme(types);
//        Table result = new Table(scheme, first.name + " * " + second.name);
//
//        for (int i = 0; i < first.Size(); i++) {
//            for (int j = 0; j < second.Size(); j++) {
//                long current = result.AddEmptyRow();
//                for (int col = 0; col < first.RowLength(); col++) {
//                    byte[] val = first.GetField(first.GetRowVersions()[i], col);
//                    result.SetField(current, col, val);
//                }
//                for (int col = 0; col < second.RowLength(); col++) {
//                    byte[] val = second.GetField(second.GetRowVersions()[j], col);
//                    result.SetField(current, first.RowLength() + col, val);
//                }
//            }
//        }
//
//        tables.add(result);
//        return result.GetVersion();
//    }

    public long TableInnerJoin(long firstTableVersion, long secondTableVersion,
                               int firstTableColumn, int secondTableColumn) {
        Table first = GetTable(firstTableVersion);
        Table second = GetTable(secondTableVersion);

        ArrayList<AbstractType> types = new ArrayList<>();
        types.addAll(first.scheme.types);
        types.addAll(second.scheme.types);
        Scheme scheme = new Scheme(types);
        Table result = new Table(scheme, first.name + " INNER JOIN " + second.name);

        for (Row row : first.rows) {
            ArrayList<byte[]> result_table_row = new ArrayList<>();
            byte[] join_on = "".getBytes();
            for (int i = 0; i < row.Size(); ++i) {
                if (i == firstTableColumn) {
                    join_on = row.GetField(i);
                }
                result_table_row.add(row.GetField(i));
            }

            String pattern = "";
            int size_second_table_row = second.rows.get(0).Size();

            for (int i = 0; i < size_second_table_row; ++i) {
                if (i == secondTableColumn) {
                    pattern += new String(join_on);
                    if (i != size_second_table_row - 1) {
                        pattern += "|";
                    }
                } else {
                    pattern += ".*|";
                }
            }

            long vers = TableFind(secondTableVersion, pattern);
            Table foundTable = GetTable(vers);

            for (Row found_row : foundTable.rows) {
                for (int i = 0; i < found_row.Size(); ++i) {
                    result_table_row.add(found_row.GetField(i));
                }
            }
            Row result_row = new Row(result_table_row);
            result.AddRow(result_row);
            tables.remove(foundTable);
        }

        tables.add(result);
        return result.GetVersion();
    }

}
