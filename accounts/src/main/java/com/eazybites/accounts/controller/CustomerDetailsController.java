package com.eazybites.accounts.controller;

import com.eazybites.accounts.dto.CustomerDetailsDto;
import com.eazybites.accounts.dto.CustomerDto;
import com.eazybites.accounts.dto.ErrorResponseDto;
import com.eazybites.accounts.service.CustomerDetailsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(
        name = "RestAPIs for CustomerDetailsFetch Operation",
        description = "This controller provides all HTTP method handlers for CustomerDetails"
)
@RestController
@RequestMapping(path = "/api", produces = {MediaType.APPLICATION_JSON_VALUE})
@RequiredArgsConstructor
@Validated
public class CustomerDetailsController {

    private final CustomerDetailsService customerDetailsService;

    @Operation(
            summary = "Get api for customer details with accounts , loans and cards",
            description = "This api will be useful for fetching customer details of all microservices"
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
    @GetMapping("/fetchCustomerDetails")
    public ResponseEntity<CustomerDetailsDto> fetchCustomerDetails(@RequestParam @Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile number must be 10 digits") String mobileNumber){

        CustomerDetailsDto customerDetailsDto = customerDetailsService.fetchCustomerDetaisDto(mobileNumber);
        return new ResponseEntity<>(customerDetailsDto, HttpStatus.OK);
    }

}
