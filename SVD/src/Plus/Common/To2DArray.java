package Plus.Common;

import java.util.LinkedHashMap;
import java.util.Map.Entry;


public class To2DArray {	
	public static ItemsArr[] to2DArray(LinkedHashMap<Integer, LinkedHashMap<Integer, Double>> m,int num,int flag){
		int size = num;
		ItemsArr[] users = new ItemsArr[size];
		for (Entry<Integer, LinkedHashMap<Integer, Double>> e: m.entrySet()) {
			users[e.getKey()] = new ItemsArr(e.getValue(),flag);
//			System.out.println("当前操作的user：" + e.getKey());
		}
		for(int i=0;i<num;i++){
			if(users[i] == null){
				users[i] = new ItemsArr(null,2);
			}
		}
		return users;
	}
}
