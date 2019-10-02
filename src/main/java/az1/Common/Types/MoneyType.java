package az1.Common.Types;

public class MoneyType extends IntType{
    
    @Override
    public String ToString(byte[] data) {
        if (data == null) {
            return null;
        }

        int value = ToInt(data);
        return "" + value + "$";
    }

}
