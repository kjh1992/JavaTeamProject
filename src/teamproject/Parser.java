package teamproject;

import java.io.*;
import java.util.*;
import java.util.regex.*;

public class Parser {
	private static StringBuffer currentSource;
	
	// ���� ���� �޼ҵ�
	public static void openFile(File file) {
		currentSource = new StringBuffer("");
		int b = 0;
		FileInputStream fs = null;
		try {
			fs = new FileInputStream(file);
			b = fs.read();
			while (b != -1) {
				currentSource.append((char)b);
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
		Pattern p;
		Matcher m;
		
		// Ŭ���� ���� + ��ü
		p = Pattern.compile("class\\s+(?<name>\\w+)\\s*\\{(?<body>[^{}]*)\\}");
		m = p.matcher(currentSource);
		while (m.find()) parseClass(m.group("name"), m.group("body"));
		
		// �޼ҵ� ����
		p = Pattern.compile("((?<classname>\\w+)::~?\\k<classname>|\\w+\\s+\\w+::\\w+)\\([^()]*\\)");
		m = p.matcher(currentSource);
		int from, to, braceChecker;
		String declaration, body;
		
		while (m.find()) {
			from = m.end();
			while (currentSource.charAt(from) != '{') from++;
			braceChecker = 1;
			to = from;
			while (braceChecker > 0) {
				to++;
				if (currentSource.charAt(to) == '{') braceChecker++;
				else if (currentSource.charAt(to) == '}') braceChecker--;
			}
			declaration = m.group();
			if (++from == to) body = "";
			else body = currentSource.substring(from, to);
			parseMethod(declaration, body);
		}
	}
	
	// Ŭ���� ���� ���� �Ľ� �޼ҵ�
	private static void parseClass(String name, String body) {
		ClassInfor c = new ClassInfor(name); // Ŭ���� ������� �̸��� �Ű��� Ŭ�������� ��ü ����
		StringTokenizer st = new StringTokenizer(body, ";:");
		String currentAccess = "private"; // Ŭ���� ����� ����Ʈ ���� �����ڴ� private
		String token = null;
		
		while (st.hasMoreTokens()) { 
			token = st.nextToken().trim();
			if (token.equals("private")) currentAccess = "private";
			else if (token.equals("public")) currentAccess = "public";
			else parseMember(c, currentAccess, token);
		}
		ClassViewer.addClass(c);
	}
	
	// ��� ���� �Ľ� �޼ҵ�
	private static void parseMember(ClassInfor c, String access, String declaration) {
		String s = declaration.replace("()", "(void)");
		Matcher m1 = Pattern.compile("^(?<name>~?\\w+)\\((?<parameter>.+)\\)").matcher(s);
		Matcher m2 = Pattern.compile("^(?<type>\\w+)\\s+(?<name>\\w+)\\((?<parameter>.+)\\)").matcher(s);
		Matcher m3 = Pattern.compile("^(?<type>\\w+)\\s+(?<name>\\w+)$").matcher(s);
		Matcher m4 = Pattern.compile("^(?<type>\\w+)\\s+(?<name>\\w+)\\[.+\\]").matcher(s);
		
		if (m1.find()) c.addMethod(new MethodInfor(c, access, "void", m1.group("name"), parseParameter(m1.group("parameter"))));
		else if (m2.find()) c.addMethod(new MethodInfor(c, access, m2.group("type"), m2.group("name"), parseParameter(m2.group("parameter"))));
		else if (m3.find()) c.addMember(new MemberInfor(c, access, m3.group("type"), m3.group("name")));
		else if (m4.find()) c.addMember(new MemberInfor(c, access, m4.group("type") + "[]", m4.group("name")));
	}
	
	// �Ķ���� �Ľ� �޼ҵ�
	private static ArrayList<ParameterInfor> parseParameter(String parameters) {
		ArrayList<ParameterInfor> list = new ArrayList<ParameterInfor>();
		String[] splitedParameters = parameters.split(",");
		Pattern p = Pattern.compile("(?<type>\\w+)\\s+(?<name>\\w+)");
		Matcher m;
		
		for (String s : splitedParameters) {
			s = s.trim();
			m = p.matcher(s);
//			if (m.find()) list.add(new ParameterInfor(m.group("type"), ""));
			if (m.find()) list.add(new ParameterInfor(m.group("type"), m.group("name")));
			else list.add(new ParameterInfor(s, ""));
		}
		return list;
	}
	
	// �޼ҵ� ���� ���� �Ľ� �޼ҵ�
	private static void parseMethod(String declaration, String body) {
		String s = declaration.replace("()", "(void)");
		ClassInfor c;
		MethodInfor method;
		ArrayList<ParameterInfor> list;
		Matcher m = Pattern.compile("(?<type>\\w+\\s+)?(?<classname>\\w+)::(?<name>~?\\w+)\\((?<parameter>.+)\\)").matcher(s);

		if (m.find()) {
			c = ClassViewer.getTheClass(m.group("classname"));
			list = parseParameter(m.group("parameter"));
			method = c.getTheMethod(m.group("name"), list);
			if (method != null) method.setBody(body);
		}
	}
	
	// ���� ���� �޼ҵ�
	public static void saveFile(File file) {
		StringBuffer s = new StringBuffer(currentSource);
		Matcher m = Pattern.compile("((?<classname>\\w+)::~?\\k<classname>|\\w+\\s+\\w+::\\w+)\\([^()]*\\)").matcher(s);
		Pattern p = Pattern.compile("(?<type>\\w+\\s+)?(?<classname>\\w+)::(?<name>~?\\w+)\\((?<parameter>.+)\\)");
		int from, to, braceChecker;
		ClassInfor c;
		MethodInfor method;
		ArrayList<ParameterInfor> list;
		String body;

		while (m.find()) {
			// �޼ҵ� ã��
			Matcher sub_m = p.matcher(m.group().replace("()", "(void)"));
			sub_m.find();
			c = ClassViewer.getTheClass(sub_m.group("classname"));
			list = parseParameter(sub_m.group("parameter"));
			method = c.getTheMethod(sub_m.group("name"), list);
			body = method.getBody();
			// �ش� �޼ҵ��� �ٵ� ���� ��ü
			from = m.end();
			while (s.charAt(from) != '{') from++;
			braceChecker = 1;
			to = from;
			while (braceChecker > 0) {
				to++;
				if (s.charAt(to) == '{') braceChecker++;
				else if (s.charAt(to) == '}') braceChecker--;
			}
			if (body != "") s.replace(++from, to, body);
			else s.replace(from, to + 1, "{}");
			
			m.region(from, s.length());
		}
		
		try {
			PrintWriter out = new PrintWriter(file);
			out.print(s);
			out.flush();
			out.close();
		} catch (FileNotFoundException e) {
			System.out.println("FileNotFoundException");
		}
	}
}