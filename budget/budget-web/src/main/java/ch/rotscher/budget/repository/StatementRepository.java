package ch.rotscher.budget.repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import ch.rotscher.budget.model.Statement;

@Repository
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class StatementRepository {

	private List<Statement> currentStatements = new ArrayList<>();
	public static int COUNTER = 1;
	public StatementRepository() {
		Statement stmt1 = new Statement();
		stmt1.setAccount("Miete");
		stmt1.setAmount(-1956);
		stmt1.setBookingDate(new Date());
		stmt1.setBalanced(true);
		stmt1.setDescription("Miete Mai 2014");
		//currentStatements.add(stmt1);
		
		Statement stmt2 = new Statement();
		stmt2.setAccount("Miete");
		stmt2.setAmount(-125);
		stmt2.setBookingDate(new Date());
		stmt2.setBalanced(false);
		stmt2.setDescription("Parkplatz Mai 2014");
		//currentStatements.add(stmt2);
	}
	
	public List<Statement> getCurrentStatements() {
		return currentStatements;
	}
	
	public void balanceStatement(int id) {
		for (Statement stmt : currentStatements){
			if (stmt.getId() == id) {
				stmt.setBalanced(true);
			}
		}
	}
	
	public void unbalanceStatement(int id) {
		for (Statement stmt : currentStatements){
			if (stmt.getId() == id) {
				stmt.setBalanced(false);
			}
		}
	}
}
