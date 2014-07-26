package ch.rotscher.budget.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ch.rotscher.budget.model.AccountTemplate;
import ch.rotscher.budget.repository.AccountTemplateRepository;
import ch.rotscher.budget.repository.StatementRepository;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class AccountTemplateService {

	@Autowired private AccountTemplateRepository accountRepository;
	@Autowired private StatementRepository statementRepository;
	
	public void createAccounts() throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		InputStream stream = getClass().getResourceAsStream("account-template.json");
		List<AccountTemplate> accounts = mapper.readValue(stream, new TypeReference<List<AccountTemplate>>() {
			
		});
		for (AccountTemplate account : accounts) {
			accountRepository.save(account);
		}
	}
}
