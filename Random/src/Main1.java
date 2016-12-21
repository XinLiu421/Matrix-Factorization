import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main1 {
	
	public  int compute(String s){
		char[] c = s.toCharArray();
		Map<Character,Integer> map = new HashMap<Character,Integer>();
		for(int i=0;i<c.length;i++){
			if(map.containsKey(c[i])){
				map.put(c[i], map.get(c[i])+1);
			}
			else{
				map.put(c[i], 1);
			}
		}
//		System.out.println(map);
		List<Map.Entry<Character, Integer>> list = new ArrayList<Map.Entry<Character,Integer>>(map.entrySet());
		Collections.sort(list,new Comparator<Map.Entry<Character, Integer>>(){

			@Override
			public int compare(Map.Entry<Character, Integer> o1,
					Map.Entry<Character, Integer> o2) {
				return o2.getValue().compareTo(o1.getValue());
			}
			
		});
//		System.out.println("排序后的list:" + list);
		int result=0;
		for(int i=0;i<list.size();i++){
			result+=list.get(i).getValue() *(26-i);
//			System.out.println(list.get(i).getValue());
		}
		return result;
	}

	public static void main(String[] args){
		Scanner s = new Scanner(System.in);
		int n = s.nextInt();
		String[] name = new String[n];
		Main1 m = new Main1();
		for(int i=0;i<name.length;i++){
			name[i] = s.next();
//			System.out.println(name[i]);
			System.out.println(m.compute(name[i]));
		}
	}
}
