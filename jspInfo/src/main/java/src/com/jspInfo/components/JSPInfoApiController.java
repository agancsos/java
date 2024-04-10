package com.jspInfo.components;
import com.jspInfo.services.JSPInfoService;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.json.JSONObject;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value="/api")
class JSPInfoApiController {
	private JSPInfoService jspInfoService           = null;

	@Autowired
	public JSPInfoApiController() {
	}


	@GetMapping(value="/", produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	String home(HttpServletRequest request) {
		this.jspInfoService = JSPInfoService.getInstance(String.format("%sconfig.json", request.getServletContext().getRealPath("/")));
		return ApiHelpers.getPropertiesJson();
	}
}


