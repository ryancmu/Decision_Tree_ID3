import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ryan on 4/3/16.
 * <p>
 * Entry means one row of data
 */
public class Entry {
    // kvPair used to store the value of every field of this row
    Map<String, String> kvPair;

    public Entry(List<String> attributes, String[] row) throws Exception {

        // if the row miss some field(s), throw exception
        if (attributes.size() != row.length) {
            throw new Exception("Illegal Row");
        }

        // initial the map
        kvPair = new HashMap<>();

        // put the attribute & value into the map
        for (int i = 0; i < attributes.size(); i++) {
            kvPair.put(attributes.get(i), row[i]);
        }
    }

    public Map<String, String> getKvPair() {
        return kvPair;
    }
}
