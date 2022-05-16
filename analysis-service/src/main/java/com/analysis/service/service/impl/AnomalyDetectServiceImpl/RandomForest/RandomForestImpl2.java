//package com.analysis.service.service.impl.AnomalyDetectServiceImpl.RandomForest;
//
///**
// * @description:
// * @author: lingwanxian
// * @date: 2022/5/16 21:22
// */
//public class RandomForestImpl {
//    List<Sample> mSamples;
//    List<Cart> mCarts;
//    double mFeatureRate;
//    int mMaxDepth;
//    int mMinLeaf;
//    Random mRandom;
//    /**
//     * 加载数据  回归树
//     * @param path
//     * @param regex
//     * @throws Exception
//     */
//    public void loadData(String path,String regex) throws Exception{
//        mSamples = new ArrayList<Sample>();
//        BufferedReader reader = new BufferedReader(new FileReader(path));
//        String line = null;
//        String splits[] = null;
//        Sample sample = null;
//        while(null != (line=reader.readLine())){
//            splits = line.split(regex);
//            sample = new Sample();
//            sample.label = Double.valueOf(splits[0]);
//            sample.feature = new ArrayList<Double>(splits.length-1);
//            for(int i=0;i<splits.length-1;i++){
//                sample.feature.add(new Double(splits[i+1]));
//            }
//            mSamples.add(sample);
//        }
//        reader.close();
//    }
//    public void train(int iters){
//        mCarts = new ArrayList<Cart>(iters);
//        Cart cart = null;
//        for(int iter=0;iter<iters;iter++){
//            cart = new Cart();
//            cart.mFeatureRate = mFeatureRate;
//            cart.mMaxDepth = mMaxDepth;
//            cart.mMinLeaf = mMinLeaf;
//            cart.mRandom = mRandom;
//            List<Sample> s = new ArrayList<Sample>(mSamples.size());
//            for(int i=0;i<mSamples.size();i++){
//                s.add(mSamples.get(cart.mRandom.nextInt(mSamples.size())));
//            }
//            cart.setData(s);
//            cart.train();
//            mCarts.add(cart);
//            System.out.println("iter: "+iter);
//            s = null;
//        }
//    }
//    /**
//     * 回归问题简单平均法 分类问题多数投票法
//     * @param sample
//     * @return
//     */
//    public double classify(Sample sample){
//        double val = 0;
//        for(Cart cart:mCarts){
//            val += cart.classify(sample);
//        }
//        return val/mCarts.size();
//    }
//}
