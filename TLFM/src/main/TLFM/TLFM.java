package main.TLFM;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;

import common.TLFM.common_TLFM;
import common.TLFM.objectArray;

public class TLFM extends common_TLFM{
	
	public TLFM(){
//		//按比例筛选item
//		proportionChoose = false;
//		
//		//按topN选取item
//		topNChoose = false;
	}
	
	
	/**
	  * 描述: 计算dev
	  * 变量 @param user
	  * 变量 @param time
	  * 变量 @return
	  */
	public double CalDev(int user,double time){
		//当前时间与平均评价时间相差的天数
		int tTime = Math.abs((int)(time - Tu[user]));
//		System.out.println("与平均评价时间相差的天数:" + tTime);
		//计算dev
		double dev = Math.signum(time - Tu[user]) * Math.pow(tTime, 0.4);
//		System.out.println("当前dev:" + dev);
		return dev;
		
	}
	
	/**
	  * 描述: 计算time属于哪个bin
	  * 变量 @param time
	  * 变量 @return
	  */
	public int CalBin(double time){
		//表示时间属于哪个bin
		int binId = (int)(time-minTime)/binRange;
//		System.out.println("test:" + binRange);
		if(binId == binNum){
			binId = binNum - 1;
		}
//		System.out.println("当前time :" + time + ",对应的bin:" +binId);
		return binId;
	}
	
	public int CalTime(double time){
		int temp=0;
		for(int i=0;i<timeArray.length;i++){
			if(timeArray[i] == time){
				temp = i;
			}
		}
//		System.out.println("当前user的时间下标集合:" + temp);
		return temp;
	}
	
	/**
	  * 描述: 训练函数
	  * 变量 
	  */
	public void train(){
//		System.out.println(timeMap.length);
		System.out.println("开始训练模型...");
		//每个bin的跨度
		binRange = (maxTime - minTime)/binNum;
//		System.out.println("每个bin宽度:" + binRange);
//		objectArray it = trainData[2];
//		System.out.println("item个数:" + it.length);
//		System.out.println("user的天数:" + it.time.size());
		//迭代循环
		for(int i=0;i<iterTimes;i++){
			//记录起始时间
			long start = System.currentTimeMillis();
			//user循环
			for (int uid=0; uid< trainData.length; uid++) {
				//主体循环
				objectArray items = trainData[uid];

				//筛选的item集合
//				objectItemArray iData = itemData[uid];

				//取出筛选的item集合
				int[] ids = items.itemId;

				int user = uid;
				
				//当前user的所选的item的yi累加
				getTempY(user,items);
//				if(user == 0){
//				System.out.println("当前user:" + user);
//				System.out.println("当前user的时间下标集合:" + items.time);
//				}
				//item循环
				for(int iidx=0; iidx<items.length; iidx++) {					
					int item = items.itemId[iidx];
					double rating = items.itemScore[iidx];
					double time = items.itemTime[iidx];
					HashMap<Integer,Double> t = new HashMap<Integer,Double>();
					t = items.time;
//					System.out.println("当前bu:" + bu[user]);
					double pscore = predictScore(averageValue,user,item,time,t);
					double eui = rating - pscore;
					bu[user] += G*(eui - L*bu[user]);
					bi[item] += G*(eui - L*bi[item]);
					alpha_u[user] += G_alpha*(eui*CalDev(user,time) - L_alpha*alpha_u[user]);
					double d = 0.0001*(eui - 0.001*t.get(CalTime(time))) + t.get(CalTime(time));
					t.put(CalTime(time), d);
					Bi_bin[item][CalBin(time)] += G*(eui - L_bin*Bi_bin[item][CalBin(time)]);
					for(int f=0;f<factorNumber;f++){
						double temp = eui*Math.pow(Ru[user], -1.0/2)* qi[item][f];
						qi[item][f] += G*((tempForY[user][f]+ pu[user][f])*eui - L_pq*qi[item][f]);
						pu[user][f] += G*(eui*qi[item][f] - L_pq*pu[user][f]);
						//更新当前user对应item的yi
						for(int p_idx=0; p_idx<ids.length; p_idx++){
							yi[ids[p_idx]][f] += G*(temp - L_pq*yi[ids[p_idx]][f]);
						}
					}
				}
			}
//			G*=0.9;
//			G_alpha*=0.9;
//			for (int uid=0; uid< trainData.length; uid++) {
//				System.out.println("当前user:" + uid);
//				System.out.println("当前user的bu:" + bu[uid]);
//				
//			}
			double curRmse = test(averageValue);
			System.out.println(i+1 + "次迭代," + "RMSE是" + curRmse);
			//输出
			try {
				FileOutputStream out = new FileOutputStream(".\\data\\out.txt",true);
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
	public double predictScore(double average, int user,int item,double time,HashMap<Integer,Double> t){

		double[] temp = new double[factorNumber];
		for(int i=0;i<factorNumber;i++){
			temp[i] = tempForY[user][i] + pu[user][i];
		}
		double pscore = average + bu[user] + alpha_u[user] * CalDev(user,time) + t.get(CalTime(time)) + 
				bi[item] + Bi_bin[item][CalBin(time)] + innerproduct(temp,qi[item]);
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
	//test(averageValue,bu,au,Bu_time,bi,bin,pu,auk,pu_time,qi,tempForY);
	public double test(double averageValue){
		double rmse = 0.0;
		int cnt = 0;
		//测试集
		for (int u_idx=0; u_idx<userNumber; u_idx++) {
			objectArray items = testData[u_idx];			
			int user = u_idx;
				for(int i_idx=0; i_idx<items.length; i_idx++){
					cnt += 1;
					int item = items.itemId[i_idx];
					double score = items.itemScore[i_idx];
					double time = items.itemTime[i_idx];
					HashMap<Integer,Double> t = new HashMap<>();
					t = items.time;
					double pscore = predictScore(averageValue,user,item,time,t);
					rmse+=Math.pow(score-pscore, 2);
				}
		}
		return Math.sqrt(rmse/cnt);
	}

	/**
	 * 描述: 主函数
	 * 变量 @param args
	 * @throws IOException 
	 * @throws NumberFormatException 
	 */
	public static void main(String[] args) throws NumberFormatException, 
			IOException {		
		common_TLFM.initialize(
				".\\data\\TML1M_train.dat",
				".\\data\\TML1M_test.dat", "::");
		common_TLFM.iterTimes = 1000;
		common_TLFM.G = 0.002;
		common_TLFM.L = 0.005;
		common_TLFM.L_pq = 0.015;
		common_TLFM.L_bin = 0.08;
		common_TLFM.G_alpha = 0.0001;
		common_TLFM.L_alpha = 0.005;
		//训练
		TLFM l = new TLFM();
		long start = System.currentTimeMillis();
		l.train();
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
