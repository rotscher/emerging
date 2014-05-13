package ch.rotscher.budget.model;

public class Account {

	private String name;
	private double amount;
	private double salary;
	private double fixExpense;
	private double projectedAmount;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
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

	public double getProjectedAmount() {
		return projectedAmount;
	}

	public void setProjectedAmount(double projectedAmount) {
		this.projectedAmount = projectedAmount;
	}
	
	public Account clone() {
		Account clone = new Account();
		clone.setName(getName());
		clone.setAmount(getAmount());
		clone.setFixExpense(getFixExpense());
		clone.setProjectedAmount(getProjectedAmount());
		clone.setSalary(getSalary());
		
		return clone;
	}
}
