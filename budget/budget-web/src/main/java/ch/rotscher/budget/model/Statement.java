package ch.rotscher.budget.model;

import java.util.Date;

import ch.rotscher.budget.repository.StatementRepository;

public class Statement {

	private int id;
	private Date bookingDate;
	private String account;
	private double amount;
	private String description;
	private boolean balanced;
	
	
	public Statement() {
		this.id = StatementRepository.COUNTER++;
	}

	public int getId() {
		return id;
	}
	
	public Date getBookingDate() {
		return bookingDate;
	}

	public void setBookingDate(Date bookingDate) {
		this.bookingDate = bookingDate;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isBalanced() {
		return balanced;
	}

	public void setBalanced(boolean balanced) {
		this.balanced = balanced;
	}

}
