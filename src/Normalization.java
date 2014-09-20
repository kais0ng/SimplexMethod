import java.util.ArrayList;

public class Normalization {
	private String objectiveFunction;
	private int numOfDecisionVar;
	private ArrayList<Double> coeffOfDecisionVar = new ArrayList<Double>(); 
	private ArrayList<String> constraints = new ArrayList<String>();
	private ArrayList<ArrayList<Double>> coeffOfMatrix = new ArrayList<ArrayList<Double>>();
	private ArrayList<Double> constantTermList = new ArrayList<Double>();
	private boolean maxProblem;
	
	public Normalization(String objectiveFunction, ArrayList<String> constraints){
		this.objectiveFunction = objectiveFunction;
		this.constraints = constraints;
	}
	public void readObjectiveFunc(String objectiveFunc){
		objectiveFunction = objectiveFunc;
		
	}
	public void readConstraints(ArrayList<String> constraintsList){
		constraints = constraintsList;
	}
	public ArrayList<ArrayList<Double>> getCoeffOfMatrix(){
		return coeffOfMatrix;
	}
	public ArrayList<Double> getConstantTermList(){
		return constantTermList;
	}
	public ArrayList<Double> getCoeffOfDecisionVar(){
		return coeffOfDecisionVar;
	}
	public int getNumOfDecisionVar(){
		return numOfDecisionVar;
	}
	public boolean getMaxProblemState(){
		return maxProblem;
	}
	public void transferToCanonicalForm(){
		//step0
		ArrayList<Integer> num = new ArrayList<Integer>();
		for(String value: constraints){
			if(value.contains("无约束")){
				if(!value.contains(",")){
					value = value.substring(0,value.indexOf("无"));    
					String[] sub = value.split("x");
					num.add(Integer.parseInt(sub[1].trim())-1);
				}
				else {
					value = value.substring(0,value.indexOf("无"));
					String[] subValue = value.split(",");
					for(int i=0;i<subValue.length;i++){
						String[] sub = subValue[i].split("x");
						num.add(Integer.parseInt(sub[1].trim())-1);
					}
				}
			}
		}
		//step1
		String[] temp = objectiveFunction.split("=");
		String[] tmp = temp[1].split("\\+");
		for(String str: tmp){
			String sub = str.substring(0, str.indexOf("x"));
			if(objectiveFunction.startsWith("max")){
				maxProblem = true;
				if(num.contains(coeffOfDecisionVar.size())){
					coeffOfDecisionVar.add(Double.parseDouble(sub.trim()));
					coeffOfDecisionVar.add(-Double.parseDouble(sub.trim()));
				}
				else{
					coeffOfDecisionVar.add(Double.parseDouble(sub.trim()));
				}
				
			}
				
			if(objectiveFunction.startsWith("min")){
				maxProblem = false;
				if(num.contains(coeffOfDecisionVar.size())){
					coeffOfDecisionVar.add(-Double.parseDouble(sub.trim()));
					coeffOfDecisionVar.add(Double.parseDouble(sub.trim()));
				}
				else {
					coeffOfDecisionVar.add(-Double.parseDouble(sub.trim()));
				}
				
			}
		}
		numOfDecisionVar = coeffOfDecisionVar.size();
		
		//step2
		int numOfRow = 0;
		for(String value: constraints){
			if(value.contains("<=")){
				numOfRow++;
			}
			if(value.contains(">=")){
				numOfRow++;
			}
			if(value.contains("==")){
				numOfRow += 2;
			}
		}
		//step3
		for(String value: constraints){
			
			if(value.contains("<=")){
				ArrayList<Double> row = new ArrayList<Double>();
				String[] sub = value.split("<=");
				constantTermList.add(Double.parseDouble(sub[1].trim()));
				String[] coeffList = sub[0].split("\\+");
				for(String coeff: coeffList){
					String s = coeff.substring(0, coeff.indexOf("x"));
					if(num.contains(row.size())){
						row.add(Double.parseDouble(s.trim()));
						row.add(-Double.parseDouble(s.trim()));
					}else {
						row.add(Double.parseDouble(s.trim()));	
					}
						
				}
				for(int i=numOfDecisionVar;i<numOfDecisionVar+numOfRow;i++){
					row.add(0.0);
				}
				coeffOfMatrix.add(row);
				continue;
			}
			if(value.contains(">=")){
				ArrayList<Double> row = new ArrayList<Double>();
				String[] sub = value.split(">=");
				constantTermList.add(-Double.parseDouble(sub[1].trim()));
				String[] coeffList = sub[0].split("\\+");
				for(String coeff: coeffList){
					String s = coeff.substring(0, coeff.indexOf("x"));
					if(num.contains(row.size())){
						if(Double.parseDouble(s.trim()) == 0){
							row.add(Double.parseDouble(s.trim()));
							row.add(Double.parseDouble(s.trim()));
						}
						else {
							row.add(-Double.parseDouble(s.trim()));
							row.add(Double.parseDouble(s.trim()));
						}
					}
					else {
						if(Double.parseDouble(s.trim()) == 0){
							row.add(Double.parseDouble(s.trim()));
						}
						else {
							row.add(-Double.parseDouble(s.trim()));
						}
					}
					
				}
				for(int i=numOfDecisionVar;i<numOfDecisionVar+numOfRow;i++){
					row.add(0.0);
				}
				coeffOfMatrix.add(row);
				continue;
			}
			if(value.contains("==")){
				ArrayList<Double> moreRow = new ArrayList<Double>();
				String[] sub = value.split("==");
				String[] coeffList = sub[0].split("\\+");
				for(String coeff: coeffList){
					String s = coeff.substring(0, coeff.indexOf("x"));
					if(num.contains(moreRow.size())){
						if(Double.parseDouble(s.trim()) == 0.0){
							moreRow.add(Double.parseDouble(s.trim()));
							moreRow.add(Double.parseDouble(s.trim()));
						}	
						else {
							moreRow.add(-Double.parseDouble(s.trim()));
							moreRow.add(Double.parseDouble(s.trim()));
						}
					}
					else {
						if(Double.parseDouble(s.trim()) == 0.0)
							moreRow.add(Double.parseDouble(s.trim()));
						else {
							moreRow.add(-Double.parseDouble(s.trim()));
						}
					}
				}
				for(int i=numOfDecisionVar;i<numOfDecisionVar+numOfRow;i++){
					moreRow.add(0.0);
				}
				constantTermList.add(-Double.parseDouble(sub[1].trim()));
				coeffOfMatrix.add(moreRow);
				ArrayList<Double> lessRow = new ArrayList<Double>();
				for(String coeff: coeffList){
					String s = coeff.substring(0, coeff.indexOf("x"));
					if(num.contains(lessRow.size())){
						lessRow.add(Double.parseDouble(s.trim()));
						lessRow.add(-Double.parseDouble(s.trim()));
					}
					else{
						lessRow.add(Double.parseDouble(s.trim()));
					}
				}
				for(int i=numOfDecisionVar;i<numOfDecisionVar+numOfRow;i++){
					lessRow.add(0.0);
				}
				constantTermList.add(Double.parseDouble(sub[1].trim()));
				coeffOfMatrix.add(lessRow);
				continue;
			}
		}
		//step4
		int j = numOfDecisionVar;
		for(int i=0;i<numOfRow;i++){
			coeffOfMatrix.get(i).set(j++, 1.0);
		}
		for(int i=numOfDecisionVar;i<numOfDecisionVar+numOfRow;i++){
			coeffOfDecisionVar.add(0.0);
		}
	}
	public void canonicalFormPrint(){
		for(int i=0;i<coeffOfMatrix.size();i++){
			for(int j=0;j<coeffOfMatrix.get(i).size();j++){
				System.out.print(coeffOfMatrix.get(i).get(j)+" ");
			}
			System.out.println();
		}
	}
	public void constantTermPrint(){
		for(int i=0;i<constantTermList.size();i++){
			System.out.print(constantTermList.get(i)+" ");
		}
		System.out.println();
	}
	public void objectiveFuncPrint(){
		for(int i=0;i<coeffOfDecisionVar.size();i++){
			System.out.print(coeffOfDecisionVar.get(i)+" ");
		}
		System.out.println();
	}
}
