package simple;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * @author yuyang
 * @create 2019-08-06 16:12
 */
public class UFTree {
    private String splitChar;                                        //分隔符
    private double support;                                            //支持度阈值（比例值）
    private double supportNum;                                        //支持度阈值（绝对值）

    /*
     * @param1	:指定支持度阈值
     * @return	:无
     * @function:构造一个FPTree实例，以调用相关方法，分隔符默认为","，
     * 			 判断输入支持度阈值为比例值或绝对值
     */
    public UFTree(double support) {
        super();
        this.splitChar = ",";
        if (support >= 1)
            this.supportNum = support;
        else
            this.support = support;
    }

    public String getSplitChar() {
        return this.splitChar;
    }

    /*
     * @param1	:给定文件分隔符
     * @function:文件分隔符仅可通过此方法进行修改
     */
    public void setSplitChar(String splitChar) {
        this.splitChar = splitChar;
    }

    /*
     * @param1	:输入文件路径
     * @return	:所有事务组成的二维列表
     * @function:将文件内容按行读出，一行为一条事务，按指定的分隔符分隔成项，存入列表
     * 			 文件内容要求为购物篮形式，如：牛奶，啤酒，咖啡
     * 			 					      啤酒，咖啡
     */
    public ArrayList<ArrayList<String>> readBacketFile(String path) {
        ArrayList<ArrayList<String>> transactions = new ArrayList<ArrayList<String>>();
        File file = new File(path);
        Scanner reader = null;
        int countLine = 0;

        try {
            reader = new Scanner(new
                    FileInputStream(file), "GBK");
            while (reader.hasNext()) {
                String line = reader.nextLine().trim();
                ArrayList<String> items = new ArrayList<String>(Arrays.asList(line.split(splitChar)));
                transactions.add(items);                            //需要记录事务总数
                countLine++;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        reader.close();
        if (supportNum == 0)    //当给定的支持度阈值为比例值时，需要乘上事务总数求出支持度阈值绝对值
            supportNum = support * countLine;   //如给定的支持度阈值为0.4，事务总数为20条，则支持度阈值绝对值为：0.4 * 20 = 8

        return transactions;
    }

    /*
     * @param1	:输入文件路径
     * @param2	:给定判断项是否存在的标识，如给出的例子中标识为“1”
     * @return	:所有事务组成的二维列表
     * @function:将文件内容按行读出，一行为一条事务，按指定的分隔符分隔成项，存入列表
     * 			 文件内容要求为向量形式，如：牛奶，啤酒，咖啡
     * 									 1 ， 1 ， 1
     * 									 0 ， 1 ， 1
     */
    public ArrayList<ArrayList<String>> readVectorFile(String path, String exist) {
        ArrayList<ArrayList<String>> transactions = new ArrayList<ArrayList<String>>();
        File file = new File(path);
        Scanner reader = null;
        int countLine = 0;
        try {
            reader = new Scanner(new
                    FileInputStream(file));                                //表头是项目名称，单独处理、保存
            String[] itemNames = reader.nextLine().trim().split(this.getSplitChar());
            while (reader.hasNext()) {
                String line = reader.nextLine().trim();
                ArrayList<String> items = new ArrayList<String>(Arrays.asList(line.split(this.getSplitChar())));
                ArrayList<String> appearedItems = new ArrayList<String>();
                for (int i = 0; i < items.size(); i++)
                    if (items.get(i).equals(exist))                    //根据给定标识判断项是否出现
                        appearedItems.add(itemNames[i]);
                transactions.add(appearedItems);
                countLine++;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        reader.close();
        if (supportNum == 0)
            supportNum = support * countLine;

        return transactions;
    }

    /*
        得到项头表，项头表中包含项集期望支持度
     */
    public LinkedList<TreeNode> buildHeaderTable(ArrayList<ArrayList<String>> transactions) {
        LinkedList<TreeNode> headerTable = new LinkedList<TreeNode>();
        //key:项名 value:代表当前项名的节点（包括当前项期望支持度）
        HashMap<String, TreeNode> map = new HashMap<String, TreeNode>();
        for (ArrayList<String> items : transactions) {
            for (String item : items) {
                String[] split = item.split(":");
                double preexp = Double.parseDouble(split[1]);
                if (map.containsKey(split[0]))
                    map.get(split[0]).inc(1, preexp);
                else {
                    TreeNode node = new TreeNode(split[0], 1,preexp);
                    map.put(split[0], node);
                }
            }
        }

        for (String name : map.keySet()) {
            TreeNode node = map.get(name);
            if (node.getProb() >= supportNum)                        ////只保存支持度大于支持度阈值（绝对值）的项
                headerTable.add(node);
        }
        Collections.sort(headerTable, new TreeNodeSort());            //使用Collections的sort方法，需要对TreeNode类实现一个排序类TreeNodeSort
        return headerTable;
    }

    /*
     * @param1	:所有事务
     * @param2	:按支持度降序排列的频繁一项集
     * @return	:排序后的所有事务
     * @function:将所有事务按频繁项顺序排序后，返回排序后的事务
     */
    public ArrayList<ArrayList<String>> sortByFreqItem(ArrayList<ArrayList<String>> transactions,
                                                       LinkedList<TreeNode> itemSortByFreq) {
        //保存排序后的所有事务
        ArrayList<ArrayList<String>> sortedTransactions = new ArrayList<ArrayList<String>>();
        for (ArrayList<String> transaction : transactions) {
            ArrayList<String> sortedItem = new ArrayList<String>();    ////保存排序后的一条事务
            int itemNum = transaction.size();
            for (TreeNode node : itemSortByFreq) {
                String str = iscontains(transaction,node.getName());//////对排序后的频繁一项集遍历
                if (str != null) {            //////若当前事务中存在该频繁项集，则保存
                   // sortedItem.add(str + ":" + node.getProb1());
                     sortedItem.add(str);
                    itemNum--;
                }
                if (itemNum == 0)                                    //////用以计数避免无用的循环
                    break;
            }
            sortedTransactions.add(sortedItem);                        ////每次循环处理一条事务
        }

        return sortedTransactions;
    }

    public String iscontains(ArrayList<String> list, String name){

        for (int i = 0; i < list.size(); i++) {
            if(list.get(i).contains(name))
            {
                return list.get(i);
            }
        }
        return null;
    }
    /*
     * @param1	:按频繁一项集顺序排好序的所有事务
     * @param2	:项头表，此时表中仅包含频繁一项集及其出现次数，没有指向相同结点的链
     * @return	:返回构建好的FP树的根结点
     * @function:利用所有事务和项头表
     */
    public TreeNode buildUFTree(ArrayList<ArrayList<String>> transactions, LinkedList<TreeNode> headerTable) {
        TreeNode root = new TreeNode("root", 0, null);                //树根结点，计数为0，无父结点
        for (ArrayList<String> items : transactions) {
            TreeNode parent = root;                                    ////
            for (String item : items) {
                //new: 如果存在加入映射表<tiKey, pi>
                TreeNode itemNode = exist(item, parent);            //////根据父结点判断当前结点是否存在，并获取正确的结点
                addSameNode(headerTable, itemNode);                    //////得到结点后，放入项头表中
                parent = itemNode;                                    //////父结点动态变化
            }
        }
        return root;
    }

    /*
     * @param1	:项头表
     * @param2	:当前结点
     * @return	:无
     * @function:为项头表中对应的项添加指向当前结点的链式关系
     */
    public void addSameNode(LinkedList<TreeNode> headerTable, TreeNode itemNode) {
        for (TreeNode head : headerTable)
            if (head.getName().equals(itemNode.getName())) {            ////遍历项头表找到当前结点对应的项
                while (head.hasNextSameNode()) {
                    head = head.getNextSameNode();                    //////寻找对应的项的链表，找到其指向的最后一个相同结点
                    if (head == itemNode)                            //////若遍历链表时发现当前结点已存在链式关系，即无需添加，直接返回
                        return;
                }
                head.setNextSameNode(itemNode);                        ////若链表中不存在当前结点，则使最后一个结点指向当前结点，形成链式关系
                return;
            }
    }

    /*
     * @param1	:项名
     * @param2	:父结点
     * @return	:项名对应的结点
     * @function:根据父结点，判断当前项是否存在，若已存在则其次数加一并返回该结点，
     * 			 若不存在则创建一个计数为1的结点并返回
     */
    public TreeNode exist(String item, TreeNode parent) {
        for (TreeNode child : parent.getChilds())
            //item.substring(0,item.lastIndexOf(":"))
            if ((child.getName() + ":" + child.getProb()).equals(item.split(":")[0] + ":" + item.split(":")[1])) {
                child.inc(1);
                return child;
            }
        int len = item.split(":").length;
        TreeNode node = null;
        if(len == 2){
             node = new TreeNode(item.split(":")[0], 1,Double.parseDouble(item.split(":")[1]), Double.parseDouble(item.split(":")[1]),parent);
        }
        else if(len == 3){
            node = new TreeNode(item.split(":")[0], 1,Double.parseDouble(item.split(":")[1]), Double.parseDouble(item.split(":")[2]),parent);
        }
        parent.addChild(node);                                        //父结点的子结点列表中也要添加当前结点
        return node;
    }

    /*
     * @param1	:FP树结点
     * @return	:该结点对应的条件模式基，以列表的形式存储
     * @function:根据当前结点，遍历其父结点直到找到根结点root，其路径上的所有结点项构成条件模式基
     */
    public ArrayList<Object> getCPB(TreeNode node, double pre) {
        ArrayList<Object> list = new ArrayList<>();
        ArrayList<String> transaction = new ArrayList<String>();
        TreeNode parent = node;
        TreeNode parent1 = node;
        while (parent.hasParent()) {
            if (parent.getParent().getName().equals("root"))
                break;
            parent = parent.getParent();
            parent.setProb(pre * parent.getProb1());
            transaction.add(parent.getName() + ":" + parent.getProb() + ":" + parent.getProb1());
        }
        list.add(transaction);
        list.add(pre);
        while (parent1.hasParent()) {
            if (parent1.getParent().getName().equals("root"))
                break;
            parent1 = parent1.getParent();
            parent1.setProb(parent1.getProb()/pre);
        }
        return list;
    }

    /*
     * @param1	:无序的、针对当前项的所有事务
     * @param2	:当前项
     * @return	:频繁项集
     * @function:递归调用，根据所有事务和当前项，计算出包含当前项的频繁项集并返回
     */
    public ArrayList<String> UF_growth(ArrayList<ArrayList<String>> transactions, String item, double expsup) {
        ArrayList<String> freqItems = new ArrayList<String>();
        LinkedList<TreeNode> itemSortByFreq = buildHeaderTable(transactions);//频繁项集项头表
        transactions = sortByFreqItem(transactions, itemSortByFreq); //对原始数据集进行排序，剪枝，得到预处理后的数据集
        TreeNode root = buildUFTree(transactions, itemSortByFreq);  //创建FP_tree返回头结点
        //对于当前项和事务，生成其对应的FP树和项头表
        if (itemSortByFreq.size() == 0 || root == null)                //递归终止条件
            return freqItems;

        if (item == null) {
            for (TreeNode node : itemSortByFreq)                        //当item == null时，说明第一次递归，在此处将频繁一项集放入频繁项集列表
                freqItems.add(node.getName() + ":" + node.getProb());
        } else {                                                        //否则针对当前项，遍历项头表，与当前项组合即为一个频繁项集
            for (int i = itemSortByFreq.size() - 1; i >= 0; i--) {    //#注意，该频繁项集并不一定是频繁二项集，因为当前项可能是多个项的组合
                TreeNode node = itemSortByFreq.get(i);                ////为便于存储和使用，项头表实际上也是用的TreeNode类型表示，其NextSameNode指向的才是该项的所有结点的链表
                freqItems.add(node.getName() + "," + item + ":" + node.getProb());
            }
        }
        for (int i = itemSortByFreq.size() - 1; i >= 0; i--) {         //遍历项头表求当前项的条件模式基
            ArrayList<ArrayList<String>> newTransactions = new ArrayList<ArrayList<String>>();
            TreeNode node = itemSortByFreq.get(i);//二维列表保存针对新项集的所有事务
           // TreeNode nodes = itemSortByFreq.get(i);
            String newItem = item == null ? node.getName() : node.getName() + "," + item;
            ////将当前项和遍历取出的项组合作为新的项集
            while (node.hasNextSameNode()) {
                node = node.getNextSameNode();
                double pre = node.getProb();
                for (int j = 0; j < node.getCount(); j++){
                    ArrayList<Object> list = getCPB(node, pre);//////对取出的项，遍历其相同结点的链表，求出各条路径对应的条件模式基并保存
                    newTransactions.add((ArrayList<String>) list.get(0));
                }
            }                                                   //expsup不对，这里应该传一个类似于项头表的东西，把相应期望支持度存里边
            freqItems.addAll(UF_growth(newTransactions, newItem, expsup));    ////递归调用该函数，参数为新的项集对应的所有事务和该项集

        }
        return freqItems;                                            //返回本次调用求出的频繁项集
    }
}
