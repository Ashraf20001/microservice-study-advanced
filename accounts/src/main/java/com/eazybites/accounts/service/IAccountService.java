package com.eazybites.accounts.service;

import com.eazybites.accounts.dto.CustomerDto;

public interface IAccountService {
		
	/**
	 * @param customerDto
	 */
	public void createAccountNumber(CustomerDto customerDto);

	/**
	 *
	 * @param mobileNumber
	 * @return CustomerDto
	 */
	   public CustomerDto fetchCustomerDetail(String mobileNumber);

	/**
	 *
	 * @param customerDto - CustomerDto Object
	 * @return boolean indicating if the update of Account details is successful or not
	 */
		public boolean updateAccount(CustomerDto customerDto);


	/**
	 *
	 * @param mobileNumber - Input Mobile Number
	 * @return boolean indicating if the delete of Account details is successful or not
	 */
		public boolean deleteAccount(String mobileNumber);


	/**
	 * @param accountNumber
	 * @return
	 */
		public boolean updateAccountsForCommunication(Long accountNumber);
}
