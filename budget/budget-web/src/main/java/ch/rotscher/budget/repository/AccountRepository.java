package ch.rotscher.budget.repository;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import ch.rotscher.budget.model.Account;

@Repository
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class AccountRepository {

	private Set<Account> accounts = new HashSet<>();
	
	public AccountRepository() {
		Account miete = new Account();
		miete.setName("Miete");
		miete.setAmount(2223.45);
		miete.setSalary(2200);
		miete.setFixExpense(1960);
		
		accounts.add(miete);
	}
	
	public Set<Account> getAccounts() {
		return accounts;
	}
}
