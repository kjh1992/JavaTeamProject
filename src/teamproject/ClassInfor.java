package teamproject;

import java.util.ArrayList;

public class ClassInfor {
	
	private String classTitle;	// Ŭ���� �̸�
	ArrayList<MethodInfor> methodList = new ArrayList<MethodInfor>(); // �޼ҵ� ���
	ArrayList<MemberData> memberList = new ArrayList<MemberData>(); // ��� ���
	
	private StringToken st; // �Ľ�Ŭ����
	private KeyWord kw = new KeyWord();
	private String[] classContent;	// Ŭ��������
	private int num_of_method;	// �޼ҵ� ����
	private int num_of_member;	// ��� ����
	
	public ClassInfor(String name) {
		classTitle = name;
	}
	
	public void addMethod(MethodInfor method) {
		methodList.add(method);
	}
	
	public void addMember(MemberData member) {
		memberList.add(member);
	}
	
	public String getTitle() {
		return classTitle;
	}
	
	public String toString() {
		return classTitle;
	}
}