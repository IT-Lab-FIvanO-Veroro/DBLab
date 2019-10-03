package az1.Common;

//import az1.dblab.Client.GUILauncher;
import az1.Common.Types.*;


import java.io.Serializable;
import java.util.ArrayList;

public class Scheme implements Serializable {
    private static final long serialVersionUID = 963456366123L;
    private static final String NULL_FIELD_PATTERN_VALUE = "<NULL>";
    protected ArrayList<AbstractType> types;

    public Scheme(ArrayList<AbstractType> types) {
        this.types = types;
    }

    public int Size() {
        return types.size();
    }

    public AbstractType GetType(int index) {
        return types.get(index);
    }

    public boolean Supports(Row row) {
        if (row.Size() != Size()) {
            return false;
        }

        for (int index = 0; index < Size(); ++index) {
            if (!GetType(index).Supports(row.GetField(index))) {
                return false;
            }
        }

        return true;
    }

    public String ToString() {
        StringBuilder result = new StringBuilder();
        for (AbstractType type : types) {
            if (result.length() > 0) {
                result.append(SEPARATOR);
            }

            String typeStringValue;
            if (type instanceof IntType) {
                typeStringValue = INT_TOKEN;
            }
            else if (type instanceof EnumType) {
                typeStringValue = ENUM_TOKEN;
            }
            else if (type instanceof FloatType) {
                typeStringValue = FLOAT_TOKEN;
            }
            else if (type instanceof CharType) {
                typeStringValue = CHAR_TOKEN;
            }
            else if (type instanceof LongType) {
                typeStringValue = LONG_TOKEN;
            }
            else if (type instanceof EmailType) {
                typeStringValue = EMAIL_TOKEN;
            }
            else if (type instanceof MoneyType) {
                typeStringValue = MONEY_TOKEN;
            }
            else {
                throw new RuntimeException("Scheme format exception");
            }

            result.append(typeStringValue);
        }

        return result.toString();
    }

    public static Scheme ParseFromString(String line) {
        String[] tokens = line.split(SEPARATOR);
        ArrayList<AbstractType> parsedTypes = new ArrayList<AbstractType>();
        for (String token : tokens) {
            AbstractType type;
            if (token.equals(INT_TOKEN)) {
                type = new IntType();
            }
            else if (token.equals(ENUM_TOKEN)) {
                type = new EnumType();
            }
            else if (token.equals(FLOAT_TOKEN)) {
                type = new FloatType();
            }
            else if (token.equals(CHAR_TOKEN)) {
                type = new CharType();
            }
            else if (token.equals(LONG_TOKEN)) {
                type = new LongType();
            }
            else if (token.equals(EMAIL_TOKEN)) {
                type = new EmailType();
            }
            else if (token.equals(MONEY_TOKEN)) {
                type = new MoneyType();
            }
            else {
                throw new RuntimeException("Scheme format exception");
            }

            parsedTypes.add(type);
        }

        return new Scheme(parsedTypes);
    }

    public static final String SEPARATOR = ",";
    public static final String INT_TOKEN = "Int";
    public static final String ENUM_TOKEN = "Enum";
    public static final String FLOAT_TOKEN = "Float";
    public static final String CHAR_TOKEN = "Char";
    public static final String LONG_TOKEN = "Long";
    public static final String EMAIL_TOKEN = "Email";
    public static final String MONEY_TOKEN = "Money";


    public boolean RowMatch(Row row, String mergedPattern) {
        String[] patterns = mergedPattern.split("\\|");
        if (patterns.length != row.Size()) {
            throw new RuntimeException("Can't parse pattern: " + mergedPattern + ".");
        }

        for (int index = 0; index < row.Size(); ++index) {
            String value = types.get(index).ToString(row.GetField(index));
            if (value == null) {
                value = NULL_FIELD_PATTERN_VALUE;
            }

            if (!value.matches(patterns[index])) {
                System.out.println(value + " doesn't match " + patterns[index] + ".");
                return false;
            }
        }

        return true;
    }

    public boolean IsEqual(Scheme scheme) {
        if (scheme == null || scheme.types.size() != this.types.size()) {
            return false;
        }

        for (int col = 0; col < this.types.size(); ++col) {
            AbstractType type1 = this.GetType(col);
            AbstractType type2 = scheme.GetType(col);
            if (!type1.getClass().equals(type2.getClass())) {
                return false;
            }
        }

        return true;
    }
}
