package ch.rotscher.budget.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ch.rotscher.budget.model.Account;
import ch.rotscher.budget.model.AccountTemplate;
import ch.rotscher.budget.model.BalanceSheet;
import ch.rotscher.budget.model.Statement;
import ch.rotscher.budget.repository.AccountTemplateRepository;
import ch.rotscher.budget.repository.BalanceSheetRepository;
import ch.rotscher.budget.repository.StatementRepository;

@Service
public class BalanceSheetService {

	@Autowired private AccountTemplateRepository accountRepository;
	@Autowired private StatementRepository statementRepository;
	@Autowired private BalanceSheetRepository balanceSheetRepository;
	
	public BalanceSheet getBalancedAccounts() {
		List<BalanceSheet> balanceSheets = balanceSheetRepository.findByYear(2014);
		BalanceSheet balanceSheet = null;
		if (balanceSheets.isEmpty()) {
			balanceSheet = createBalanceSheet(2014);
		} else {
			balanceSheet = balanceSheets.get(0);
		}
		
		List<Statement> statements = statementRepository.getCurrentStatements();
		for (Statement stmt : statements) {
			if (stmt.isBalanced()) {
				Account account = findAccount(balanceSheet, stmt.getAccount());
				if (account == null) {
					account = new Account();
					account.setName(stmt.getAccount());
					account.setAmount(stmt.getAmount());
					balanceSheet.getAccounts().add(account);
				} else {
					account.setAmount(account.getAmount() + stmt.getAmount());
				}
			}
		}
		
		return balanceSheet;
	}
	
	private Account findAccount(BalanceSheet balanceSheet, String name) {
		for (Account account : balanceSheet.getAccounts()) {
			if (name.equals(account.getName())) {
				return account;
			}
		}
		
		return null;
	}

	private BalanceSheet createBalanceSheet(int year) {
		BalanceSheet sheet = new BalanceSheet();
		sheet.setYear(year );
		for (AccountTemplate accountTemplate : accountRepository.findAll()) {
			Account acc = accountTemplate.createAccount();
			sheet.getAccounts().add(acc);
		}
		
		balanceSheetRepository.save(sheet);
		return sheet;
	}

}
