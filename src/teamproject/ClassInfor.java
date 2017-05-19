package teamproject;

import java.util.ArrayList;

public class ClassInfor {
	
	private String classTitle;	// 클래스 이름
	ArrayList<MethodInfor> methodList = new ArrayList<MethodInfor>(); // 메소드 목록
	ArrayList<MemberData> memberList = new ArrayList<MemberData>(); // 멤버 목록
	
	private StringToken st; // 파싱클래스
	private KeyWord kw = new KeyWord();
	private String[] classContent;	// 클래스내용
	private int num_of_method;	// 메소드 개수
	private int num_of_member;	// 멤버 개수
	
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