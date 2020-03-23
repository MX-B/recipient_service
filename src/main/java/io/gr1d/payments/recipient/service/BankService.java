package io.gr1d.payments.recipient.service;

import io.gr1d.payments.recipient.model.Bank;
import io.gr1d.payments.recipient.repository.BankRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BankService {

	private final BankRepository bankRepository;

	@Autowired
	public BankService(final BankRepository bankRepository) {
		this.bankRepository = bankRepository;
	}

	@Cacheable("banks")
	public List<Bank> list() {
		return bankRepository.findAllByOrderByName();
	}
}
