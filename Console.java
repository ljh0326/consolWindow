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
				} else if (command.equals("type")) {
					type();
				} else if (command.equals("find")) {
					find();
				} else if (command.equals("find2")) {
					find2();
				} else if (command.equals("cd")) {
					cd();
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

	private static void cd() {
		
		if(argArr.length == 1) {
			System.out.println(curDir);
			return;
		} else if(argArr.length > 2) {
			System.out.println("USAGE : cd directory");
			return;
		}
		
		String subDir = argArr[1];
//		 1. �Էµ� ���丮(subDir)�� ".."�̸�,
//			1.1  ���� ���丮�� ���� ���丮�� �� ���� ���丮�� �����Ѵ�.
		if(subDir.equals(".."))
			 curDir = curDir.getParentFile();
//		 2. �Էµ� ���丮(subDir)�� "."�̸�,  �ܼ��� ���� ���丮�� ��θ� ȭ�鿡 ����Ѵ�.
		else if(subDir.equals("."))
			System.out.println(curDir.getPath());
		else {
			File f = new File(curDir,subDir);
			
			if(f.exists()) {
				curDir = f;
			}else {
				System.out.println("��ȿ���� ���� ���丮");
			}
		}
//    3. 1 �Ǵ� 2�� ��찡 �ƴϸ�, �Էµ� ���丮(subDir)�� ���� ���丮�� �������丮���� Ȯ���Ѵ�.
//        3.1 Ȯ�ΰ���� true�̸�, ���� ���丮(curDir)�� �Էµ� ���丮(subDir)�� �����Ѵ�.
//        3.2 Ȯ�ΰ���� false�̸�, "��ȿ���� ���� ���丮�Դϴ�."�� ȭ�鿡 ����Ѵ�.

	}

	// ��ɾ���� q�� �־��ִ� �޼���
	private static void save(String input) {
		if (input == null || "".equals(input))
			return;

		q.add(input);

		if (q.size() > MAX_SIZE)
			q.pollFirst();

	}

	// �ֱٿ� �Է��� ��ɾ �����ִ� �޼���
	private static void history() {
		int i = 0;

		ListIterator it = q.listIterator();

		while (it.hasNext()) {
			System.out.println(++i + "." + it.next());
		}

		System.out.println(q);
	}

	// ���� ����� �����ִ� �޼���
	private static void dir() {
		String pattern = "";

		switch (argArr.length) {
		case 1: // dir�� �Է��� ��� ���� ���丮�� ��� ���ϰ� ���丮�� �����ش�.
			// 1. �ݺ����� �̿��ؼ� ������丮�� ��� ������ ����� ����Ѵ�.(FileŬ������ listFiles()���)
			for (File f : curDir.listFiles()) {
				if (f.isDirectory()) {
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

	//
	private static void type() throws IOException {
		if (argArr.length != 2) {
			System.out.println("Usage : type FILE_NAME");
			return;
		}

		String fileName = argArr[1];

		File tmp = new File(fileName);

		if (tmp.exists()) {
			FileReader fr = new FileReader(tmp);
			BufferedReader br = new BufferedReader(fr);

			String line = "";
			for (int i = 0; (line = br.readLine()) != null; i++) {
				System.out.println(line);
			}

		} else {
			System.out.println(fileName + "�������� �ʴ� �����Դϴ�.");
		}
	}

	// ������ Ű���带 ������ ���Ͽ��� ã�� ȭ�鿡 �����ִ� �޼���
	private static void find() throws IOException {
		if (argArr.length != 3) {
			System.out.println("USAGE : find KEYWORD FILE_NAME");
			return;
		}

		String keyword = argArr[1];
		String fileName = argArr[2];

		File tmp = new File(fileName);

		if (tmp.exists()) {
			FileReader fr = new FileReader(tmp);
			BufferedReader br = new BufferedReader(fr);

			String line = "";
			for (int i = 0; (line = br.readLine()) != null; i++) {
				if (line.indexOf(keyword) != -1) {
					System.out.println(i + ": " + line);
				}
			}
		} else {
			System.out.println(fileName + "�������� �ʴ� �����Դϴ�.");
		}
	}

	private static void find2() throws IOException {
		if (argArr.length != 3) {
			System.out.println("USAGE : find2 KEYWORD FILE_NAME");
			return;
		}

		String keyword = argArr[1];
		String pattern = argArr[2];
		pattern = pattern.toUpperCase();
		pattern = pattern.replace(".", "\\.");
		pattern = pattern.replace("*", ".*");
		pattern = pattern.replace("?", ".{1}");

		Pattern p = Pattern.compile(pattern);

		for (File f : curDir.listFiles()) {
			String tmp = f.getName().toUpperCase();
			Matcher m = p.matcher(tmp);

			if (m.matches()) {
				if (f.isDirectory())
					continue;

				FileReader fr = new FileReader(f);
				BufferedReader br = new BufferedReader(fr);

				String line = "";

				System.out.println("--------------" + f.getName());
				for (int i = 0; (line = br.readLine()) != null; i++) {
					if (line.indexOf(keyword) != -1) {
						System.out.println(i + ": " + line);
					}
				}
			}
		}
	}

}
