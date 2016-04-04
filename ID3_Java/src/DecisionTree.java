import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
                attributes.add(elements[1]);
                line = br.readLine();
            }

            label = attributes.get(attributes.size() - 1);

            int attrSize = attributes.size();

            //step 3:   read the data rows
            while((line = br.readLine()) != null){
                String[] elements = line.split(",");
                Entry entry = new Entry(attributes, elements);
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

        //TODO!!! It's better to create an Attribute class to save all the possible values of each attribute


        return 0;
    }

    // Calculate the Information Gain
    private double calcIG(){
        return 0;
    }

    // Recusive to build the decision tree
    private void buildTree(List<Entry> curDataset, List<String> leftAttribute){
        /*      recusive stop condition
                    1. All current dataset are from same label
                    2. The current dataset is empty
                    3. All dataset's values are same for all attributes
          */
        TreeNode node = new TreeNode();
        node.setDataset(curDataset);

        double infoEntropy = calcIE(curDataset);

        // select the attribute for this node
        for (String attr: leftAttribute){

        }

    }

    // Cross Validation
    private void crossValidate(){

    }

    // Predict the test set
    private void predictTestData(){

    }


}
