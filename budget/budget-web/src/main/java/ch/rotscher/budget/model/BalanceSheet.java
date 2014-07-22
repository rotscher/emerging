package ch.rotscher.budget.model;

import java.util.HashSet;
import java.util.Set;

import org.springframework.data.annotation.Id;

public class BalanceSheet {

	@Id
	private String id;
	private String name = "huhu";
	private int year;
	private Set<Account> accounts = new HashSet<>();
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<Account> getAccounts() {
		return accounts;
	}
	
	public void setAccounts(Set<Account> accounts) {
		this.accounts = accounts;
	}
	
	public int getYear() {
		return year;
	}
	
	public void setYear(int year) {
		this.year = year;
	}
}
