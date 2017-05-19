package teamproject;

public class MemberData {
	private ClassInfor parentClass;
	private String access;
	private String type;
	private String name;
	
	public MemberData(ClassInfor parentClass, String access, String type, String name) {
		this.parentClass = parentClass;
		this.access = access;
		this.type = type;
		this.name = name;
	}
	
	public String toString() {
		return name + " : " + type;
	}
}