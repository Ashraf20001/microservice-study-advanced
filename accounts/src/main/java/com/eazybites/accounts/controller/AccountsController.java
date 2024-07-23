package com.eazybites.accounts.controller;

import com.eazybites.accounts.constants.AccountsConstants;
import com.eazybites.accounts.dto.AccountsContactInfo;
import com.eazybites.accounts.dto.CustomerDto;
import com.eazybites.accounts.dto.ErrorResponseDto;
import com.eazybites.accounts.dto.ResponseDto;
import com.eazybites.accounts.service.IAccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Tag(
        name = "CRUD RestAPIs for Accounts Microservice",
        description = "This controller provides all HTTP method handlers for Accounts Microservice"
)
@RestController
@RequestMapping(path = "/api", produces = {MediaType.APPLICATION_JSON_VALUE})
@RequiredArgsConstructor
@Validated                        // Ensure that every method of the rest controller is being validated
public class AccountsController {

    private final IAccountService accountService;

    @Value("${build.version}")
    private String buildVersion;

    private final Environment env;

    private final AccountsContactInfo accountsContactInfo;

    @Operation(
            summary = "Account creation method",
            description = "This method will be useful for creating new accounts"
    )
    @ApiResponses({

            @ApiResponse(
                    responseCode = "201",
                    description = AccountsConstants.MESSAGE_201

            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    })
    @PostMapping("/create")
    public ResponseEntity<ResponseDto> createAccount(@Valid @RequestBody CustomerDto customerDto) {

        accountService.createAccountNumber(customerDto);
//				return ResponseEntity
//						.status(HttpStatus.CREATED)
//						.body(new ResponseDto(AccountsConstants.STATUS_201,AccountsConstants.MESSAGE_201));
        return new ResponseEntity<>(new ResponseDto(AccountsConstants.STATUS_201, AccountsConstants.MESSAGE_201), HttpStatus.CREATED);
    }

    @Operation(
            summary = "Get api for customer details",
            description = "This api will be useful for fetching customer details"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK",
                    content = @Content(
                            schema = @Schema(implementation = CustomerDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    })
    @GetMapping("/fetch")
    public ResponseEntity<CustomerDto> fetchCustomerDetails(@RequestParam @Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile number must be 10 digits") String mobileNumber) {
        CustomerDto customerDto = accountService.fetchCustomerDetail(mobileNumber);
        return new ResponseEntity<>(customerDto, HttpStatus.OK);
    }

    @Operation(
            summary = "Update Account Details REST API",
            description = "REST API to update Customer &  Account details based on a account number"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "417",
                    description = "Expectation Failed"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    }
    )
    @PutMapping("/update")
    public ResponseEntity<ResponseDto> updateAccountDetails(@Valid @RequestBody CustomerDto customerDto) {
        boolean isUpdated = accountService.updateAccount(customerDto);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDto(AccountsConstants.STATUS_200, AccountsConstants.MESSAGE_200));
//				} else {
//					return ResponseEntity
//							.status(HttpStatus.EXPECTATION_FAILED)
//							.body(new ResponseDto(AccountsConstants.STATUS_417, AccountsConstants.MESSAGE_417_UPDATE));
//				}
    }


    @Operation(
            summary = "Delete Account & Customer Details REST API",
            description = "REST API to delete Customer &  Account details based on a mobile number"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "417",
                    description = "Expectation Failed"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    }
    )

    @DeleteMapping("/delete")
    public ResponseEntity<ResponseDto> deleteAccountDetails(@RequestParam
                                                            @Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile number must be 10 digits")
                                                            String mobileNumber) {
        boolean isDeleted = accountService.deleteAccount(mobileNumber);
        if (isDeleted) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseDto(AccountsConstants.STATUS_200, AccountsConstants.MESSAGE_200));
        } else {
            return ResponseEntity
                    .status(HttpStatus.EXPECTATION_FAILED)
                    .body(new ResponseDto(AccountsConstants.STATUS_417, AccountsConstants.MESSAGE_417_DELETE));
        }
    }

    @GetMapping("/build-version")
    public ResponseEntity<String> getBuildVersion(){
        return ResponseEntity.status(HttpStatus.OK).body(buildVersion);
    }

    @GetMapping("/java-version")
    public ResponseEntity<Object> getEnvironmentalVariable(){
        String chocolateyLastPathUpdate = env.getProperty("ChocolateyLastPathUpdate");
        return ResponseEntity.status(HttpStatus.OK).body(chocolateyLastPathUpdate);
    }

    @GetMapping("/contact-info")
    public ResponseEntity<Object> getDetails(){
        return ResponseEntity.status(HttpStatus.OK).body(accountsContactInfo);
    }
}
