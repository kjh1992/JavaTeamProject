package teamprojcet;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.StringTokenizer;

// cpp 파일의 내용 전체를 문자열로 읽어드리는 클래스 
class ReadFileData {
	int b = 0;	
	StringBuffer buffer = new StringBuffer();
	FileInputStream file = null;
	
	public StringBuffer readFile(String str) {
		try {
			file = new FileInputStream(str);
			b = file.read();
			
			while ( b != -1) {
				buffer.append((char)b);
				b = file.read();
			}
		} catch (FileNotFoundException e) {
			System.out.println("ERROR : FileNotFoundException");
		} catch (IOException e) {
			System.out.println("Input Error");
		}
		
		return buffer;
	}
}

// ReadFileData의 내용을 토큰으로 구분하여 문자열 배열로 만든다
class StringToken {
	private String[] token = new String[1600];
	private int tokenLength;
	String source;
	
	public StringToken(String str) {
		ReadFileData r = new ReadFileData();
		source = r.readFile(str).toString();
		StringTokenizer st = new StringTokenizer(source, " :;\n\t\r"); //
		
		for(int i = 0; st.hasMoreTokens(); i++) {
			token[i] = st.nextToken();
		}
	}
	
	public String[] getToken() { return token; }
	public String getSoure() { return source; }
	
	public int getTokenLength() {
		for(int i = 0; i<token.length; i++) {
			tokenLength = i;
			
			if(token[i] == null)
				break;
		}
		return tokenLength-1;
	}
	
	public void printToken() {
		for(int i = 0; i < token.length; i++) {
			System.out.println(i + " 번째 토큰 : " + token[i]);
		}
	}
}
