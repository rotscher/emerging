package ch.rotscher.budget.model;

import org.springframework.data.annotation.Id;

public class AccountTemplate {

	@Id
	private String id;
	private String name;
	private double salary;
	private double fixExpense;
	private double targetAmount;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getSalary() {
		return salary;
	}

	public void setSalary(double salary) {
		this.salary = salary;
	}

	public double getFixExpense() {
		return fixExpense;
	}

	public void setFixExpense(double fixExpense) {
		this.fixExpense = fixExpense;
	}

	public double getTargetAmount() {
		return targetAmount;
	}

	public void setTargetAmount(double projectedAmount) {
		this.targetAmount = projectedAmount;
	}
	
	public AccountTemplate clone() {
		AccountTemplate clone = new AccountTemplate();
		clone.setName(getName());
		clone.setFixExpense(getFixExpense());
		clone.setTargetAmount(getTargetAmount());
		clone.setSalary(getSalary());
		
		return clone;
	}

	public Account createAccount() {
		Account account = new Account();
		account.setName(getName());
		account.setAmount(0);
		account.setFixExpense(getFixExpense());
		account.setPerformanceAmount(0);
		account.setSalary(getSalary());
		return account;
	}
}
