package com.catalogger.components;
import com.catalogger.models.User;
import com.catalogger.services.AuthenticationService;
import com.catalogger.services.CustomerService;
import com.catalogger.services.AuthorService;
import com.catalogger.services.PublisherService;
import com.catalogger.services.TitleService;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Cookie;
import com.catalogger.gui.components.FormField;
import com.catalogger.models.*;
import java.util.ArrayList;
import org.json.JSONObject;
import java.util.Iterator;

public class FormHelpers {
	public static FormField[] getFields(User user, Item item) throws Exception {
		JSONObject itemJson = new JSONObject(item.toJsonString());
		ArrayList<FormField> rst = new ArrayList<FormField>();
		Iterator<String> keys = itemJson.keys();
		while (keys.hasNext()) {
			String key = keys.next();
			if (key.equals("copies")) {
				continue;
			}
			FormField formField = new FormField();
			formField.setLabel(key);
			switch (key) {
				case "lastUpdatedBy":
				case "createdBy":
				case "owner":
					if (itemJson.getInt(key) == 0) {
						formField.setValue(String.format("%d", user.getId()));
					}
					formField.setType("<select");
					String tempOptions = "";
					for (User cursor : AuthenticationService.getInstance().getUsers()) {
						tempOptions += String.format("<option value='%d'", cursor.getId());
						if (itemJson.getInt(key) == cursor.getId()) {
							tempOptions += " selected";
						}
						tempOptions += String.format("> %s %s</option>", cursor.getFirstName(), cursor.getLastName());
					}
					formField.setOptions(tempOptions);
					formField.setEditable(false);
					formField.setEndTag("</select>");
					break;
				case "mediaType":
					formField.setType("<select");
					formField.setEditable(true);
					formField.setEndTag("</select>");
					tempOptions = "";
					for (int i = 0; i < MediaType.values().length; i++) {
						tempOptions += String.format("<option value='%d'", i);
						if (i == itemJson.getInt(key)) {
							tempOptions += " selected";
						}
						tempOptions += String.format(">%s</option>", MediaType.values()[i]);
					}
					formField.setOptions(tempOptions);
					break;
				case "state":
					if (item.getObjectType() == 3) {
						formField.setType("<select");
						formField.setEditable(false);
						formField.setEndTag("</select>");
						tempOptions = "";
						for (int i = 0; i < "Disabled,Enabled".split(",").length; i++) {
							tempOptions += String.format("<option value='%d'", i);
							if (itemJson.getInt(key) == i) {
								tempOptions += " selected";
							}
							tempOptions += String.format(">%s</option>", "Disabled,Enabled".split(",")[i].toUpperCase());
						}
						formField.setOptions(tempOptions);
					} else if (item.getObjectType() == 6) {
						formField.setType("<select");
                        formField.setEditable(false);
                        formField.setEndTag("</select>");
						tempOptions = "";
                        for (int i = 0; i < "Available,Checkedout".split(",").length; i++) {
                            tempOptions += String.format("<option value='%d'", i);
                            if (itemJson.getInt(key) == i) {
                                tempOptions += " selected";
                            }
                            tempOptions += String.format(">%s</option>", "Available,Checkedout".split(",")[i].toUpperCase());
                        }
                        formField.setOptions(tempOptions);
					} else {
						formField.setValue(itemJson.getString(key));
						formField.setType("<input type='text'");
						formField.setEditable(true);
					}
					break;
				case "author":
                    formField.setType("<select");
                    formField.setEditable(false);
                    formField.setEndTag("</select>");
                    tempOptions = "";
                    for (Author author : AuthorService.getInstance().getAuthors(true)) {
                        tempOptions += String.format("<option value='%d'", author.getId());
                        if (author.getId() == itemJson.getInt(key)) {
                            tempOptions += " selected";
                        }
                        tempOptions += String.format(">%s</option>", author.getLabel());
                    }
                    formField.setOptions(tempOptions);
                    break;
				case "customer":
                    formField.setType("<select");
                    formField.setEditable(false);
                    formField.setEndTag("</select>");
					tempOptions = "";
                    for (Customer customer : CustomerService.getInstance().getCustomers(true)) {
                        tempOptions += String.format("<option value='%d'", customer.getId());
                        if (customer.getId() == itemJson.getInt(key)) {
                            tempOptions += " selected";
                        }
                        tempOptions += String.format(">%s</option>", customer.getLabel());
                    }
                    formField.setOptions(tempOptions);
                    break;
				case "title":
					formField.setType("<select");
                    formField.setEditable(false);
                    formField.setEndTag("</select>");
					tempOptions = "";
                    for (Title title : TitleService.getInstance().getTitles(true)) {
                        tempOptions += String.format("<option value='%d'", title.getId());
                        if (title.getId() == itemJson.getInt(key)) {
                            tempOptions += " selected";
                        }
                        tempOptions += String.format(">%s</option>", title.getLabel());
                    }
                    formField.setOptions(tempOptions);
                    break;
				case "publisher":
                    formField.setType("<select");
                    formField.setEditable(false);
                    formField.setEndTag("</select>");
                    tempOptions = "";
                    for (Publisher publisher : PublisherService.getInstance().getPublishers(true)) {
                        tempOptions += String.format("<option value='%d'", publisher.getId());
                        if (publisher.getId() == itemJson.getInt(key)) {
                            tempOptions += " selected";
                        }
                        tempOptions += String.format(">%s</option>", publisher.getLabel());
                    }
                    formField.setOptions(tempOptions);
                    break;
				case "late":
				case "paid":
					formField.setType("<select");
                    formField.setEditable(false);
                    formField.setEndTag("</select>");
					tempOptions = "";
                    for (int i = 0; i < "NO,YES".split(",").length; i++) {
                        tempOptions += String.format("<option value='%d'", i);
                        if (itemJson.getInt(key) == i) {
                            tempOptions += " selected";
                        }
                        tempOptions += String.format(">%s</option>", "NO,YES".split(",")[i].toUpperCase());
                    }
                    formField.setOptions(tempOptions);
					break;
				case "id":
				case "rid":
				case "label":
				case "path":
				case "parent":
				case "revision":
				case "createdDate":
				case "checkoutDate":
				case "checkinDate":
				case "lastUpdatedDate":
					formField.setValue(itemJson.get(key).toString());
					formField.setType("<input type='text'");
					formField.setEditable(false);
					break;
				case "@type":
					formField.setType("<select");
        			formField.setEditable(false);
        			formField.setEndTag("</select>");
        			tempOptions = "";
        			for (int i = 0; i < ObjectType.values().length; i++) {
            			tempOptions += String.format("<option value='%d'", i);
            			if (i == item.getObjectType()) {
                			tempOptions += " selected";
            			}           
            			tempOptions += String.format(">%s</option>", ObjectType.values()[i]);
        			}                   
        			formField.setOptions(tempOptions);
					break;
				default:
					formField.setValue((itemJson.get(key) == null ? "" : itemJson.get(key).toString()));
					formField.setType("<input type='text' ");
					break;
			}
			rst.add(formField);		
		}
		return rst.toArray(new FormField[rst.size()]);
	}

	public static Item getItemFromForm(JSONObject form) {
		return null;
	}
}


