package ch.rotscher.budget.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import ch.rotscher.budget.model.BalanceSheet;
import ch.rotscher.budget.service.BalanceSheetService;

@Controller
public class BudgetController {

	@Autowired BalanceSheetService balanceSheetService;

    @RequestMapping(value = "/balancesheet", method=RequestMethod.GET)
    public @ResponseBody BalanceSheet  getBalanceSheet() {
        return balanceSheetService.getBalancedAccounts();
    }
}
