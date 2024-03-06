import com.cacheMoney.components.CacheMoneyServer;
import java.util.ArrayList;

public class MainClass {
	public static void main(String[] args) {
		String configFile = "./config.json";
		ArrayList<String> springArgs = new ArrayList<String>();

		for (int i = 0; i < args.length - 1; i++) {
			if (args[i].equals("-f")) {
				configFile = args[i + 1];
			} else {
				springArgs.add(args[i]);
			}
		}

		CacheMoneyServer server = new CacheMoneyServer(configFile, springArgs.toArray(new String[springArgs.size()]));
		server.start();
	}
}

