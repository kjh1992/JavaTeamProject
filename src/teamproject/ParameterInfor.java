package teamproject;

public class ParameterInfor {
	private String type;
	private String name;
	
	public ParameterInfor(String type, String name) {
		this.type = type;
		this.name = name;
	}
	
	public String getType() {
		return type;
	}
	
	public String getName() {
		return name;
	}
	
	public String toString() {
		if (type.equals("void")) return "";
		else return type;
	}
	
	@Override
	public boolean equals(Object o) {
		ParameterInfor p = (ParameterInfor) o;
		if (type.equals(p.getType()) && (name == "" || name.equals(p.getName()))) return true;
		else return false;
	}
}