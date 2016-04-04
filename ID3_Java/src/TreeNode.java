import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ryan on 4/3/16.
 *
 * This class is used to define the struture of the node
 *
 */
public class TreeNode {

    public final static String ROOT_NODE = "root";

    public final static String LEAF_NODE = "leaf";

    // Two types of node: leaf or root
    private String type;

    // The attribute that this node stand for
    private String attribute;

    // The children nodes of this node
    private Map<String, TreeNode> children;

    // The result label of this node
    private String resLabel;

    // The data in this node
    private List<Entry> dataset;

    public TreeNode(List<Entry> dataset){
        this.dataset = dataset;
        children = new HashMap<>();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public Map<String, TreeNode> getChildren() {
        return children;
    }

    public void setChildren(Map<String, TreeNode> children) {
        this.children = children;
    }

    public String getResLabel() {
        return resLabel;
    }

    public void setResLabel(String resLabel) {
        this.resLabel = resLabel;
    }

    public List<Entry> getDataset() {
        return dataset;
    }

    public void setDataset(List<Entry> dataset) {
        this.dataset = dataset;
    }
}
