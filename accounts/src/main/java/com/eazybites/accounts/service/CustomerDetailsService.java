package com.eazybites.accounts.service;

import com.eazybites.accounts.dto.CustomerDetailsDto;
import com.eazybites.accounts.exception.ResourceNotFoundException;

public interface CustomerDetailsService {

        public CustomerDetailsDto fetchCustomerDetaisDto(String mobileNumber) throws ResourceNotFoundException;
}
