package Plus.Common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class ItemsArr {
	//id数组
	public int[] itemId;
	//评分数组
	public double[] itemScore;
	public int length;
	ItemsArr(LinkedHashMap<Integer,Double> entryMap,int flag){
		if(entryMap == null){
			length = 0;
		}
		else{
			length = entryMap.size();
//			System.out.println("排序前的数组:" + entryMap);
			itemId = new int[length];
			itemScore = new double[length];
			if(flag == 1){
				LinkedHashMap<Integer,Double> sortedMap = sortMapByValue(entryMap);
//				System.out.println("排序后的数组:" + sortedMap);
				int idx = 0;
//				int count =0;
				for(Map.Entry<Integer, Double> e : sortedMap.entrySet()){					
					itemId[idx] = e.getKey();
					itemScore[idx] = e.getValue();
					idx++;
//					count++;
				}
			}
			else if(flag == 2){
//				System.out.println("排序后的数组:" + sortedMap);
				//填充数组下标
				int idx = 0;
//				int count =0;
				for(Map.Entry<Integer, Double> e : entryMap.entrySet()){
					//填充数组
					itemId[idx] = e.getKey();
					itemScore[idx] = e.getValue();
					idx++;
//					count++;
				}
			}

		}
	}
	//排序函数，可以不用
	public  LinkedHashMap<Integer,Double> sortMapByValue(Map<Integer,Double> oriMap){
		if(oriMap == null || oriMap.isEmpty()){
			return null;
		}
		int size = (int)(oriMap.size() * 1.5);
		LinkedHashMap<Integer, Double> sortedMap = new LinkedHashMap<>(size);
		ArrayList<Map.Entry<Integer, Double>> entryList = new ArrayList<>(oriMap.entrySet()); 
		Collections.sort(entryList, new MapValueComparator());
		Iterator<Map.Entry<Integer,Double>> iter = entryList.iterator();
		Map.Entry<Integer, Double> tmpEntry = null;
		while(iter.hasNext()){
			tmpEntry = iter.next();
			sortedMap.put(tmpEntry.getKey(), tmpEntry.getValue());
		}
		return sortedMap;
	}
	
	 class MapValueComparator implements Comparator<Map.Entry<Integer, Double>> {

		@Override
		public int compare(Map.Entry<Integer, Double> o1, Map.Entry<Integer, Double> o2) {
			return o2.getValue().compareTo(o1.getValue());
		}
	}
}

