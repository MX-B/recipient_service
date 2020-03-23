package io.gr1d.payments.recipient.request;

import io.gr1d.core.validation.Value;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@ToString
@Getter@Setter
public class RecipientRequest {

	@NotEmpty
	@Pattern(regexp = ".{3,5}")
	@ApiModelProperty(required = true, notes = "Bank code retrieved from Bank request")
	private String bankCode;

	@NotEmpty
	@ApiModelProperty(required = true, notes = "The recipient legal name")
	private String name;

	@NotEmpty
	@Pattern(regexp = "\\d{4,8}")
	@ApiModelProperty(required = true, notes = "Bank account number")
	private String bankAccount;

	@NotEmpty
	@Pattern(regexp = "\\d")
	@ApiModelProperty(required = true, notes = "Bank account verification number")
	private String bankAccountVerification;

	@NotEmpty
	@Pattern(regexp = "\\d{4}")
	@ApiModelProperty(required = true, notes = "Agency number")
	private String agency;

	@NotEmpty
	@Pattern(regexp = "\\d")
	@ApiModelProperty(required = true, notes = "Agency verification number")
	private String agencyVerification;

	@NotEmpty
	@Value(values = { "CONTA_CORRENTE", "CONTA_POUPANCA", "CONTA_CORRENTE_CONJUNTA", "CONTA_POUPANCA_CONJUNTA" })
	@ApiModelProperty(required = true, notes = "Represents the bank account type, see details",
			allowableValues = "CONTA_CORRENTE,CONTA_POUPANCA,CONTA_CORRENTE_CONJUNTA,CONTA_POUPANCA_CONJUNTA")
	private String bankAccountType;

	@NotEmpty
	@Value(values = { "DAILY", "WEEKLY", "MONTHLY" })
	@ApiModelProperty(notes = "Frequency which the recipient will receive payments", allowableValues = "DAILY,WEEKLY,MONTHLY")
	private String transferInterval;

	@ApiModelProperty(notes = "Day which the recipient will receive payments. Depends on transfer_interval. " +
			"This field isn't necessary on daily interval. On weekly, can be 1 (monday) to 5 (friday). On monthly, can be between 1 and 31", example = "1")
	private Integer transferDay;

	@NotNull
	@ApiModelProperty(notes = "Variable to indicate if the recipient can receive payments automaticaly", example = "true")
	private Boolean transferEnabled;

}
