package common.TLFM;

import java.util.LinkedHashMap;
import java.util.Map.Entry;

public class objectItemArray {
	//id数组
		public int[] itemId;
		public int length;
		objectItemArray(LinkedHashMap<Integer, double[]> linkedHashMap){
			if(linkedHashMap == null){
				length = 0;
			}
			else{
				length = linkedHashMap.size();
				itemId = new int[length];
				//填充数组下标
				int idx = 0;
				for(Entry<Integer, double[]> e : linkedHashMap.entrySet()){
					//填充数组
					itemId[idx] = e.getKey();
					idx++;
				}
			}
		}
}
