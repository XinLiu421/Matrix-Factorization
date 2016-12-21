package common.TLFM;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;


public abstract class common_TLFM {
	
	//按比例筛选item
//	public static boolean proportionChoose = false;
//	
//	//按topN选取item,yNUm代表每个得分段选的topN
//	public static boolean topNChoose = false;
//	
//	//按topN选取Item,yNum代表每个user选多少个item
//	public static boolean topN = false;
//	
//	//共用的yNUm
//	public static int yNum = 0;
	
	//存储数据hash
	public static LinkedHashMap<Integer, LinkedHashMap<Integer, Object[]>> trainMap;
	
	//存储数据hash
	public static LinkedHashMap<Integer, LinkedHashMap<Integer, Object[]>> testMap;

	
	//主体迭代循环数组
	public static objectArray[] trainData;
	
	//主体迭代循环数组
	public static objectArray[] testData;
	
	//数组存储筛选出来的item
	public static objectItemArray[] itemData;

	//用户个数
	public static int userNumber=0;
	
	//物品个数
	public static int itemNumber=0;
	
	//最大时间戳
	public static int maxTime=0;
	
	//最小时间戳
	public static int minTime= 2000000000;
	
	//时间映射
	public static int[] timeArray;
	
	//
	public static double flag;
	//平均值
	public static double averageValue;
	
	//static userBias
	public static  double[] bu;
	
	//根据时间变化的bu的参数
	public static double[] alpha_u;
	
	//alpha_u的参数
	public static double G_alpha;
	public static double L_alpha;
	
	//根据时间变化的bu的表示user在某一天的偏好特征
//	public static double[][] Bu_t;
	
	//static itemBias
	public static double[] bi;
	
	//time-change Bin
	public static double[][] Bi_bin;
	
	//该数据集bin的范围
	public static int binRange;
	
	//Bin的个数
	public static int binNum = 30;
	
	//用户因子矩阵
	public static double[][] pu;
	
	//用户因子矩阵的参数
	public static double[][] auk;
	
	//用户因子矩阵随时间的变化
	public static double[][] pu_time;
	
	//物品因子矩阵
	public static double[][] qi;
	
	//表示用户物品相关性的物品因子矩阵
	public static double[][] yi;
	
	//存储每个user的item个数
	public static double[] Ru;
	
	//存储每个user的平均评价时间
	public static double[] Tu;
	
	//计算yi累加和的中间变量
	public static double[][] tempForY;

	
	//因子数
	public static int factorNumber = 20;
	
	//学习率
	public static double G;
	
	//回归系数
	public static double L;
	
	public static double L_pq;
	
	public static double L_bin;
	
	//增加迭代次数
	public int delayCount = 2;
	
	public int min_round = 0;
	
	//初始RMSE
	public double preRmse = 10000.0;
	
	//迭代次数
	public static int iterTimes = 1000;
	
	//每个user对于特定评分的item个数
	public static int[][] itemD;
	
	//存储被选中计算yi的item
	public int[][] chooseItem;
	
	//user对应item的最大个数
	public int maxSize = 0;
	
	/**
	  * 描述: 初始化数据
	  * 变量 @param trainFilePath
	  * 变量 @param testFilePath
	  * 变量 @param separator
	  * 变量 @throws NumberFormatException
	  * 变量 @throws IOException
	  */
	public static void initialize(String trainFilePath,
			String testFilePath, String separator)
			throws NumberFormatException, IOException {
		//构造训练测试集hash
		trainMap = getData(trainFilePath,separator);
		testMap = getData(testFilePath,separator);
		userNumber = userNumber + 1;
		itemNumber = itemNumber + 1;
		//构造主体迭代对象数组
		trainData = to2DArray(trainMap,userNumber);
		trainMap = null;
		testData = to2DArray(testMap,userNumber);
		testMap = null;
		//设置变量size
		initSetSize();
	}
	
	
	/**
	  * 描述: init
	  * 变量 
	  */
	public static  void initSetSize() {

		//static userBias
		bu = new double[userNumber];
		
		//根据时间变化的bu的表示user在某一天的偏好特征
//		Bu_t = new double[userNumber][timeMap.length];
		
		//userBias au
		alpha_u = new double[userNumber];
		
		//item的bia
		bi = new double[itemNumber];
		
		//Bin
		Bi_bin = new double[itemNumber][binNum];
		
		//每个user对应的item个数
		Ru = new double[userNumber];
		
		//每个user的平均评价时间
		Tu = new double[userNumber];
		
		//yi之和的中间变量
		tempForY = new double[userNumber][factorNumber];

		//user因子矩阵
		pu = init(userNumber,factorNumber);
		
		//用户因子矩阵的参数
		auk = new double[userNumber][factorNumber];
		
		//用户因子矩阵随时间的变化
		pu_time = new double[userNumber][factorNumber];
		
		//item因子矩阵
		qi = init(itemNumber, factorNumber);
		
		//item隐特征矩阵
		yi = init(itemNumber, factorNumber);
				
		//计算Ru和均值
		averageValue = compute(trainData);
		
//		//按比例筛选item
//		if(proportionChoose == true){
//			pItem();
//			System.out.println("当前筛选item的模式为:");
//			System.out.println("按对应得分的item个数占该user总item的比例作概率筛选item.");
//		}
//		
//		//topN按分数段筛选item
//		if(topNChoose == true){
//			tItem();
//			System.out.println("当前筛选item的模式为:");
//			System.out.println("选" + yNum +"个当前user对应分数的item.");
//		}
//		
//		//topN选取item
//		if(topN == true){
//			rItem();
//			System.out.println("选" + yNum +"个当前user的item.");
//		}
//		itemData = toArray(trainMap,userNumber);
		System.out.println("初始化完毕,max user id is:" + userNumber + ",max item id is:" + itemNumber + ",and the average score is:" + averageValue);
	}
	
	/**
	  * 描述: 构造文件HashMap
	  * 变量 @param path 文件路径
	 * @param userNumber 
	  */
	public static  LinkedHashMap<Integer, LinkedHashMap<Integer, Object[]>> getData(String path,String separator){
		//读文件
		File train = new File(path);
		BufferedReader reader = null;
		HashSet<Integer> timeList = null;
		LinkedHashMap<Integer, LinkedHashMap<Integer, Object[]>> userMap = new LinkedHashMap<>();
		try{
			reader = new BufferedReader(new FileReader(train));
			String tempString = null;
			timeList = new HashSet<Integer>();
//			int count = 0;
			while((tempString = reader.readLine())!=null){
//				count++;
//				System.out.println(count);
				String[] content = tempString.split(separator);
				int user = Integer.parseInt(content[0])-1;
				int item = Integer.parseInt(content[1])-1;
				Object[] temp = new Object[2];
				//存储评分
				temp[0] = Double.parseDouble(content[2]);
				//存储时间
				temp[1] = Integer.parseInt(content[3])/60/60/24;
				//映射时间
				timeList.add((int)temp[1]);
//				System.out.println("test" + (int)temp[1]);
			        LinkedHashMap<Integer, Object[]> itemMap = userMap.get(user);
			        if(itemMap == null) {
			            itemMap = new LinkedHashMap<>();
			            itemMap.put(item, temp);
			            userMap.put(user, itemMap);
			        } else {
			            itemMap.put(item, temp);
			        }
					// 记录下最大的itemid和userid
			        userNumber = (userNumber > user) ? userNumber : user;
			        itemNumber = (itemNumber > item) ? itemNumber : item;
//			        System.out.println("user :" +user +", item: " + item + ", time:" + temp[1]);
			        //记录下最大时间和最小时间
			        maxTime = (maxTime > (int)temp[1]) ? maxTime : (int)temp[1];
			        minTime = (minTime > (int)temp[1]) ? (int)temp[1] : minTime;
			}
//	        System.out.println("最大时间:" + maxTime);
		}catch(IOException e){
			e.printStackTrace();
		}
		finally{
			if(reader!=null){
				try{
					reader.close();
				}catch(IOException e){
					
				}
			}
		}
		List<Integer> list = new ArrayList<Integer>();
		for(Integer value : timeList){
			list.add(value);
		}
		Collections.sort(list);
		//存储时间对应的映射下标
//		int s = (int)minTime/60/06/24;
//		System.out.println(s);
		timeArray = new int[list.size()];
		for(int i=0;i<list.size();i++){
			timeArray[i] = list.get(i);
//			System.out.println("第" + i + "个时间" + ",为:" + timeArray[i]);
		}
//		System.out.println(timeMap.length);
		return  userMap;
	}
	
	/**
	  * 描述: 构造对象数组
	  * 变量 @param m
	  * 变量 @param num
	  * 变量 @return
	  */
	public static objectArray[] to2DArray(LinkedHashMap<Integer, LinkedHashMap<Integer, Object[]>> trainMap2,int num){
		int size = num;
		objectArray[] users = new objectArray[size];
		for (Entry<Integer, LinkedHashMap<Integer, Object[]>> e: trainMap2.entrySet()) {
			users[e.getKey()] = new objectArray(e.getValue(),timeArray);
//			System.out.println("当前user:" + e.getKey());
		}
		for(int i=0;i<num;i++){
			if(users[i] == null){
				users[i] = new objectArray(null,null);
			}
		}
		return users;
	}
	
	/**
	  * 描述: 为筛选item构造对象数组
	  * 变量 @param m
	  * 变量 @param num
	  * 变量 @return
	  */
	public static  objectItemArray[] toArray(LinkedHashMap<Integer, LinkedHashMap<Integer, double[]>> trainMap,int num){
		int size = num;
		objectItemArray[] users = new objectItemArray[size];
		for (Entry<Integer, LinkedHashMap<Integer, double[]>> e: trainMap.entrySet()) {
			users[e.getKey()] = new objectItemArray(e.getValue());
		}
		for(int i=0;i<num;i++){
			if(users[i] == null){
				users[i] = new objectItemArray(null);
			}
		}
		return users;
	}
	
	/**
	  * 描述: 初始化参数
	  * 变量 @param num
	  * 变量 @param factorNumber
	  * 变量 @return
	  */
	public static  double[][] init(int num,int factorNumber){
		double[][] d = new double[num][factorNumber];
		double temp = Math.sqrt(factorNumber);
		for(int i = 0; i < num; i++){
			for(int j = 0; j<factorNumber; j++){
				d[i][j] = 0.1* (float)Math.random()/temp;
			}
		}
		return d;		
	}
	
	/**
	  * 描述: 计算user对应item个数以及评分均值
	  * 变量 @param tMap
	  * 变量 @return
	  */
	public static  double compute(objectArray[] users){
		double result = 0.0;
		int count = 0;
		int length = userNumber;
//		itemD = new int[userNumber][6];
		for (int i=0; i<length; i++) {
			objectArray items = users[i];
			double rTime = 0;
			//记录时间个数
			int r = 0;
//			System.out.println("当前user:" + i);
			int item_length = items.length;
			for(int j=0; j<item_length; j++){
//				System.out.println("当前item:" + j + ",当前time" + items.itemTime[j]);
				result += items.itemScore[j];
				rTime += items.itemTime[j];
				count++;
				Ru[i]++;
				r++;
			}
			if(r!=0){
			Tu[i] = rTime/r;
//			System.out.println("当前time均值:" + rTime/r);
			}
			else{
				Tu[i] = 0;
			}
		}
		return result/count;		
	}
	
	/**
	  * 描述: 某个user的所有item的yi累加
	  * 变量 @param user
	  */
	public void getTempY(int user,objectArray iData){
		if(iData.length!=0){
			for(int i=0; i<iData.length; ++i) {
				int item = iData.itemId[i];
					for(int k = 0;k<factorNumber;k++){
						tempForY[user][k] += yi[item][k]*Math.pow(Ru[user], -1.0/2);
					}
			}
		}
	}
}
