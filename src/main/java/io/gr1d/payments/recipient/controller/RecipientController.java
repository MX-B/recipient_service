package io.gr1d.payments.recipient.controller;

import io.gr1d.auth.keycloak.LoggedUser;
import io.gr1d.core.controller.BaseController;
import io.gr1d.core.datasource.model.Gr1dPageable;
import io.gr1d.core.datasource.model.PageResult;
import io.gr1d.core.model.CreatedResponse;
import io.gr1d.payments.recipient.dto.RecipientDTO;
import io.gr1d.payments.recipient.exception.BankNotFoundException;
import io.gr1d.payments.recipient.exception.RecipientException;
import io.gr1d.payments.recipient.exception.RecipientNotFoundException;
import io.gr1d.payments.recipient.model.Recipient;
import io.gr1d.payments.recipient.repository.RecipientRepository;
import io.gr1d.payments.recipient.request.CreateRecipientRequest;
import io.gr1d.payments.recipient.request.RecipientRequest;
import io.gr1d.payments.recipient.service.RecipientService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;
import java.util.Optional;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@Slf4j
@RestController
@Api(tags = "Recipient")
@RequestMapping(path = "/recipient")
public class RecipientController extends BaseController {

    private final LoggedUser user;
    private final RecipientRepository recipientRepository;
    private final RecipientService recipientService;

    @Autowired
    public RecipientController(final LoggedUser user, final RecipientRepository recipientRepository, final RecipientService recipientService) {
        this.user = user;
        this.recipientRepository = recipientRepository;
        this.recipientService = recipientService;
    }

    @ApiOperation(value = "listRecipients", notes = "Lists all recipients")
    @RequestMapping(path = "", method = GET, produces = JSON)
    public PageResult<RecipientDTO> list(final Gr1dPageable page) {
        log.info("Listing Recipients {}", page);
        return PageResult.ofPage(recipientRepository.findAllByRemovedAtIsNull(page.toPageable()));
    }

    @ApiOperation(value = "createRecipient", notes = "Create one recipient")
    @RequestMapping(path = "", method = POST, consumes = JSON)
    public ResponseEntity<CreatedResponse> create(@Valid @RequestBody final CreateRecipientRequest request) throws RecipientException, BankNotFoundException {
        log.info("Creating Recipient with request {}", request);
        final Recipient recipient = recipientService.save(request);
        final URI uri = URI.create(String.format("/recipient/%s", recipient.getUuid()));
        return ResponseEntity.created(uri).body(new CreatedResponse(recipient.getUuid()));
    }

    @ApiOperation(value = "updateRecipient", notes = "Update one recipient")
    @RequestMapping(path = "/{uuid}", method = PUT, consumes = JSON)
    public void update(@PathVariable final String uuid,
                       @Valid @RequestBody final RecipientRequest request) throws RecipientNotFoundException, RecipientException, BankNotFoundException {
        log.info("Updating Recipient {} with request {}", uuid, request);
        recipientService.update(uuid, request);
    }

    @ApiOperation(value = "removeRecipient", notes = "Removes one recipient, removed recipient aren't able to receive receivables")
    @RequestMapping(path = "/{uuid}", method = DELETE)
    public void remove(@PathVariable final String uuid) throws RecipientNotFoundException {
        log.info("Removing Recipient {}", uuid);
        recipientService.remove(uuid);
    }

    @ApiOperation(value = "getRecipient", notes = "Retrieve recipient information")
    @RequestMapping(path = "/{uuid}", method = GET, produces = JSON)
    public ResponseEntity<? extends RecipientDTO> get(@PathVariable final String uuid) {
        log.info("Requesting Recipient {}", uuid);

        final Optional<? extends RecipientDTO> optional = user.isAdmin()
                ? recipientRepository.findMetadataByUuidAndRemovedAtIsNull(uuid)
                : recipientRepository.findOneByUuidAndRemovedAtIsNull(uuid);

        return optional.map(item -> ResponseEntity.ok().body(item))
                .orElse(ResponseEntity.notFound().build());
    }

}
