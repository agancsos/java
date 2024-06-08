package com.catalogger.components;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import com.catalogger.services.*;
import com.catalogger.models.*;
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
import javax.servlet.http.Cookie;

@Controller
@RequestMapping(value="/")
class CataloggerConsoleController {
	private AuthenticationService authService       = null;
	private CustomerService custService             = null;
	private BorrowService borrowService             = null;
	private TitleService titleService               = null;
	private Customer[] customers                    = null;

	@Autowired
	public CataloggerConsoleController() {
		this.authService    = AuthenticationService.getInstance();
		this.custService    = CustomerService.getInstance();
		this.borrowService  = BorrowService.getInstance();
		this.titleService   = TitleService.getInstance();
		this.customers      = this.custService.getCustomers(true);
	}

	private Customer findCustomer(String isbn) {
		for (Customer customer : this.customers) {
			if (customer.getISBN().equals(isbn)) {
				return customer;
			}
		}
		return null;
	}

	@RequestMapping(value="/")
    public String defaultHome(Model model, HttpServletRequest request) {
        return "redirect:/console";
    }

	@RequestMapping(value="/console")
	public String landing(Model model, HttpServletRequest request) {
		return "index";
	}

	@RequestMapping(value="/console/checkout", method=RequestMethod.POST)
	public String checkout(Model model, HttpServletRequest request, @CookieValue("X-API-TOKEN") String token) {
		User sessionUser  = this.authService.getUser(token);
    	Customer customer = this.findCustomer(request.getParameter("customer-input"));
        String[] isbns    = request.getParameter("titles-input").split("\n");
        for (String isbn : isbns) {
       		Title title = this.titleService.isbnLookup(isbn);
            this.borrowService.checkout(title, customer, sessionUser);
        }
		return String.format("redirect:/console");
	}

	@RequestMapping(value="/console/setter", method=RequestMethod.POST)
    public String login(Model model, HttpServletRequest request, HttpServletResponse response) {
		String email    = request.getParameter("username-in");
        String password = SecurityService.getBase64Encoded(request.getParameter("password-in"));
        if (this.authService.authenticate(email, password)) {
        	String token = authService.generateToken(email, password);
            Cookie c = new Cookie("X-API-TOKEN", token);
			c.setPath("/");
            response.addCookie(c);
        } else {
        }
        return "redirect:/console";
    }

	@RequestMapping(value="/console/signout")
    public String logout(Model model, HttpServletRequest request, HttpServletResponse response) {
		Cookie c = new Cookie("X-API-TOKEN", "");
		c.setPath("/");
        response.addCookie(c);
        return "redirect:/console";
    }
}


