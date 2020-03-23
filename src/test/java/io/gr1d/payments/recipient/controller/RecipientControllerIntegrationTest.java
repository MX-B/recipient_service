package io.gr1d.payments.recipient.controller;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;
import io.gr1d.core.model.CreatedResponse;
import io.gr1d.payments.recipient.SpringTestApplication;
import io.gr1d.payments.recipient.request.CreateRecipientRequest;
import org.flywaydb.core.Flyway;
import org.flywaydb.test.FlywayTestExecutionListener;
import org.flywaydb.test.annotation.FlywayTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static io.gr1d.payments.recipient.TestUtils.*;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = SpringTestApplication.class)
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, FlywayTestExecutionListener.class })
public class RecipientControllerIntegrationTest {

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private Flyway flyway;

	@Before
	public void init() {
		FixtureFactoryLoader.loadTemplates("io.gr1d.payments.recipient.fixtures");
	}

	@After
	public void clean() throws IllegalArgumentException {
		flyway.clean();
	}

	@Test
	@FlywayTest
	public void testRecipientCreation() throws Exception {
		final CreateRecipientRequest request = Fixture.from(CreateRecipientRequest.class).gimme("valid");
		final String uri = "/recipient";

		mockMvc.perform(get(uri)).andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("content").isArray())
				.andExpect(jsonPath("content").value(hasSize(0)));

		final ResultActions actions = checkXss(mockMvc.perform(post("/recipient")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(json(request))).andDo(print()).andExpect(status().isCreated()));
		final CreatedResponse response = getResult(actions, CreatedResponse.class);
		final String uuid = response.getUuid();
		final String uriSingle = String.format("%s/%s", uri, uuid);

		mockMvc.perform(get(uri)).andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("content").isArray())
				.andExpect(jsonPath("content").value(hasSize(1)))
				.andExpect(jsonPath("content[0].name").value(request.getName()))
				.andExpect(jsonPath("content[0].bank_name").isNotEmpty())
				.andExpect(jsonPath("content[0].bank_code").value(request.getBankCode()))
				.andExpect(jsonPath("content[0].document_number").value(request.getDocumentNumber()))
				.andExpect(jsonPath("content[0].bank_account").value(request.getBankAccount()))
				.andExpect(jsonPath("content[0].bank_account_verification").value(request.getBankAccountVerification()))
				.andExpect(jsonPath("content[0].agency").value(request.getAgency()))
				.andExpect(jsonPath("content[0].agency_verification").value(request.getAgencyVerification()))
				.andExpect(jsonPath("content[0].bank_account_type").value(request.getBankAccountType()))
				.andExpect(jsonPath("content[0].transfer_interval").value(request.getTransferInterval()))
				.andExpect(jsonPath("content[0].transfer_day").value(request.getTransferDay()))
				.andExpect(jsonPath("content[0].transfer_enabled").value(request.getTransferEnabled()))
				.andExpect(jsonPath("content[0].uuid").value(uuid));

		mockMvc.perform(get(uriSingle).accept(MediaType.APPLICATION_JSON)).andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("name").value(request.getName()))
				.andExpect(jsonPath("bank_name").isNotEmpty())
				.andExpect(jsonPath("bank_code").value(request.getBankCode()))
				.andExpect(jsonPath("document_number").value(request.getDocumentNumber()))
				.andExpect(jsonPath("bank_account").value(request.getBankAccount()))
				.andExpect(jsonPath("bank_account_verification").value(request.getBankAccountVerification()))
				.andExpect(jsonPath("agency").value(request.getAgency()))
				.andExpect(jsonPath("agency_verification").value(request.getAgencyVerification()))
				.andExpect(jsonPath("bank_account_type").value(request.getBankAccountType()))
				.andExpect(jsonPath("transfer_interval").value(request.getTransferInterval()))
				.andExpect(jsonPath("transfer_day").value(request.getTransferDay()))
				.andExpect(jsonPath("transfer_enabled").value(request.getTransferEnabled()))
				.andExpect(jsonPath("uuid").value(uuid));

		final CreateRecipientRequest anotherRequest = Fixture.from(CreateRecipientRequest.class).gimme("valid");

		checkXss(mockMvc.perform(put(uriSingle)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(json(anotherRequest))).andDo(print()).andExpect(status().isOk()));

		mockMvc.perform(get(uri)).andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("content").isArray())
				.andExpect(jsonPath("content").value(hasSize(1)))
				.andExpect(jsonPath("content[0].name").value(anotherRequest.getName()))
				.andExpect(jsonPath("content[0].bank_name").isNotEmpty())
				.andExpect(jsonPath("content[0].bank_code").value(anotherRequest.getBankCode()))
				.andExpect(jsonPath("content[0].document_number").value(request.getDocumentNumber()))
				.andExpect(jsonPath("content[0].bank_account").value(anotherRequest.getBankAccount()))
				.andExpect(jsonPath("content[0].bank_account_verification").value(anotherRequest.getBankAccountVerification()))
				.andExpect(jsonPath("content[0].agency").value(anotherRequest.getAgency()))
				.andExpect(jsonPath("content[0].agency_verification").value(anotherRequest.getAgencyVerification()))
				.andExpect(jsonPath("content[0].bank_account_type").value(anotherRequest.getBankAccountType()))
				.andExpect(jsonPath("content[0].transfer_interval").value(anotherRequest.getTransferInterval()))
				.andExpect(jsonPath("content[0].transfer_day").value(anotherRequest.getTransferDay()))
				.andExpect(jsonPath("content[0].transfer_enabled").value(anotherRequest.getTransferEnabled()))
				.andExpect(jsonPath("content[0].uuid").value(uuid));

		mockMvc.perform(get(uriSingle).accept(MediaType.APPLICATION_JSON)).andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("metadata").isMap())
				.andExpect(jsonPath("metadata.pagarme_id").isNotEmpty())
				.andExpect(jsonPath("name").value(anotherRequest.getName()))
				.andExpect(jsonPath("bank_name").isNotEmpty())
				.andExpect(jsonPath("bank_code").value(anotherRequest.getBankCode()))
				.andExpect(jsonPath("document_number").value(request.getDocumentNumber()))
				.andExpect(jsonPath("bank_account").value(anotherRequest.getBankAccount()))
				.andExpect(jsonPath("bank_account_verification").value(anotherRequest.getBankAccountVerification()))
				.andExpect(jsonPath("agency").value(anotherRequest.getAgency()))
				.andExpect(jsonPath("agency_verification").value(anotherRequest.getAgencyVerification()))
				.andExpect(jsonPath("bank_account_type").value(anotherRequest.getBankAccountType()))
				.andExpect(jsonPath("transfer_interval").value(anotherRequest.getTransferInterval()))
				.andExpect(jsonPath("transfer_day").value(anotherRequest.getTransferDay()))
				.andExpect(jsonPath("transfer_enabled").value(anotherRequest.getTransferEnabled()))
				.andExpect(jsonPath("uuid").value(uuid));
	}
	
	@Test
	@FlywayTest
	public void testRecipientInactivation() throws Exception {
		final CreateRecipientRequest request = Fixture.from(CreateRecipientRequest.class).gimme("valid");
		final String uri = "/recipient";

		mockMvc.perform(get(uri)).andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("content").isArray())
				.andExpect(jsonPath("content").value(hasSize(0)));
		
		final ResultActions actions = mockMvc.perform(post(uri)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(json(request))).andDo(print()).andExpect(status().isCreated());
		final CreatedResponse response = getResult(actions, CreatedResponse.class);
		final String uuid = response.getUuid();
		final String uriSingle = String.format("%s/%s", uri, uuid);

		mockMvc.perform(get(uri)).andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("content").isArray())
				.andExpect(jsonPath("content").value(hasSize(1)))
				.andExpect(jsonPath("content[0].uuid").value(uuid));
		mockMvc.perform(get(uriSingle).accept(MediaType.APPLICATION_JSON)).andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("uuid").value(uuid));
		
		mockMvc.perform(delete(uriSingle)).andDo(print()).andExpect(status().isOk());
		mockMvc.perform(get(uriSingle)).andDo(print()).andExpect(status().isNotFound());
		mockMvc.perform(get(uri)).andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("content").isArray())
				.andExpect(jsonPath("content").value(hasSize(0)));
		mockMvc.perform(delete(uriSingle)).andDo(print()).andExpect(status().isNotFound());
	}

	@Test
	@FlywayTest
	public void testNotFound() throws Exception {
		final String uri = "/recipient/12y8e9129y8h";
		mockMvc.perform(get(uri)
				.accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isNotFound());
	}

	@Test
	@FlywayTest
	public void testValidations() throws Exception {
		final CreateRecipientRequest invalidBankAccount = Fixture.from(CreateRecipientRequest.class).gimme("invalid_bankAccount");
		final CreateRecipientRequest invalidAgency = Fixture.from(CreateRecipientRequest.class).gimme("invalid_agency");
		final CreateRecipientRequest invalidAgencyVerification = Fixture.from(CreateRecipientRequest.class).gimme("invalid_agencyVerification");
		final CreateRecipientRequest invalidBankCode = Fixture.from(CreateRecipientRequest.class).gimme("invalid_bankCode");

		checkXss(mockMvc.perform(post("/recipient")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(json(invalidBankAccount)))
				.andDo(print()).andExpect(status().isUnprocessableEntity()));

		checkXss(mockMvc.perform(post("/recipient")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(json(invalidAgency)))
				.andDo(print()).andExpect(status().isUnprocessableEntity()));

		checkXss(mockMvc.perform(post("/recipient")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(json(invalidAgencyVerification)))
				.andDo(print()).andExpect(status().isUnprocessableEntity()));

		checkXss(mockMvc.perform(post("/recipient")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(json(invalidBankCode)))
				.andDo(print()).andExpect(status().isUnprocessableEntity()));
	}

	@Test
	public void testInvalidDocumentNumber() throws Exception {
		final CreateRecipientRequest invalidCPF = Fixture.from(CreateRecipientRequest.class).gimme("invalid_documentNumberCPF");
		final CreateRecipientRequest invalidCNPJ = Fixture.from(CreateRecipientRequest.class).gimme("invalid_documentNumberCNPJ");

		checkXss(mockMvc.perform(post("/recipient")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(json(invalidCPF)))
				.andDo(print()).andExpect(status().isUnprocessableEntity()));

		checkXss(mockMvc.perform(post("/recipient")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(json(invalidCNPJ)))
				.andDo(print()).andExpect(status().isUnprocessableEntity()));
	}

}
