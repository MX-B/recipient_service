package io.gr1d.payments.recipient.model;


import io.gr1d.core.datasource.audit.AuditListener;
import io.gr1d.core.datasource.model.BaseModel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Map;

@Getter
@Setter
@Entity
@Table(name = "recipient")
@EntityListeners(AuditListener.class)
public class Recipient extends BaseModel {

	@Column(name = "bank_code", nullable = false)
	private String bankCode;

	@ManyToOne
	@JoinColumn(name = "bank_id", nullable = false)
	private Bank bank;

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "document_number", nullable = false)
	private String documentNumber;

	@Column(name = "document_type", nullable = false)
	private DocumentType documentType;

	@Column(name = "agency", nullable = false)
	private String agency;

	@Column(name = "agency_verification", nullable = false)
	private String agencyVerification;

	@Column(name = "bank_account", nullable = false)
	private String bankAccount;

	@Column(name = "bank_account_verification", nullable = false)
	private String bankAccountVerification;

	@Column(name = "bank_account_type", nullable = false)
	private BankAccountType bankAccountType;

	@Column(name = "transfer_interval", nullable = false)
	private TransferInterval transferInterval;

	@Column(name = "transfer_day", nullable = false)
	private Integer transferDay;

	@Column(name = "transfer_enabled", nullable = false)
	private boolean transferEnabled;

	@Column(name = "value", length = 24)
	@ElementCollection(fetch = FetchType.EAGER)
	@MapKeyColumn(name = "property", length = 24)
	@JoinTable(name = "recipient_metadata", joinColumns = @JoinColumn(name = "recipient_id"))
	private Map<String, String> metadata;

}
