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
import java.util.ArrayList;
import java.util.HashMap;
import org.json.JSONObject;

@Controller
@RequestMapping(value="/console/browse")
class BrowseConsoleController {
	private AuthenticationService authService       = null;
	private CustomerService custService             = null;
	private BorrowService borrowService             = null;
	private TitleService titleService               = null;
	private AuthorService authorService             = null;
	private PublisherService publisherService       = null;

	@Autowired
	public BrowseConsoleController() {
		this.authService      = AuthenticationService.getInstance();
		this.custService      = CustomerService.getInstance();
		this.borrowService    = BorrowService.getInstance();
		this.titleService     = TitleService.getInstance();
		this.authorService    = AuthorService.getInstance();
		this.publisherService = PublisherService.getInstance();
	}

	@RequestMapping(value="/")
    public String browseFeatures(Model model, HttpServletRequest request, @RequestParam("objType") String objType) {
		switch (objType) { 
			case "Titles":
				ArrayList<Title> titles = new ArrayList<Title>();
				Title newTitle = new Title();
				newTitle.setLabel("NEW OBJECT");
				titles.add(newTitle);
				for (Title item : this.titleService.getTitles(true)) {
					if (request.getParameterMap().containsKey("id") && Integer.parseInt(request.getParameter("id").toString()) == item.getId()) {
						model.addAttribute("selectedItem", item);
					}
					titles.add(item);
				}
				if (!request.getParameterMap().containsKey("id") && titles.size() > 0) {
					model.addAttribute("selectedItem", titles.get(0));
				}
				model.addAttribute("items", titles.toArray(new Title[titles.size()]));
				break;
			case "Authors":
				ArrayList<Author> authors = new ArrayList<Author>();
				Author newAuthor = new Author();
				newAuthor.setLabel("NEW OBJECT");
				authors.add(newAuthor);
                for (Author item : this.authorService.getAuthors(true)) {
					if (request.getParameterMap().containsKey("id") && Integer.parseInt(request.getParameter("id").toString()) == item.getId()) {
                        model.addAttribute("selectedItem", item);
                    }
                   	authors.add(item);
                }
				if (!request.getParameterMap().containsKey("id") && authors.size() > 0) {
                    model.addAttribute("selectedItem", authors.get(0));
                }
                model.addAttribute("items", authors.toArray(new Author[authors.size()]));
				break;
			case "Publishers":
				ArrayList<Publisher> publishers = new ArrayList<Publisher>();
				Publisher newPublisher = new Publisher();
				newPublisher.setLabel("NEW OBJECT");
				publishers.add(newPublisher);
                for (Publisher item : this.publisherService.getPublishers(true)) {
					if (request.getParameterMap().containsKey("id") && Integer.parseInt(request.getParameter("id").toString()) == item.getId()) {
                        model.addAttribute("selectedItem", item);
                    }
                    publishers.add(item);
                }
				if (!request.getParameterMap().containsKey("id") && publishers.size() > 0) {
                    model.addAttribute("selectedItem", publishers.get(0));
                }
                model.addAttribute("items", publishers.toArray(new Publisher[publishers.size()]));
				break;
			case "Borrows":
				 if (request.getParameterMap().containsKey("custId")) {
					int customerId = Integer.parseInt(request.getParameter("custId").toString());
					Customer customer = this.custService.getCustomer(customerId, true);
					ArrayList<Borrow> borrows = new ArrayList<Borrow>();
                	for (Borrow item : this.borrowService.getBorrows(customer)) {
						if (request.getParameterMap().containsKey("id") && Integer.parseInt(request.getParameter("id").toString()) == item.getId()) {
                        	model.addAttribute("selectedItem", item);
                    	}
                    	borrows.add(item);
                	}
					if (!request.getParameterMap().containsKey("id") && borrows.size() > 0) {
                    	model.addAttribute("selectedItem", borrows.get(0));
                	}
                	model.addAttribute("items", borrows.toArray(new Borrow[borrows.size()]));
				}
				break;
			case "Customers":
				ArrayList<Customer> customers = new ArrayList<Customer>();
				Customer newCustomer = new Customer();
				newCustomer.setLabel("NEW OBJECT");
				customers.add(newCustomer);
                for (Customer item : this.custService.getCustomers(true)) {
					if (request.getParameterMap().containsKey("id") && Integer.parseInt(request.getParameter("id").toString()) == item.getId()) {
                        model.addAttribute("selectedItem", item);
                    }
                    customers.add(item);
                }
				if (!request.getParameterMap().containsKey("id") && customers.size() > 0) {
                    model.addAttribute("selectedItem", customers.get(0));
                }
                model.addAttribute("items", customers.toArray(new Customer[customers.size()]));
				break;
			default: break;
		}
        return "browse";
    }

	@RequestMapping(value="/save", method=RequestMethod.POST)
    public String saveResource(Model model, HttpServletRequest request, @RequestParam("objType") String objType, @RequestParam("id") int id, @CookieValue("X-API-TOKEN") String token) {
		JSONObject obj   = new JSONObject();
		User sessionUser = ApiHelpers.extractUser(token); 
		for (HashMap.Entry<String, String[]> param : request.getParameterMap().entrySet()) {
			if (param.getKey().equals("borrows-submit") || param.getKey().equals("save-submit") 
				|| param.getKey().equals("delete-submit") || param.getKey().equals("checkin-button")
				|| param.getKey().equals("paid-button")) {
				continue;
			}
			try {
				obj.put(param.getKey(), Integer.parseInt(param.getValue()[0]));
			} catch (Exception ex) {
				obj.put(param.getKey(), param.getValue()[0]);
			}
		}

		Item item = null;
		switch (obj.getInt("@type")) {
			case 1:
				item = new Author();
				((Author)item).reloadFromJson(obj.toString());
				break;
			case 2:
				item = new Publisher();
				((Publisher)item).reloadFromJson(obj.toString());
				break;
			case 3:
				item = new Title();
				((Title)item).reloadFromJson(obj.toString());
				break;
			case 5:
				item = new Customer();
				((Customer)item).reloadFromJson(obj.toString());
				break;
			default: break;
		}

		if (request.getParameterMap().containsKey("borrows-submit")) {
			return String.format("redirect:/console/browse/?objType=Borrows&custId=%s", obj.getString("id"));
		} else if (request.getParameterMap().containsKey("save-submit")) {
			switch (obj.getInt("@type")) {
            	case 1:
					if (obj.getInt("id") < 1) {
						AuthorService.getInstance().addAuthor((Author)item);
					} else {
						AuthorService.getInstance().modifyAuthor((Author)item);
					}
                	break;
            	case 2:
					if (obj.getInt("id") < 1) {
						PublisherService.getInstance().addPublisher((Publisher)item);
                    } else {
						PublisherService.getInstance().modifyPublisher((Publisher)item);
                    }
                	break;
            	case 3:
					if (obj.getInt("id") < 1) {
						TitleService.getInstance().addTitle((Title)item);
                    } else {
						TitleService.getInstance().modifyTitle((Title)item);
                    }
                	break;
            	case 5:
					if (obj.getInt("id") < 1) {
						CustomerService.getInstance().addCustomer((Customer)item);
                    } else {
						CustomerService.getInstance().modifyCustomer((Customer)item);
                    }
                	break;
            	default: break;
        	}
		} else if (request.getParameterMap().containsKey("delete-submit")) {
			switch (obj.getInt("@type")) {
                case 1:
					AuthorService.getInstance().removeAuthor((Author)item);
                    break;
                case 2:
					PublisherService.getInstance().removePublisher((Publisher)item);
                    break;
                case 3:
					TitleService.getInstance().removeTitle((Title)item);
                    break;
                case 5:
					CustomerService.getInstance().removeCustomer((Customer)item);
                    break;
                default: break;
            }
		} else if (request.getParameterMap().containsKey("checkin-button")) {
			BorrowService.getInstance().checkin((Borrow)item, sessionUser);
		} else if (request.getParameterMap().containsKey("paid-button")) {
			BorrowService.getInstance().markPaid((Borrow)item, sessionUser);
		}
		return String.format("redirect:/console/browse/?objType=%s&id=%d", objType, id);
	}
}


