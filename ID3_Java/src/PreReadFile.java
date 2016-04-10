/**
 * Created by messy on 4/7/16.
 */
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class PreReadFile {
    //store the median of column 2,3,6,7 in trainProdIntro
    private static double introMedian2 = 0;
    private static double introMedian3 = 0;
    private static double introMedian6 = 0;
    private static double introMedian7 = 0;

    //store the median of column 2,3,4,5 in trainProdSelection
    private static double selectMedian2 = 0;
    private static double selectMedian3 = 0;
    private static double selectMedian4 = 0;
    private static double selectMedian5 = 0;

    //store value of column 2,3,6,7 in trainProdIntro
    private static List<Double> i2 = new ArrayList<>();
    private static List<Double> i3 = new ArrayList<>();
    private static List<Integer> i6 = new ArrayList<>();
    private static List<Integer> i7 = new ArrayList<>();

    //store value of column 2,3,4,5 in trainProdSelection
    private static List<Integer> s2 = new ArrayList<>();
    private static List<Integer> s3 = new ArrayList<>();
    private static List<Double> s4 = new ArrayList<>();
    private static List<Double> s5 = new ArrayList<>();

    public static void main(String[] args) {
//        String fileName = args[0];
        String fileName = "trainProdIntro.binary.arff";
//          String fileName = "trainProdIntro.binary.arff";

//        if (fileName.equals("trainProdIntro.binary.arff")) {
//            parseTrainProdIntro(fileName);
//        } else {
//            parseTrainProdSelect(fileName);
//        }
    }

    public void parseTrainProdSelect(String fileName) {
        BufferedReader br = null;
        FileWriter fWriter = null;
        BufferedWriter bw = null;
        ArrayList<ArrayList<String>> set = new ArrayList<>();
        System.out.println(fileName);
        try {

            fWriter = new FileWriter("trainProdSelection2.arff", false);
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
            selectMedian2 = s2.get(s2.size() / 2);
            selectMedian3 = s3.get(s3.size() / 2);
            selectMedian4 = s4.get(s4.size() / 2);
            selectMedian5 = s5.get(s5.size() / 2);

            //change value to 1(smaller) or 2(Bigger)

            for (int i = 0; i < set.size(); i++) {
                if (Double.valueOf(set.get(i).get(2)) < selectMedian2) {
                    set.get(i).set(2, "1");
                } else {
                    set.get(i).set(2, "2");
                }
                if (Double.valueOf(set.get(i).get(3)) < selectMedian3) {
                    set.get(i).set(3, "1");
                } else {
                    set.get(i).set(3, "2");
                }
                if (Double.valueOf(set.get(i).get(4)) < selectMedian4) {
                    set.get(i).set(4, "1");
                } else {
                    set.get(i).set(4, "2");
                }
                if (Double.valueOf(set.get(i).get(5)) < selectMedian5) {
                    set.get(i).set(5, "1");
                } else {
                    set.get(i).set(5, "2");
                }
//                    System.out.println("each line : " + set.get(i).toString());

                int j = 0;
                for (; j < set.get(i).size() - 1; j++) {
                    bw.write(set.get(i).get(j) + ",");
                }
                bw.write(set.get(i).get(j) + "\n");
            }

            //write the median on the new file
            /*
            bw.write("@Node partition for column 2,3,4,5 in Selection \n");
            bw.write("2nd: " + selectMedian2 + "\n");
            bw.write("3rd: " + selectMedian3 + "\n");
            bw.write("4th: " + selectMedian4 + "\n");
            bw.write("5th: " + selectMedian5 + "\n");
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

    public void parseTrainProdIntro(String fileName) {

        BufferedReader br = null;
        FileWriter fWriter = null;
        BufferedWriter bw = null;
        ArrayList<ArrayList<String>> set = new ArrayList<>();
        System.out.println(fileName);
        try {

            fWriter = new FileWriter("trainProdIntro.binary2.arff", false);
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
            introMedian2 = i2.get(i2.size() / 2);
            introMedian3 = i3.get(i3.size() / 2);
            introMedian6 = i6.get(i6.size() / 2);
            introMedian7 = i7.get(i7.size() / 2);

            //change value to 1(smaller) or 2(Bigger)
            for (int i = 0; i < set.size(); i++) {
                if (Double.valueOf(set.get(i).get(2)) < introMedian2) {
                    set.get(i).set(2, "1");
                } else {
                    set.get(i).set(2, "2");
                }
                if (Double.valueOf(set.get(i).get(3)) < introMedian3) {
                    set.get(i).set(3, "1");
                } else {
                    set.get(i).set(3, "2");
                }
                if (Double.valueOf(set.get(i).get(6)) < introMedian6) {
                    set.get(i).set(6, "1");
                } else {
                    set.get(i).set(6, "2");
                }
                if (Double.valueOf(set.get(i).get(7)) < introMedian7) {
                    set.get(i).set(7, "1");
                } else {
                    set.get(i).set(7, "2");
                }
//                    System.out.println("each line : " + set.get(i).toString());

                int j = 0;
                for (; j < set.get(i).size() - 1; j++) {
                    bw.write(set.get(i).get(j) + ",");
                }
                bw.write(set.get(i).get(j) + "\n");
            }

            //write the median on the new file
            /*
            bw.write("@Node partition for column 2,3,6,7 in Intro \n");
            bw.write("2nd: " + introMedian2 + "\n");
            bw.write("3rd: " + introMedian3 + "\n");
            bw.write("6th: " + introMedian6 + "\n");
            bw.write("7th: " + introMedian7 + "\n");
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

}
