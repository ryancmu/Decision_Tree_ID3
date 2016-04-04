import java.util.ArrayList;
import java.util.List;

/**
 * Created by messy on 4/4/16.
 */
public class Attribute {
    private String name;
    private List<String> valueList;

    public  Attribute(String name){
        this.name = name;
        valueList = new ArrayList<>();
    }

    public Attribute(String name, List<String> values){
        this.name = name;
        this.valueList = values;
    }

    public void addValue(String value){
        valueList.add(value);
    }


    public String getName() {
        return name;
    }

    public List<String> getValueList() {
        return valueList;
    }
}
