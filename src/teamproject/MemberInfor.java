package teamproject;

public class MemberInfor {
	private ClassInfor parentClass;
	private String access;
	private String type;
	private String name;
	
	public MemberInfor(ClassInfor c, String access, String type, String name) {
		this.parentClass = c;
		this.access = access;
		this.type = type;
		this.name = name;
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
	
	public ClassInfor getParentClass() {
		return parentClass;
	}
	
	public String toString() {
		return name + " : " + type;
	}
}