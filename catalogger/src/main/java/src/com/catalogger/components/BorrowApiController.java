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
import com.catalogger.services.AuthorService;
import com.catalogger.services.TitleService;
import com.catalogger.services.PublisherService;
import com.catalogger.services.BorrowService;
import com.catalogger.services.CustomerService;
import com.catalogger.services.AuthenticationService;
import com.catalogger.services.DbTraceService;

@RestController
@RequestMapping(value="/api/borrows")
class BorrowApiController {
	private AuthorService authorService        = null;
    private PublisherService pubService        = null;
    private TitleService titleService          = null;
    private AuthenticationService authService  = null;
	private BorrowService borrowService        = null;
	private CustomerService custService        = null;

	@Autowired
	public BorrowApiController() {
		this.authorService = AuthorService.getInstance();
        this.pubService    = PublisherService.getInstance();
        this.titleService  = TitleService.getInstance();
        this.authService   = AuthenticationService.getInstance();
		this.borrowService = BorrowService.getInstance();
		this.custService   = CustomerService.getInstance();
	}


	@GetMapping(value="/", produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	String home(HttpServletRequest request) {
		return "{\"result\": \"Pong\"}";
	}

	@GetMapping(value="/get", produces=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String getBorrow(HttpServletRequest request, @RequestHeader("X-API-TOKEN") String token, @RequestParam("id") int id) {
		try {
            ApiHelpers.ensureAuthenticated(token);
            if (!this.authService.isGroupMember(ApiHelpers.extractUser(token), SR.adminGroupId)) {
                return String.format("\"{message\": \"%s\"}", SR.notAuthorizedMessage);
            }
			return this.borrowService.getBorrow(id).toJsonString();
        } catch(Exception ex) {
            return String.format("{\"message\": \"%s\"}", ex.getMessage());
        }
    }

	@GetMapping(value="/list", produces=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
	String listBorrows(HttpServletRequest request, @RequestHeader("X-API-TOKEN") String token, @RequestParam("id") int id) {
		try {
            ApiHelpers.ensureAuthenticated(token);
            if (!this.authService.isGroupMember(ApiHelpers.extractUser(token), SR.adminGroupId)) {
                return String.format("\"{message\": \"%s\"}", SR.notAuthorizedMessage);
            }
			String rst = "{\"result\":[";
        	Customer customer = this.custService.getCustomer(id, true);
        	Borrow[] borrows = this.borrowService.getBorrows(customer);
        	for (int i = 0; i < borrows.length; i++) {
            	if (i > 0) { rst += ","; }
            	rst += borrows[i].toJsonString();
        	}
        	rst += "]}";
        	return rst;
        } catch(Exception ex) {
            return String.format("{\"message\": \"%s\"}", ex.getMessage());
        }
	}

	@GetMapping(value="/getbytitle", produces=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String listBorrowsByTitle(HttpServletRequest request, @RequestHeader("X-API-TOKEN") String token, @RequestParam("id") int id) {
        try {
            ApiHelpers.ensureAuthenticated(token);
            if (!this.authService.isGroupMember(ApiHelpers.extractUser(token), SR.adminGroupId)) {
                return String.format("\"{message\": \"%s\"}", SR.notAuthorizedMessage);
            }
            String rst = "{\"result\":[";
            Title title = this.titleService.getTitle(id, true);
            Borrow[] borrows = this.borrowService.getBorrowsByTitle(title);
            for (int i = 0; i < borrows.length; i++) {
                if (i > 0) { rst += ","; }
                rst += borrows[i].toJsonString();
            }
            rst += "]}";
            return rst;
        } catch(Exception ex) {
            return String.format("{\"message\": \"%s\"}", ex.getMessage());
        }
    }
	
	@GetMapping(value="/lates", produces=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String listLateBorrows(HttpServletRequest request, @RequestHeader("X-API-TOKEN") String token) {
        try {
            ApiHelpers.ensureAuthenticated(token);
            if (!this.authService.isGroupMember(ApiHelpers.extractUser(token), SR.adminGroupId)) {
                return String.format("\"{message\": \"%s\"}", SR.notAuthorizedMessage);
            }
            String rst = "{\"result\":[";
            Object[] borrows = this.borrowService.getLateBorrows();
            for (int i = 0; i < borrows.length; i++) {
				HashMap<String, Object> borrow = (HashMap<String, Object>)borrows[i];
                if (i > 0) { rst += ","; }
				rst += String.format("{\"id\": %d, \"days\": %d, \"customerId\": %d, \"checkoutDate\": \"%s\", \"checkinDate\": \"%s\"}",
					(int)borrow.get("id"),
					(int)borrow.get("days"),
					(int)borrow.get("customerId"),
					(String)borrow.get("checkoutDate"),
					(String)borrow.get("checkinDate"));
            }
            rst += "]}";
            return rst;
        } catch(Exception ex) {
            return String.format("{\"message\": \"%s\"}", ex.getMessage());
        }
    }

	@GetMapping(value="/late", produces=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String isLate(HttpServletRequest request, @RequestHeader("X-API-TOKEN") String token, @RequestParam("id") int id) {
        try {
            ApiHelpers.ensureAuthenticated(token);
            if (!this.authService.isGroupMember(ApiHelpers.extractUser(token), SR.adminGroupId)) {
                return String.format("\"{message\": \"%s\"}", SR.notAuthorizedMessage);
            }
			Borrow temp = this.borrowService.getBorrow(id);
            HashMap<String, Object> rst = this.borrowService.isLate(temp);
            return String.format("{\"result\": {\"late\": %d, \"days\": %d}}", ((boolean)rst.get("late") ? 1 : 0), (int)rst.get("days"));
        } catch(Exception ex) {
            return String.format("{\"message\": \"%s\"}", ex.getMessage());
        }
    }

	@GetMapping(value="/markpaid", produces=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String markPaid(HttpServletRequest request, @RequestHeader("X-API-TOKEN") String token, @RequestBody String raw) {
        try {
            ApiHelpers.ensureAuthenticated(token);
            if (!this.authService.isGroupMember(ApiHelpers.extractUser(token), SR.adminGroupId)) {
                return String.format("\"{message\": \"%s\"}", SR.notAuthorizedMessage);
            }
			Borrow temp = new Borrow();        
            temp.reloadFromJson(raw);
            return String.format("{\"result\": %d}", (this.borrowService.markPaid(temp, ApiHelpers.extractUser(token)) ? 1 : 0));
        } catch(Exception ex) {
            return String.format("{\"message\": \"%s\"}", ex.getMessage());
        }
    }

	@GetMapping(value="/checkout", produces=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String checkout(HttpServletRequest request, @RequestHeader("X-API-TOKEN") String token, @RequestBody String raw) {
        try {
            ApiHelpers.ensureAuthenticated(token);
            if (!this.authService.isGroupMember(ApiHelpers.extractUser(token), SR.adminGroupId)) {
                return String.format("\"{message\": \"%s\"}", SR.notAuthorizedMessage);
            }
			JSONObject dict    = new JSONObject(raw);
			Customer customer  = this.custService.getCustomer(dict.getInt("customer"), true);
			Title title        = this.titleService.getTitle(dict.getInt("title"), true);
            return String.format("{\"result\": %d}", (this.borrowService.checkout(title, customer, ApiHelpers.extractUser(token)) ? 1 : 0));
        } catch(Exception ex) {
            return String.format("{\"message\": \"%s\"}", ex.getMessage());
        }
    }

	@GetMapping(value="/checkin", produces=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String checkin(HttpServletRequest request, @RequestHeader("X-API-TOKEN") String token, @RequestBody String raw) {
        try {
            ApiHelpers.ensureAuthenticated(token);
            if (!this.authService.isGroupMember(ApiHelpers.extractUser(token), SR.adminGroupId)) {
                return String.format("\"{message\": \"%s\"}", SR.notAuthorizedMessage);
            }
            Borrow borrow = new Borrow();
			borrow.reloadFromJson(raw);
            return String.format("{\"result\": %d}", (this.borrowService.checkin(borrow, ApiHelpers.extractUser(token)) ? 1 : 0));
        } catch(Exception ex) {
            return String.format("{\"message\": \"%s\"}", ex.getMessage());
        }
    }
}


