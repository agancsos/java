package com.assets.components;
import com.assets.services.AssetService;
import com.assets.services.ConfigurationService;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.context.annotation.Bean;
import com.assets.models.Asset;
import org.springframework.http.MediaType;

@RestController
@RequestMapping(value="/api/polls")
class AssetsApiController {
	private AssetService assetService          = null;
	private ConfigurationService configService = ConfigurationService.getInstance("");
	
	@Autowired
	public AssetsApiController() {
		this.assetService = AssetService.getInstance();
	}

	@GetMapping(value="/list", produces=MediaType.APPLICATION_JSON_VALUE)
	String listAssets() {
		Asset[] temp = this.assetService.listAssets();
		String rst = "{\"results\":[";
		for (int i = 0; i < temp.length; i++) {
			if (i > 0) {
				rst += ",";
			}
			rst += temp[i].toJsonString();		
		}
		rst += "]";
		rst += "}";
		return rst;
	}

	@GetMapping(value="/get", produces=MediaType.APPLICATION_JSON_VALUE)
    String getAsset(@RequestParam("id") int id) {
        Asset temp = this.assetService.getAsset(id);
        return temp.toJsonString();
    }

	@PostMapping(value="/add", produces=MediaType.APPLICATION_JSON_VALUE)
	String add(@RequestBody String raw) {
		try {
			Asset asset = new Asset(raw);
			this.assetService.addAsset(asset);
			return "{\"result\": 1}";
		} catch (Exception ex) {
			ex.printStackTrace();
			return "{\"result\": 0}";
		}
	}
}

