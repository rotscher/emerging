package ch.rotscher.budget.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import ch.rotscher.budget.model.Account;
import ch.rotscher.budget.model.Statement;
import ch.rotscher.budget.service.AccountService;
import ch.rotscher.budget.service.StatementService;

@Controller
public class BudgetController {

	@Autowired AccountService accountService;
	@Autowired StatementService statementService;
	
    @RequestMapping(value = "/accounts", method=RequestMethod.GET)
    public @ResponseBody Iterable<Account> getAccounts() {
        return accountService.getBalancedAccounts();
    }
    
    @RequestMapping(value = "/statements", method=RequestMethod.GET)
    public @ResponseBody Iterable<Statement> getStatement() {
        return statementService.getCurrentStatements();
    }

    @RequestMapping(value = "/statement/{statementId}/balance", method=RequestMethod.GET)
    public ModelAndView balanceStatement(@PathVariable int statementId, @RequestParam boolean isBalanced) {
        statementService.balanceStatement(statementId, isBalanced);
        return new ModelAndView(new RedirectView("/index.html", true));
    }
    
    @RequestMapping(value = "/statement/save", method=RequestMethod.POST)
    public ModelAndView saveStatement(@RequestParam Date bookingDate, @RequestParam String account,
                                   @RequestParam double amount, @RequestParam String description) {

        Statement statement = new Statement();
        statement.setAccount(account);
        statement.setBookingDate(bookingDate);
        statement.setAmount(amount);
        statement.setDescription(description);
        
    	statementService.saveStatement(statement);
        
        return new ModelAndView(new RedirectView("/index.html", true));

    }
}
