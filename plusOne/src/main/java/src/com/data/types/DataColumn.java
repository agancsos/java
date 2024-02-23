package com.data.types;

public class DataColumn {
	private String columnName;
	private String columnType;
	private String columnValue;

	public DataColumn() {
		this.columnName = null;
		this.columnType = null;
		this.columnValue = null;
	}

	public DataColumn(String name, String type, String value) {
		this.columnName = name;
        this.columnType = type;
        this.columnValue = value;
	}

	public DataColumn(String name, String value) {
        this.columnName = name;
        this.columnType = "";
        this.columnValue = value;
    }

	public void setColumnName(String name) { this.columnName = name; }
	public void setColumnType(String type) { this.columnType = type; }
	public void setColumnValue(String value) { this.columnValue = value; }
	public String getColumnName() { return this.columnName; }
	public String getColumnType() { return this.columnType; }
	public String getColumnValue() { return this.columnValue; }
}
