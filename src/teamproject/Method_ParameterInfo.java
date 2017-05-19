package teamproject;

import java.util.ArrayList;

public class Method_ParameterInfo {
	ArrayList<String> parameterList = new ArrayList<String>();
	
	public void addParameter(String parameter) {
		parameterList.add(parameter);
	}
	
	public ArrayList<String> getParameterList() {
		return parameterList;
	}
	
	public String toString() {
		String str = "";
		if (!parameterList.isEmpty() && !parameterList.get(0).equals("void")) {
			boolean isFirst = true;
			for (String s : parameterList) {
				if (isFirst) {
					str = s;
					isFirst = false;
				} else
					str = str + ", " + s;	
			}
		}
		return str;
	}
}