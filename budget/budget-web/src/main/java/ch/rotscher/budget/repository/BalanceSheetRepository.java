package ch.rotscher.budget.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import ch.rotscher.budget.model.BalanceSheet;

public interface BalanceSheetRepository extends MongoRepository<BalanceSheet, String> {

	List<BalanceSheet> findByYear(int year);
}
