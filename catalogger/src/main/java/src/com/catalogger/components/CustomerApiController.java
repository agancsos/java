package com.catalogger.components;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.json.JSONObject;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import com.catalogger.models.*;
import com.catalogger.SR;
import com.catalogger.services.CustomerService;
import com.catalogger.services.AuthenticationService;
import com.catalogger.services.DbTraceService;

@RestController
@RequestMapping(value="/api/customers")
class CustomerApiController {
    private AuthenticationService authService  = null;
    private CustomerService custService        = null;

    @Autowired
    public CustomerApiController() {
        this.authService   = AuthenticationService.getInstance();
        this.custService   = CustomerService.getInstance();
    }


    @GetMapping(value="/", produces=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String home(HttpServletRequest request) {
        return "{\"result\": \"Pong\"}";
    }

	String getCustomer(HttpServletRequest request, @RequestHeader("X-API-TOKEN") String token, @RequestParam("id") int id) {
		try {
            ApiHelpers.ensureAuthenticated(token);
            if (!this.authService.isGroupMember(ApiHelpers.extractUser(token), SR.adminGroupId)) {
                return String.format("\"{message\": \"%s\"}", SR.notAuthorizedMessage);
            }
            return this.custService.getCustomer(id, true).toJsonString();
        } catch(Exception ex) {
            return String.format("{\"message\": \"%s\"}", ex.getMessage());
        }
	}

	@GetMapping(value="/list", produces=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String listCustomers(HttpServletRequest request, @RequestHeader("X-API-TOKEN") String token) {
		try {
            ApiHelpers.ensureAuthenticated(token);
            if (!this.authService.isGroupMember(ApiHelpers.extractUser(token), SR.adminGroupId)) {
                return String.format("\"{message\": \"%s\"}", SR.notAuthorizedMessage);
            }
			String rst = "{\"result\":[";
        	Customer[] customers = this.custService.getCustomers(true);
        	for (int i = 0; i < customers.length; i++) {
            	if (i > 0) { rst += ","; }
            	rst += customers[i].toJsonString();
        	}
        	rst += "]}";
        	return rst;
        } catch(Exception ex) {
            return String.format("{\"message\": \"%s\"}", ex.getMessage());
        }
    }

	@GetMapping(value="/add", produces=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String addCustomer(HttpServletRequest request, @RequestHeader("X-API-TOKEN") String token, @RequestBody String raw) {
        try {
            ApiHelpers.ensureAuthenticated(token);
            if (!this.authService.isGroupMember(ApiHelpers.extractUser(token), SR.adminGroupId)) {
                return String.format("\"{message\": \"%s\"}", SR.notAuthorizedMessage);
            }
            Customer temp = new Customer();
            temp.reloadFromJson(raw);
            return String.format("{\"result\": %d}", (this.custService.addCustomer(temp) ? 1 : 0));
        } catch(Exception ex) {
            return String.format("{\"message\": \"%s\"}", ex.getMessage());
        }
    }

    @GetMapping(value="/update", produces=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String updateCustomer(HttpServletRequest request, @RequestHeader("X-API-TOKEN") String token, @RequestBody String raw) {
        try {
            ApiHelpers.ensureAuthenticated(token);
            if (!this.authService.isGroupMember(ApiHelpers.extractUser(token), SR.adminGroupId)) {
                return String.format("\"{message\": \"%s\"}", SR.notAuthorizedMessage);
            }
			Customer temp = new Customer();
            temp.reloadFromJson(raw);
            return String.format("{\"result\": %d}", (this.custService.modifyCustomer(temp) ? 1 : 0));
        } catch(Exception ex) {
            return String.format("{\"message\": \"%s\"}", ex.getMessage());
        }
    }

    @GetMapping(value="/remove", produces=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String removeCustomer(HttpServletRequest request, @RequestHeader("X-API-TOKEN") String token, @RequestBody String raw) {
        try {
            ApiHelpers.ensureAuthenticated(token);
            if (!this.authService.isGroupMember(ApiHelpers.extractUser(token), SR.adminGroupId)) {
                return String.format("\"{message\": \"%s\"}", SR.notAuthorizedMessage);
            }
			Customer temp = new Customer();
            temp.reloadFromJson(raw);
            return String.format("{\"result\": %d}", (this.custService.removeCustomer(temp) ? 1 : 0));
        } catch(Exception ex) {
            return String.format("{\"message\": \"%s\"}", ex.getMessage());
        }
    }
}

