package consoleWindow;

import java.io.*;
import java.util.*;
import java.util.regex.*;

public class Console {

	static String[] argArr; // �Է��� �Ű������� ������� ���ڿ��迭
	static LinkedList q = new LinkedList(); // ����ڰ� �Է��� ������ ������ ť(Queue)
	static final int MAX_SIZE = 5; // q(ť)�� ����� �� �ִ� ���� ����

	static File curDir; // ���� ���丮

	static {
		// �ý��ۼӼ� "user.dir"���� �о File��ü�� �����, curDir�� �Ҵ�
		try {
			File file = new File(System.getProperty("user.dir"));
			curDir = file;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);

		while (true) {
			try {
				String prompt = curDir.getCanonicalPath() + ">>";
				System.out.print(prompt);

				// 1. ȭ�����κ��� ���δ����� �Է¹޴´�.
				String input = sc.nextLine();

				save(input);
				// 1. �Է¹��� ������ �յ� ������ �����Ѵ�.
				// trim �� ������ �ٲ��� �ʴ´�.
				input = input.trim();
				// 2. �Է¹��� ��ɶ����� ������ ������ �����ڷ� �ؼ� ������ argArr�� ��´�.
				argArr = input.split(" +");

				String command = argArr[0].trim();

				if ("".equals(command))
					continue;

				command = command.toLowerCase();

				// 2. q �Ǵ� Q�� �Է��ϸ� ���������Ѵ�.
				if (command.equals("q"))
					System.exit(0);
				else if (command.equals("history")) {
					history();
				} else if (command.equals("dir")) {
					dir();
				} else if(command.equals("type")) {
					type();
				} else {
					for (String arg : argArr) {
						System.out.println(arg);
					}
				}
			} catch (Exception e) {
				System.out.println("�Է¿��� �Դϴ�.");
			}
		}
	}

	private static void save(String input) {
		if (input == null || "".equals(input))
			return;

		q.add(input);

		if (q.size() > MAX_SIZE)
			q.pollFirst();

		System.out.println(q);
	}

	private static void history() {
		int i = 0;

		ListIterator it = q.listIterator();

		while (it.hasNext()) {
			System.out.println(++i + "." + it.next());
		}

		System.out.println(q);
	}

	private static void dir() {
		String pattern = "";

		switch (argArr.length) {
		case 1: // dir�� �Է��� ��� ���� ���丮�� ��� ���ϰ� ���丮�� �����ش�.
			// 1. �ݺ����� �̿��ؼ� ������丮�� ��� ������ ����� ����Ѵ�.(FileŬ������ listFiles()���)
			for (File f : curDir.listFiles()) {
				if (curDir.isDirectory()) {
					System.out.println("[" + f.getName() + "]");
				} else {
					System.out.println(f.getName());
				}
			}
			break;
		case 2: // dir�� ������ ���� �Է��� ���, ������� dir *.class
			pattern = argArr[1];
			pattern = pattern.toUpperCase();
			pattern = pattern.replace(".", "\\.");
			pattern = pattern.replace("*", ".*");
			pattern = pattern.replace("?", ".{1}");

			Pattern p = Pattern.compile(pattern);

			for (File f : curDir.listFiles()) {
				String tmp = f.getName().toUpperCase();
				Matcher m = p.matcher(tmp);

				if (m.matches()) {
					if (f.isDirectory()) {
						System.out.println("[" + f.getName() + "]");
					} else {
						System.out.println(f.getName());
					}
				}
			}
			break;
		default:
			System.out.println("USAGE: dir [FILENAME]");
		}
	}

}
