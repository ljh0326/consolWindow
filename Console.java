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
			
			save(input);
//			1. �Է¹��� ������ �յ� ������ �����Ѵ�.
			//trim �� ������ �ٲ��� �ʴ´�.
			input = input.trim();
//          2. �Է¹��� ��ɶ����� ������ ������ �����ڷ� �ؼ� ������ argArr�� ��´�.
			argArr = input.split(" +");
			
			String command = argArr[0].trim();
			
			if("".equals(command))
				continue;
			
			command = command.toLowerCase();
			
//            2. q �Ǵ� Q�� �Է��ϸ� ���������Ѵ�.
			if(command.equals("q"))
				System.exit(0);
			else if(command.equals("history")) {
				history();
			} else {
				for(String arg : argArr) {
					System.out.println(arg);
				}
			}
		}
	}
	
	private static void save(String input) {
		if(input == null || "".equals(input))
			return;
		
		q.add(input);
		
		if(q.size() > MAX_SIZE)
			q.pollFirst();
		
		System.out.println(q);
	}

	private static void history() {
		int i = 0;
		
		while(i != 5) {
			System.out.println(q.get(i));
			i ++;
		}
		
		System.out.println(q);
	}


	
}
