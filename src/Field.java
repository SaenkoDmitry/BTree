import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dmitry on 29.11.2015.
 */
public class Field {
    public String name;
    public String date;
    public List<Item> description;

    Field(String name, String date, List<Item> description)
    {
        this.name = name;
        this.date = date;
        this.description = description;
    }
    void print()
    {
        System.out.println("|*\n name : " + name + "; date : " + date + "; description : ");
        for (int i = 0; i < description.size(); i++) {
            System.out.println(description.get(i).string);
        }
        System.out.println(" *|\n");
    }
    String getPassword()
    {
        return date;
    }
}
class Item
{
    Item (String string)
    {
        super();
        this.string = string;
    }
    String string;
}