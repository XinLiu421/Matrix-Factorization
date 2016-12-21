package Plus.Common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedHashMap;

/**
 * 类名:getData
 * 描述:初始化
 * 作者:liuxin-pc
 * 时间:2016-5-13 下午1:56:59
 *
 */
public class GetFile {
	
	/**
	  * 描述: 构造文件HashMap
	  * 变量 @param path 文件路径
	 * @param userNumber 
	  */
	public LinkedHashMap<Integer, LinkedHashMap<Integer, Double>> getData(String path,int userNumber){
		//读文件
		File train = new File(path);
		BufferedReader reader = null;
		LinkedHashMap<Integer, LinkedHashMap<Integer, Double>> userMap = new LinkedHashMap<>();
		try{
			reader = new BufferedReader(new FileReader(train));
			String tempString = null;
			while((tempString = reader.readLine())!=null){
				String[] content = tempString.split("::");
				int user = Integer.parseInt(content[0])-1;
				int item = Integer.parseInt(content[1])-1;
				double rating = Double.parseDouble(content[2]);
			        LinkedHashMap<Integer, Double> itemMap = userMap.get(user);
			        if(itemMap == null) {
			            itemMap = new LinkedHashMap<>();
			            itemMap.put(item, rating);
			            userMap.put(user, itemMap);
			        } else {
			            itemMap.put(item, rating);
			        }
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
		return  userMap;
	}
	
	/**
	  * 描述: 获取user和item的数目
	  * 变量 
	  */
	public int[] getUserAndItemNum(String path){
		File data = new File(path);
		BufferedReader reader = null;
		int[] num = new int[2];
		try{
			reader = new BufferedReader(new FileReader(data));
			String tempString = null;
			for(int i=0;(tempString = reader.readLine())!=null&&i<2;i++){
				String[] s = tempString.split(":");
				num[i] = Integer.parseInt(s[1]);
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
		return num;
	}
}
