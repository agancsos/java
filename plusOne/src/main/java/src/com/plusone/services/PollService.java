package com.plusone.services;
import com.plusone.models.Poll;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import com.data.types.DataTable;
import com.data.types.DataRow;
import com.data.types.DataColumn;

public class PollService {
	private ConfigurationService configService = null;
	private static PollService instance        = null;
	private DataService dataService            = null;

	private PollService() {
		this.configService = ConfigurationService.getInstance("");
		this.dataService   = DataService.getInstance();
		this.dataService.createSchema();
	}

	public static PollService getInstance() {
		if (PollService.instance == null) {
			PollService.instance = new PollService();
		}
		return PollService.instance;
	}

	public void addPoll(Poll poll) throws Exception {
		DataTable tab = this.dataService.serviceQuery(String.format("SELECT POLL_ID FROM POLLS WHERE POLL_QUESTION = '%s'", poll.getText()));
        if (tab.getRows().size() == 0) {
			this.dataService.runServiceQuery(String.format("INSERT INTO POLLS (POLL_QUESTION, CREATED_DATE) VALUES ('%s', CURRENT_TIMESTAMP)",
				poll.getText()
			));
			tab = this.dataService.serviceQuery(String.format("SELECT POLL_ID FROM POLLS WHERE POLL_QUESTION = '%s'", poll.getText()));
			if (tab.getRows().size() > 0) {
				int pollId = Integer.parseInt(tab.getRows().get(0).getColumn("poll_id").getColumnValue());
				for (Map.Entry<String, String> pair : poll.getOptions().entrySet()) {
					this.dataService.runServiceQuery(String.format("INSERT INTO OPTIONS (OPTION_TEXT) VALUES ('%s')", pair.getValue()));
					DataTable tab2 = this.dataService.serviceQuery(String.format("SELECT OPTION_ID FROM OPTIONS WHERE OPTION_TEXT = '%s'", pair.getValue()));
					if (tab2.getRows().size() > 0) {
						int optionId = Integer.parseInt(tab2.getRows().get(0).getColumn("option_id").getColumnValue());
						this.dataService.runServiceQuery(String.format("INSERT INTO POLL_OPTIONS (POLL_ID, OPTION_ID, CREATED_DATE) VALUES ('%d', '%d', CURRENT_TIMESTAMP)",
							pollId, optionId));
					}
				}
			}
		}
	}

	public Poll getPoll(int id) {
		Poll rst = new Poll();
		DataTable tab = this.dataService.serviceQuery(String.format("SELECT * FROM POLLS WHERE POLL_ID = '%d'", id));
		if (tab.getRows().size() > 0) {
			DataRow row = tab.getRows().get(0);
			rst.setPollId(id);
			rst.setText(row.getColumn("poll_question").getColumnValue());
			rst.setCreatedDate(row.getColumn("created_date").getColumnValue());
			rst.setLastUpdatedDate(row.getColumn("last_updated_date").getColumnValue());
			HashMap<String, String> options = new HashMap<String, String>();
			DataTable tab2 = this.dataService.serviceQuery(String.format("SELECT * FROM OPTIONS WHERE OPTION_ID IN (SELECT OPTION_ID FROM POLL_OPTIONS WHERE POLL_ID = '%d')", id));
			for (DataRow row2 : tab2.getRows()) {
				options.put(row2.getColumn("option_id").getColumnValue(), row2.getColumn("option_text").getColumnValue());
			}
			rst.setOptions(options);
		}
		return rst;
	}

	public Poll[] listPolls() {
		ArrayList<Poll> rst = new ArrayList<Poll>();
		DataTable tab = this.dataService.serviceQuery(String.format("SELECT POLL_ID FROM POLLS"));
		for (DataRow row : tab.getRows()) {
			int id = Integer.parseInt(row.getColumn("poll_id").getColumnValue());
			rst.add(this.getPoll(id));
		}
		return rst.toArray(new Poll[rst.size()]);
	}
}

