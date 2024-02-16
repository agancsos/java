package cleaner;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PushCommand;
import java.util.Base64;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import java.nio.file.Paths;
import java.io.File;
import classes.SystemHelpers;

public class CleanerService {
	private String repoPath      = "";
	private String outputPath    = "";
	private String searchPattern = "";
	private Boolean dryRun       = true;
	private String localPath     = "";
	private String username      = "";
	private String password      = "";

	public CleanerService(String repo, String output, String pattern, Boolean dry, String pat) {
		this.repoPath      = repo;
		this.outputPath    = output;
		this.searchPattern = pattern;
		this.dryRun        = dry;
		if (!this.dryRun) {
			this.localPath     = String.format("%s/%s", this.outputPath, Paths.get(this.repoPath).toFile().getName().replace(".git", ""));
		}
		if (pat != null && !pat.equals("")) {
			String raw     = new String(Base64.getDecoder().decode(pat));
			String[] comps = raw.split(":");
			if (comps.length > 0) {
				this.username = comps[0];
			}
			if (comps.length > 1) {
				this.password = comps[1];
			} 
		}
	}

	private void cloneRepository() throws Exception {
		if (!this.dryRun) {
			Git.cloneRepository().setURI(this.repoPath).setDirectory(Paths.get(this.localPath).toFile()).call();
		}
	}

	private void sanitize(String path) throws Exception {
		File file = Paths.get(path).toFile();
		if (file.getName().equals(".git") ||
			file.getName().contains(".jar") ||
			file.getName().contains(".class") ||
			file.getName().contains(".db")) {
			return;
		}
		if (file.isDirectory()) {
			File[] files = file.listFiles();
			for (File f : files) {
				this.sanitize(f.getPath());
			}
		} else {
			System.out.println(String.format("\033[35mCleaning: %s\033[m", file.getPath()));
			String current = SystemHelpers.readFile(file.getPath());
			current        = current.replaceAll(this.searchPattern, "");
			if (!this.dryRun) {
				SystemHelpers.writeFile(file.getPath(), current);
			} else {
				System.out.println(current);
			}
		}
	}

	private void pushChanges() throws Exception {
		if (!this.dryRun) {
			System.out.println("Pushing...");
			Git repo             = Git.open(Paths.get(this.localPath).toFile());
			repo.commit().setMessage("Clean").call();
			PushCommand push     = repo.push();
			push.setCredentialsProvider(new UsernamePasswordCredentialsProvider(this.username, this.password)); 
			push.call();
		}
	}

	private void clean(String path) throws Exception {
		if (!this.dryRun) {
			File file = Paths.get(path).toFile();
			if (file.isDirectory()) {
				File[] files = file.listFiles();
				for (File f : files) {
					this.clean(f.getPath());
				}
				file.delete();
			} else {
				file.delete();
			}
		}
	}

	public void invoke() throws Exception {
		this.cloneRepository();
		this.sanitize(this.localPath);
		this.pushChanges();
		this.clean(this.localPath);
	}
}

