package simple;

import java.util.ArrayList;

import static simple.DPUtils.DPutil.dpIndex;
import static simple.DPUtils.DPutil.dpLaplace;

//import org.apache.commons.math3.distribution.LaplaceDistribution;

/**
 * @author yuyang
 * @create 2019-11-20 9:12
 */
public class UFDP_Main {
    public static void main(String[] args) {
        UFTree ufTree = new UFTree(1);                                //期望支持度阈值为1
        ArrayList<ArrayList<String>> transactions = ufTree.readBacketFile("D:\\workspece_idea_lunwen_date\\U_data.txt");
        //ArrayList<ArrayList<String>> transactions = fptree.readVectorFile("D:/workspece_idea_lunwen_date/2.txt", "1");
        ArrayList<String> list = ufTree.UF_growth(transactions, null, 0.0);

        double budget = 10.0 / list.size();
        int k = 5;
        ArrayList<String> items = new ArrayList<>();
        ArrayList<Double> exps = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            String[] split = list.get(i).split(":");
            items.add(split[0]);
            exps.add(Double.parseDouble(split[1]));
        }

        ArrayList<String> listInx = new ArrayList<>();
        for (int i = 0; i < k; i++) {
            int j = dpIndex(exps,1.0,10.0/5);
            listInx.add(items.remove(j));
            exps.remove(j);
        }

        for (int i = 0; i < exps.size(); i++) {
            double noise = dpLaplace(1, budget);
            exps.set(i,exps.get(i) + noise);
        }

        for (int i = 0; i < listInx.size(); i++) {
            System.out.println(listInx.get(i) + ":" + exps.get(i));
        }
    }
}
