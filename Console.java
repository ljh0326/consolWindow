package consoleWindow;

import java.util.*;
import java.util.Scanner;

public class Console {

	static String[] argArr;      	         // �Է��� �Ű������� ������� ���ڿ��迭
	static LinkedList q = new LinkedList();  // ����ڰ� �Է��� ������ ������ ť(Queue) 
	static final int MAX_SIZE = 5;           // q(ť)�� ����� �� �ִ� ���� ����

	
	
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		String str = "  asfasdfadsf  ";
		
		while(true) {
			String prompt = ">>";
			System.out.print(prompt);
                 
//          1. ȭ�����κ��� ���δ����� �Է¹޴´�.
			String input = sc.nextLine();
			
		
//			1. �Է¹��� ������ �յ� ������ �����Ѵ�.
			//trim �� ������ �ٲ��� �ʴ´�.
			input = input.trim();
//          2. �Է¹��� ��ɶ����� ������ ������ �����ڷ� �ؼ� ������ argArr�� ��´�.
			argArr = input.split(" +");
			
			String command = argArr[0].trim();
			
			if("".equals(command))
				continue;
			
//            2. q �Ǵ� Q�� �Է��ϸ� ���������Ѵ�.
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
