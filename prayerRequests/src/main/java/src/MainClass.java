import com.requests.components.PrayerRequestsServer;
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

		PrayerRequestsServer server = new PrayerRequestsServer(configFile, springArgs.toArray(new String[springArgs.size()]));
		server.start();
	}
}

