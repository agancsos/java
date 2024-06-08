package com.data.types;
import java.util.ArrayList;

public class DataTable {
	private ArrayList<DataRow> rows;

	public DataTable() {
		this.rows = new ArrayList<DataRow>();
	}

	public void addRow(DataRow row) { this.rows.add(row); }
	public ArrayList<DataRow> getRows() { return this.rows; }
}
