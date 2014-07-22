package ch.rotscher.budget.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import ch.rotscher.budget.model.BalanceSheet;
import ch.rotscher.budget.model.Statement;
import ch.rotscher.budget.service.BalanceSheetService;
import ch.rotscher.budget.service.StatementService;

@Controller
public class BudgetController {

	@Autowired BalanceSheetService balanceSheetService;
	@Autowired StatementService statementService;

    @RequestMapping(value = "/balancesheet", method=RequestMethod.GET)
    public @ResponseBody BalanceSheet  getBalanceSheet() {
        return balanceSheetService.getBalancedAccounts();
    }
    
    @RequestMapping(value = "/statement", method=RequestMethod.GET)
    public @ResponseBody Iterable<Statement> getStatement() {
        return statementService.getCurrentStatements();
    }
    
    @RequestMapping(value = "/statement", method=RequestMethod.POST)
    public ResponseEntity<String> saveStatement(@RequestBody Statement statement) {
        statementService.balanceStatement(statement.getId(), statement.isBalanced());
        return new ResponseEntity<String>(HttpStatus.OK);
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
