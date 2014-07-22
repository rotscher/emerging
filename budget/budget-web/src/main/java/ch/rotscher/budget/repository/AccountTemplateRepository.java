package ch.rotscher.budget.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import ch.rotscher.budget.model.AccountTemplate;

public interface AccountTemplateRepository extends MongoRepository<AccountTemplate, String> {


}
