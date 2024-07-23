package com.eazybites.accounts.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(
		name = "Accounts",
		description = "Schema to hold accounts information of a customer"
)
public class AccountsDto {

	@NotEmpty(message = "AccountNumber can not be a null or empty")
	@Pattern(regexp = "(^$|[0-9]{10})", message = "Account number must be 10 digits")
	@Schema(
			description = "Account number of a customer", example = "3234525463"
	)
	private Long accountNumber;

	@NotEmpty(message = "AccountType can not be a null or empty")
	@Schema(
			description = "Account type of a customer", example = "Savings"
	)
	private String accountType;

	@NotEmpty(message = "BranchAddress can not be a null or empty")
	@Schema(
			description = "Branch address of a customer", example = "123 st, Chennai"
	)
	private String branchAddress;
}
