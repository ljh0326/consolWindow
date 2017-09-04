package consoleWindow;

import java.io.*;
import java.util.*;
import java.util.regex.*;

public class Console {

	static String[] argArr; // 입력한 매개변수를 담기위한 문자열배열
	static LinkedList q = new LinkedList(); // 사용자가 입력한 내용을 저장할 큐(Queue)
	static final int MAX_SIZE = 5; // q(큐)에 저장될 수 있는 값의 개수

	static File curDir; // 현재 디렉토리

	static {
		// 시스템속성 "user.dir"값을 읽어서 File객체를 만들고, curDir에 할당
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

				// 1. 화면으로부터 라인단위로 입력받는다.
				String input = sc.nextLine();

				save(input);
				// 1. 입력받은 값에서 앞뒤 공백을 제거한다.
				// trim 은 원본을 바꾸지 않는다.
				input = input.trim();
				// 2. 입력받은 명령라인의 내용을 공백을 구분자로 해서 나눠서 argArr에 담는다.
				argArr = input.split(" +");

				String command = argArr[0].trim();

				if ("".equals(command))
					continue;

				command = command.toLowerCase();

				// 2. q 또는 Q를 입력하면 실행종료한다.
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
				System.out.println("입력오류 입니다.");
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
//		 1. 입력된 디렉토리(subDir)가 ".."이면,
//			1.1  현재 디렉토리의 조상 디렉토리를 얻어서 현재 디렉토리로 지정한다.
		if(subDir.equals(".."))
			 curDir = curDir.getParentFile();
//		 2. 입력된 디렉토리(subDir)가 "."이면,  단순히 현재 디렉토리의 경로를 화면에 출력한다.
		else if(subDir.equals("."))
			System.out.println(curDir.getPath());
		else {
			File f = new File(curDir,subDir);
			
			if(f.exists()) {
				curDir = f;
			}else {
				System.out.println("유효하지 않은 디렉토리");
			}
		}
//    3. 1 또는 2의 경우가 아니면, 입력된 디렉토리(subDir)가 현재 디렉토리의 하위디렉토리인지 확인한다.
//        3.1 확인결과가 true이면, 현재 디렉토리(curDir)을 입력된 디렉토리(subDir)로 변경한다.
//        3.2 확인결과가 false이면, "유효하지 않은 디렉토리입니다."고 화면에 출력한다.

	}

	// 명령어들을 q에 넣어주는 메서드
	private static void save(String input) {
		if (input == null || "".equals(input))
			return;

		q.add(input);

		if (q.size() > MAX_SIZE)
			q.pollFirst();

	}

	// 최근에 입력한 명령어를 보여주는 메서드
	private static void history() {
		int i = 0;

		ListIterator it = q.listIterator();

		while (it.hasNext()) {
			System.out.println(++i + "." + it.next());
		}

		System.out.println(q);
	}

	// 파일 목록을 보여주는 메서드
	private static void dir() {
		String pattern = "";

		switch (argArr.length) {
		case 1: // dir만 입력한 경우 현재 디렉토리의 모든 파일과 디렉토리를 보여준다.
			// 1. 반복문을 이용해서 현재디렉토리의 모든 파일의 목록을 출력한다.(File클래스의 listFiles()사용)
			for (File f : curDir.listFiles()) {
				if (f.isDirectory()) {
					System.out.println("[" + f.getName() + "]");
				} else {
					System.out.println(f.getName());
				}
			}
			break;
		case 2: // dir과 패턴을 같이 입력한 경우, 예를들면 dir *.class
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
			System.out.println(fileName + "존재하지 않는 파일입니다.");
		}
	}

	// 지정된 키워드를 지정된 파일에서 찾아 화면에 보여주는 메서드
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
			System.out.println(fileName + "존재하지 않는 파일입니다.");
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
