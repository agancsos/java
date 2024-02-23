package com.plusone.components;
import com.plusone.services.VoteService;
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
import com.plusone.models.Vote;
import org.springframework.http.MediaType;

@RestController
@RequestMapping(value="/api/votes")
class VotesApiController {
	private VoteService voteService            = null;
	private ConfigurationService configService = ConfigurationService.getInstance("");
	
	@Autowired
	public VotesApiController() {
		this.voteService = VoteService.getInstance();
	}

	@GetMapping(value="/get", produces=MediaType.APPLICATION_JSON_VALUE)
	String listVotes(@RequestParam("pollId") int pollId) {
		Vote[] temp = this.voteService.listVotes(pollId);
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

	@PostMapping(value="/add", produces=MediaType.APPLICATION_JSON_VALUE)
	String add(@RequestBody String raw) {
		try {
			Vote vote = new Vote(raw);
			this.voteService.pushVote(vote);
			return "{\"result\": 1}";
		} catch (Exception ex) {
			ex.printStackTrace();
			return "{\"result\": 0}";
		}
	}
}

