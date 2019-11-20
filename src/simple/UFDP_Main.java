package simple;

import java.util.ArrayList;

/**
 * @author yuyang
 * @create 2019-11-20 9:12
 */
public class UFDP_Main {
    public static void main(String[] args) {
        UFTree ufTree = new UFTree(1);                                //期望支持度阈值为1
        ArrayList<ArrayList<String>> transactions = ufTree.readBacketFile("D:\\workspece_idea_lunwen_date\\U_data.txt");
        //ArrayList<ArrayList<String>> transactions = fptree.readVectorFile("D:/workspece_idea_lunwen_date/2.txt", "1");
        ArrayList<String> list = ufTree.UF_growth(transactions, null,0.0);
        for (String s : list)
            System.out.println(s);
    }
}
