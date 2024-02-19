package com.data;
import com.data.types.DataTable;

public interface IDataConnection {
	public String[] getColumnNames(String sql);
	public boolean runQuery(String sql);
	public DataTable query(String sql);
}
