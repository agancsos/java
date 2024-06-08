package com.catalogger.services;
import com.catalogger.models.Author;
import java.util.ArrayList;
import com.data.types.DataTable;
import com.data.types.DataRow;
import com.data.types.DataColumn;

public class AuthorService {
	private static AuthorService instance = null;
	private CacheService cacheService     = null;
	private DataService dbService         = null;

	private AuthorService() {
		this.cacheService = CacheService.getInstance();
		this.dbService    = DataService.getInstance();
	}

	public static AuthorService getInstance() {
		if (AuthorService.instance == null) {
			AuthorService.instance = new AuthorService();
		}
		return AuthorService.instance;
	}

	public boolean addAuthor(Author author) {
		DataTable tab = this.dbService.serviceQuery(String.format("SELECT * FROM AUTHORS WHERE AUTHOR_FIRSTNAME = '%s' AND AUTHOR_LASTNAME = '%s'",
			author.getFirstName(), author.getLastName()));
		if (tab.getRows().size() > 0) {
			return true;
		}
		if (this.dbService.runServiceQuery(String.format("INSERT INTO AUTHORS (AUTHOR_FIRSTNAME, AUTHOR_LASTNAME, AUTHOR_BIOGRAPHY, LAST_UPDATED_DATE) VALUES ('%s', '%s', '%s', CURRENT_TIMESTAMP)",
			author.getFirstName(), author.getLastName(), author.getBiography()))) {
			this.cacheService.updateAuthorCache(author.getId(), author);
			return true;
		}
		return false;
	}

	public boolean modifyAuthor(Author author) {
		DataTable tab = this.dbService.serviceQuery(String.format("SELECT * FROM AUTHORS WHERE AUTHOR_ID = '%d'", author.getId()));
		if (tab.getRows().size() < 1) {
			return true;
		}
		if (this.dbService.runServiceQuery(String.format("UPDATE AUTHORS SET AUTHOR_FIRSTNAME = '%s', AUTHOR_LASTNAME = '%s', AUTHOR_BIOGRAPHY = '%s', LAST_UPDATED_DATE = CURRENT_TIMESTAMP WHERE AUTHOR_ID = '%d'",
			author.getFirstName(), author.getLastName(), author.getBiography(), author.getId()))) {
			this.cacheService.updateAuthorCache(author.getId(), author);
			return true;
		}
		return false;
	}

	public boolean removeAuthor(Author author) {
		if (this.dbService.runServiceQuery(String.format("DELETE FROM TITLE_HISTORY WHERE TITLE_COPY_ID IN (SELECT TITLE_COPY_ID FROM TITLE_COPY WHERE TITLE_ID IN (SELECT TITLE_ID FROM TITLES WHERE AUTHOR_ID = '%d'))", author.getId()))) {
			if (this.dbService.runServiceQuery(String.format("DELETE FROM TITLE_COPY WHERE TITLE_ID IN (SELECT TITLE_ID FROM TITLES WHERE AUTHOR_ID = '%d'", author.getId()))) {
				if (this.dbService.runServiceQuery(String.format("DELETE FROM TITLES WHERE AUTHOR_ID = '%d'", author.getId()))) {
					return this.dbService.runServiceQuery(String.format("DELETE FROM AUTHORS WHERE AUTHOR_ID = '%d'", author.getId()));
				}
			}
		}
		return false;	
	}

	public Author getAuthor(int id, boolean useCache) {
		if (useCache && this.cacheService.containsAuthor(id)) {
			return this.cacheService.getAuthor(id);
		}
		Author rst = null;
		DataTable tab = this.dbService.serviceQuery(String.format("SELECT * FROM AUTHORS WHERE AUTHOR_ID = '%d'", id));
		if (tab.getRows().size() > 0) {
			DataRow row = tab.getRows().get(0);
			rst = new Author();
			rst.setId(id);
			rst.setFirstName(row.getColumn("author_firstname").getColumnValue());
			rst.setLastName(row.getColumn("author_lastname").getColumnValue());
			rst.setLabel(String.format("%s %s", rst.getFirstName(), rst.getLastName()));
			rst.setBiography(row.getColumn("author_biography").getColumnValue());
			rst.setLastUpdatedDate(row.getColumn("last_updated_date").getColumnValue());
			this.cacheService.updateAuthorCache(id, rst);
		}
		return rst;
	}

	public Author[] getAuthors(boolean useCache) {
		ArrayList<Author> rst = new ArrayList<Author>();
		DataTable tab = this.dbService.serviceQuery("SELECT * FROM AUTHORS");
		for (DataRow row : tab.getRows()) {
			rst.add(this.getAuthor(Integer.parseInt(row.getColumn("author_id").getColumnValue()), useCache));
		}
		return rst.toArray(new Author[rst.size()]);
	}

	public Author searchByLabel(String label) {
		Author rst       = null;
		String[] comps   = label.split(" ");
		String firstName = "";
		String lastName  = "";
		if (comps.length > 0) {
			firstName = comps[0];
		}
		if (comps.length > 1) {
			lastName = comps[1];
		}
		DataTable tab = this.dbService.serviceQuery(String.format("SELECT AUTHOR_ID FROM AUTHORS WHERE AUTHOR_FIRSTNAME = '%s' AND AUTHOR_LASTNAME = '%s'", firstName, lastName));
		if (tab.getRows().size() < 1) {
			return rst;
		}
		rst = this.getAuthor(Integer.parseInt(tab.getRows().get(0).getColumn("author_id").getColumnValue()), false); 
		return rst;
	}
}

