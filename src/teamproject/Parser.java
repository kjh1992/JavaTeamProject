package teamproject;

import java.io.*;
import java.util.StringTokenizer;

public class Parser {
	private static StringBuffer str = new StringBuffer();
	static ClassInfor theClass;
	
	// 파일 오픈 메소드
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
	
	// 전체 소스 파싱 메소드
	public static void parse() {
		int from = 0, to;
		String declare;
		String body;
		
		for (to = 0; to < str.length(); to++) {
			
			if(str.charAt(to) == '{') { // 다음 '{'를 찾는다.
				
				declare = str.substring(from, to).trim(); // 선언문을 잘라낸다.
				from = ++to; // from을 '{' 다음 위치로 설정
				
				if (declare.split(" ")[0].equals("class")) { // 클래스 선언일 경우
					
					while (str.charAt(to) != '}') // '}'를 찾는다.
						to++;
					
					body = str.substring(from, to).trim(); // 바디를 잘라내,
					parseClass(declare, body); // 클래스 전용 파싱 메소드 호출
					from = ++to + 1; // from과 to를 '};' 다음 위치로 설정
					
				} else { // 메소드 선언일 경우
					
					int braceChecker = 1; // 중첩 중괄호를 세기 위한 변수
					
					while (braceChecker > 0) { // 바디를 닫는 '}'를 찾는다.
						to++;
						if (str.charAt(to) == '{')
							braceChecker++;
						else if (str.charAt(to) == '}')
							braceChecker--;
					}
					
					body = str.substring(from, to); // 바디를 잘라내,
					parseMethod(declare, body); // 메소드 전용 파싱 메소드 호출
					from = to + 1; // from을 '}' 다음 위치로 설정
				}
			}
		}
	}
	
	// 클래스 정의 영역 파싱 메소드
	private static void parseClass(String declare, String body) {
		ClassInfor theClass = new ClassInfor(declare.split(" ")[1]); // 클래스 선언부의 이름을 매개로 클래스정보 객체 생성
		StringTokenizer st = new StringTokenizer(body, ";:");
		String currentAccess = "private"; // cpp 클래스 멤버의 디폴트 접근 지정자는 private
		String currentStr = null;
		
		while (st.hasMoreTokens()) { 
			currentStr = st.nextToken().trim();
			
			if (currentStr.equals("private")) // 접근 지정자가 나오면 현재 접근 지정자를 변경
				currentAccess = "private";
			
			else if (currentStr.equals("public")) // 접근 지정자가 나오면 현재 접근 지정자를 변경
				currentAccess = "public";
			
			else
				parseMember(theClass, currentAccess, currentStr); // 접근 지정자가 아니면 멤버 전용 파싱 메소드 호출
		}
		
		Parser.theClass = theClass;
	}
	
	// 멤버 선언 파싱 메서드
	private static void parseMember(ClassInfor theClass, String access, String define) {
		String[] strArr;
		
		if (define.contains("(")) { // 메소드일 경우
			Method_ParameterInfo parameterInfo = new Method_ParameterInfo();
			String type = null;
			String name = null;
			
			strArr = define.split("[(]");
			
			String[] parameters = strArr[1].split("[), ]"); // 매개변수들을 쪼개서 매개변수 정보 객체에 넣는다.
			for (String s : parameters) {
				if (s == null)
					parameterInfo.addParameter("void");
				else
					parameterInfo.addParameter(s);
			}
			
			String[] typeAndName = strArr[0].split(" "); // 타입과 이름을 쪼갠다.
			if (typeAndName.length == 1) { // 생성자 또는 소멸자일 경우
				type = "void";
				name = typeAndName[0];
				
			} else { // 일반 메소드일 경우
				type = typeAndName[0];
				name = typeAndName[1];
			}
			
			theClass.addMethod(new MethodInfor(theClass, access, type, name, parameterInfo));
			
		} else if (define.contains("[")) { // 배열 멤버일 경우
			strArr = define.split("[ \\[\\]]");
			theClass.addMember(new MemberData(theClass, access, strArr[0] + "[]", strArr[1])); // 클래스 정보 객체에 멤버 자료 추가
			
		} else { // 일반 멤버일 경우
			strArr = define.split(" ");
			theClass.addMember(new MemberData(theClass, access, strArr[0], strArr[1])); // 클래스 정보 객체에 멤버 자료 추가
		}
	}
	
	// 메소드 정의 영역 파싱 메소드
	private static void parseMethod(String declare, String body) {
		String classTitle = theClass.getTitle();
		String[] classAndName = declare.split("[:][:]");
		String typeAndClass = classAndName[0];
		String method = classAndName[1];
		
		if (typeAndClass.equals(classTitle) || typeAndClass.split(" ")[1].equals(classTitle)) { // 해당 클래스이면
			String[] strArr = method.split("[(]");
			
			// 이름 추출
			String name = strArr[0];
			
			// 파라미터 추출
			String[] parameters = strArr[1].split("[,)]");
			Method_ParameterInfo parameterInfo = new Method_ParameterInfo();
			for (String s : parameters) {
				if (s == null)
					parameterInfo.addParameter("void");
				else
					parameterInfo.addParameter(s.split(" ")[0]);
			}
			
			// 이름과 파라미터가 일치하는 메소드에 바디 삽입
			for (MethodInfor m : theClass.methodList) {
				if (m.getName().equals(name) && m.getParameter().getParameterList().equals(parameterInfo.getParameterList())) {
					m.setBody(body);
				}
			}
		}
	}
}