package ch.rotscher.budget.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ch.rotscher.budget.model.Account;
import ch.rotscher.budget.model.Statement;
import ch.rotscher.budget.repository.AccountRepository;
import ch.rotscher.budget.repository.StatementRepository;

@Service
public class AccountService {

	@Autowired private AccountRepository accountRepository;
	@Autowired private StatementRepository statementRepository;
	
	public Set<Account> getBalancedAccounts() {
		Set<Account> unbalancedAccounts = accountRepository.getAccounts();
		List<Statement> statements = statementRepository.getCurrentStatements();
		Set<Account> balancedAccounts = new HashSet<>();
		for (Account account : unbalancedAccounts) {
			Account balancedAccount = account.clone();
			
			for (Statement stmt : statements) {
				if (stmt.isBalanced() && stmt.getAccount().equals(account.getName())) {
					balancedAccount.setAmount(balancedAccount.getAmount() + stmt.getAmount());
				}
			}
			balancedAccounts.add(balancedAccount);
		}
		
		return balancedAccounts;
	}
}
