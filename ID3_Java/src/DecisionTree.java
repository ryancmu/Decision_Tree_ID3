import java.io.*;
import java.util.*;

/**
 * Created by ryan on 4/2/16.
 *
 * Some References: http://www.cnblogs.com/zhangchaoyang/articles/2196631.html
 *                  http://czhsuccess.iteye.com/blog/1864652
 *                  https://github.com/charlottelinxue/C4.5-Decision-Tree-Implementation
 *
 *
 */
public class DecisionTree {

    // Save all attribute names
    List<String> attributes = new ArrayList<>();

    Map<String, Attribute> attributeMap = new HashMap<>();

    // Save all data rows
    List<Entry> dataset = new ArrayList<>();

    // Save the label name
    String label = null;

    // Input Process
    private void processInput() throws Exception {


        BufferedReader br;

        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream("dataset.arff")));

            String line = null;
            while((line = br.readLine()) != null){
                // First Read until @attribute attrName value(s)
                if (line.split("\\s+").length != 3){
                    continue;
                } else {
                    break;
                }
            }

            //step 2:   read and add all atttributes
            while(!line.equals("")){
                String[] elements = line.split("\\s+");

                Attribute tmpAttr = new Attribute(elements[1].trim());
                attributeMap.put(elements[1].trim(), tmpAttr);
                attributes.add(elements[1]);
                line = br.readLine();
            }
            label = attributes.get(attributes.size() - 1);

            //step 3:   read the data rows
            while((line = br.readLine()) != null){
                String[] elements = line.split(",");
                Entry entry = new Entry(attributes, elements);
                //TODO!!! add all possible values to corresponding Attribute
                dataset.add(entry);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Calculate the Information Entropy
    private double calcIE(List<Entry> curDataset){
        Map<String, Integer> labelMap = new HashMap<>();

        for (Entry e: curDataset){
            String tmpLabel = e.getKvPair().get(label);
            if(labelMap.containsKey(tmpLabel)){
                labelMap.put(tmpLabel, labelMap.get(tmpLabel) + 1);
            } else {
                labelMap.put(tmpLabel, 1);
            }
        }

        double entropy = 0;
        int datasetSize = curDataset.size();

        Iterator it = labelMap.entrySet().iterator();
        while(it.hasNext()){
            Map.Entry<String, Integer> pair = (Map.Entry) it.next();
            int times = pair.getValue();
            double pk = (double)times / (double)datasetSize;
            entropy += - pk * Math.log(pk);
        }

        return entropy;
    }

    // Calculate the Information Gain
    private double calcIG(){
        return 0;
    }

    // Recusive to build the decision tree
    private TreeNode buildTree(List<Entry> curDataset, List<String> leftAttribute){
        /*      recusive stop condition
                    1. All current dataset are from same label
                    2. The current dataset is empty
                    3. All dataset's values are same for all attributes
          */
        TreeNode node = new TreeNode(curDataset);

        // If satisfy the stop condition
        if(checkSameLabel(curDataset)){
            node.setType(TreeNode.LEAF_NODE);
            node.setResLabel(curDataset.get(0).getKvPair().get(label));
            return node;
        }
        if (checkEmptyDataset(curDataset)){
            return null;
        }

        if (checkEmptyAttribute(leftAttribute)){
            node.setType(TreeNode.LEAF_NODE);
            node.setResLabel(findMajorityLabel(dataset));
            return node;
        }

        node.setType(TreeNode.ROOT_NODE);
        double infoEntropy = calcIE(curDataset);
        double infoGain = 0;
        String splitAttr = "";

        // select the attribute for this node
        for (String attr: leftAttribute){
            // get the new dataset of attr
            Attribute tmpAttr = attributeMap.get(attr);
            List<String> tmpValueList = tmpAttr.getValueList();
            double cIG = 0;
            for (String value: tmpValueList){
                List<Entry> tmpDataset = new ArrayList<>();
                for (Entry e : curDataset){
                    if (e.getKvPair().get(tmpAttr.getName()).equals(value)){
                        tmpDataset.add(e);
                    }
                }
                cIG += ((double) tmpDataset.size() / curDataset.size() ) * calcIE(tmpDataset);
            }

            double ig = infoEntropy - cIG;
            if(ig > infoGain){
                infoGain = ig;
                splitAttr = attr;
            }
        }

        // Generate the new leftAttribetes for Children
        node.setAttribute(splitAttr);
        List<String> nextLeftAttributes = removeAttr(leftAttribute, splitAttr);

        // Recusive to build children
        Attribute attribute = attributeMap.get(splitAttr);
        for (String attrValue : attribute.getValueList()){
            List<Entry> tmpDataset = getDatasetByAttrValue(curDataset, splitAttr, attrValue);
            node.getChildren().put(attrValue, buildTree(tmpDataset, nextLeftAttributes));
        }

        return node;
    }

    private boolean checkSameLabel(List<Entry> curDataset){
        String curLabel = null;
        for (Entry e: curDataset){
            if (curLabel == null){
                curLabel = e.getKvPair().get(label);
            } else {
                if(!curLabel.equals(e.getKvPair().get(label))){
                    return false;
                }
            }
        }
        return true;
    }

    private boolean checkEmptyDataset(List<Entry> curDataset){
        return curDataset.size() == 0 ? true : false;
    }

    private boolean checkEmptyAttribute (List<String> leftAttributes){
        return leftAttributes.size() == 0 ? true : false;
    }

    private String findMajorityLabel(List<Entry> curDataset){
        Map<String, Integer> labelMap = new HashMap<>();
        for(Entry e : curDataset){
            String tmpLabel = e.getKvPair().get(label);
            if(!labelMap.containsKey(tmpLabel)){
                labelMap.put(tmpLabel, 0);
            } else {
                labelMap.put(tmpLabel, labelMap.get(tmpLabel) + 1);
            }
        }

        int maxTime = 0;
        String maxLabel = null;
        for (Map.Entry<String, Integer> entry : labelMap.entrySet()) {
            if(entry.getValue() > maxTime){
                maxLabel = entry.getKey();
                maxTime = entry.getValue();
            }
        }
        return maxLabel;
    }


    private List<Entry> getDatasetByAttrValue(List<Entry> curDataset, String attr, String value){

        //TODO this method can be optimized by get all datasets of all values by one loop
        List<Entry> newDataset = new ArrayList<>();
        for (Entry e : curDataset){
            if(e.getKvPair().get(attr).equals(value)){
                newDataset.add(e);
            }
        }
        return newDataset;
    }

    private List<String> removeAttr(List<String> attrs, String rmAttr){
        List<String> leftAttributes = new ArrayList<>();
        for (String s : attrs){
            if(!s.equals(rmAttr)){
                leftAttributes.add(s);
            }
        }
        return leftAttributes;
    }

    // Cross Validation
    private void crossValidate(){

    }

    // Predict the test set
    private void predictTestData(){

    }


}
