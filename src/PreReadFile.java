
/**
 * Created by messy on 4/7/16.
 */
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PreReadFile {
    // store the median of column 2,3,6,7 in trainProdIntro
    private static List<Double> introPartition2 = new ArrayList<>();
    private static List<Double> introPartition3 = new ArrayList<>();
    private static List<Double> introPartition6 = new ArrayList<>();
    private static List<Double> introPartition7 = new ArrayList<>();

    // store the median of column 2,3,4,5 in trainProdSelection
    private static int partition;
    private static List<Double> selectPartition2 = new ArrayList<>();
    private static List<Double> selectPartition3 = new ArrayList<>();
    private static List<Double> selectPartition4 = new ArrayList<>();
    private static List<Double> selectPartition5 = new ArrayList<>();

    // store value of column 2,3,6,7 in trainProdIntro
    private static List<Double> i2 = new ArrayList<>();
    private static List<Double> i3 = new ArrayList<>();
    private static List<Integer> i6 = new ArrayList<>();
    private static List<Integer> i7 = new ArrayList<>();

    // store value of column 2,3,4,5 in trainProdSelection
    private static List<Integer> s2 = new ArrayList<>();
    private static List<Integer> s3 = new ArrayList<>();
    private static List<Double> s4 = new ArrayList<>();
    private static List<Double> s5 = new ArrayList<>();

    public static void main(String[] args) {
        // String fileName = args[0];
//        String fileName = "trainProdIntro.binary.arff";
         String fileName = "trainProdSelection.arff";

         if (fileName.equals("trainProdIntro.binary.arff")) {
             parseTrainProdIntro(fileName, 1);
         } else {
             parseTrainProdSelect(fileName, 3);
         }
         
         //parseTest
//         String testFileName = "testProdIntro.binary.arff";
         String testFileName =  "testProdSelection.arff";
         
         if (testFileName.equals("testProdIntro.binary.arff")) {
             parseTestProdIntro(testFileName);
         } else {
             parseTestProdSelect(testFileName);
         }
    }

    public static void parseTestProdIntro(String testFileName) {
        BufferedWriter bw = null;
        BufferedReader br = null;
        ArrayList<ArrayList<String>> set = new ArrayList<>();

        try {
            br = new BufferedReader(new FileReader(testFileName));
            bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("test_tmp.arff")));
            String line = null;
            boolean record = false;
            while ((line = br.readLine()) != null) {
                if (!record) {
                    if (line.contains("@data")) {
                        record = true;
                    }
                    bw.write(line + "\n");
                } else {
                    // 2,3,6,7
                    String[] recordLine = line.split(",");
                    List<String> subset = new ArrayList<>();
                    for (int i = 0; i < recordLine.length; i++) {
                        subset.add(recordLine[i]);
                    }
                    // partition 2,3,6,7 columns based on partition number
                    set.add(new ArrayList<>(subset));
                }
            }

            prodIntroHelper(set, bw);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    
    public static void parseTestProdSelect(String testFileName) {
        BufferedWriter bw = null;
        BufferedReader br = null;
        ArrayList<ArrayList<String>> set = new ArrayList<>();

        try {
            bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("test_tmp.arff")));
            br = new BufferedReader(new FileReader(testFileName));
            String line = null;
            boolean record = false;
            while ((line = br.readLine()) != null) {
                if (!record) {
                    if (line.contains("@data")) {
                        record = true;
                    }
                    bw.write(line + "\n");
                } else {
                    String[] recordLine = line.split(",");

                    List<String> subset = new ArrayList<>();
                    for (int i = 0; i < recordLine.length; i++) {
                        subset.add(recordLine[i]);
                    }
                    set.add(new ArrayList<>(subset));
                }
            }
            
            prodSelectHelper(set, bw);
            
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void prodSelectHelper(ArrayList<ArrayList<String>> set, BufferedWriter bw) throws IOException {
        for (int i = 0; i < set.size(); i++) {
            // 2
            boolean f2 = false;
            for (int j = 0; j < selectPartition2.size(); j++) {
                double number = Double.valueOf(set.get(i).get(2));
                if (number < selectPartition2.get(j)) {
                    set.get(i).set(2, String.valueOf(j));
                    f2 = true;
                    break;
                }
            }
            if (!f2) {
                set.get(i).set(2, String.valueOf(selectPartition2.size()));
            }
            // 3
            boolean f3 = false;
            for (int j = 0; j < selectPartition3.size(); j++) {
                double number = Double.valueOf(set.get(i).get(3));
                if (number < selectPartition3.get(j)) {
                    set.get(i).set(3, String.valueOf(j));
                    f3 = true;
                    break;
                }
            }
            if (!f3) {
                set.get(i).set(3, String.valueOf(selectPartition3.size()));
            }

            // 4
            boolean f4 = false;
            for (int j = 0; j < selectPartition4.size(); j++) {
                double number = Double.valueOf(set.get(i).get(4));
                if (number < selectPartition4.get(j)) {
                    set.get(i).set(4, String.valueOf(j));
                    f4 = true;
                    break;
                }
            }
            if (!f4) {
                set.get(i).set(4, String.valueOf(selectPartition4.size()));
            }

            // 5
            boolean f5 = false;
            for (int j = 0; j < selectPartition5.size(); j++) {
                double number = Double.valueOf(set.get(i).get(5));
                if (number < selectPartition5.get(j)) {
                    set.get(i).set(5, String.valueOf(j));
                    f5 = true;
                    break;
                }
            }
            if (!f5) {
                set.get(i).set(5, String.valueOf(selectPartition5.size()));
            }

            int j = 0;
            for (; j < set.get(i).size() - 1; j++) {
                bw.write(set.get(i).get(j) + ",");
            }
            bw.write(set.get(i).get(j) + "\n");
        }
    }

    public static void parseTrainProdSelect(String fileName, int partitionNum) {
        partition = partitionNum;
        BufferedReader br = null;
        FileWriter fWriter = null;
        BufferedWriter bw = null;
        ArrayList<ArrayList<String>> set = new ArrayList<>();
        System.out.println(fileName);
        try {

            fWriter = new FileWriter("train_tmp.arff", false);
            bw = new BufferedWriter(fWriter);

            br = new BufferedReader(new FileReader(fileName));
            String line = "";
            boolean record = false;
            while ((line = br.readLine()) != null) {
                if (!record) {
                    if (line.contains("@data")) {
                        record = true;
                    }
                    bw.write(line + "\n");
                } else {
                    String[] recordLine = line.split(",");

                    List<String> subset = new ArrayList<>();
                    for (int i = 0; i < recordLine.length; i++) {
                        subset.add(recordLine[i]);
                    }
                    set.add(new ArrayList<>(subset));

                    s2.add(Integer.valueOf(recordLine[2]));
                    s3.add(Integer.valueOf(recordLine[3]));
                    s4.add(Double.valueOf(recordLine[4]));
                    s5.add(Double.valueOf(recordLine[5]));
                }
            }

            Collections.sort(s2);
            Collections.sort(s3);
            Collections.sort(s4);
            Collections.sort(s5);
            int denominator = partition + 1;// 1/n
            for (int i = 1; i <= partition; i++) {
                double sp2 = s2.get((int) (s2.size() * (i * 1.0 / denominator * 1.0)));
                double sp3 = s3.get((int) (s3.size() * (i * 1.0 / denominator * 1.0)));
                double sp4 = s4.get((int) (s4.size() * (i * 1.0 / denominator * 1.0)));
                double sp5 = s5.get((int) (s5.size() * (i * 1.0 / denominator * 1.0)));
                selectPartition2.add(sp2);
                selectPartition3.add(sp3);
                selectPartition4.add(sp4);
                selectPartition5.add(sp5);
            }
            System.out.println(selectPartition2.toString());
            System.out.println(selectPartition3.toString());
            System.out.println(selectPartition4.toString());
            System.out.println(selectPartition5.toString());


            prodSelectHelper(set, bw);

            // write the median on the new file
            /*
             * bw.write("@Node partition for column 2,3,4,5 in Selection \n");
             * bw.write("2nd: " + selectMedian2 + "\n"); bw.write("3rd: " +
             * selectMedian3 + "\n"); bw.write("4th: " + selectMedian4 + "\n");
             * bw.write("5th: " + selectMedian5 + "\n");
             */
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        } finally {
            try {
                br.close();
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public static void parseTrainProdIntro(String fileName, int partitionNum) {
        partition = partitionNum;
        BufferedReader br = null;
        FileWriter fWriter = null;
        BufferedWriter bw = null;
        ArrayList<ArrayList<String>> set = new ArrayList<>();
        System.out.println(fileName);
        try {

            fWriter = new FileWriter("train_tmp.arff", false);
            bw = new BufferedWriter(fWriter);

            br = new BufferedReader(new FileReader(fileName));
            String line = "";
            boolean record = false;
            while ((line = br.readLine()) != null) {
                if (!record) { // not record
                    if (line.contains("@data")) {
                        record = true;
                    }
                    bw.write(line + "\n");
                } else { // record
                    String[] recordLine = line.split(",");

                    List<String> subset = new ArrayList<>();
                    for (int i = 0; i < recordLine.length; i++) {
                        subset.add(recordLine[i]);
                    }
                    set.add(new ArrayList<>(subset));

                    i2.add(Double.valueOf(recordLine[2]));
                    i3.add(Double.valueOf(recordLine[3]));
                    i6.add(Integer.valueOf(recordLine[6]));
                    i7.add(Integer.valueOf(recordLine[7]));
                }
            }

            Collections.sort(i2);
            Collections.sort(i3);
            Collections.sort(i6);
            Collections.sort(i7);

            int denominator = partition + 1;// 1/n
            for (int i = 1; i <= partition; i++) {
                double ip2 = i2.get((int) (i2.size() * (i * 1.0 / denominator * 1.0)));
                double ip3 = i3.get((int) (i3.size() * (i * 1.0 / denominator * 1.0)));
                double ip6 = i6.get((int) (i6.size() * (i * 1.0 / denominator * 1.0)));
                double ip7 = i7.get((int) (i7.size() * (i * 1.0 / denominator * 1.0)));
                introPartition2.add(ip2);
                introPartition3.add(ip3);
                introPartition6.add(ip6);
                introPartition7.add(ip7);
            }
            System.out.println(introPartition2.toString());
            System.out.println(introPartition3.toString());
            System.out.println(introPartition6.toString());
            System.out.println(introPartition7.toString());

            prodIntroHelper(set, bw);

            // write the median on the new file
            /*
             * bw.write("@Node partition for column 2,3,6,7 in Intro \n");
             * bw.write("2nd: " + introMedian2 + "\n"); bw.write("3rd: " +
             * introMedian3 + "\n"); bw.write("6th: " + introMedian6 + "\n");
             * bw.write("7th: " + introMedian7 + "\n");
             */
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        } finally {
            try {
                br.close();
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    private static void prodIntroHelper(ArrayList<ArrayList<String>> set, BufferedWriter bw) {
        try {

            for (int i = 0; i < set.size(); i++) {
                // 2
                boolean f2 = false;
                for (int j = 0; j < introPartition2.size(); j++) {
                    double number = Double.valueOf(set.get(i).get(2));
                    if (number < introPartition2.get(j)) {
                        set.get(i).set(2, String.valueOf(j));
                        f2 = true;
                        break;
                    }
                }
                if (!f2) {
                    set.get(i).set(2, String.valueOf(introPartition2.size()));
                }

                // 3
                boolean f3 = false;
                for (int j = 0; j < introPartition3.size(); j++) {
                    double number = Double.valueOf(set.get(i).get(3));
                    if (number < introPartition3.get(j)) {
                        set.get(i).set(3, String.valueOf(j));
                        f3 = true;
                        break;
                    }
                }
                if (!f3) {
                    set.get(i).set(3, String.valueOf(introPartition3.size()));
                }

                // 6
                boolean f6 = false;
                for (int j = 0; j < introPartition6.size(); j++) {
                    double number = Double.valueOf(set.get(i).get(6));
                    if (number < introPartition6.get(j)) {
                        set.get(i).set(6, String.valueOf(j));
                        f6 = true;
                        break;
                    }
                }
                
                if (!f6) {
                    set.get(i).set(6, String.valueOf(introPartition6.size()));
                }

                // 7
                boolean f7 = false;
                for (int j = 0; j < introPartition7.size(); j++) {
                    double number = Double.valueOf(set.get(i).get(7));
                    if (number < introPartition7.get(j)) {
                        set.get(i).set(7, String.valueOf(j));
                        f7 = true;
                        break;
                    }
                }
                if (!f7) {
                    set.get(i).set(7, String.valueOf(introPartition7.size()));
                }

                int j = 0;
                for (; j < set.get(i).size() - 1; j++) {
                    bw.write(set.get(i).get(j) + ",");
                }
                bw.write(set.get(i).get(j) + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
