package com.assets.services;
import com.assets.models.Asset;
import java.util.ArrayList;
import com.data.types.DataTable;
import com.data.types.DataRow;
import com.data.types.DataColumn;

public class AssetService {
	private ConfigurationService configService = null;
	private static AssetService instance       = null;
	private DataService dataService            = null;

	private AssetService() {
		this.configService = ConfigurationService.getInstance("");
		this.dataService   = DataService.getInstance();
		this.dataService.createSchema();
	}

	public static AssetService getInstance() {
		if (AssetService.instance == null) {
			AssetService.instance = new AssetService();
		}
		return AssetService.instance;
	}

	public void addAsset(Asset asset) throws Exception {
		this.dataService.runServiceQuery(String.format("INSERT INTO ASSETS (ASSET_IP_ADDRESS, ASSET_DNS_NAME, ASSET_STATUS, CREATED_DATE) VALUES ('%s', '%s', '%s', CURRENT_TIMESTAMP)",
			asset.getIpAddress(),
			asset.getDnsName(),
			asset.getStatus()
		));
	}

	public void updateAsset(Asset asset) throws Exception {
		this.dataService.runServiceQuery(String.format("UPDATE ASSETS SET ASSET_IP_ADDRESS = '%s', ASSET_DNS_NAME = '%s', ASSET_STATUS = '%s', LAST_UPDATED_DATE = CURRENT_TIMESTAMP WHERE ASSET_ID = '%d'",
			asset.getIpAddress(),
            asset.getDnsName(),
            asset.getStatus(),
			asset.getAssetId()
        ));
	}

	public Asset getAsset(int id) {
		Asset rst = new Asset();
		DataTable tab = this.dataService.serviceQuery(String.format("SELECT * FROM ASSETS WHERE ASSET_ID = '%d'", id));
		if (tab.getRows().size() > 0) {
			DataRow row = tab.getRows().get(0);
			rst.setAssetId(id);
			rst.setCreatedDate(row.getColumn("created_date").getColumnValue());
			rst.setLastUpdatedDate(row.getColumn("last_updated_date").getColumnValue());
			rst.setIpAddress(row.getColumn("asset_ip_address").getColumnValue());
			rst.setDnsName(row.getColumn("asset_dns_name").getColumnValue());
			rst.setStatus(row.getColumn("asset_status").getColumnValue());
		}
		return rst;
	}

	public Asset getAssetByAddress(String address) {
		DataTable tab = this.dataService.serviceQuery(String.format("SELECT ASSET_ID FROM ASSETS WHERE ASSET_IP_ADDRESS = '%s'", address));
		if (tab.getRows().size() > 0) {
			int id = Integer.parseInt(tab.getRows().get(0).getColumn("asset_id").getColumnValue());
			return this.getAsset(id);
		}
		return new Asset();
	}

	public  Asset[] listAssets() {
		ArrayList<Asset> rst = new ArrayList<Asset>();
		DataTable tab = this.dataService.serviceQuery(String.format("SELECT ASSET_ID FROM ASSETS"));
		for (DataRow row : tab.getRows()) {
			int id = Integer.parseInt(row.getColumn("asset_id").getColumnValue());
			rst.add(this.getAsset(id));
		}
		return rst.toArray(new Asset[rst.size()]);
	}
}

