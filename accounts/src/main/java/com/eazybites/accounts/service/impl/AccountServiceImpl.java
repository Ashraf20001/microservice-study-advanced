package com.eazybites.accounts.service.impl;

import com.eazybites.accounts.constants.AccountsConstants;
import com.eazybites.accounts.dto.AccountsDto;
import com.eazybites.accounts.dto.CustomerDto;
import com.eazybites.accounts.entity.AccountsEntity;
import com.eazybites.accounts.entity.CustomerEntity;
import com.eazybites.accounts.exception.CustomerAlreadyExistException;
import com.eazybites.accounts.exception.ResourceNotFoundException;
import com.eazybites.accounts.mapper.AccountsMapper;
import com.eazybites.accounts.mapper.CustomerMapper;
import com.eazybites.accounts.repository.AccountsRepository;
import com.eazybites.accounts.repository.CustomerRepository;
import com.eazybites.accounts.service.IAccountService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
@AllArgsConstructor
public class AccountServiceImpl implements IAccountService {
	
	private AccountsRepository accountsRepository;
	
	private CustomerRepository customerRepository;

	/**
	 *@param customerDto
	 */
	@Override
	public void createAccountNumber(CustomerDto customerDto) {
			CustomerEntity customer = CustomerMapper.mapToCustomer(customerDto, new CustomerEntity());
		Optional<CustomerEntity> isCustomerExist = customerRepository.findByMobileNumber(customerDto.getMobileNumber());
		if(isCustomerExist.isPresent()){
			throw new CustomerAlreadyExistException("Customer "+customerDto.getName()+" is already present");
		}
		CustomerEntity customerSavedRepo =customerRepository.save(customer);
			accountsRepository.save(createAccount(customerSavedRepo));
	}

	/**
	 * @param mobileNumber
	 * @return
	 */
	@Override
	public CustomerDto fetchCustomerDetail(String mobileNumber) {

		CustomerEntity customer = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(() -> {
			return new ResourceNotFoundException("Customer","Mobile Number",mobileNumber);
		});
		AccountsEntity accounts = accountsRepository.findByCustomerId(customer.getCustomerId()).orElseThrow(() -> {
			return new ResourceNotFoundException("Accounts", "Customer id", customer.getCustomerId().toString());
		});

		CustomerDto customerDto = CustomerMapper.mapToCustomerDto(customer, new CustomerDto());
		customerDto.setAccountsDto(AccountsMapper.mapToAccountsDto(accounts,new AccountsDto()));
		return customerDto;
	}

    /**
     * @param customerDto - CustomerDto Object
     * @return
     */
    @Override
    public boolean updateAccount(CustomerDto customerDto) {
		boolean isUpdated = false;
		AccountsDto accountsDto = customerDto.getAccountsDto();
		if(accountsDto !=null ){
			AccountsEntity accounts = accountsRepository.findById(accountsDto.getAccountNumber()).orElseThrow(
					() -> new ResourceNotFoundException("Account", "AccountNumber", accountsDto.getAccountNumber().toString())
			);
			AccountsMapper.mapToAccounts(accountsDto, accounts);
			accounts = accountsRepository.save(accounts);

			Long customerId = accounts.getCustomerId();
			CustomerEntity customer = customerRepository.findById(customerId).orElseThrow(
					() -> new ResourceNotFoundException("Customer", "CustomerID", customerId.toString())
			);
			CustomerMapper.mapToCustomer(customerDto,customer);
			customerRepository.save(customer);
			isUpdated = true;
		}
		return  isUpdated;
    }

	/**
	 * @param mobileNumber - Input Mobile Number
	 * @return boolean indicating if the delete of Account details is successful or not
	 */
	@Override
	public boolean deleteAccount(String mobileNumber) {
		CustomerEntity customer = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(
				() -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber)
		);
		accountsRepository.deleteByCustomerId(customer.getCustomerId());
		customerRepository.deleteById(customer.getCustomerId());
		return true;
	}


	/**
	 * @param customer
	 * @return
	 */
	private AccountsEntity createAccount(CustomerEntity customer) {
		AccountsEntity accountsEntity = new AccountsEntity();
		accountsEntity.setCustomerId(customer.getCustomerId());
		Long accNo=10000000L+ new Random().nextInt(900000);
		accountsEntity.setAccountNumber(accNo);
		accountsEntity.setAccountType(AccountsConstants.SAVINGS);
		accountsEntity.setBranchAddress(AccountsConstants.ADDRESS);
		return accountsEntity;
	}



}
