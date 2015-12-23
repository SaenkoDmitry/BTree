/**
 * Created by Dmitry on 14.12.2015.
 */
public class DataItem {
    private int iData;
    private Field iField;
    public DataItem(int ii, Field f)
    {
        iData = ii;
        iField = f;
    }
    public int getKey()
    { return iData; }
    public Field getField()
    { return iField; }
}