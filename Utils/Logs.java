package demo2.Utils;

public class Logs {
	/**
	 * 	对信息计数
	 */
	private static Long logCount = 0L;
	
	public static void logError(String error, String message) {
		addLog();
		System.out.append("[" + logCount + "](E)");
		System.out.append("[" + error + "]\t");
		System.out.println(message);
	}
	public static void logMessage(String classMessage, String message) {
		addLog();
		System.out.append("[" + logCount + "](S)");
		System.out.append("[" + classMessage + "]\t");
		System.out.println(message);
	}
	
	private static void addLog() {
		synchronized(logCount) {
			++logCount;
		}
	}
	
}
