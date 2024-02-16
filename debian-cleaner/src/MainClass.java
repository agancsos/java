import classes.Helpers;
import cleaner.CleanerService;
class MainClass {
	public static void main(String[] args){
		String githubURL     = System.getenv().get("GITHUB_URL");
		String searchPattern = System.getenv().get("SEARCH_PATTERN");
		String outputPath    = System.getenv().get("GIT_OUTPUT_PATH");
		String gitPAT        = System.getenv().get("GIT_PAT");
		Boolean isDryRun     = true;

		for(int i = 0; i < args.length; i++){
			if (args[i].equals("--dry")) {
				isDryRun = Integer.parseInt(args[i + 1]) > 0;
			} else if (args[i].equals("--pattern")) {
				searchPattern = args[i + 1];
			} else if (args[i].equals("--repo")) {
				githubURL = args[i + 1];
			} else if (args[i].equals("-o")) {
				outputPath = args[i + 1];
			} else if (args[i].equals("--pat")) {
				gitPAT = args[i + 1];
			}
		}

		if ((System.getenv().get("DRY_RUN") != null && !System.getenv().get("DRY_RUN").equals("") && Integer.parseInt(System.getenv().get("DRY_RUN")) < 1)) {
			isDryRun = false;
		}
		if (outputPath == null || outputPath.equals("")) {
			outputPath = ".";
		}

		System.out.println(Helpers.padRight("", 80, "#"));
		System.out.println(Helpers.padRight(String.format("# GITHUB URL  : %s", githubURL), 79, " ") + "#");
		System.out.println(Helpers.padRight(String.format("# OUTPUT PATH : %s", outputPath), 79, " ") + "#");
		System.out.println(Helpers.padRight(String.format("# PATTERN     : %s", searchPattern), 79, " ") + "#");
		System.out.println(Helpers.padRight(String.format("# DRYRUN      : %s", isDryRun), 79, " ") + "#");
		System.out.println(Helpers.padRight("", 80, "#"));

		CleanerService service = new CleanerService(githubURL, outputPath, searchPattern, isDryRun, gitPAT);
		try {
			service.invoke();
			System.out.println("\033[32mDone!\033[m");
		} catch (Exception ex) {
			System.out.println(String.format("\033[33m%s\033[m", ex.getMessage()));
			ex.printStackTrace();
		}
	}	
}
