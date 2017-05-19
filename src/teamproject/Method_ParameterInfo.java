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
		if (!parameterList.get(0).equals("void")) {
			for (String s : parameterList) {
				if (str.equals("")) {
					str += s;
				} else
					str += ", " + s;	
			}
		}
		return str;
	}
}