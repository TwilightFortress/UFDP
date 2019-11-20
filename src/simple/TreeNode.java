package simple;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * @author yuyang
 * @create 2019-08-06 16:11
 */
public class TreeNode {
    private String name;                                            //项名称
    private int count;//支持度
    private double prob;//存在概率
    private double prob1;
    private double expsup1;//1-项集用
    private double expsup;
    private TreeNode parent;                                        //父结点
    private ArrayList<TreeNode> childs;                                //子结点（列表）
    private TreeNode nextSameNode;                                    //指向下一个相同结点

    public TreeNode() {
        super();
    }

    public TreeNode(String name, int count) {
        super();
        this.name = name;
        this.count = count;
        this.prob = 0;
        this.prob1 = 0;
        this.expsup1 = 0;
        this.expsup = 0;
        this.childs = new ArrayList<TreeNode>();
        this.parent = null;
        this.nextSameNode = null;
    }

    public TreeNode(String name, int count, double expsup1) {
        super();
        this.name = name;
        this.count = count;
        this.prob = expsup1;
        this.prob1 = expsup1;
        this.expsup = 0;
        this.expsup1 = expsup1;
        this.childs = new ArrayList<TreeNode>();
        this.parent = null;
        this.nextSameNode = null;
    }
    public TreeNode(String name, int count, double prob, double expsup1) {
        super();
        this.name = name;
        this.count = count;
        this.prob = prob;
        this.prob1 = 0;
        this.expsup = 0;
        this.expsup1 = expsup1;
        this.childs = new ArrayList<TreeNode>();
        this.parent = null;
        this.nextSameNode = null;
    }
    public TreeNode(String name, int count,TreeNode parent) {
        super();
        this.name = name;
        this.count = count;
        this.prob1 = 0;
        this.prob = 0;
        this.expsup = 0;
        this.expsup1 = 0;
        this.childs = new ArrayList<TreeNode>();
        this.parent = parent;
        this.nextSameNode = null;
    }


    public TreeNode(String name, int count,double prob, double prob1,TreeNode parent) {
        super();
        this.name = name;
        this.count = count;
        this.prob = prob;
        this.prob1 = prob1;
        this.expsup = 0;
        this.expsup1 = 0;
        this.childs = new ArrayList<TreeNode>();
        this.parent = parent;
        this.nextSameNode = null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public double getProb(){ return prob;}

    public void setProb(double prob) {this.prob = prob;}

    public double getProb1(){ return prob1;}

    public void setProb1(double prob1) {this.prob1 = prob1;}

    public double getExpsup1(){ return expsup1;}

    public void setExpsup1(double expsup1) {this.prob = expsup1;}

    public double getExpsup(){ return expsup;}

    public void setExpsup(double expsup) {this.prob = expsup;}

    public TreeNode getParent() {
        return this.parent;
    }

    public boolean hasParent() {
        return this.parent == null ? false : true;
    }

    public void setParent(TreeNode parent) {
        this.parent = parent;
    }

    public void addChild(TreeNode child) {
        this.childs.add(child);
    }

    public void addChilds(ArrayList<TreeNode> childs) {
        this.childs.addAll(childs);
    }

    public void delChild(TreeNode child) {
        this.childs.remove(child);
    }

    public TreeNode getNextSameNode() {
        return this.nextSameNode;
    }

    public boolean hasNextSameNode() {
        return this.nextSameNode == null ? false : true;
    }

    public void setNextSameNode(TreeNode nextSameNode) {
        this.nextSameNode = nextSameNode;
    }

    public ArrayList<TreeNode> getChilds() {
        return childs;
    }

    public void inc(int i) {
        this.count += i;
    }
    public void inc(int i, double p){
        this.prob += p;
        this.count += i;
    }

    @Override
    public String toString() {
        return this.getName() + ":" + this.getProb();
    }
}

/*
 * @function：为实现使用Collections.sort方法对泛型为TreeNode的List进行排序，需要实现Comparator接口
 */
class TreeNodeSort implements Comparator<TreeNode> {
    @Override
    public int compare(TreeNode o1, TreeNode o2) {
        int result = Double.compare(o2.getProb(), o1.getProb());        //实现降序排列
        //int result = Integer.compare(o1.getCount(), o2.getCount());	//实现升序排列
        if (result != 0)
            return result;
        return o1.getName().compareTo(o2.getName());
    }
}
