package com.transactions.components;
import com.transactions.services.TransactionService;
import com.transactions.services.ConfigurationService;
import com.transactions.services.MessageService;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.context.annotation.Bean;
import com.transactions.models.Transaction;
import org.springframework.http.MediaType;

@RestController
@RequestMapping(value="/api")
class TransactionsApiController {
	private TransactionService transService = null;
	private ConfigurationService configService = ConfigurationService.getInstance("");
	
	@Autowired
	public TransactionsApiController() {
		this.transService = TransactionService.getInstance();
	}

	@GetMapping(value="/get", produces=MediaType.APPLICATION_JSON_VALUE)
	String listTransactions(@RequestParam("symbol") String symbol) {
		Transaction[] temp = this.transService.listTransactions(symbol);
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
			Transaction trans = new Transaction(raw);
			this.transService.pushTransaction(trans);
			return "{\"result\": 1}";
		} catch (Exception ex) {
			ex.printStackTrace();
			return "{\"result\": 0}";
		}
	}
}

