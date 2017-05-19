package teamprojcet;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.StringTokenizer;

// cpp ������ ���� ��ü�� ���ڿ��� �о�帮�� Ŭ���� 
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

// ReadFileData�� ������ ��ū���� �����Ͽ� ���ڿ� �迭�� �����
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
			System.out.println(i + " ��° ��ū : " + token[i]);
		}
	}
}
