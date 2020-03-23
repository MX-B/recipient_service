package io.gr1d.payments.recipient.exception;

import io.gr1d.core.exception.Gr1dNotFoundException;
import lombok.Getter;

@Getter
public class RecipientNotFoundException extends Gr1dNotFoundException {

	public RecipientNotFoundException() {
		super("io.gr1d.recipient.recipientNotFound");
	}
}
