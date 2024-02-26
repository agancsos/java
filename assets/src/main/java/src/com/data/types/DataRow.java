package com.data.types;
import java.util.ArrayList;

public class DataRow {
	private ArrayList<DataColumn> columns;
	
	public DataRow() {
		this.columns = new ArrayList<DataColumn>();
	}

	public DataRow(ArrayList<DataColumn> columns) {
		this.columns = columns;
	}

	public boolean contains(String name) {
		for (DataColumn column : this.columns) {
			if (column.getColumnName().equals(name)) {
				return true;
			}
		}
		return false;
	}

	public void addColumn(DataColumn column) {
		if (!this.contains(column.getColumnName())) {
			this.columns.add(column);
		}
	}

	public DataColumn getColumn(String name) {
		for (DataColumn column : this.columns) {
            if (column.getColumnName().equals(name)) {
                return column;
            }
        }
		return new DataColumn();
	}	

	public ArrayList<DataColumn> getColumns() { return this.columns; }
}	
