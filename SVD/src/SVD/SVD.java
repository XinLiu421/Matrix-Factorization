package SVD;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import Plus.Common.GetFile;
import Plus.Common.InitParameter;
import Plus.Common.ItemsArr;
import Plus.Common.To2DArray;




public class SVD {
	//文件路径
	private String numPath;
	private String trainDataPath;
	private String testDataPath;
	//文件数据
	ItemsArr[] trainData;
	ItemsArr[] testData;
	LinkedHashMap<Integer, LinkedHashMap<Integer, Double>> trainMap;
	LinkedHashMap<Integer, LinkedHashMap<Integer, Double>> testMap;
	LinkedHashMap<Integer, LinkedHashMap<Integer, Double>> cTrainMap = new LinkedHashMap<>();
	//用户个数
	private int userNumber;
	//物品个数
	private int itemNumber;
	//平均值
	private double averageValue;
	private double[] bu;
	private double[] bi;
	//pu pi yi Ru
	private double[][] pu;
	private double[][] qi;
	private double[][] yi;
	private double[] Ru;
	private double[][] tempForY;
	//因子数
	private int factorNumber;
	private double learningRate;
	private double regularization;
	private double regularization1;
	private int delayCount = 5;
	private int min_round = 0;
	//初始RMSE
	double preRmse = 10000.0;
	//迭代次数
	int iterTimes = 1000;
//	//yi个数
//	private int yNum =400;
	//每个user根据评分的item个数
	public int[][] itemD;
	//存储被选中计算yi的item
	public int[][] chooseItem;
	//user对应item的最大个数
	public int maxSize = 0;
	
	/**
	  * 描述: 构造函数
	  * 变量 @param num 获取user item个数路径
	  * 变量 @param trainData 训练集路径
	  * 变量 @param testData  测试集路径
	  */
	public SVD(String num,String trainData,String testData){
		//路径
		numPath = num;
		trainDataPath = trainData;
		testDataPath = testData;
		//因子数
		factorNumber = 50;
		//参数γ
		learningRate = 0.007;
		//参数λ5
		regularization = 0.01;
		//参数λ6
		regularization1 = 0.015;
	}
	/**
	  * 描述: 构造HasnMap,初始化矩阵
	  * 变量 
	  */
	public void prepare(){
		
		GetFile data = new GetFile();
		//获取user item 数量
		int[] num = data.getUserAndItemNum(numPath);
		userNumber = 6040;//num[0];
		itemNumber = 3706;//num[1];
		
		//获取训练测试集 
		trainMap = data.getData(trainDataPath,userNumber);
		testMap = data.getData(testDataPath,userNumber);
		trainData = To2DArray.to2DArray(trainMap,userNumber,2);
		testData = To2DArray.to2DArray(testMap,userNumber,2);
		//初始化bu等参数
		InitParameter initP = new InitParameter();
		//user的bias
		bu = new double[userNumber];
		//item的bia
		bi = new double[itemNumber];
		//每个user对应的item个数
		Ru = new double[userNumber];
		//user因子矩阵
		pu = initP.init(userNumber,factorNumber);
		//item因子矩阵
		qi = initP.init(itemNumber, factorNumber);
		//item隐特征矩阵
		yi = initP.init(itemNumber, factorNumber);	
		//计算Ru和均值
		averageValue = compute(trainData);
		
		//确定本次实验要计算yi的item
//		cItem();
		System.out.println("初始化完毕,user number is:" + userNumber + ",item number is:" + itemNumber + ",and the average score is:" + averageValue);
	}

	/**
	  * 描述: 计算user对应item个数以及评分均值
	  * 变量 @param tMap
	  * 变量 @return
	  */
	public double compute(ItemsArr[] users){
		double result = 0.0;
		int count = 0;
		int length = userNumber;
		itemD = new int[userNumber][6];
		for (int i=0; i<length; i++) {
			ItemsArr items = users[i];
			int item_length = items.length;
			for(int j=0; j<item_length; j++){
				int temp = (int)items.itemScore[j];
				result += items.itemScore[j];
				count++;
				Ru[i]++;
				switch(temp){
					case 0:itemD[i][temp]++;
					break;
					case 1:itemD[i][temp]++;
					break;
					case 2:itemD[i][temp]++;
					break;
					case 3:itemD[i][temp]++;
					break;
					case 4:itemD[i][temp]++;
					break;
					case 5:itemD[i][temp]++;
					break;						
				}
			}	
		}
		return result/count;		
	}
	
	/**
	  * 描述: 概率筛选要计算累加和的item
	  * 变量 
	  */
	public void cItem(){
		for(Entry<Integer, LinkedHashMap<Integer,Double>> entryU : trainMap.entrySet()){
			int user = entryU.getKey();
//			System.out.println("当前user为:" + user);
			Random r = new Random();
			//当前user对应的item集合
			LinkedHashMap<Integer,Double> choose = entryU.getValue();
//			System.out.println("删除之前的集合大小:" + choose.size());
			//保留要删除的item
			ArrayList<Integer> delList = new ArrayList<Integer>();
			for(Entry<Integer, Double> e : choose.entrySet()){
				//得分
				double temp = e.getValue();
				//概率
				double p = itemD[user][(int)temp]/Ru[user];
//				int num = (int)p*itemD[user][(int)temp];
				//产生的随机数的范围是当前user的对应评分的item的个数
//				System.out.println(itemD[user][(int)temp]);
//				int n= r.nextInt(itemD[user][(int)temp]);
				double d=r.nextDouble();

//				System.out.println(n);
				//概率选中该item
				if(d > p){
					delList.add(e.getKey());
				}
			}
//			System.out.println("要删除的集合大小:" + delList.size());
			for(int s=0;s<delList.size();s++){
				trainMap.get(user).remove(delList.get(s));
			}
//			System.out.println("删除后的集合大小:" + trainMap.get(user).size());
		}
	}
	
	/**
	  * 描述: 某个user的所有item的yi累加
	  * 变量 @param user
	  */
	public void getTempY(int user){			
		//计算对应于user的所有item的yi的累加	
		
//		ItemsArr items = trainData[user];
//		int length = 0;
		//取出当前user筛选的item
		//筛选出的item个数
//		int length = chooseItem[user].length;
////		取前yNum个yi进行累加
////		if(items.length < yNum){
////			length =items.length;
////		}
////		else{
////			length = yNum;
////		}
//		for(int i=0; i<length; ++i) {
////			int item = items.itemId[i];
//			int item = chooseItem[user][i];
//			if(item!=-1){
//				for(int k = 0;k<factorNumber;k++){
//					tempForY[user][k] += yi[item][k]*Math.pow(Ru[i], -1.0/2);
//				}
//			}
//		}
		LinkedHashMap<Integer,Double> a = trainMap.get(user);
		for(Map.Entry<Integer,Double> e : a.entrySet()){
			int item = e.getKey();
			for(int k = 0; k<factorNumber;k++){
				tempForY[user][k] += yi[item][k]*Math.pow(Ru[user], -1.0/2);
			}
		}
	}

	/**
	  * 描述: 训练函数
	  * 变量 
	  */
	public void train(){
		//yi之和的中间变量
		tempForY = new double[userNumber][factorNumber];
		System.out.println("开始训练模型...");		
		//迭代循环
		for(int i=0;i<iterTimes;i++){
			//记录起始时间
			long start = System.currentTimeMillis();
			//user循环
			for (int uid=0; uid<trainData.length; uid++) {
				ItemsArr items = trainData[uid];			
				int user = uid;
				//当前user的所有item的yi累加
//				getTempY(user);
				//item循环
//				int itemLength = 0;
				
				//取前yNum个yi进行更新
//				if(items.length < yNum){
//					itemLength =items.length;
//				}
//				else{
//					itemLength = yNum;
//				}
				for (int iidx=0; iidx<items.length; iidx++) {			
					int item = items.itemId[iidx];
					double rating = items.itemScore[iidx];
					double pscore = predictScore(averageValue,bu[user],bi[item],pu[user],qi[item]);
					double eui = rating - pscore;
					bu[user] += learningRate*(eui - regularization*bu[user]);
					bi[item] += learningRate*(eui - regularization*bi[item]);
					LinkedHashMap<Integer,Double> a = trainMap.get(user);
					for(int f=0;f<factorNumber;f++){
						double temp = eui*Math.pow(Ru[i], -1.0/2)* qi[item][f];
						qi[item][f] += learningRate*((tempForY[user][f]+ pu[user][f])*eui - regularization1*qi[item][f]);
						pu[user][f] += learningRate*(eui*qi[item][f] - regularization1*pu[user][f]);
						//更新当前user对应item的yi,且取前yNUm个yi更新						

//						int[] ids = items.itemId;
//						for(int p_idx=0; p_idx<items.length; p_idx++){							
//							yi[ids[p_idx]][f] += learningRate*(temp - regularization1*yi[ids[p_idx]][f]);
//						}
//						int[] ids = chooseItem[user];
//						for(int p_idx =0;p_idx<ids.length;p_idx++){
//							if(ids[p_idx]!=-1){
//							yi[ids[p_idx]][f] += learningRate*(temp - regularization1*yi[ids[p_idx]][f]);
//							}
//						}
					}
				}
			}
			double curRmse = test(averageValue,bu,bi,pu,qi,tempForY);
			System.out.println(i+1 + "次迭代," + "RMSE是" + curRmse);
			//输出
			try {
				FileOutputStream out = new FileOutputStream("C:\\Users\\liuxin-pc\\Desktop\\out.txt",true);
				OutputStreamWriter osw = new OutputStreamWriter(out, "utf-8");
				BufferedWriter bwr = new BufferedWriter(osw);
				bwr.write(curRmse + "\r\n");
				bwr.flush();
				bwr.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			if(preRmse > curRmse){
				preRmse = curRmse;
				min_round = i;
			}
			else if((i - min_round) >= delayCount){
				break;
			}
			long end = System.currentTimeMillis();		// 记录结束时间
			System.out.println("运行时间为:" + (double)(end-start)/1000 + "秒");	
		}
	}
	//预测值
	public double predictScore(double average, double bu, double bi, double[] pu, double[] qi){
		double pscore = average + bu + bi + innerproduct(pu,qi);
		if(pscore < 1){
			pscore = 1;
		}
		if(pscore > 5){
			pscore = 5;
		}
		return pscore;
	}
	//内积
	public double innerproduct(double[] temp, double[] qi) {
		double result = 0.0;
		for(int i=0;i<temp.length;i++){
			result+= temp[i]*qi[i];
		}
		return result;
	}
	//test
	public double test(double averageValue, double[] bu, double[] bi, double[][] pu, double[][] qi,double[][] tempForY){
		double rmse = 0.0;
		int cnt = 0;
		//测试集
		for (int u_idx=0; u_idx<userNumber; u_idx++) {
			ItemsArr items = testData[u_idx];			
			int user = u_idx;
				for(int i_idx=0; i_idx<items.length; i_idx++){
					cnt += 1;
					int item = items.itemId[i_idx];
					double score = items.itemScore[i_idx];
					double pscore = predictScore(averageValue,bu[user],bi[item],pu[user],qi[item]);
					rmse+=Math.pow(score-pscore, 2);
				}
		}
		return Math.sqrt(rmse/cnt);
	}


	/**
	  * 描述: 主函数
	  */
	public static void main(String[] args) {
		//文件路径
		String num = "C:\\Users\\liuxin-pc\\Desktop\\ML1M_outline.txt";//EachMovie_outline.txt";//
		String trainData = "C:\\Users\\liuxin-pc\\Desktop\\ML1M_train.txt";//EachMovie_train.txt";//
		String testData = "C:\\Users\\liuxin-pc\\Desktop\\ML1M_test.txt";//EachMovie_test.txt";//
		//构造函数
		SVD s = new SVD(num,trainData,testData);
		//准备阶段	
		s.prepare();
		//训练
		long start = System.currentTimeMillis();
		s.train();
		long end = System.currentTimeMillis();
		int time = (int)(end - start);
		if(time/1000 == 0){
			System.out.println("毫秒以下");
		}
		else if(time/1000/60 == 0){
			System.out.println("总运行时间为:" + time/1000 + "秒");
		}
		else if(time/1000/60/60 ==0){
			System.out.println("总运行时间为:" + time/1000/60 + "分");
		}
		else {
			System.out.println("总运行时间为:" + time/1000/60/60 + "小时" + time/1000/60%60 + "分");
		}
		System.out.println("end");
	}
}
