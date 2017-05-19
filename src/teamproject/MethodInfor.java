package teamproject;

public class MethodInfor {
	private ClassInfor parentClass;
	private String access;
	private String type;
	private String name;
	private Method_ParameterInfo parameter;
	private String body;
	
	public MethodInfor(ClassInfor parentClass, String access, String type, String name, Method_ParameterInfo parameter) {
		this.parentClass = parentClass;
		this.access = access;
		this.type = type;
		this.name = name;
		this.parameter = parameter;
	}
	
	public String getName() {
		return name;
	}
	
	public String getBody() {
		return body;
	}
	
	public Method_ParameterInfo getParameter() {
		return parameter;
	}
	
	public void setBody(String body) {
		this.body = body;
	}
	
	public String toString() {
		return name + "(" + parameter + ")";
	}
}