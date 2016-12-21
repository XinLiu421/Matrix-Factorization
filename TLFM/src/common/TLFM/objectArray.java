package common.TLFM;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

public class objectArray {
	//id数组
	public int[] itemId;
	//评分数组
	public double[] itemScore;
	//时间数组
	public int[] itemTime;
	public int length;
	//时间的hash,Integer装时间的映射,Double装递推公式里bu_t的值
	public HashMap<Integer,Double> time = new HashMap<Integer,Double>();
	objectArray(LinkedHashMap<Integer, Object[]> linkedHashMap,int[] timeArray){
		if(linkedHashMap == null){
			length = 0;
		}
		else{
			length = linkedHashMap.size();
			itemId = new int[length];
			itemScore = new double[length];
			itemTime = new int[length];
			//填充数组下标
			int idx = 0;
			for(Entry<Integer, Object[]> e : linkedHashMap.entrySet()){
				//填充数组
				itemId[idx] = e.getKey();
				Object[] temp = e.getValue();
				itemScore[idx] = (double) temp[0];
				itemTime[idx] = (int)temp[1];
//				System.out.println("time is:" + itemTime[idx] );
				int tag = CalTime((int)temp[1],timeArray);
				idx++;
				//填充time的Map
				time.put(tag, 0.0);
//				System.out.println("当前user的time的test:" + time);
			}
//			System.out.println("当前user的time的test:" + time);
		}
	}
	
	public int CalTime(int time,int[] timeMap){
//		System.out.println("time1 is" + time);
		int temp=0;
		for(int i=0;i<timeMap.length;i++){
			if(timeMap[i] == time){
				temp = i;
//				System.out.println("time 下表数组里:" +timeMap[i]);
			}
		}
//		System.out.println("time 下标:" + temp);
		return temp;

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

