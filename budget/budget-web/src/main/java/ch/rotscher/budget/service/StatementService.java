package ch.rotscher.budget.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ch.rotscher.budget.model.Statement;
import ch.rotscher.budget.repository.StatementRepository;

@Service
public class StatementService {

	@Autowired private StatementRepository statementRepository;
	
	public void saveStatement(Statement statement) {
		statementRepository.getCurrentStatements().add(statement);
	}

	public Iterable<Statement> getCurrentStatements() {
		return statementRepository.getCurrentStatements();
	}
	
	public void balanceStatement(int statementId, boolean isBalanced) {
		if (isBalanced) {
			statementRepository.unbalanceStatement(statementId);
		} else {
			statementRepository.balanceStatement(statementId);
		}
		
	}
}
