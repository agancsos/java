package com.catalogger.services;
import java.util.ArrayList;
import com.catalogger.models.Message;
import com.data.types.DataTable;
import com.data.types.DataRow;
import com.data.types.DataColumn;

public class DbTraceService {
	private static DbTraceService instance = null;
	private DataService dbService          = null;
	private ConfigurationService config    = null;
	private int traceLevel                 = 3;

	private DbTraceService() {
		this.dbService  = DataService.getInstance();
		this.config     = ConfigurationService.getInstance("");
		this.traceLevel = (int)this.config.getProperty("traceLevel", 3); 
	}

	public static DbTraceService getInstance() {
		if (DbTraceService.instance == null) {
			DbTraceService.instance = new DbTraceService();
		}
		return DbTraceService.instance;
	}

	private void traceMessage(int level, int category, String msg) {
		this.traceLevel = (int)this.config.getProperty("traceLevel", 3);
		if (this.traceLevel >= level) {
			this.dbService.runServiceQuery(String.format("INSERT INTO MESSAGES (MESSAGE_TEXT, MESSAGE_LEVEL, MESSAGE_CATEGORY) values ('%s', '%d', '%d')", msg, level, category));
		}
	}

	public void traceError(int category, String msg) {
		this.traceMessage(1, category, msg);
	}

	public void traceWarning(int category, String msg) {
        this.traceMessage(2, category, msg);
    }

	public void traceInformational(int category, String msg) {
        this.traceMessage(3, category, msg);
    }

	public void traceVerbose(int category, String msg) {
        this.traceMessage(4, category, msg);
    }

	public void traceDebug(int category, String msg) {
        this.traceMessage(-999, category, msg);
    }

	public ArrayList<Message> getMessages() {
		ArrayList<Message> rst = new ArrayList<Message>();
		DataTable tab = this.dbService.serviceQuery(String.format("SELECT * FROM MESSAGES ORDER BY LAST_UPDATED_DATE DESC"));
		for (DataRow row : tab.getRows()) {
			Message temp = new Message();
			temp.setId(Integer.parseInt(row.getColumn("message_id").getColumnValue()));
			temp.setLevel(Integer.parseInt(row.getColumn("message_level").getColumnValue()));
			temp.setCategory(Integer.parseInt(row.getColumn("message_category").getColumnValue()));
			temp.setText(row.getColumn("message_text").getColumnValue());
			temp.setLastUpdatedDate(row.getColumn("last_updated_date").getColumnValue());
			rst.add(temp);
		}
		return rst;
	}

	public void purgeAudits(int days) {
		if (days < 1) { return; }
		this.dbService.runServiceQuery(String.format("DELETE FROM MESSAGES WHERE LAST_UPDATED_DATE < CAST((CURRENT_TIMESTAMP - %d) AS DATETIME)", days));
	}
}

