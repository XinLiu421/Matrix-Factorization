package common.TLFM;

import java.util.ArrayList;
import java.util.HashMap;

public class objectArray {
	//id数组
	public ArrayList<Integer> itemId;
	//评分数组
	public ArrayList<Double> itemScore;
	//时间数组
	public ArrayList<Double> itemTime;

	//时间的hash,Integer装时间的映射,Double装递推公式里bu_t的值
	public HashMap<Integer,Double> time = new HashMap<Integer,Double>();
	objectArray(int user,int item,double rating,double t){
			itemId.add(user);
			itemScore.add(rating);
			itemTime.add(t);
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

