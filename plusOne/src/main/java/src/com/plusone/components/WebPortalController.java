package com.plusone.components;
import org.springframework.ui.ModelMap;
import com.plusone.services.ConfigurationService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
class WebPortalController {
	private ConfigurationService configService = ConfigurationService.getInstance("");
	
	public WebPortalController() {
	}

	/*@RequestMapping(value="test")
	String test(ModelMap map) {
		return "ping";
	}*/
}

