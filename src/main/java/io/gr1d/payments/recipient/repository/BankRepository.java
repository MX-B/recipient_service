package io.gr1d.payments.recipient.repository;

import io.gr1d.payments.recipient.model.Bank;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BankRepository extends CrudRepository<Bank, Long> {

    List<Bank> findAllByOrderByName();

    Optional<Bank> findByBankCode(String bankCode);
}
