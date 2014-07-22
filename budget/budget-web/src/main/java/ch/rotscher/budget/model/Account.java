package ch.rotscher.budget.model;


public class Account extends AccountTemplate {

	private double amount;
	private double performanceAmount;

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public double getPerformanceAmount() {
		return performanceAmount;
	}

	public void setPerformanceAmount(double performanceAmount) {
		this.performanceAmount = performanceAmount;
	}
}
