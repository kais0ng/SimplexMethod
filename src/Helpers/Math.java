package Helpers;

import java.util.ArrayList;

public class Math {
	public static Pair<Integer, Double> max(ArrayList<Pair<Integer, Double>> list){
		int pos = list.get(0).getFirstEle();
		double max = list.get(0).getSecondEle();
		for(Pair<Integer,Double> value: list){
			if(max < value.getSecondEle()){
				max = value.getSecondEle();
				pos = value.getFirstEle();
			}
		}
		return new Pair<Integer, Double>(pos, max);
		
	}
	public static Pair<Integer, Double> min(ArrayList<Double> list){
		int pos = 0;
		double min = list.get(0);
		for(int i=0;i<list.size();i++){
			if(min > list.get(i)){
				min = list.get(i);
				pos = i;
			}
		}
		return new Pair<Integer, Double>(pos, min);
	}
}
