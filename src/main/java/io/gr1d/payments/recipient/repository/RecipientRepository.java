package io.gr1d.payments.recipient.repository;

import io.gr1d.payments.recipient.dto.RecipientDTO;
import io.gr1d.payments.recipient.dto.RecipientMetadataDTO;
import io.gr1d.payments.recipient.model.Recipient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RecipientRepository extends CrudRepository<Recipient, Long> {

    Optional<Recipient> findByUuidAndRemovedAtIsNull(String uuid);

    Optional<RecipientDTO> findOneByUuidAndRemovedAtIsNull(String uuid);

    Optional<RecipientMetadataDTO> findMetadataByUuidAndRemovedAtIsNull(String uuid);

    Page<RecipientDTO> findAllByRemovedAtIsNull(Pageable pageable);

}
