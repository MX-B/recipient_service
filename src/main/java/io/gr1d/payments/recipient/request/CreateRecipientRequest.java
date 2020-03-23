package io.gr1d.payments.recipient.request;

import io.gr1d.core.validation.CPFCNPJ;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Getter@Setter
@ToString(callSuper = true)
public class CreateRecipientRequest extends RecipientRequest {

    @NotEmpty
    @Pattern.List({
            @Pattern(regexp = "([0-9]{11})|([0-9]{14})"),
            @Pattern(regexp = "^(?:(?!00000000000).)*$"),
            @Pattern(regexp = "^(?:(?!11111111111).)*$"),
            @Pattern(regexp = "^(?:(?!22222222222).)*$"),
            @Pattern(regexp = "^(?:(?!33333333333).)*$"),
            @Pattern(regexp = "^(?:(?!44444444444).)*$"),
            @Pattern(regexp = "^(?:(?!55555555555).)*$"),
            @Pattern(regexp = "^(?:(?!66666666666).)*$"),
            @Pattern(regexp = "^(?:(?!77777777777).)*$"),
            @Pattern(regexp = "^(?:(?!88888888888).)*$"),
            @Pattern(regexp = "^(?:(?!99999999999).)*$")
    })
    @CPFCNPJ
    @ApiModelProperty(required = true, notes = "CPF or CNPJ number")
    private String documentNumber;

}
