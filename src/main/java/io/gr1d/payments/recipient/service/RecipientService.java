package io.gr1d.payments.recipient.service;

import io.gr1d.payments.recipient.exception.BankNotFoundException;
import io.gr1d.payments.recipient.exception.RecipientException;
import io.gr1d.payments.recipient.exception.RecipientNotFoundException;
import io.gr1d.payments.recipient.model.*;
import io.gr1d.payments.recipient.repository.BankRepository;
import io.gr1d.payments.recipient.repository.RecipientRepository;
import io.gr1d.payments.recipient.request.CreateRecipientRequest;
import io.gr1d.payments.recipient.request.RecipientRequest;
import lombok.extern.slf4j.Slf4j;
import me.pagar.model.PagarMeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static io.gr1d.payments.recipient.model.DocumentType.CNPJ;
import static io.gr1d.payments.recipient.model.DocumentType.CPF;

@Slf4j
@Service
public class RecipientService {

    private static final String PAGARME_ID_KEY = "pagarme_id";

    private final RecipientRepository recipientRepository;
    private final BankRepository bankRepository;
    private final PagarmeService pagarmeService;

    @Autowired
    public RecipientService(final RecipientRepository recipientRepository, final PagarmeService pagarmeService, final BankRepository bankRepository) {
        this.recipientRepository = recipientRepository;
        this.pagarmeService = pagarmeService;
        this.bankRepository = bankRepository;
    }

    @Transactional(rollbackFor = Throwable.class)
    public Recipient save(final CreateRecipientRequest request) throws RecipientException, BankNotFoundException {
        final Recipient recipient = new Recipient();

        recipient.setName(request.getName());
        recipient.setBankCode(request.getBankCode());
        recipient.setBankAccountType(BankAccountType.valueOf(request.getBankAccountType()));
        recipient.setBankAccount(request.getBankAccount());
        recipient.setBankAccountVerification(request.getBankAccountVerification());
        recipient.setAgency(request.getAgency());
        recipient.setAgencyVerification(request.getAgencyVerification());
        recipient.setDocumentNumber(request.getDocumentNumber());
        recipient.setDocumentType(resolveDocumentType(request.getDocumentNumber()));
        recipient.setTransferDay(request.getTransferDay());
        recipient.setTransferEnabled(request.getTransferEnabled());
        recipient.setTransferInterval(TransferInterval.valueOf(request.getTransferInterval()));

        getBank(recipient, request.getBankCode());

        try {
            final String pagarmeId = pagarmeService.create(recipient);
            recipient.setMetadata(Collections.singletonMap(PAGARME_ID_KEY, pagarmeId));
        } catch (final PagarMeException exception) {
            log.error("Error while trying to create a recipient on pagarme", exception);
            throw new RecipientException();
        }

        return recipientRepository.save(recipient);
    }

    private DocumentType resolveDocumentType(final String documentNumber) {
        return documentNumber.length() == 11 ? CPF : CNPJ;
    }

    @Transactional(rollbackFor = Throwable.class)
    public void update(final String uuid, final RecipientRequest request) throws RecipientNotFoundException, RecipientException, BankNotFoundException {
        final Recipient recipient = findNotRemoved(uuid);

        recipient.setName(request.getName());
        recipient.setBankCode(request.getBankCode());
        recipient.setBankAccountType(BankAccountType.valueOf(request.getBankAccountType()));
        recipient.setBankAccount(request.getBankAccount());
        recipient.setBankAccountVerification(request.getBankAccountVerification());
        recipient.setAgency(request.getAgency());
        recipient.setAgencyVerification(request.getAgencyVerification());
        recipient.setTransferDay(request.getTransferDay());
        recipient.setTransferEnabled(request.getTransferEnabled());
        recipient.setTransferInterval(TransferInterval.valueOf(request.getTransferInterval()));

        getBank(recipient, request.getBankCode());

        try {
            pagarmeService.update(recipient.getMetadata().get(PAGARME_ID_KEY), request);
        } catch (final PagarMeException exception) {
            log.error("Error while trying to update a recipient on pagarme", exception);
            throw new RecipientException();
        }

        recipientRepository.save(recipient);
    }

    @Transactional(rollbackFor = Throwable.class)
    public void remove(final String uuid) throws RecipientNotFoundException {
        final Recipient recipient = findNotRemoved(uuid);
        recipient.setRemovedAt(LocalDateTime.now());
        recipientRepository.save(recipient);
    }
    
    private Recipient findNotRemoved(final String uuid) throws RecipientNotFoundException {
        return recipientRepository.findByUuidAndRemovedAtIsNull(uuid)
                .orElseThrow(RecipientNotFoundException::new);
    }

    private void getBank(final Recipient recipient, final String bankCode) throws BankNotFoundException {
        final Optional<Bank> bank = bankRepository.findByBankCode(bankCode);

        if (!bank.isPresent()) {
            log.error("Bank not found with code " + bankCode);
            throw new BankNotFoundException();
        }

        recipient.setBank(bank.get());
    }

}
