package com.rest2.services;
import java.util.Iterator;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.rest2.models.Rest2Repository;
import com.rest2.models.Rest2Item;
import com.rest2.models.BasicItem;
import org.springframework.context.annotation.Bean;
import org.json.JSONObject;
import org.springframework.http.MediaType;

@RestController
@RequestMapping(value="/api")
class Rest2ApiController {
  	private Rest2Repository repository = null;

	public Rest2ApiController() {
		this.repository = new Rest2Repository();
		LoadDatabase.initDatabase2(this.repository);
	}

  	public Rest2ApiController(Rest2Repository repository) {  
    	this.repository = repository;
  	}

	@GetMapping(value="test", produces="application/json")
  	String test() {
		return new BasicItem("test3").toJsonString();
  	}

	@GetMapping(value="list", produces="application/json")
	String listAll() {
		String result = "{\"items\":[";
		for (int i = 0; i < this.repository.findAll().size(); i++) {
			Rest2Item e = this.repository.findAll().get(i);
			if (i > 0) {
				result += ",";
			}
			result += e.toJsonString();
		}
		result += "]}";
		return result;
	}

	@GetMapping(value="get", produces="application/json")
	String get(@RequestParam(value = "id")Long id) {
		return this.repository.findById(id).toJsonString();
	}

	@PostMapping(value="add", produces="application/json")
	String add(@RequestBody String raw) {
		JSONObject json = new JSONObject(raw);
		switch ((String)json.get("@type")) {
			case "basic":
				this.repository.save(new BasicItem(raw));
				break;
			default: break;
		}
		return this.listAll();
	}
}
