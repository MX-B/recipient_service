package io.gr1d.payments.recipient.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Setter
@Entity
@Table(name = "bank")
public class Bank {

	@Id
	@JsonIgnore
	private Long id;

	@ApiModelProperty(notes = "Bank code to be used on Recipient")
	@Column(name = "bank_code", nullable = false)
	private String bankCode;

	@ApiModelProperty(notes = "Bank name")
	@Column(name = "name", nullable = false)
	private String name;

}
