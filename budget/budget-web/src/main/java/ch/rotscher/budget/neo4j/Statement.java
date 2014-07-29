package ch.rotscher.budget.neo4j;

import java.util.Date;

import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.NodeEntity;

@NodeEntity
public class Statement {

	@GraphId Long id;
	private Date bookingDate;
	private String account;
	private double amount;
	private String description;
	private boolean balanced;
	
	
	public Long getId() {
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
