package io.gr1d.payments.recipient.service;

import io.gr1d.payments.recipient.request.RecipientRequest;
import me.pagar.BankAccountType;
import me.pagar.model.BankAccount;
import me.pagar.model.PagarMe;
import me.pagar.model.PagarMeException;
import me.pagar.model.Recipient;
import me.pagar.model.Recipient.TransferInterval;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static io.gr1d.payments.recipient.model.BankAccountType.*;
import static io.gr1d.payments.recipient.model.DocumentType.CNPJ;
import static io.gr1d.payments.recipient.model.DocumentType.CPF;
import static java.util.Optional.ofNullable;

@Service
public class PagarmeService {

    public PagarmeService(@Value("${gr1d.payments.recipient.pagarme.apiKey}") final String apiKey) {
        PagarMe.init(apiKey);
    }

    String create(final io.gr1d.payments.recipient.model.Recipient request) throws PagarMeException {
        final BankAccount bankAccount = new BankAccount();
        bankAccount.setAgencia(request.getAgency());
        bankAccount.setAgenciaDv(request.getAgencyVerification());
        bankAccount.setBankCode(request.getBankCode());
        bankAccount.setConta(request.getBankAccount());
        bankAccount.setContaDv(request.getBankAccountVerification());
        bankAccount.setType(bankAccountTypeMap.get(request.getBankAccountType().name()));
        bankAccount.setLegalName(request.getName());
        bankAccount.setDocumentNumber(request.getDocumentNumber());
        bankAccount.setDocumentType(typeMap.get(request.getDocumentType().name()));

        final Recipient recipient = new Recipient();
        recipient.setAnticipatableVolumePercentage(0);
        recipient.setAutomaticAnticipationEnabled(false);
        recipient.setTransferInterval(transferIntervalMap.get(request.getTransferInterval().name()));
        recipient.setTransferEnabled(request.isTransferEnabled());
        recipient.setTransferDay(ofNullable(request.getTransferDay()).orElse(10));
        recipient.setBankAccount(bankAccount);

        recipient.save();
        return recipient.getId();
    }

    void update(final String pagarmeId, final RecipientRequest request) throws PagarMeException {
        final Recipient recipient = new Recipient().find(pagarmeId);
        final BankAccount bankAccount = recipient.getBankAccount();

        ofNullable(request.getAgency()).ifPresent(bankAccount::setAgencia);
        ofNullable(request.getAgencyVerification()).ifPresent(bankAccount::setAgenciaDv);
        ofNullable(request.getBankCode()).ifPresent(bankAccount::setBankCode);
        ofNullable(request.getBankAccount()).ifPresent(bankAccount::setConta);
        ofNullable(request.getBankAccountVerification()).ifPresent(bankAccount::setContaDv);
        ofNullable(request.getBankAccountType()).map(bankAccountTypeMap::get).ifPresent(bankAccount::setType);
        ofNullable(request.getName()).ifPresent(bankAccount::setLegalName);

        recipient.setBankAccount(bankAccount);
        recipient.save();
    }

    private static final Map<String, BankAccount.DocumentType> typeMap = new HashMap<>();
    private static final Map<String, BankAccountType> bankAccountTypeMap = new HashMap<>();
    private static final Map<String, TransferInterval> transferIntervalMap = new HashMap<>();

    static {
        typeMap.put(CNPJ.name(), BankAccount.DocumentType.CNPJ);
        typeMap.put(CPF.name(), BankAccount.DocumentType.CPF);

        transferIntervalMap.put("DAILY", TransferInterval.DAILY);
        transferIntervalMap.put("WEEKLY", TransferInterval.WEEKLY);
        transferIntervalMap.put("MONTHLY", TransferInterval.MONTHLY);

        bankAccountTypeMap.put(CONTA_CORRENTE.name(), BankAccountType.CORRENTE);
        bankAccountTypeMap.put(CONTA_CORRENTE_CONJUNTA.name(), BankAccountType.CORRENTE_CONJUNTA);
        bankAccountTypeMap.put(CONTA_POUPANCA.name(), BankAccountType.POUPANCA);
        bankAccountTypeMap.put(CONTA_POUPANCA_CONJUNTA.name(), BankAccountType.POUPANCA_CONJUNTA);
    }

}
