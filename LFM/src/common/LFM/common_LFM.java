package common.LFM;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map.Entry;


public abstract class common_LFM {
	
	//存储数据hash
	public static LinkedHashMap<Integer, LinkedHashMap<Integer, Integer>> trainMap;
	
	//存储数据hash
	public static LinkedHashMap<Integer, LinkedHashMap<Integer, Integer>> testMap;

	
	//主体迭代循环数组
	public static objectArray[] trainData;
	
	//主体迭代循环数组
	public static objectArray[] testData;

	//用户个数
	public static int userNumber=0;
	
	//物品个数
	public static int itemNumber=0;
	
	//平均值
	public static double averageValue;
	
	//userBias
	public static  double[] bu;
	
	//itemBias
	public static double[] bi;
	
	//用户因子矩阵
	public static double[][] pu;
	
	//物品因子矩阵
	public static double[][] qi;
	
	//表示用户物品相关性的物品因子矩阵
	public static double[][] yi;
	
	//存储每个user的item个数
	public static double[] Ru;
	
	//计算yi累加和的中间变量
	public static double[][] tempForY;

	
	//因子数
	public static int factorNumber = 20;
	
	//学习率
	public static double learningRate;
	
	//回归系数
	public static double regularization;
	
	public static double regularization1;
	
	//增加迭代次数
	public int delayCount = 1;
	
	public int min_round = 0;
	
	//初始RMSE
	public double preRmse = 10000.0;
	
	//迭代次数
	public static int iterTimes = 1000;
	
	//每个user对于特定评分的item个数
	public static int[][] itemD;
	
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
		testData = to2DArray(testMap,userNumber);
		//设置变量size
		initSetSize();
	}
	
	/**
	  * 描述: 构造文件HashMap
	  * 变量 @param path 文件路径
	 * @param userNumber 
	  */
	public static  LinkedHashMap<Integer, LinkedHashMap<Integer, Integer>> getData(String path,String separator){
		//读文件
		File train = new File(path);
		BufferedReader reader = null;
		LinkedHashMap<Integer, LinkedHashMap<Integer, Integer>> userMap = new LinkedHashMap<>();
		try{
			reader = new BufferedReader(new FileReader(train));
			String tempString = null;
			while((tempString = reader.readLine())!=null){
				String[] content = tempString.split(separator);
				int user = Integer.parseInt(content[0])-1;
				int item = Integer.parseInt(content[1])-1;
				int rating = (int)Double.parseDouble(content[2]);
			        LinkedHashMap<Integer, Integer> itemMap = userMap.get(user);
			        if(itemMap == null) {
			            itemMap = new LinkedHashMap<>();
			            itemMap.put(item, rating);
			            userMap.put(user, itemMap);
			        } else {
			            itemMap.put(item, rating);
			        }
					// 记录下最大的itemid和userid
			        userNumber = (userNumber > user) ? userNumber : user;
			        itemNumber = (itemNumber > item) ? itemNumber : item;
//			        System.out.println("max user and item :" +userNumber +"," + itemNumber);
			}
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
//		System.out.println("此map大小:" + userMap.size());
		return  userMap;
	}
	
	/**
	  * 描述: 构造对象数组
	  * 变量 @param m
	  * 变量 @param num
	  * 变量 @return
	  */
	public static  objectArray[] to2DArray(LinkedHashMap<Integer, LinkedHashMap<Integer, Integer>> trainMap2,int num){
		int size = num;
		objectArray[] users = new objectArray[size];
		for (Entry<Integer, LinkedHashMap<Integer, Integer>> e: trainMap2.entrySet()) {
			users[e.getKey()] = new objectArray(e.getValue());
		}
		for(int i=0;i<num;i++){
			if(users[i] == null){
				users[i] = new objectArray(null);
			}
		}
		return users;
	}
	
	public static  void initSetSize() {

		//user的bias
		bu = new double[userNumber];
		
		//item的bia
		bi = new double[itemNumber];
		
		//每个user对应的item个数
		Ru = new double[userNumber];
		
		//yi之和的中间变量
		tempForY = new double[userNumber][factorNumber];
//		System.out.println("userNumber"+ userNumber + ","+ "factorNumber" + factorNumber);
		//user因子矩阵
		pu = init(userNumber,factorNumber);
		
		//item因子矩阵
		qi = init(itemNumber, factorNumber);
		
		//item隐特征矩阵
		yi = init(itemNumber, factorNumber);
				
		//计算Ru和均值
		averageValue = compute(trainData);
		System.out.println("初始化完毕,user number is:" + userNumber + ",item number is:" + itemNumber + ",and the average score is:" + averageValue);
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
			int item_length = items.length;
			for(int j=0; j<item_length; j++){
//				int temp = (int)items.itemScore[j];
				result += items.itemScore[j];
				count++;
				Ru[i]++;
//				switch(temp){
//					case 0:itemD[i][temp]++;
//					break;
//					case 1:itemD[i][temp]++;
//					break;
//					case 2:itemD[i][temp]++;
//					break;
//					case 3:itemD[i][temp]++;
//					break;
//					case 4:itemD[i][temp]++;
//					break;
//					case 5:itemD[i][temp]++;
//					break;						
//				}
			}	
		}
		return result/count;		
	}
	
	/**
	  * 描述: 某个user的所有item的yi累加
	  * 变量 @param user
	  */
	public void getTempY(int user, objectArray items){
		if(items.length!=0){
			for(int i=0; i<items.length; ++i) {
				int item = items.itemId[i];
					for(int k = 0;k<factorNumber;k++){
						tempForY[user][k] += yi[item][k]*Math.pow(Ru[user], -1.0/2);
					}
			}
		}
	}
}
