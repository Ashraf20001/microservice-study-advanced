package com.eazybites.accounts.dto;

/**
 * @param accountNumber
 * @param name
 * @param email
 * @param mobileNumber
 */
public record MessageDto(Long accountNumber, String name, String email, String mobileNumber) {
}
