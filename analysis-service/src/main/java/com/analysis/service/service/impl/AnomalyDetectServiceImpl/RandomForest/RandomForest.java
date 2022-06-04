package com.analysis.service.service.impl.AnomalyDetectServiceImpl.RandomForest;

import java.util.*;

public class RandomForest {


    static class Util {
        public static void CHECK(boolean condition, String message) {
            if (!condition) {
                throw new RuntimeException(message);
            }
        }
    }

    public double[][] instances;
    public int[] targets;
    public int numOfTrees;
    public int numOfFeatures;
    public int maxDepth;
    public int featureSize;
    public TreeNode[] trees;
    public ArrayList<TreeInfo> treeinfo;
    public static Random rand = new Random();

    /**
     * Train the RF model
     *
     * @param instances
     * @param targets
     * @param numOfTrees
     * @param numOfFeatures this could be -1, if so, the default value will be
     *                      len(features)^0.5
     * @param maxDepth      this could be -1, if so, any leaf node will have only 1
     *                      instance.
     */
    public void train(double[][] instances, int[] targets, int numOfTrees, int numOfFeatures,
                      int maxDepth, int treeSize) {
        Util.CHECK(instances.length == targets.length, "the length of instances does not match the length of targets");
        Util.CHECK(numOfTrees > 0, "the num of trees must more than zero");
        this.instances = instances;
        this.targets = targets;
        this.numOfTrees = numOfTrees;
        this.numOfFeatures = numOfFeatures;
        this.maxDepth = maxDepth;
        this.featureSize = instances[0].length;
        this.trees = new TreeNode[numOfTrees];
        this.treeinfo = new ArrayList<>(numOfTrees);
        for (int i = 0; i < trees.length; i++) {
            System.out.println("building the tree:" + i);
            treeinfo.add(new TreeInfo());
            treeinfo.get(i).setTreeID(i);
            trees[i] = buildTree(getRandomInstances(treeSize), 1, treeinfo.get(i), i);
            treeinfo.get(i).setNumOfNode(treeinfo.get(i).nodeinfo.size());
            /*
            for(int j=0;j<treeinfo[i].nodeinfo.size();j++){
            	System.out.println("treeID: "+treeinfo[i].TreeID+" nodeID: "+j);
            	System.out.println(treeinfo[i].nodeinfo.get(j).nodeID);
            	System.out.println(treeinfo[i].nodeinfo.get(j).featureIndex_);
            	System.out.println(treeinfo[i].nodeinfo.get(j).nodeDepth);
            	System.out.println(treeinfo[i].nodeinfo.get(j).left_NodeID);
            	System.out.println(treeinfo[i].nodeinfo.get(j).right_NodeID);
            }
                */
        }

    }

    // 随机获取所有实例的子集
    List<Integer> getRandomInstances(int numOfInstances) {
        List<Integer> ret = new ArrayList<Integer>(numOfInstances);
        //System.out.print("this is ret length: "+ret.size()+"\n");
        while (ret.size() < numOfInstances) {
            ret.add(rand.nextInt(instances.length));
        }
        return ret;
    }

    // 获取具有索引的所有样本的大多数类
    private int getMajorClass(List<Integer> indices) {
        Map<Integer, Integer> mii = new HashMap<Integer, Integer>();
        int best = -1;
        int ret = -1;
        for (int index : indices) {
            Integer v = mii.get(targets[index]);
            if (v == null) {
                v = 0;
            }
            mii.put(targets[index], v + 1);
            if (v + 1 > best) {
                best = v + 1;
                ret = targets[index];
            }
        }
        return ret;
    }

    // 所有具有指数的样本是否都具有相同的类别？
    private boolean haveSameClass(List<Integer> indices) {
        for (int i = 1; i < indices.size(); i++) {
            if (targets[indices.get(i)] != targets[indices.get(0)]) {
                return false;
            }
        }
        return true;
    }

    // 随机获取特征索引列表
    private List<Integer> getRandomFeatures() {

        Set<Integer> set = new HashSet<Integer>();
        //int featureSize = instances[0].length;
        while (set.size() < numOfFeatures) {
            int temp = rand.nextInt(featureSize);
            if (!set.contains(temp)) {
                set.add(temp);
            }

        }

        List<Integer> ret = new ArrayList<Integer>();
        ret.addAll(set);
        return ret;
    }

    boolean contain(int[] group, int ID) {
        boolean contain_result = false;
        for (int i = 0; i < group.length; i++) {
            if (group[i] == ID) {
                contain_result = true;
                break;
            }
        }
        return contain_result;
    }

    private List<Integer> getRandomFeatures(int[] badFeatureID) {

        Set<Integer> set = new HashSet<Integer>();
        //int featureSize = instances[0].length;
        while (set.size() < numOfFeatures) {
            int temp = rand.nextInt(featureSize);
            if (!set.contains(temp) && !contain(badFeatureID, temp)) {
                set.add(temp);
            }

        }

        List<Integer> ret = new ArrayList<Integer>();
        ret.addAll(set);
        return ret;
    }

    // 得到一些样本的熵
    private double getEntropy(List<Integer> indices, int from, int to) {
        Util.CHECK(to <= indices.size(), "index of begin can not bigger than index of end");
        Map<Integer, Integer> mii = new HashMap<Integer, Integer>();
        for (int i = from; i < to; i++) {
            Integer v = mii.get(targets[indices.get(i)]);
            if (v == null) {
                v = 0;
            }
            mii.put(targets[indices.get(i)], v + 1);
        }
        double ret = 0;
        for (Integer key : mii.keySet()) {
            int v = mii.get(key);
            ret += Math.log((to - from) * 1.0 / v);
        }
        return ret;
    }

    private TreeNode buildTree(List<Integer> indices, int curDepth, TreeInfo treeinfo, long ID) {
        long nodeID = ID;
        System.out.println("building tree, depth:" + curDepth);
        if (curDepth == 1) {
            treeinfo.RootNodeID = nodeID;
        }
        if (maxDepth == curDepth) {
            NodeInfo nodeinfo = new NodeInfo(nodeID, -1, -1, getMajorClass(indices), -1, true, curDepth);
            treeinfo.nodeinfo.add(nodeinfo);
            return new TreeNode(-1, -1, getMajorClass(indices), null, null, true, curDepth, nodeID, -1, -1, -1);
        }
        if (haveSameClass(indices)) {
            NodeInfo nodeinfo = new NodeInfo(nodeID, -1, -1, getMajorClass(indices), -1, true, curDepth);
            treeinfo.nodeinfo.add(nodeinfo);
            return new TreeNode(-1, -1, targets[indices.get(0)], null, null, true, curDepth, nodeID, -1, -1, -1);
        }
        // 随机获取特征索引列表
        List<Integer> featureInices = getRandomFeatures();
        double bestEntropy = Double.MAX_VALUE;
        int bestFeatureIndex = -1;
        double splitValue = -1;
        List<Integer> leftIndices = null;
        List<Integer> rightIndices = null;
        int splitIndex = -1;

        for (final int featureIndex : featureInices) {
            Collections.sort(indices, new Comparator<Integer>() {
                @Override
                public int compare(Integer o1, Integer o2) {
                    if (instances[o1][featureIndex] < instances[o2][featureIndex]) {
                        return -1;
                    } else if (instances[o1][featureIndex] == instances[o2][featureIndex]) {
                        return o1 - o2;
                    } else {
                        return 1;
                    }
                }
            });
            int bestIndex = -1;
            for (int i = 0; i < indices.size() - 1; i++) {
                if (instances[indices.get(i)][featureIndex] == instances[indices.get(i + 1)][featureIndex]) {
                    continue;
                }
                double entropy = 1.0 * (i + 1 - 0) / indices.size() * getEntropy(indices, 0, i + 1)
                        + 1.0 * (indices.size() - (i + 1)) / indices.size()
                        * getEntropy(indices, i + 1, indices.size());
                if (entropy < bestEntropy) {
                    bestEntropy = entropy;
                    bestFeatureIndex = featureIndex;
                    bestIndex = i;
                    splitValue = instances[indices.get(i)][featureIndex];
                }
            }
            if (bestIndex >= 0) {
                splitIndex = bestIndex;
                leftIndices = new ArrayList<Integer>();
                rightIndices = new ArrayList<Integer>();
                leftIndices.addAll(indices.subList(0, bestIndex + 1));
                rightIndices.addAll(indices.subList(bestIndex + 1, indices.size()));
            }
        }
        if (bestFeatureIndex >= 0) {

            long left = get_left_nodeID(curDepth, nodeID);
            long right = get_right_nodeID(curDepth, nodeID);

            NodeInfo nodeinfo = new NodeInfo(nodeID, bestFeatureIndex, splitValue, -1, splitIndex, false, left, right, curDepth);
            treeinfo.nodeinfo.add(nodeinfo);
            return new TreeNode(bestFeatureIndex, splitValue, -1,
                    buildTree(leftIndices, curDepth + 1, treeinfo, left),
                    buildTree(rightIndices, curDepth + 1, treeinfo, right),
                    false, curDepth, nodeID, splitIndex, left, right);
        } else {
            // All instances have the same features.
            NodeInfo nodeinfo = new NodeInfo(nodeID, -1, -1, getMajorClass(indices), splitIndex, true, -1, -1, curDepth);
            treeinfo.nodeinfo.add(nodeinfo);
            return new TreeNode(-1, -1, getMajorClass(indices), null, null, true, curDepth, nodeID, -1, -1, -1);
        }
    }


    int predicateByOneTree(TreeNode node, double[] instance) {
        if (node.isLeafNode_) {
            return node.target_;
        }
        if (instance[node.featureIndex_] <= node.value_) {
            return predicateByOneTree(node.left_, instance);
        } else {
            return predicateByOneTree(node.right_, instance);
        }
    }

    int predicateByOneTreeBySavedData(TreeInfo treeinfo, NodeInfo nodeinfo, double[] instance) {

        if (nodeinfo.isLeafNode_) {
            return nodeinfo.target_;
        }
        if (instance[nodeinfo.featureIndex_] <= nodeinfo.value_) {
            return predicateByOneTreeBySavedData(treeinfo, treeinfo.nodeinfo.get(treeinfo.findIndexByID(nodeinfo.left_NodeID)), instance);
        } else {
            return predicateByOneTreeBySavedData(treeinfo, treeinfo.nodeinfo.get(treeinfo.findIndexByID(nodeinfo.right_NodeID)), instance);
        }
    }


    // 预测一个实例
    public int predicate(double[] instance) {
        Map<Integer, Integer> mii = new HashMap<Integer, Integer>();
        int bestTarget = -1;
        int bestCount = -1;
        for (TreeNode root : trees) {
            int target = predicateByOneTree(root, instance);
            Integer v = mii.get(target);
            if (v == null) {
                v = 0;
            }
            mii.put(target, v + 1);
            if (v + 1 > bestCount) {
                bestCount = v + 1;
                bestTarget = target;
            }
        }
        return bestTarget;
    }

    public int predicateBySavedData(double[] instance) {
        Map<Integer, Integer> mii = new HashMap<Integer, Integer>();
        int bestTarget = -1;
        int bestCount = -1;
        for (TreeInfo root : treeinfo) {
            int target = predicateByOneTreeBySavedData(root, root.nodeinfo.get(0), instance);
            Integer v = mii.get(target);
            if (v == null) {
                v = 0;
            }
            mii.put(target, v + 1);
            if (v + 1 > bestCount) {
                bestCount = v + 1;
                bestTarget = target;
            }
        }
        return bestTarget;
    }

    //决策树节点
    static class TreeNode {
        public int featureIndex_;
        public double value_;
        public int target_;
        public TreeNode left_;
        public TreeNode right_;
        public boolean isLeafNode_;
        //public NodeInfo nodeinfo;
        public long nodeID;
        //public int father_NodeID;
        //public int nodeType;
        public int split_Indice_ID;
        public long left_NodeID;
        public long right_NodeID;
        public int nodeDepth;

        public TreeNode(int featureIndex, double value_, int target_, TreeNode left_,
                        TreeNode right_, boolean isLeafNode, int curDepth, long nodeID, int split_Indice_ID, long left_NodeID, long right_NodeID) {
            this.featureIndex_ = featureIndex;
            this.value_ = value_;
            this.target_ = target_;
            this.left_ = left_;
            this.right_ = right_;
            this.isLeafNode_ = isLeafNode;
            this.nodeDepth = curDepth;
            this.nodeID = nodeID;
            //this.father_NodeID=father_NodeID;
            this.split_Indice_ID = split_Indice_ID;
            this.left_NodeID = left_NodeID;
            this.right_NodeID = right_NodeID;

        }
    }

    public long get_left_nodeID(int curDepth, long nodeID) {
        long left_nodeID = numOfTrees * sum8421(curDepth) + (nodeID - numOfTrees * sum8421(curDepth - 1)) * 2 + 1;
        return left_nodeID;
    }

    public long get_right_nodeID(int curDepth, long nodeID) {
        long right_nodeID = numOfTrees * sum8421(curDepth) + (nodeID - numOfTrees * sum8421(curDepth - 1)) * 2 + 2;
        return right_nodeID;
    }

    class TreeInfo {
        int TreeID;
        long RootNodeID;
        List<NodeInfo> nodeinfo = new ArrayList<NodeInfo>();
        long NumOfNode;
        int tree_correct = 0;
        int tree_wrong = 0;
        double correct_rate = 0;

        public void setTreeID(int TreeID) {
            this.TreeID = TreeID;
        }

        public void setRootNodeID(int RootNodeID) {
            this.RootNodeID = RootNodeID;
        }

        public void setNumOfNode(int NumOfNode) {
            this.NumOfNode = NumOfNode;
        }

        int findIndexByID(long nodeID) {
            int result = -1;
            for (int i = 0; i < this.nodeinfo.size(); i++) {
                if (this.nodeinfo.get(i).nodeID == nodeID) {
                    result = i;
                }
            }
            return result;
        }
    }

    public class NodeInfo {
        public long nodeID;
        public long father_NodeID;
        public int nodeType;
        public int featureIndex_;
        public double value_;
        public int target_;
        public int split_Indice_ID;
        public boolean isLeafNode_;
        public long left_NodeID;
        public long right_NodeID;
        public int nodeDepth;
//        public double weight = 0.5;
//        public int correct = 0;
//        public int wrong = 0;

        public NodeInfo(long nodeID, int featureIndex, double value_, int target_, int split_Indice_ID,
                        boolean isLeafNode, int nodeDepth) {
            this.nodeID = nodeID;
            this.featureIndex_ = featureIndex;
            this.value_ = value_;
            this.target_ = target_;
            this.split_Indice_ID = split_Indice_ID;
            this.isLeafNode_ = isLeafNode;
            this.nodeDepth = nodeDepth;
        }

        public NodeInfo(long nodeID, int featureIndex, double value_, int target_, int split_Indice_ID,
                        boolean isLeafNode, long left_NodeID, long right_NodeID, int nodeDepth) {
            this.nodeID = nodeID;
            this.featureIndex_ = featureIndex;
            this.value_ = value_;
            this.target_ = target_;
            this.split_Indice_ID = split_Indice_ID;
            this.isLeafNode_ = isLeafNode;
            this.left_NodeID = left_NodeID;
            this.right_NodeID = right_NodeID;
            this.nodeDepth = nodeDepth;
        }
    }

    public static long sum8421(long curDepth) {
        long result = 0;
        while (curDepth > 0) {
            result = result + (long) Math.pow(2, (--curDepth));
        }
        return result;
    }

    public static void printTree(TreeNode node, String indedent) {
        if (node.isLeafNode_) {
            System.out.println(indedent + "target:" + node.target_);
        } else {
            System.out.println(indedent + "feature index:" + node.featureIndex_ + ", split value:"
                    + node.value_);
            printTree(node.left_, indedent + "    ");
            printTree(node.right_, indedent + "    ");
        }
    }

}
