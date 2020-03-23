package io.gr1d.payments.recipient.dto;

import java.util.Map;

public interface RecipientMetadataDTO extends RecipientDTO {

	Map<String, String> getMetadata();

}
