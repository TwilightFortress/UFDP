package dataProcessing;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import static Utils.UncertainDataUtils.RoadomProb;

/**
 * @author yuyang
 * @create 2019-11-20 19:24
 */
public class dataProcessing {
    public static void main(String[] args) throws IOException {
      //  getData();
    }

    public static void getData() throws IOException {
        ArrayList<ArrayList<String>> list = readFile("D:\\workspece_idea_lunwen_date\\确定数据集\\connect2.dat");
        Processing(list);
    }

    // 读数据
    public static ArrayList<ArrayList<String>> readFile(String path) {
        ArrayList<ArrayList<String>> transactions = new ArrayList<ArrayList<String>>();
        File file = new File(path);
        Scanner reader = null;
        int countLine = 0;

        try {
            reader = new Scanner(new
                    FileInputStream(file), "GBK");
            while (reader.hasNext()) {
                String line = reader.nextLine().trim();
                ArrayList<String> items = new ArrayList<String>(Arrays.asList(line.split(" ")));
                transactions.add(items);                            //需要记录事务总数
                countLine++;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        reader.close();
        return transactions;
    }

    // 概率处理，格式处理,写入文件
    public static void Processing(ArrayList<ArrayList<String>> list) throws IOException {
        File writename = new File("D:\\workspece_idea_lunwen_date\\确定数据集\\connect3.txt");
        writename.createNewFile();
        BufferedWriter out = new BufferedWriter(new FileWriter(writename));
        for (int i = 0; i < list.size(); i++) {
            for (int j = 0; j < list.get(i).size(); j++) {
                out.write(list.get(i).get(j) + ":" + RoadomProb() + ",");
                out.flush(); // 把缓存区内容压入文件
            }
            out.write("\r\n");
        }
        out.close();
    }

}
