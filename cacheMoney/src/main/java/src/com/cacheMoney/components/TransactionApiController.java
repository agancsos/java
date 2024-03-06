package com.cacheMoney.components;
import com.cacheMoney.services.ConfigurationService;
import com.cacheMoney.services.TransactionService;
import com.cacheMoney.models.Transaction;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;

@RestController
@RequestMapping(value="/api/transactions")
class TransactionApiController {
	private ConfigurationService configService      = ConfigurationService.getInstance("");
	private TransactionService   transactionService = null;

	@Autowired
	public TransactionApiController() {
		this.transactionService = TransactionService.getInstance();
	}

	@GetMapping(value="/list", produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	String listTransactions(@RequestParam("acct") int accountId) {
		Transaction[] temp = this.transactionService.listTransactions(accountId, false);
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
	@ResponseBody
    String getTransaction(@RequestParam("id") int id) {
        Transaction temp = this.transactionService.getTransaction(id);
        return temp.toJsonString();
    }

	@PostMapping(value="/add", produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	String add(@RequestBody String raw) {
		try {
			Transaction transaction = new Transaction(raw);
			transaction.setMethod("API");
			this.transactionService.addTransaction(transaction);
			return "{\"result\": 1}";
		} catch (Exception ex) {
			ex.printStackTrace();
			return "{\"result\": 0}";
		}
	}
}

