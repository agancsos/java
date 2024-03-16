package com.requests.services;
import com.requests.models.PrayerRequest;
import java.util.ArrayList;
import com.data.types.DataTable;
import com.data.types.DataRow;
import com.data.types.DataColumn;

public class PrayerRequestService {
	private ConfigurationService configService   = null;
	private static PrayerRequestService instance = null;
	private MessageService msgService            = null;
	private DataService dataService              = null;
	private CacheService cacheService            = null;

	private PrayerRequestService() {
		this.configService = ConfigurationService.getInstance("");
		this.msgService    = MessageService.getInstance();
		this.dataService   = DataService.getInstance();
		this.cacheService  = CacheService.getInstance();
		this.dataService.createSchema();
	}

	public static PrayerRequestService getInstance() {
		if (PrayerRequestService.instance == null) {
			PrayerRequestService.instance = new PrayerRequestService();
		}
		return PrayerRequestService.instance;
	}

	public void pushPrayerRequest(PrayerRequest request) throws Exception {
        if (!(Boolean)this.configService.getProperty("mq.disabled", false) && this.msgService.isReachable(false)) {
            this.msgService.putMessagege((String)this.configService.getProperty("mq.queue", ""), request.toJsonString());
        } else {
            this.addPrayerRequest(request);
        }
    }

	public void addPrayerRequest(PrayerRequest request) throws Exception {
		this.dataService.runServiceQuery(String.format("INSERT INTO PRAYER_REQUESTS (USER_ID, PRAYER_REQUEST_METHOD, PRAYER_REQUEST_ISPUBLIC, PRAYER_REQUEST_ISSHARED, PRAYER_REQUEST_STATUS, PRAYER_REQUEST_TEXT, PRAYER_REQUEST_PROCESSED) VALUES ('%d', '%s', '%d', '%d', '%s', '%s', '%d')",
			request.getUserId(),
			request.getMethod(),
			(request.getIsPublic() ? 1 : 0),
			(request.getIsShared() ? 1 : 0),
			request.getStatus(),
			request.getRequest(),
			(request.getProcessed() ? 1 : 0)
		));
		this.cacheService.updateCache(request.getPrayerRequestId(), this.listPrayerRequests(request.getUserId(), false));
	}

	public void updatePrayerRequest(PrayerRequest request) throws Exception {
        this.dataService.runServiceQuery(String.format("UPDATE PRAYER_REQUESTS SET PRAYER_REQUEST_METHOD = '%s', PRAYER_REQUEST_ISPUBLIC = '%d', PRAYER_REQUEST_ISSHARED = '%d', PRAYER_REQUEST_STATUS = '%s', PRAYER_REQUEST_TEXT = '%s', PRAYER_REQUEST_PROCESSED = '%d', LAST_UPDATED_DATE = CURRENT_TIMESTAMP WHERE PRAYER_REQUEST_ID = '%d'",
            request.getMethod(),
			(request.getIsPublic() ? 1 : 0),
            (request.getIsShared() ? 1 : 0),
            request.getStatus(),
            request.getRequest(),
            (request.getProcessed() ? 1 : 0),
			request.getPrayerRequestId()
        ));
        this.cacheService.updateCache(request.getUserId(), this.listPrayerRequests(request.getUserId(), false));
    }

	public void removePrayerRequest(PrayerRequest request) {
		this.dataService.runServiceQuery(String.format("DELETE FROM PRAYER_REQUESTS WHERE PRAYER_REQUEST_ID = '%d'", request.getPrayerRequestId()));
	}

	public PrayerRequest getPrayerRequest(int id) {
		PrayerRequest rst = new PrayerRequest();
		DataTable tab = this.dataService.serviceQuery(String.format("SELECT * FROM PRAYER_REQUESTS WHERE PRAYER_REQUEST_ID = '%d'", id));
		if (tab.getRows().size() > 0) {
			DataRow row = tab.getRows().get(0);
			rst.setPrayerRequestId(id);
			rst.setUserId(Integer.parseInt(row.getColumn("user_id").getColumnValue()));
			rst.setIsPublic((Integer.parseInt(row.getColumn("prayer_request_ispublic").getColumnValue()) < 1 ? false : true));
			rst.setIsShared((Integer.parseInt(row.getColumn("prayer_request_isshared").getColumnValue()) < 1 ? false : true));
			rst.setStatus(row.getColumn("prayer_request_status").getColumnValue());
			rst.setRequest(row.getColumn("prayer_request_text").getColumnValue());
			rst.setMethod(row.getColumn("prayer_request_method").getColumnValue());
			rst.setLastUpdatedDate(row.getColumn("last_updated_date").getColumnValue());
			rst.setProcessed((Integer.parseInt(row.getColumn("prayer_request_processed").getColumnValue()) < 1 ? false : true));
		}
		return rst;
	}

	public PrayerRequest[] listPrayerRequests(int userId, boolean useCache) {
		if (useCache && this.cacheService.contains(userId)) {
            return this.cacheService.getPrayerRequests(userId);
        }
		ArrayList<PrayerRequest> rst = new ArrayList<PrayerRequest>();
		DataTable tab = this.dataService.serviceQuery(String.format("SELECT PRAYER_REQUEST_ID FROM PRAYER_REQUESTS WHERE USER_ID = '%d' ORDER BY LAST_UPDATED_DATE DESC", userId));
		for (DataRow row : tab.getRows()) {
			int id = Integer.parseInt(row.getColumn("prayer_request_id").getColumnValue());
			PrayerRequest request = this.getPrayerRequest(id);
			rst.add(request);
		}
		this.cacheService.updateCache(userId, rst.toArray(new PrayerRequest[rst.size()]));
		return rst.toArray(new PrayerRequest[rst.size()]);
	}
}

