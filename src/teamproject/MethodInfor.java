package teamproject;

import java.util.ArrayList;

public class MethodInfor {
	private ClassInfor parentClass;
	private String access;
	private String type;
	private String name;
	private Method_ParameterInfo parameter;
	private String body;
	ArrayList<MemberData> memberList = new ArrayList<MemberData>();
	
	public MethodInfor(ClassInfor parentClass, String access, String type, String name, Method_ParameterInfo parameter) {
		this.parentClass = parentClass;
		this.access = access;
		this.type = type;
		this.name = name;
		this.parameter = parameter;
	}
	
	public void checkBody(String body) { // 메소드 객체의 바디를 확인하여 멤버 리스트를 다시 채움
		String[] bodySplit = body.split("[\\[\\](){}+\\-*/%=!;\\s]");
		memberList.clear();
		for (String s : bodySplit) {
			for (MemberData m : Parser.theClass.memberList) {
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
	
	public Method_ParameterInfo getParameter() {
		return parameter;
	}
	
	public void setBody(String body) {
		this.body = body;
		checkBody(body);
	}
	
	public String toString() {
		return name + "(" + parameter + ")";
	}
}