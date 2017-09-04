package consoleWindow;

import java.util.*;
import java.util.Scanner;

public class Console {

	static String[] argArr;      	         // 입력한 매개변수를 담기위한 문자열배열
	static LinkedList q = new LinkedList();  // 사용자가 입력한 내용을 저장할 큐(Queue) 
	static final int MAX_SIZE = 5;           // q(큐)에 저장될 수 있는 값의 개수

	
	
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		String str = "  asfasdfadsf  ";
		
		while(true) {
			String prompt = ">>";
			System.out.print(prompt);
                 
//          1. 화면으로부터 라인단위로 입력받는다.
			String input = sc.nextLine();
			
		
//			1. 입력받은 값에서 앞뒤 공백을 제거한다.
			//trim 은 원본을 바꾸지 않는다.
			input = input.trim();
//          2. 입력받은 명령라인의 내용을 공백을 구분자로 해서 나눠서 argArr에 담는다.
			argArr = input.split(" +");
			
			String command = argArr[0].trim();
			
			if("".equals(command))
				continue;
			
//            2. q 또는 Q를 입력하면 실행종료한다.
			if(input.equalsIgnoreCase("q"))
				System.exit(0);
			else {
				for(String arg : argArr) {
					System.out.println(arg);
				}
			}
		}
	}
	
}
