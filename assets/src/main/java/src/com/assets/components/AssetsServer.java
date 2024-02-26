package com.assets.components;
import com.assets.services.ConfigurationService;
import com.assets.services.AssetService;
import com.assets.models.Asset;
import java.lang.Thread;
import java.net.InetAddress;

public class AssetsServer implements Runnable {
	private ConfigurationService configService   = null;
	private AssetService assetService            = null;
	private String[] springArgs;

    public AssetsServer(String path, String[] args) {
        this.readConfig(path);
		this.springArgs = args;
    }

    private void readConfig(String path) {
        this.configService = ConfigurationService.getInstance(path);
		this.assetService  = AssetService.getInstance();
    }

	private void poll() {
		try {
			String rawIP   = InetAddress.getLocalHost().getHostAddress();
			String[] comps = rawIP.split(".");
			if (comps.length > 3) {
				String lanIP = String.format("%s.%s.%s", comps[0], comps[1], comps[2]); 
				for (int i = 1; i < 255; i++) {
					Asset asset = this.assetService.getAssetByAddress(String.format("%s.%d", lanIP, i));
					try {
						InetAddress temp = InetAddress.getByAddress(String.format("%s.%d", lanIP, i).getBytes());
						asset.setDnsName(temp.getHostName());
						asset.setStatus("ACTIVE");
					} catch (Exception ex2) {
						if (asset.getAssetId() < 0) {
							continue;
						}
						asset.setStatus("INACIVE");
					}					
					if (asset.getAssetId() < 0) {
						this.assetService.addAsset(asset);
					} else {
						this.assetService.updateAsset(asset);
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void run() {
		this.poll();
	}

    public void start() {
		new AssetsAPI(this.springArgs).start();

		while (true) {
			new Thread(new AssetsServer("", this.springArgs)).start();
			try {
				Thread.sleep(10000);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
    }
}

