package com.catalogger.models;
import org.json.JSONObject;
import java.util.ArrayList;

public class Title extends Item {
	private int id                       = -1;
    private String label                 = "";
	private String genre                 = "";
	private int mediaType                = 0;
	private Author author                = new Author();
	private String serial                = "";
	private Publisher publisher          = new Publisher();
	private String description           = "";
	private User lastUpdatedBy           = new User();
    private String createdDate           = "";
    private String lastUpdatedDate       = "";
	private String shelf                 = "";
	private int state                    = 0;
	private String row                   = "";
	private String column                = "";
	private ArrayList<TitleCopy> copies  = new ArrayList<TitleCopy>();

	public Title() {
	}

	public void reloadFromJson(String raw) {
		JSONObject dict = new JSONObject(raw);
		if (dict.has("id")) {
			this.id = dict.getInt("id");
		}
		if (dict.has("label")) {
			this.label = dict.getString("label");
		}
		if (dict.has("genre")) {
			this.genre = dict.getString("genre");
		}
		if (dict.has("mediaType")) {
			this.mediaType = dict.getInt("mediaType");
		}
		if (dict.has("author")) {
			this.author = new Author();
			this.author.setId(dict.getInt("author"));
		}
		if (dict.has("serial")) {
			this.serial = dict.getString("serial");
		}
		if (dict.has("publisher")) {
			this.publisher = new Publisher();
			this.publisher.setId(dict.getInt("publisher"));
		}
		if (dict.has("description")) {
			this.description = dict.getString("description");
		}
		if (dict.has("lastUpdatedBy")) {
			this.lastUpdatedBy = new User();
			this.lastUpdatedBy.setId(dict.getInt("lastUpdatedBy"));
		}
		if (dict.has("createdDate")) {
			this.createdDate = dict.getString("createdDate");
		}
		if (dict.has("lastUpdatedDate")) {
			this.lastUpdatedDate = dict.getString("lastUpdatedDate");
		}
		if (dict.has("shelf")) {
			this.shelf = dict.getString("shelf");
		}
		if (dict.has("row")) {
			this.row = dict.getString("row");
		}
		if (dict.has("column")) {
			this.column = dict.getString("column");
		}
	}

	public String toJsonString() {
		String rst = "{";
        rst += String.format("\"@type\": %d", ObjectType.getOrdinal(ObjectType.TITLE));
        rst += String.format(",\"id\": %d", this.id);
        rst += String.format(",\"label\": \"%s\"", this.label);
        rst += String.format(",\"createdDate\": \"%s\"", this.createdDate);
        rst += String.format(",\"lastUpdatedDate\": \"%s\"", this.lastUpdatedDate);
        rst += String.format(",\"state\": %d", this.state);
        rst += String.format(",\"lastUpdatedBy\": %d", this.lastUpdatedBy.getId());
		rst += String.format(",\"mediaType\": %d", this.mediaType);
		rst += String.format(",\"author\": %d", this.author.getId());
		rst += String.format(",\"publisher\": %d", this.publisher.getId());
		rst += String.format(",\"genre\": \"%s\"", this.genre);
		rst += String.format(",\"description\": \"%s\"", this.description.replace("\n", "\\n"));
		rst += String.format(",\"shelf\": \"%s\"", this.shelf);
		rst += String.format(",\"row\": \"%s\"", this.row);
		rst += String.format(",\"column\": \"%s\"", this.column);
		rst += String.format(",\"copies\": [");
		for (int i = 0; i < this.copies.size(); i++) {
			if (i > 0) { rst += ","; }
			rst += this.copies.get(i).toJsonString();
		}
		rst += "]";
        rst += "}";
        return rst;	
	}

	public int getId() { return this.id; }
	public String getLabel() { return this.label; }
	public String getCreatedDate() { return this.createdDate; }
	public String getLastUpdatedDate() { return this.lastUpdatedDate; }
	public int getState() { return this.state; }
	public User getLastUpdatedBy() { return this.lastUpdatedBy; }
	public int getMediaType() { return this.mediaType; }
	public String getSerial() { return this.serial; }
	public Author getAuthor() { return this.author; }
	public Publisher getPublisher() { return this.publisher; }
	public String getGenre() { return this.genre; }
	public String getDescription() { return this.description; }
	public String getShelf() { return this.shelf; }
	public String getRow() { return this.row; }
	public String getColumn() { return this.column; }
	public ArrayList<TitleCopy> getCopies() { return this.copies; }

	public void setId(int a) { this.id = a; }
	public void setLabel(String a) { this.label = a; }
	public void setCreatedDate(String a) { this.createdDate = a; }
	public void setLastUpdatedDate(String a) { this.lastUpdatedDate = a; }
	public void setState(int a) { this.state = a; }
	public void setLastUpdatedBy(User a) { this.lastUpdatedBy = a; }
	public void setMediaType(int a) { this.mediaType = a; }
	public void setSerial(String a) { this.serial = a; }
	public void setAuthor(Author a) { this.author = a; }
	public void setPublisher(Publisher a) { this.publisher = a; }
	public void setGenre(String a) { this.genre = a; }
	public void setDescription(String a) { this.description = a; }
	public void setShelf(String a) { this.shelf = a; }
	public void setRow(String a) { this.row = a; }
	public void setColumn(String a) { this.column = a; }
	public void setCopies(ArrayList<TitleCopy> a) { this.copies = a; }	
	public int getObjectType() { return ObjectType.getOrdinal(ObjectType.TITLE); }
}

