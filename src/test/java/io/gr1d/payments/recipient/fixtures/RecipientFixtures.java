package io.gr1d.payments.recipient.fixtures;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;
import io.gr1d.payments.recipient.request.CreateRecipientRequest;

public class RecipientFixtures implements TemplateLoader {

	@Override
	public void load() {
		Fixture.of(CreateRecipientRequest.class).addTemplate("valid", new Rule() {
			{
				add("name", name());
				add("bankCode", random("001", "003", "033"));
				add("documentNumber", random("84155023032", "51682110036", "30259637009"));
				add("bankAccount", random("18549", "36945", "28452", "29587"));
				add("bankAccountVerification", random("1", "2", "3", "4", "5", "6", "7", "8", "9"));
				add("agency", random("9548", "2687", "3951", "1059"));
				add("agencyVerification", random("1", "2", "3", "4", "5", "6", "7", "8", "9"));
				add("bankAccountType", "CONTA_CORRENTE");

				add("transferInterval", random("WEEKLY", "MONTHLY"));
				add("transferDay", random(1,2,3,4,5));
				add("transferEnabled", random(true, false));
			}
		});
		Fixture.of(CreateRecipientRequest.class).addTemplate("invalid_documentNumber").inherits("valid", new Rule() {
			{
				add("documentNumber", "12345678911");
			}
		});
		Fixture.of(CreateRecipientRequest.class).addTemplate("invalid_bankAccount").inherits("valid", new Rule() {
			{
				add("bankAccount", "489489789789789789789");
			}
		});
		Fixture.of(CreateRecipientRequest.class).addTemplate("invalid_agency").inherits("valid", new Rule() {
			{
				add("agency", "1");
			}
		});
		Fixture.of(CreateRecipientRequest.class).addTemplate("invalid_agencyVerification").inherits("valid", new Rule() {
			{
				add("agencyVerification", "3143");
			}
		});
		Fixture.of(CreateRecipientRequest.class).addTemplate("invalid_bankCode").inherits("valid", new Rule() {
			{
				add("bankCode", "1");
			}
		});
		Fixture.of(CreateRecipientRequest.class).addTemplate("invalid_bankAccountType").inherits("valid", new Rule() {
			{
				add("bankAccountType", "TIPO_INVALIDO");
			}
		});

		Fixture.of(CreateRecipientRequest.class).addTemplate("invalid_documentNumberCPF").inherits("valid", new Rule() {
			{
				add("documentNumber", "12345678900");
			}
		});

		Fixture.of(CreateRecipientRequest.class).addTemplate("invalid_documentNumberCNPJ").inherits("valid", new Rule() {
			{
				add("documentNumber", "11111111111111");
			}
		});
	}
	
}
