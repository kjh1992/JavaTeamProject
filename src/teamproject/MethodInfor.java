package teamproject;

import java.util.ArrayList;

public class MethodInfor {
	private ClassInfor parentClass;
	private String access;
	private String type;
	private String name;
	private String body;
	private ArrayList<ParameterInfor> parameterList = new ArrayList<ParameterInfor>();
	private ArrayList<MemberInfor> memberList = new ArrayList<MemberInfor>();
	
	public MethodInfor(ClassInfor c, String access, String type, String name, ArrayList<ParameterInfor> parameterList) {
		this.parentClass = c;
		this.access = access;
		this.type = type;
		this.name = name;
		this.parameterList = parameterList;
	}
	
	public void checkBody() { // 메소드 객체의 바디를 확인하여 멤버 리스트를 다시 채움
		String[] bodySplit = body.split("[\\[\\](){}+\\-*/%=!;\\s]");
		memberList.clear();
		for (String s : bodySplit) {
			for (MemberInfor m : parentClass.memberList) {
				if (m.getName().equals(s) && !memberList.contains(m)) {
					memberList.add(m);
				}
			}
		}
	}
	
	public String getName() {
		return name;
	}
	
	public String getType() {
		return type;
	}
	
	public String getAccess() {
		return access;
	}
	
	public String getBody() {
		return body;
	}
	
	public ArrayList<ParameterInfor> getParameterList() {
		return parameterList;
	}
	
	public ArrayList<MemberInfor> getMemberList() {
		return memberList;
	}
	
	public void setBody(String body) {
		this.body = body;
		checkBody();
	}
	
	public String toString() {
		String parameter = "";
		for (ParameterInfor p : parameterList) {
			if (parameter.equals("")) parameter += p;
			else parameter += ", " + p;
		}
		return name + "(" + parameter + ")";
	}
}