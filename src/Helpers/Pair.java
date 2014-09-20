package Helpers;

public class Pair<T1, T2> {
	public T1 firstEle;
	public T2 secondEle;
	public Pair(T1 firstEle, T2 secondEle){
		if(firstEle == null || secondEle == null)
			throw new NullPointerException();
		this.firstEle = firstEle;
		this.secondEle = secondEle;
	}
	public T1 getFirstEle(){
		return this.firstEle;
	}
	public T2 getSecondEle(){
		return this.secondEle;
	}
	public void setFirstEle(T1 value){
		this.firstEle = value;
	}
	public void setSecondEle(T2 value){
		this.secondEle = value;
	}
}
