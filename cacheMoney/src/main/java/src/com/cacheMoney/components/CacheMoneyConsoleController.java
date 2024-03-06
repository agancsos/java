package com.cacheMoney.components;
import com.cacheMoney.services.ConfigurationService;
import com.cacheMoney.services.TransactionService;
import com.cacheMoney.models.Transaction;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;

@Controller
@RequestMapping(value="/console")
class CacheMoneyConsoleController {
	private ConfigurationService configService      = ConfigurationService.getInstance("");
	private TransactionService   transactionService = null;

	@Autowired
	public CacheMoneyConsoleController() {
		this.transactionService = TransactionService.getInstance();
	}

	@RequestMapping(value="/")
	public String landing() {
		return "index";
	}

	@RequestMapping(value="/transactions")
	String listTransactions(Model model, @RequestParam("acct") int accountId) {
		Transaction[] temp = this.transactionService.listTransactions(accountId, true);
		model.addAttribute("results", temp);
		return "list-transactions";
	}

	@RequestMapping(value="/transaction")
	String getTransaction(Model model, @RequestParam("trans") int transactionId) {
		Transaction temp = this.transactionService.getTransaction(transactionId);
        model.addAttribute("transaction", temp);
        return "get-transaction";
	}
}

