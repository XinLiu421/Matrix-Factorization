package main.LFM;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import common.LFM.common_LFM;
import common.LFM.objectArray;

public class LFM extends common_LFM{
	
	/**
	  * 描述: 训练函数
	  * 变量 
	  */
	public void train(){
		System.out.println("开始训练模型...");		
		//迭代循环
		for(int i=0;i<iterTimes;i++){
			//记录起始时间
			long start = System.currentTimeMillis();
			//user循环
			for (int uid=0; uid< trainData.length; uid++) {
				//主体循环
				objectArray items = trainData[uid];
				//取出item集合
				int[] ids = items.itemId;
				int user = uid;
				//当前user的所选的item的yi累加
				getTempY(user,items);
				//item循环
				for (int iidx=0; iidx<items.length; iidx++) {				
					int item = items.itemId[iidx];
					int rating = items.itemScore[iidx];
					double pscore = predictScore(averageValue,bu[user],bi[item],pu[user],qi[item],tempForY[user]);
					double eui = rating - pscore;
					bu[user] += learningRate*(eui - regularization*bu[user]);
					bi[item] += learningRate*(eui - regularization*bi[item]);
//					LinkedHashMap<Integer,Double> a = trainMap.get(user);
					for(int f=0;f<factorNumber;f++){
						double temp = eui*Math.pow(Ru[user], -1.0/2)* qi[item][f];
						qi[item][f] += learningRate*((tempForY[user][f]+ pu[user][f])*eui - regularization1*qi[item][f]);
						pu[user][f] += learningRate*(eui*qi[item][f] - regularization1*pu[user][f]);
						//更新当前user对应item的yi,且取前yNUm个yi更新
						for(int p_idx=0; p_idx<ids.length; p_idx++){							
							yi[ids[p_idx]][f] += learningRate*(temp - regularization1*yi[ids[p_idx]][f]);
						}
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
	public double predictScore(double average, double bu, double bi, double[] pu, double[] qi,double[] tempForY){
		double[] temp = new double[factorNumber];
		for(int i=0;i<factorNumber;i++){
			temp[i] = tempForY[i] + pu[i];
		}
		double pscore = average + bu + bi + innerproduct(temp,qi);
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
			objectArray items = testData[u_idx];			
			int user = u_idx;
				for(int i_idx=0; i_idx<items.length; i_idx++){
					cnt += 1;
					int item = items.itemId[i_idx];
					double score = items.itemScore[i_idx];
					double pscore = predictScore(averageValue,bu[user],bi[item],pu[user],qi[item],tempForY[user]);
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
		common_LFM.initialize(
				"C:\\Users\\liuxin-pc\\Desktop\\NF_to_ML_10M_train.dat",
				"C:\\Users\\liuxin-pc\\Desktop\\NF_to_ML_10M_test.dat", "::");
		common_LFM.iterTimes = 1000;
		common_LFM.learningRate = 0.007;
		common_LFM.regularization = 0.005;
		common_LFM.regularization1 = 0.015;
		//训练
		LFM l = new LFM();
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
