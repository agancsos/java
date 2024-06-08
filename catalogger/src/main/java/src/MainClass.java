import com.catalogger.components.CataloggerServer;
import java.util.ArrayList;

public class MainClass {
	public static void main(String[] args) {
		ArrayList<String> springArgs = new ArrayList<String>();

		for (int i = 0; i < args.length - 1; i++) {
			if (args[i].equals("-f")) {
			} else {
				springArgs.add(args[i]);
			}
		}

		CataloggerServer server = new CataloggerServer(springArgs.toArray(new String[springArgs.size()]));
		server.start();
	}
}

