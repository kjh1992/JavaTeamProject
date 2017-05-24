package teamproject;

import java.util.ArrayList;

public class ClassInfor {
	private String name;	// 클래스 이름
	ArrayList<MethodInfor> methodList = new ArrayList<MethodInfor>(); // 메소드 목록
	ArrayList<MemberInfor> memberList = new ArrayList<MemberInfor>(); // 멤버 목록
	
	public ClassInfor(String name) {
		this.name = name;
	}
	
	public MethodInfor getTheMethod(String name, ArrayList<ParameterInfor> list) {
		for (MethodInfor m : methodList)
			if (m.getName().equals(name) && m.getParameterList().equals(list)) return m;
		return null;
	}
	
	public void addMethod(MethodInfor method) {
		methodList.add(method);
	}
	
	public void addMember(MemberInfor member) {
		memberList.add(member);
	}
	
	public String getName() {
		return name;
	}
	
	public String toString() {
		return name;
	}
}