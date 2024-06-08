package com.catalogger.services;
import com.catalogger.models.Title;
import com.catalogger.models.Publisher;
import com.catalogger.models.Author;
import com.catalogger.models.User;
import com.catalogger.models.TitleCopy;
import java.util.ArrayList;
import com.data.types.DataTable;
import com.data.types.DataRow;
import com.data.types.DataColumn;

public class TitleService {
	private static TitleService instance     = null;
	private CacheService cacheService        = null;
	private DataService dbService            = null;
	private AuthorService authorService      = null;
	private PublisherService pubService      = null;

	private TitleService() {
		this.cacheService  = CacheService.getInstance();
		this.dbService     = DataService.getInstance();
		this.authorService = AuthorService.getInstance();
		this.pubService    = PublisherService.getInstance();
	}

	public static TitleService getInstance() {
		if (TitleService.instance == null) {
			TitleService.instance = new TitleService();
		}
		return TitleService.instance;
	}

	public boolean addTitle(Title title) {
		DataTable tab = this.dbService.serviceQuery(String.format("SELECT * FROM TITLES WHERE TITLE_NAME = '%s'", title.getLabel()));
		if (tab.getRows().size() > 0) {
			return true;
		}
		if (this.dbService.runServiceQuery(String.format("INSERT INTO TITLES (TITLE_NAME, TITLE_GENRE, MEDIA_TYPE, AUTHOR_ID, PUBLISHER_ID, " +
			"TITLE_DESCRIPTION, TITLE_ROW, TITLE_SHELF, TITLE_COLUMN, LAST_UPDATED_DATE, USER_ID) VALUES ('%s', '%s', '%d', '%d', '%d', '%s', " + 
			"'%s', '%s', '%s', CURRENT_TIMESTAMP, '%d')", title.getLabel(), title.getGenre(), title.getMediaType(), title.getAuthor().getId(), title.getPublisher().getId(),
			title.getDescription(), title.getRow(), title.getShelf(), title.getColumn(), title.getLastUpdatedBy().getId()))) {
			this.cacheService.updateTitleCache(title.getId(), title);
			return true;
		}
		return false;
	}

	public boolean modifyTitle(Title title) {
		DataTable tab = this.dbService.serviceQuery(String.format("SELECT * FROM TITLES WHERE TITLE_ID = '%d'", title.getId()));
		if (tab.getRows().size() < 1) {
			return true;
		}
		if (this.dbService.runServiceQuery(String.format("UPDATE TITLES SET TITLE_NAME = '%s', TITLE_GENRE = '%s', MEDIA_TYPE = '%d', " +
			"TITLE_SERIAL = '%s', AUTHOR_ID='%d', PUBLISHER_ID = '%d', TITLE_DESCRIPTION = '%s', USER_ID = '%d', TITLE_ROW = '%s', " +
			"TITLE_SHELF = '%s', TITLE_COLUMN = '%s', LAST_UPDATED_DATE = CURRENT_TIMESTAMP WHERE TITLE_ID = '%d'",
        	title.getLabel(), title.getGenre(), title.getMediaType(), title.getSerial(), title.getAuthor().getId(), title.getPublisher().getId(),
			title.getDescription(), title.getLastUpdatedBy().getId(), title.getRow(), title.getShelf(), title.getColumn(), title.getId()))) {
			this.cacheService.updateTitleCache(title.getId(), title);
			return true;
		}
		return false;
	}

	public boolean removeTitle(Title title) {
		if (this.dbService.runServiceQuery(String.format("DELETE FROM TITLE_HISTORY WHERE TITLE_COPY_ID IN (SELECT TITLE_COPY_ID FROM TITLE_COPY WHERE TITLE_ID = '%d')", title.getId()))) {
			if (this.dbService.runServiceQuery(String.format("DELETE FROM TITLE_COPY WHERE TITLE_ID = '%d'", title.getId()))) {
				return this.dbService.runServiceQuery(String.format("DELETE FROM TITLES WHERE TITLE_ID = '%d'", title.getId()));
			}
		}
		return false;	
	}

	public Title getTitle(int id, boolean useCache) {
		if (useCache && this.cacheService.containsTitle(id)) {
			return this.cacheService.getTitle(id);
		}
		Title rst = null;
		DataTable tab = this.dbService.serviceQuery(String.format("SELECT * FROM TITLES WHERE TITLE_ID = '%d'", id));
		if (tab.getRows().size() > 0) {
			DataRow row = tab.getRows().get(0);
			rst = new Title();
			rst.setId(id);
			rst.setLabel(String.format("%s", row.getColumn("title_name").getColumnValue()));
			rst.setMediaType(Integer.parseInt(row.getColumn("media_type").getColumnValue()));
			rst.setState(Integer.parseInt(row.getColumn("title_state").getColumnValue()));
			rst.setSerial(row.getColumn("title_serial").getColumnValue());
			rst.setShelf(row.getColumn("title_shelf").getColumnValue());
			rst.setRow(row.getColumn("title_row").getColumnValue());
			rst.setColumn(row.getColumn("title_column").getColumnValue());
			rst.setDescription(row.getColumn("title_description").getColumnValue());
			rst.setLastUpdatedDate(row.getColumn("last_updated_date").getColumnValue());
			rst.setAuthor(this.authorService.getAuthor(Integer.parseInt(row.getColumn("author_id").getColumnValue()), false));
			rst.setPublisher(this.pubService.getPublisher(Integer.parseInt(row.getColumn("publisher_id").getColumnValue()), false));
			rst.setLastUpdatedBy(new User());
			rst.getLastUpdatedBy().setId(Integer.parseInt(row.getColumn("user_id").getColumnValue()));
			this.cacheService.updateTitleCache(id, rst);
		}
		return rst;
	}

	public Title getTitleWithCopies(int id, boolean useCache) {
		if (useCache && this.cacheService.containsTitle(id)) {
			return this.cacheService.getTitle(id);
		}
		Title rst = null;
		DataTable tab = this.dbService.serviceQuery(String.format("SELECT * FROM TITLES WHERE TITLE_ID = '%d'", id));
		if (tab.getRows().size() > 0) {
			rst = this.getTitle(id, false);
			ArrayList<TitleCopy> copies = this.getTitleCopies(rst);
			rst.setCopies(copies);
		}
		return rst;
	}

	public Title isbnLookup(String isbn) {
        Title rst     = null;
		DataTable tab = this.dbService.serviceQuery(String.format("SELECT * FROM TITLES WHERE TITLE_SERIAL = '%s'", isbn));
		if (tab.getRows().size() > 0) {
			DataRow row = tab.getRows().get(0);
			rst = this.getTitle(Integer.parseInt(row.getColumn("title_id").getColumnValue()), false);
		}
		return rst;
    }

	public Title[] getTitles(boolean useCache) {
		ArrayList<Title> rst = new ArrayList<Title>();
		DataTable tab = this.dbService.serviceQuery("SELECT * FROM TITLES");
		for (DataRow row : tab.getRows()) {
			rst.add(this.getTitleWithCopies(Integer.parseInt(row.getColumn("title_id").getColumnValue()), useCache));
		}
		return rst.toArray(new Title[rst.size()]);
	}

	public boolean addTitleCopy(Title title) {
		return this.dbService.runServiceQuery(String.format("INSERT INTO TITLE_COPY (TITLE_ID) VALUES ('%d')", title.getId()));
	}
	
	public boolean removeTitleCopy(TitleCopy tc) {
		if (this.dbService.runServiceQuery(String.format("DELETE FROM TITLE_HISTORY WHERE TITLE_COPY_ID = '%d'", tc.getId()))) {
			return this.dbService.runServiceQuery(String.format("DELETE FROM TITLE_COPY WHERE TITLE_COPY_ID = '%d'", tc.getId()));
		}
		return false;
	}	

	public TitleCopy getTitleCopy(int id) {
		TitleCopy rst = null;
		DataTable tab = this.dbService.serviceQuery(String.format("SELECT * FROM TITLE_COPY WHERE TITLE_COPY_ID = '%d'", id));
		if (tab.getRows().size() > 0) {
			DataRow row = tab.getRows().get(0);
			rst = new TitleCopy();
			rst.setId(id);
			rst.setTitle(this.getTitle(Integer.parseInt(row.getColumn("title_id").getColumnValue()), false));
			rst.setState(Integer.parseInt(row.getColumn("title_copy_state").getColumnValue()));
			rst.setLastUpdatedBy(new User());
			rst.getLastUpdatedBy().setId(Integer.parseInt(row.getColumn("user_id").getColumnValue()));
		}
		return rst;
	}

	public ArrayList<TitleCopy> getTitleCopies(Title title) {
		ArrayList<TitleCopy> rst = new ArrayList<TitleCopy>();
        DataTable tab = this.dbService.serviceQuery("SELECT * FROM TITLE_COPY WHERE TITLE_ID = '%d'");
        for (DataRow row : tab.getRows()) {
            rst.add(this.getTitleCopy(Integer.parseInt(row.getColumn("title_id").getColumnValue())));
        }
		return rst;
	}
}

