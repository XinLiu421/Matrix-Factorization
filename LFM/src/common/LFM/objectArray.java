package common.LFM;

import java.util.LinkedHashMap;
import java.util.Map;

public class objectArray {
	//id数组
	public int[] itemId;
	//评分数组
	public int[] itemScore;
	public int length;
	objectArray(LinkedHashMap<Integer,Integer> entryMap){
		if(entryMap == null){
			length = 0;
		}
		else{
			length = entryMap.size();
			itemId = new int[length];
			itemScore = new int[length];
//			System.out.println("排序后的数组:" + sortedMap);
			//填充数组下标
			int idx = 0;
//			int count =0;
			for(Map.Entry<Integer, Integer> e : entryMap.entrySet()){
				//填充数组
				itemId[idx] = e.getKey();
				itemScore[idx] = e.getValue();
				idx++;
//				count++;
			}
		}
	}
//	//排序函数，可以不用
//	public  LinkedHashMap<Integer,Double> sortMapByValue(Map<Integer,Double> oriMap){
//		if(oriMap == null || oriMap.isEmpty()){
//			return null;
//		}
//		int size = (int)(oriMap.size() * 1.5);
//		LinkedHashMap<Integer, Double> sortedMap = new LinkedHashMap<>(size);
//		ArrayList<Map.Entry<Integer, Double>> entryList = new ArrayList<>(oriMap.entrySet()); 
//		Collections.sort(entryList, new MapValueComparator());
//		Iterator<Map.Entry<Integer,Double>> iter = entryList.iterator();
//		Map.Entry<Integer, Double> tmpEntry = null;
//		while(iter.hasNext()){
//			tmpEntry = iter.next();
//			sortedMap.put(tmpEntry.getKey(), tmpEntry.getValue());
//		}
//		return sortedMap;
//	}
//	
//	 class MapValueComparator implements Comparator<Map.Entry<Integer, Double>> {
//
//		@Override
//		public int compare(Map.Entry<Integer, Double> o1, Map.Entry<Integer, Double> o2) {
//			return o2.getValue().compareTo(o1.getValue());
//		}
//	}
}

