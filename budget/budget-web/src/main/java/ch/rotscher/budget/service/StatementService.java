package ch.rotscher.budget.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ch.rotscher.budget.neo4j.Statement;
import ch.rotscher.budget.neo4j.StatementRepository;

@Service
public class StatementService {

	@Autowired private StatementRepository statementRepository;
	
	public void saveStatement(Statement statement) {
		statementRepository.save(statement);
	}

	public Iterable<Statement> getCurrentStatements() {
		return statementRepository.findAll();
	}
}
