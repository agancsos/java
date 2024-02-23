package com.plusone.services;
import com.plusone.models.Vote;
import java.util.ArrayList;
import com.data.types.DataTable;
import com.data.types.DataRow;
import com.data.types.DataColumn;

public class VoteService {
	private ConfigurationService configService = null;
	private static VoteService instance        = null;
	private MessageService msgService          = null;
	private DataService dataService            = null;

	private VoteService() {
		this.configService = ConfigurationService.getInstance("");
		this.msgService    = MessageService.getInstance();
		this.dataService   = DataService.getInstance();
		this.dataService.createSchema();
	}

	public static VoteService getInstance() {
		if (VoteService.instance == null) {
			VoteService.instance = new VoteService();
		}
		return VoteService.instance;
	}

	public void addVote(Vote vote) throws Exception {
		this.dataService.runServiceQuery(String.format("INSERT INTO VOTES (POLL_ID, OPTION_ID, VOTE_SOURCE_IP, CREATED_DATE, VOTE_PROCESSED) VALUES ('%d', '%d', '%s', CURRENT_TIMESTAMP, '%d')",
			vote.getPollId(),
			vote.getOptionId(),
			vote.getSourceIp(),
			(vote.getProcessed() ? 1 : 0)
		));
	}

	public void pushVote(Vote vote) throws Exception {
		if (!(Boolean)this.configService.getProperty("mq.disabled", false) && this.msgService.isReachable(false)) {
			this.msgService.putMessagege((String)this.configService.getProperty("mq.queue", ""), vote.toJsonString());
		} else {
			this.addVote(vote);
		}
	}

	public Vote getVote(int id) {
		Vote rst = new Vote();
		DataTable tab = this.dataService.serviceQuery(String.format("SELECT * FROM VOTE WHERE VOTE_ID = '%d'", id));
		if (tab.getRows().size() > 0) {
			DataRow row = tab.getRows().get(0);
			rst.setVoteId(id);
			rst.setSourceIp(row.getColumn("vote_source_ip").getColumnValue());
			rst.setCreatedDate(row.getColumn("created_date").getColumnValue());
			rst.setLastUpdatedDate(row.getColumn("last_updated_date").getColumnValue());
			rst.setProcessed((Integer.parseInt(row.getColumn("vote_processed").getColumnValue()) > 0 ? true : false));
			rst.setPollId(Integer.parseInt(row.getColumn("poll_id").getColumnValue()));
			rst.setOptionId(Integer.parseInt(row.getColumn("option_id").getColumnValue()));
		}
		return rst;
	}

	public Vote[] listVotes(int pollId) {
		ArrayList<Vote> rst = new ArrayList<Vote>();
		DataTable tab = this.dataService.serviceQuery(String.format("SELECT VOTE_ID FROM VOTES WHERE VOTE_POLL_ID = '%d'", pollId));
		for (DataRow row : tab.getRows()) {
			int id = Integer.parseInt(row.getColumn("vote_id").getColumnValue());
			rst.add(this.getVote(id));
		}
		return rst.toArray(new Vote[rst.size()]);
	}
}

