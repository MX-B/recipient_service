package io.gr1d.payments.recipient.exception;

import io.gr1d.core.exception.Gr1dNotFoundException;
import lombok.Getter;

@Getter
public class BankNotFoundException extends Gr1dNotFoundException {

	public BankNotFoundException() {
		super("io.gr1d.recipient.bankNotFound");
	}
}
