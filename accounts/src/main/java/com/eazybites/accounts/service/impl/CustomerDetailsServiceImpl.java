package com.eazybites.accounts.service.impl;

import com.eazybites.accounts.dto.AccountsDto;
import com.eazybites.accounts.dto.CardsDto;
import com.eazybites.accounts.dto.CustomerDetailsDto;
import com.eazybites.accounts.dto.LoansDto;
import com.eazybites.accounts.entity.AccountsEntity;
import com.eazybites.accounts.entity.CustomerEntity;
import com.eazybites.accounts.exception.ResourceNotFoundException;
import com.eazybites.accounts.mapper.AccountsMapper;
import com.eazybites.accounts.mapper.CustomerMapper;
import com.eazybites.accounts.repository.AccountsRepository;
import com.eazybites.accounts.repository.CustomerRepository;
import com.eazybites.accounts.service.CustomerDetailsService;
import com.eazybites.accounts.service.clients.CardsClient;
import com.eazybites.accounts.service.clients.LoansClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CustomerDetailsServiceImpl implements CustomerDetailsService {
    private final AccountsRepository accountsRepository;
    private final CustomerRepository customerRepository;
    private final LoansClient loansClient;
    private final CardsClient cardsClient;


    @Override
    public CustomerDetailsDto fetchCustomerDetaisDto(String correlationId,String mobileNumber) throws ResourceNotFoundException {
        CustomerEntity customer = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(() -> new ResourceNotFoundException("Customer","Mobile Number",mobileNumber));
        AccountsEntity accounts = accountsRepository.findByCustomerId(customer.getCustomerId()).orElseThrow(() -> new ResourceNotFoundException("Accounts", "Customer id", customer.getCustomerId().toString()));

        CustomerDetailsDto customerDetailsDto = CustomerMapper.mapToCustomerDetailsDto(customer, new CustomerDetailsDto());
        AccountsDto accountsDto = AccountsMapper.mapToAccountsDto(accounts, new AccountsDto());
        customerDetailsDto.setAccountsDto(accountsDto);
        ResponseEntity<LoansDto> loansDto = loansClient.fetchLoanDetails(correlationId,mobileNumber);   // Loans FeignClient
        customerDetailsDto.setLoansDto(loansDto.getBody());
        ResponseEntity<CardsDto> cardsDto = cardsClient.fetchCardDetails(correlationId,mobileNumber);   // Cards FeignClient
        customerDetailsDto.setCardsDto(cardsDto.getBody());
        return customerDetailsDto;
    }
}
