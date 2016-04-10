import java.io.*;
import java.util.*;

/**
 * Created by ryan on 4/2/16.
 *
 */
public class DecisionTree {

    // Save all attribute names
    List<String> attributes;

    Map<String, Attribute> attributeMap;

    // Save all data rows
    List<Entry> dataset;

    // Save the label name
    String label = null;


    public DecisionTree(){
        attributes = new ArrayList<>();
        attributeMap = new HashMap<>();
        dataset = new ArrayList<>();
    }

    public void testBuild(){
        List<String> leftAttrs = new ArrayList<>();
        for(String s : attributes){
            if(s.equals(label)){
                continue;
            } else {
                leftAttrs.add(s);
            }
        }
        buildTree(dataset, leftAttrs);
    }

    // Input Process
    public void processInput() throws Exception {

        BufferedReader br;

        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream("trainProdIntro.binary2.arff")));
            //br = new BufferedReader(new InputStreamReader(new FileInputStream("test.arff")));

            String line = null;
            while ((line = br.readLine()) != null) {
                // First Read until @attribute attrName value(s)
                if (line.split("\\s+").length != 3) {
                    continue;
                } else {
                    break;
                }
            }

            //step 2:   read and add all atttributes
            while (!line.equals("")) {
                String[] elements = line.split("\\s+");

                Attribute tmpAttr = new Attribute(elements[1].trim());
                attributeMap.put(elements[1].trim(), tmpAttr);
                attributes.add(elements[1]);
                line = br.readLine();
            }
            label = attributes.get(attributes.size() - 1);

            // read the useless "@data" line
            line = br.readLine();

            //step 3:   read the data rows
            while ((line = br.readLine()) != null) {
                String[] elements = line.split(",");
                Entry entry = new Entry(attributes, elements);

                //add all possible values to corresponding Attribute
                addValueToAttributes(attributes, attributeMap, elements);
                dataset.add(entry);
            }

//            System.out.println(dataset.size());
//            for (Entry e : dataset){
//                for (String s: attributes){
//                    System.out.print(e.getKvPair().get(s) + " ");
//                }
//                System.out.println("");
//            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Entry> processTestInput(){

        List<Entry> testDataSet = new ArrayList<>();

        BufferedReader br;

        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream("test_tmp.arff")));

            String line = null;
            while ((line = br.readLine()) != null) {
                // First Read until @attribute attrName value(s)
                if (line.split("\\s+").length != 3) {
                    continue;
                } else {
                    break;
                }
            }

            //step 2:   read and add all atttributes
            while (!line.equals("")) {
                line = br.readLine();
            }

            // read the useless "@data" line
            line = br.readLine();

            //step 3:   read the data rows
            while ((line = br.readLine()) != null) {
                String[] elements = line.split(",");
                Entry entry = new Entry(attributes, elements);

                //add all possible values to corresponding Attribute
                //addValueToAttributes(attributes, attributeMap, elements);
                testDataSet.add(entry);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return testDataSet;
    }


    // Calculate the Information Entropy
    private double calcIE(List<Entry> curDataset) {
        Map<String, Integer> labelMap = new HashMap<>();
//        for (Entry e: curDataset){
//
//        }

        for (Entry e : curDataset) {
            String tmpLabel = e.getKvPair().get(label);
            if (labelMap.containsKey(tmpLabel)) {
                labelMap.put(tmpLabel, labelMap.get(tmpLabel) + 1);
            } else {
                labelMap.put(tmpLabel, 1);
            }
        }

        double entropy = 0;
        int datasetSize = curDataset.size();

        Iterator it = labelMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Integer> pair = (Map.Entry) it.next();
            int times = pair.getValue();
            double pk = (double) times / (double) datasetSize;
            entropy += (-1) * pk * Math.log(pk);
        }

        return entropy;
    }

    // Calculate the Information Gain
    private double calcIG() {
        return 0;
    }

    // Recusive to build the decision tree
    public TreeNode buildTree(List<Entry> curDataset, List<String> leftAttribute) {
        /*      recusive stop condition
                    1. All current dataset are from same label
                    2. The current dataset is empty
                    3. There's no attribute left
                    4. All dataset's values are same for all attributes
          */
        TreeNode node = new TreeNode(curDataset);

        // If satisfy the stop condition
        if (checkSameLabel(curDataset)) {
            node.setType(TreeNode.LEAF_NODE);
            node.setResLabel(curDataset.get(0).getKvPair().get(label));
            return node;
        }

        if(checkSameAttributeValue(curDataset, leftAttribute)){
            node.setType(TreeNode.LEAF_NODE);
            node.setResLabel(findMajorityLabel(curDataset));
            return node;
        }


        if (checkEmptyAttribute(leftAttribute)) {
            node.setType(TreeNode.LEAF_NODE);
            node.setResLabel(findMajorityLabel(curDataset));
            return node;
        }

        node.setType(TreeNode.ROOT_NODE);
        double infoEntropy = calcIE(curDataset);
        System.out.println("=====calculate info entropy=====");
//        for (Entry e: curDataset){
//            System.out.print(e.getKvPair().get(label) + " / ");
//        }
        System.out.println(infoEntropy);
        double infoGain = 0;
        String splitAttr = "";

        // select the attribute for this node
        System.out.println("Attribute Number: " + leftAttribute.size() + " " + leftAttribute.get(0));
        System.out.println("number of dataset " + curDataset.size());
//        if(curDataset.size() == 4){
//            Map<String, String> tmpMap = curDataset.get(i)
//        }
        for (String attr : leftAttribute) {

            System.out.println("Attr Name: " + attr);

            // get the new dataset of attr
            Attribute tmpAttr = attributeMap.get(attr);
            List<String> tmpValueList = tmpAttr.getValueList();
            double cIG = 0;
            for (String value : tmpValueList) {
                List<Entry> tmpDataset = new ArrayList<>();
                for (Entry e : curDataset) {
                    if (e.getKvPair().get(tmpAttr.getName()).equals(value)) {
                        tmpDataset.add(e);
                    }
                }

                cIG += ((double) tmpDataset.size() / curDataset.size()) * calcIE(tmpDataset);
                System.out.println("Attr Value " + value + " cIG = " + cIG);
            }

            double ig = infoEntropy - cIG;
            if (ig > infoGain) {
                infoGain = ig;
                splitAttr = attr;
            }
        }

        System.out.println("split Attribute: " + splitAttr + " IG: " + infoGain);

        // Generate the new leftAttribetes for Children
        node.setAttribute(splitAttr);
        List<String> nextLeftAttributes = removeAttr(leftAttribute, splitAttr);

        // Recusive to build children
        Attribute attribute = attributeMap.get(splitAttr);
        for (String attrValue : attribute.getValueList()) {
            List<Entry> tmpDataset = getDatasetByAttrValue(curDataset, splitAttr, attrValue);
            // if the datasize is empty, just create a new lead node and the resLabel is the majority of parent dataset
            if (tmpDataset == null || tmpDataset.size() == 0) {
                TreeNode newNode = new TreeNode(null);
                newNode.setResLabel(findMajorityLabel(curDataset));
                newNode.setType(TreeNode.LEAF_NODE);
                node.getChildren().put(attrValue, newNode);
            } else if(nextLeftAttributes.size() == 0){
                TreeNode newNode = new TreeNode(null);
                newNode.setResLabel(findMajorityLabel(tmpDataset));
                newNode.setType(TreeNode.LEAF_NODE);
                node.getChildren().put(attrValue, newNode);
            }
            else {
                node.getChildren().put(attrValue, buildTree(tmpDataset, nextLeftAttributes));
            }
        }
        return node;
    }

    // Check whether all dataset have the same label
    private boolean checkSameLabel(List<Entry> curDataset) {
        String curLabel = null;
        for (Entry e : curDataset) {
            if (curLabel == null) {
                curLabel = e.getKvPair().get(label);
            } else {
                if (!curLabel.equals(e.getKvPair().get(label))) {
                    return false;
                }
            }
        }
        return true;
    }

    // Check whether the dataset is empty
    private boolean checkEmptyDataset(List<Entry> curDataset) {
        return curDataset.size() == 0 ? true : false;
    }

    // Check whether the attribute left is empty
    private boolean checkEmptyAttribute(List<String> leftAttributes) {
        return leftAttributes.size() == 0 ? true : false;
    }

    private boolean checkSameAttributeValue(List<Entry> curDataset, List<String> leftAttributes){
        for (String s : leftAttributes){
            String attrValue = curDataset.get(0).getKvPair().get(s);
            for(int i = 1; i < curDataset.size(); i++){
                if(!curDataset.get(i).getKvPair().get(s).equals(attrValue)){
                    return false;
                }
            }
        }
        return true;
    }

    // Find the majority label for this dataset
    private String findMajorityLabel(List<Entry> curDataset) {
        Map<String, Integer> labelMap = new HashMap<>();
        for (Entry e : curDataset) {
            String tmpLabel = e.getKvPair().get(label);
            if (!labelMap.containsKey(tmpLabel)) {
                labelMap.put(tmpLabel, 0);
            } else {
                labelMap.put(tmpLabel, labelMap.get(tmpLabel) + 1);
            }
        }

        int maxTime = 0;
        String maxLabel = null;
        for (Map.Entry<String, Integer> entry : labelMap.entrySet()) {
            if (entry.getValue() > maxTime) {
                maxLabel = entry.getKey();
                maxTime = entry.getValue();
            }
        }
        return maxLabel;
    }


    private List<Entry> getDatasetByAttrValue(List<Entry> curDataset, String attr, String value) {

        //TODO this method can be optimized by get all datasets of all values by one loop
        List<Entry> newDataset = new ArrayList<>();
        for (Entry e : curDataset) {
            if (e.getKvPair().get(attr).equals(value)) {
                newDataset.add(e);
            }
        }
        return newDataset;
    }

    private List<String> removeAttr(List<String> attrs, String rmAttr) {
        List<String> leftAttributes = new ArrayList<>();
        for (String s : attrs) {
            if (!s.equals(rmAttr)) {
                leftAttributes.add(s);
            }
        }
        return leftAttributes;
    }

    private void addValueToAttributes(List<String> attrs, Map<String, Attribute> attrMap, String[] elements) {
        for (int i = 0; i < attrs.size(); i++) {
            String value = elements[i];
            Attribute attribute = attrMap.get(attrs.get(i));
            if (attribute.getValueList().contains(value)) {
                continue;
            } else {
                attribute.addValue(value);
            }
        }
    }

    // Cross Validation
    public double crossValidate(int fold) {

        // Make the dataset random
        Collections.shuffle(dataset);

        // Extract 10% every time to test, and left dataset to train
        // Split the dataset into fold pieces;
        List<List<Entry>> foldList = new ArrayList<>();
        int numFold = dataset.size() / fold;
        for (int i = 0; i < fold; i++) {
            List<Entry> tmpList = new ArrayList<>();
            int start = i * numFold;
            int end = i == (fold - 1) ? dataset.size() : (i + 1) * numFold;
            for (int j = start; j < end; j++) {
                tmpList.add(dataset.get(j));
            }
            foldList.add(tmpList);
        }

        // Validate fold times;
        double accuracy = 0;
        for (int i = 0; i < fold; i++) {
            List<Entry> newDataset = new ArrayList<>();
            for (int j = 0; j < fold; j++) {
                if (j == i) {
                    continue;
                }
                newDataset.addAll(foldList.get(j));
            }
            accuracy += getAccuracy(newDataset, foldList.get(i));

        }
        double avgAccuracy = accuracy / fold;
        return avgAccuracy;
    }

    private double getAccuracy(List<Entry> trainDataset, List<Entry> testDataset){
        List<String> leftAttrs = new ArrayList<>();
        for(String s : attributes){
            if(s.equals(label)){
                continue;
            } else {
                leftAttrs.add(s);
            }
        }
        TreeNode root = buildTree(trainDataset, leftAttrs);
        int testNum = testDataset.size();
        int correctNum = 0;
        for(Entry e: testDataset) {
            if (classifyOneEntry(e, root).equals(e.getKvPair().get(label))){
                correctNum++;
            }
        }
        double accuracy = (double)correctNum / testNum;
        return accuracy;
    }

    private String classifyOneEntry(Entry e, TreeNode root){
        // If it is a leaf node, just return true or false;
        if (root.getType().equals(TreeNode.LEAF_NODE)){
            return root.getResLabel();
        }
        String attr = root.getAttribute();
        String entryAttrValue = e.getKvPair().get(attr);
        TreeNode nextNode = root.getChildren().get(entryAttrValue);
        return classifyOneEntry(e, nextNode);
    }


    // Predict the test set
    private void predictTestData() {

        List<Entry> testDataset = processTestInput();

        List<String> leftAttrs = new ArrayList<>();
        for(String s : attributes){
            if(s.equals(label)){
                continue;
            } else {
                leftAttrs.add(s);
            }
        }
        TreeNode root = buildTree(dataset, leftAttrs);

        BufferedWriter bw;
        try {
            bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("result.arff")));
            for (Entry e : testDataset){
                String eLabel = classifyOneEntry(e, root);
                e.getKvPair().put(label, eLabel);
                bw.write(e.toString());
                bw.newLine();
            }
            bw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
