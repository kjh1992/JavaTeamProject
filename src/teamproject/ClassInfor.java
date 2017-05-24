package teamproject;

import java.util.ArrayList;

public class ClassInfor {
	private String name;	// Ŭ���� �̸�
	ArrayList<MethodInfor> methodList = new ArrayList<MethodInfor>(); // �޼ҵ� ���
	ArrayList<MemberInfor> memberList = new ArrayList<MemberInfor>(); // ��� ���
	
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