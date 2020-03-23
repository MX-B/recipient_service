package io.gr1d.payments.recipient.exception;

import io.gr1d.core.exception.Gr1dHttpException;
import org.springframework.http.HttpStatus;

public class RecipientException extends Gr1dHttpException {

	public RecipientException() {
		super(HttpStatus.INTERNAL_SERVER_ERROR.value(), "io.gr1d.recipients.recipientError");
	}

}
