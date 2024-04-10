package com.jspInfo.components;
import com.jspInfo.services.JSPInfoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping(value="/")
class JSPInfoConsoleController {
	private JSPInfoService jspInfoService       = null;    

	@Autowired
	public JSPInfoConsoleController() {
	}

	@RequestMapping(value="/")
    public String defaultHome(Model model, HttpServletRequest request) {
		this.jspInfoService = JSPInfoService.getInstance(String.format("%sconfig.json", request.getServletContext().getRealPath("/")));
        model.addAttribute("properties", this.jspInfoService.getProperties());
        return "index";
    }

	@RequestMapping(value="console/")
	public String landing(Model model, HttpServletRequest request) {
		this.jspInfoService = JSPInfoService.getInstance(String.format("%sconfig.json", request.getServletContext().getRealPath("/")));
		model.addAttribute("properties", this.jspInfoService.getProperties());
		return "index";
	}
}


