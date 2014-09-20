import java.util.ArrayList;


import Helpers.Math;
import Helpers.Pair;


public class SimplexTable {
	private ArrayList<Integer> basicVarCol = new ArrayList<Integer>();
	private ArrayList<Double> coeffOfBasicVarCol = new ArrayList<Double>();
	private ArrayList<Double> constTermCol = new ArrayList<Double>();
	private ArrayList<Double> coeffOfVarRow = new ArrayList<Double>();
	private ArrayList<Double> ruleOfSubstituteCol = new ArrayList<Double>();
	private ArrayList<Double> checkLineRow = new ArrayList<Double>();
	private ArrayList<ArrayList<Double>> coeffMatrix = new ArrayList<ArrayList<Double>>();
    private boolean maxProblem;
	private StringBuffer sb = new StringBuffer();
	public SimplexTable(boolean maxProblem){
		this.maxProblem = maxProblem;
	}
	public StringBuffer getStringBuffer(){
		return sb;
	}
	public void initSimplexTable(int m, ArrayList<ArrayList<Double>> coeffMatrix, ArrayList<Double> coeffOfDecisionVar, ArrayList<Double> constantTerms){
		for(int i=0;i<constantTerms.size();i++){
			coeffOfBasicVarCol.add(0.0);
			basicVarCol.add(m++);
		}
		constTermCol = constantTerms;
		this.coeffMatrix = coeffMatrix;
		coeffOfVarRow = coeffOfDecisionVar;
		initCheckLineRow();
		initRuleruleOfSubstituteCol(constantTerms.size());
	}
	private void initCheckLineRow(){
		for(int i=0;i<coeffOfVarRow.size();i++){
			if(basicVarCol.contains(i)){
				checkLineRow.add(0.0);
			}
			else {
				double total = 0;
				for(int j=0;j<coeffOfBasicVarCol.size();j++){
					total += coeffOfBasicVarCol.get(j) * coeffMatrix.get(j).get(i);
				}
					checkLineRow.add(coeffOfVarRow.get(i) - total); 
			}
		}
//		for(int i=0;i<checkLineRow.size();i++){
//			System.out.print(checkLineRow.get(i)+" ");
//		}
	}
	private void initRuleruleOfSubstituteCol(int size){
		for(int i=0;i<size;i++){
			ruleOfSubstituteCol.add(0.0);
		}
	}
	public void execIteration(){
		//step2
		boolean flag = false;
		ArrayList<Pair<Integer, Double>> list = new ArrayList<Pair<Integer,Double>>();
		for(int i=0;i<coeffOfVarRow.size();i++){
			if(!basicVarCol.contains(i) && checkLineRow.get(i) > 0){
				flag = true;
				list.add(new Pair<Integer, Double>(i, checkLineRow.get(i)));
			}
		}
		if(flag == false){
			System.out.println("已得到最优解");
			sb.append("已得到最优解"+"\n");
			return ;
		}
		//step3
		boolean flag1 = false;
		for(int index=0;index<list.size();index++){
			int value = list.get(index).getFirstEle();
			for(int j=0;j<coeffMatrix.size();j++){
				if(coeffMatrix.get(j).get(value) > 0){
					flag1 = true;
				}
			}
		}
		if(flag1 == false){
			System.out.println("此问题属无界");
			sb.append("此问题属无界"+"\n");
			return ;
		}
		//step4
		Pair<Integer, Double> VarSwappenIn = Math.max(list);
		ruleOfSubstituteColUpdate(VarSwappenIn.getFirstEle());
		Pair<Integer, Double> VarSwappedOut = Math.min(ruleOfSubstituteCol);
		//step5  
		pivot(VarSwappedOut.getFirstEle(), VarSwappenIn.getFirstEle());
		checkLineRowUpdate();
		for(int i=0;i<constTermCol.size();i++){
			System.out.println("基变量：x"+(basicVarCol.get(i)+1)+" 常数项： "+constTermCol.get(i));
			sb.append("基变量：x"+(basicVarCol.get(i)+1)+" 常数项： "+constTermCol.get(i)+"\n");
		}
		double z = 0.0;
		for(int i=0;i<coeffOfVarRow.size()-coeffOfBasicVarCol.size();i++){
			if(basicVarCol.contains(i)){ 
				int position = basicVarCol.indexOf(i);
				z += coeffOfVarRow.get(i) * constTermCol.get(position);
			}
		}
		if(maxProblem == true){
			System.out.println("目标函数的取值 z = "+ z);
			sb.append("目标函数的取值 z = "+ z+"\n");
		}
		else {
			System.out.println("目标函数的取值 z = "+ -z);
			sb.append("目标函数的取值 z = "+ -z+"\n");
		}
		
		execIteration();
	}
	/**
	 * 初等行变换
	 * @param l 换出变量（基变量）
	 * @param k 换入变量（非基变量）
	 */
	private void pivot(int l, int k){
		double mainEle = coeffMatrix.get(l).get(k); 
//		System.out.println("主元素是："+ mainEle);
		for(int i=0;i<constTermCol.size();i++){
			double ratio = coeffMatrix.get(i).get(k) / mainEle;
			if(i != l){
				for(int j=0;j<coeffMatrix.get(i).size();j++){
					if(j != k){
						coeffMatrix.get(i).set(j, coeffMatrix.get(i).get(j) - ratio * coeffMatrix.get(l).get(j));
					}
					coeffMatrix.get(i).set(k, 0.0);
				}
				constTermCol.set(i, constTermCol.get(i) - ratio * constTermCol.get(l));
			}
		}
		for(int j=0;j<coeffMatrix.get(l).size();j++){
			if(j != k){
				coeffMatrix.get(l).set(j, coeffMatrix.get(l).get(j) / mainEle);
			}
			coeffMatrix.get(l).set(k, 1.0);
		}
		constTermCol.set(l, constTermCol.get(l) / mainEle);
		
	    basicVarCol.set(l, k);
	    coeffOfBasicVarCol.set(l, coeffOfVarRow.get(k));

	}
	private void checkLineRowUpdate(){
		for(int i=0;i<checkLineRow.size();i++){
			if(basicVarCol.contains(i)){
				checkLineRow.set(i, 0.0);
			}
			else {
				double total = 0;
				for(int j=0;j<coeffOfBasicVarCol.size();j++){
					total += coeffOfBasicVarCol.get(j) * coeffMatrix.get(j).get(i);
				}
				checkLineRow.set(i, coeffOfVarRow.get(i) - total);
			}
		}
	}
	private void ruleOfSubstituteColUpdate(int k){
		for(int i=0;i<ruleOfSubstituteCol.size();i++){
			if(coeffMatrix.get(i).get(k) <= 0){
				ruleOfSubstituteCol.set(i, Double.POSITIVE_INFINITY); 
			}
			else {
				ruleOfSubstituteCol.set(i, constTermCol.get(i) / coeffMatrix.get(i).get(k));
			}
			
		}
	}
}
