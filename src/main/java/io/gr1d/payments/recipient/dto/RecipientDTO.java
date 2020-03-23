package io.gr1d.payments.recipient.dto;

import io.gr1d.payments.recipient.model.BankAccountType;
import io.gr1d.payments.recipient.model.DocumentType;
import io.gr1d.payments.recipient.model.TransferInterval;
import org.springframework.beans.factory.annotation.Value;

public interface RecipientDTO {

	String getUuid();

	@Value("#{target.bank.name}")
	String getBankName();
	String getBankCode();
	String getName();
	String getDocumentNumber();
	DocumentType getDocumentType();
	String getBankAccount();
	String getBankAccountVerification();
	String getAgency();
	String getAgencyVerification();
	BankAccountType getBankAccountType();
	TransferInterval getTransferInterval();
	Integer getTransferDay();
	boolean getTransferEnabled();

}
