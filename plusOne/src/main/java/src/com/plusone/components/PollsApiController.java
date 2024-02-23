package com.plusone.components;
import com.plusone.services.PollService;
import com.plusone.services.ConfigurationService;
import com.plusone.services.MessageService;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.context.annotation.Bean;
import com.plusone.models.Poll;
import org.springframework.http.MediaType;

@RestController
@RequestMapping(value="/api/polls")
class PollsApiController {
	private PollService pollService            = null;
	private ConfigurationService configService = ConfigurationService.getInstance("");
	
	@Autowired
	public PollsApiController() {
		this.pollService = PollService.getInstance();
	}

	@GetMapping(value="/list", produces=MediaType.APPLICATION_JSON_VALUE)
	String listVotes() {
		Poll[] temp = this.pollService.listPolls();
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
    String getPoll(@RequestParam("id") int id) {
        Poll temp = this.pollService.getPoll(id);
        return temp.toJsonString();
    }

	@PostMapping(value="/add", produces=MediaType.APPLICATION_JSON_VALUE)
	String add(@RequestBody String raw) {
		try {
			Poll poll = new Poll(raw);
			this.pollService.addPoll(poll);
			return "{\"result\": 1}";
		} catch (Exception ex) {
			ex.printStackTrace();
			return "{\"result\": 0}";
		}
	}
}

