package io.gr1d.payments.recipient.controller;

import io.gr1d.core.controller.BaseController;
import io.gr1d.payments.recipient.model.Bank;
import io.gr1d.payments.recipient.service.BankService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Slf4j
@RestController
@Api(tags = "Bank")
@RequestMapping(path = "/bank")
public class BankController extends BaseController {

    private final BankService bankService;

    @Autowired
    public BankController(final BankService bankService) {
        this.bankService = bankService;
    }

    @ApiOperation(value = "listBanks", notes = "Lists all banks")
    @RequestMapping(path = "", method = GET, produces = JSON)
    public List<Bank> list() {
        log.info("Listing all Banks");
        return bankService.list();
    }

}
