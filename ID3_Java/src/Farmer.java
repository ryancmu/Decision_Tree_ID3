/**
 * Created by messy on 4/7/16.
 * This is the manage class, to build the tree, to test the tree, to predict new data.
 */
public class Farmer {

    private static String taskNo = "";
    private static String trainInputPath = "";
    private static String operationType;
    private static int fold = 10;
    private static String testInputPath = "";

    public static void main(String[] args) throws Exception {

        /**
         *      Input Parameter: a/b TrainDataPath 'cross_validation' fold
         *                       a/b TrainDataPath 'predict' testDataPath
         *
         */
        if(args.length != 4){
            printError();
            return;
        }

        taskNo = args[0];
        trainInputPath = args[1];
        if("cross_validation".equals(args[2])){
            fold = Integer.parseInt(args[3]);
            crossValidation(trainInputPath, fold);
        } else if("predict".equals(args[2])){
            testInputPath = args[3];
            predict(trainInputPath, testInputPath);
        } else {
            printError();
        }


    }

    // Do the cross validation
    private static void crossValidation(String inputPath, int fold) throws Exception {

        PreReadFile prf = new PreReadFile();
        if ("b".equals(taskNo)){
            prf.parseTrainProdIntro(inputPath);
        } else {
            prf.parseTrainProdSelect(inputPath);
        }

        DecisionTree dt = new DecisionTree();
        dt.processInput();
        String res = fold + " fold Cross Validation Accuracy: " + String.format("%.4f",dt.crossValidate(fold));
        System.out.println(res);
    }


    // DO the predict and generate the predict file.
    private static void predict(String trainPath, String testPath) throws Exception {
        PreReadFile prf = new PreReadFile();
        if ("b".equals(taskNo)){
            prf.parseTrainProdIntro(trainPath);
            prf.parseTestProdIntro(testPath);
        } else {
            prf.parseTrainProdSelect(trainPath);
            prf.parseTestProdSelect(testPath);
        }

        DecisionTree dt = new DecisionTree();
        dt.processInput();
        dt.predictTestData();
    }

    // output the error
    private static void printError(){
        System.out.println("Parameters Error!");
        System.out.println("Please Follow the README file");
    }
}
