package classes;

public class Helpers {
	public static String padLeft(String str, int len, String pad) {
		String rst = "";
		for (int i = str.length(); i < len; i++) {
			rst += pad;
		}
		return rst + str;
	}

	public static String padRight(String str, int len, String pad) {
		String rst = "";
		for (int i = str.length(); i < len; i++) {
			rst += pad;
		}
		return str + rst;
	}

	public static void helpMenu() {
	}
}

