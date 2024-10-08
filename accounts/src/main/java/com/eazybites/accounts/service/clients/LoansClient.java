package com.eazybites.accounts.service.clients;

import com.eazybites.accounts.dto.LoansDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

// name inside the Feign client must match with eureka info name
@FeignClient(value = "loans",fallback = LoansCallback.class)
public interface LoansClient {
    @GetMapping(value = "/api/fetch",consumes = "application/json")
    public ResponseEntity<LoansDto> fetchLoanDetails(@RequestHeader("microservice-correlation-id") String correlationId, @RequestParam String mobileNumber);
}
