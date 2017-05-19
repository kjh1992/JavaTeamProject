package teamproject;

import java.io.*;
import java.util.StringTokenizer;

public class Parser {
	private static StringBuffer str = new StringBuffer();
	static ClassInfor theClass;
	
	// ���� ���� �޼ҵ�
	public static void openFile(File file) {
		int b = 0;
		FileInputStream fs = null;
		
		try {
			fs = new FileInputStream(file);
			b = fs.read();
			while (b != -1) {
				str.append((char)b);
				b = fs.read();
			}
		} catch (FileNotFoundException e) {
			System.out.println("FileNotFoundException");
			
		} catch (IOException e) {
			System.out.println("IOException");
		}
	}
	
	// ��ü �ҽ� �Ľ� �޼ҵ�
	public static void parse() {
		int from = 0, to;
		String declare;
		String body;
		
		for (to = 0; to < str.length(); to++) {
			
			if(str.charAt(to) == '{') { // ���� '{'�� ã�´�.
				
				declare = str.substring(from, to).trim(); // ������ �߶󳽴�.
				from = ++to; // from�� '{' ���� ��ġ�� ����
				
				if (declare.split(" ")[0].equals("class")) { // Ŭ���� ������ ���
					
					while (str.charAt(to) != '}') // '}'�� ã�´�.
						to++;
					
					body = str.substring(from, to).trim(); // �ٵ� �߶�,
					parseClass(declare, body); // Ŭ���� ���� �Ľ� �޼ҵ� ȣ��
					from = ++to + 1; // from�� to�� '};' ���� ��ġ�� ����
					
				} else { // �޼ҵ� ������ ���
					
					int braceChecker = 1; // ��ø �߰�ȣ�� ���� ���� ����
					
					while (braceChecker > 0) { // �ٵ� �ݴ� '}'�� ã�´�.
						to++;
						if (str.charAt(to) == '{')
							braceChecker++;
						else if (str.charAt(to) == '}')
							braceChecker--;
					}
					
					body = str.substring(from, to); // �ٵ� �߶�,
					parseMethod(declare, body); // �޼ҵ� ���� �Ľ� �޼ҵ� ȣ��
					from = to + 1; // from�� '}' ���� ��ġ�� ����
				}
			}
		}
	}
	
	// Ŭ���� ���� ���� �Ľ� �޼ҵ�
	private static void parseClass(String declare, String body) {
		ClassInfor theClass = new ClassInfor(declare.split(" ")[1]); // Ŭ���� ������� �̸��� �Ű��� Ŭ�������� ��ü ����
		StringTokenizer st = new StringTokenizer(body, ";:");
		String currentAccess = "private"; // cpp Ŭ���� ����� ����Ʈ ���� �����ڴ� private
		String currentStr = null;
		
		while (st.hasMoreTokens()) { 
			currentStr = st.nextToken().trim();
			
			if (currentStr.equals("private")) // ���� �����ڰ� ������ ���� ���� �����ڸ� ����
				currentAccess = "private";
			
			else if (currentStr.equals("public")) // ���� �����ڰ� ������ ���� ���� �����ڸ� ����
				currentAccess = "public";
			
			else
				parseMember(theClass, currentAccess, currentStr); // ���� �����ڰ� �ƴϸ� ��� ���� �Ľ� �޼ҵ� ȣ��
		}
		
		Parser.theClass = theClass;
	}
	
	// ��� ���� �Ľ� �޼���
	private static void parseMember(ClassInfor theClass, String access, String define) {
		String[] strArr;
		
		if (define.contains("(")) { // �޼ҵ��� ���
			Method_ParameterInfo parameterInfo = new Method_ParameterInfo();
			String type = null;
			String name = null;
			
			strArr = define.split("[(]");
			
			String[] parameters = strArr[1].split("[), ]"); // �Ű��������� �ɰ��� �Ű����� ���� ��ü�� �ִ´�.
			for (String s : parameters) {
				if (s == null)
					parameterInfo.addParameter("void");
				else
					parameterInfo.addParameter(s);
			}
			
			String[] typeAndName = strArr[0].split(" "); // Ÿ�԰� �̸��� �ɰ���.
			if (typeAndName.length == 1) { // ������ �Ǵ� �Ҹ����� ���
				type = "void";
				name = typeAndName[0];
				
			} else { // �Ϲ� �޼ҵ��� ���
				type = typeAndName[0];
				name = typeAndName[1];
			}
			
			theClass.addMethod(new MethodInfor(theClass, access, type, name, parameterInfo));
			
		} else if (define.contains("[")) { // �迭 ����� ���
			strArr = define.split("[ \\[\\]]");
			theClass.addMember(new MemberData(theClass, access, strArr[0] + "[]", strArr[1])); // Ŭ���� ���� ��ü�� ��� �ڷ� �߰�
			
		} else { // �Ϲ� ����� ���
			strArr = define.split(" ");
			theClass.addMember(new MemberData(theClass, access, strArr[0], strArr[1])); // Ŭ���� ���� ��ü�� ��� �ڷ� �߰�
		}
	}
	
	// �޼ҵ� ���� ���� �Ľ� �޼ҵ�
	private static void parseMethod(String declare, String body) {
		String classTitle = theClass.getTitle();
		String[] classAndName = declare.split("[:][:]");
		String typeAndClass = classAndName[0];
		String method = classAndName[1];
		
		if (typeAndClass.equals(classTitle) || typeAndClass.split(" ")[1].equals(classTitle)) { // �ش� Ŭ�����̸�
			String[] strArr = method.split("[(]");
			
			// �̸� ����
			String name = strArr[0];
			
			// �Ķ���� ����
			String[] parameters = strArr[1].split("[,)]");
			Method_ParameterInfo parameterInfo = new Method_ParameterInfo();
			for (String s : parameters) {
				if (s == null)
					parameterInfo.addParameter("void");
				else
					parameterInfo.addParameter(s.split(" ")[0]);
			}
			
			// �̸��� �Ķ���Ͱ� ��ġ�ϴ� �޼ҵ忡 �ٵ� ����
			for (MethodInfor m : theClass.methodList) {
				if (m.getName().equals(name) && m.getParameter().getParameterList().equals(parameterInfo.getParameterList())) {
					m.setBody(body);
				}
			}
		}
	}
}