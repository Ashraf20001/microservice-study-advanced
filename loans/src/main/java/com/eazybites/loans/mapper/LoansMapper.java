package com.eazybites.loans.mapper;

import com.eazybites.loans.dto.LoansDto;
import com.eazybites.loans.entity.Loans;

public class LoansMapper {
    public static Loans mapDtoToEntity(LoansDto dto, Loans entity) {
        entity.setLoanType(dto.getLoanType());
        entity.setTotalLoan(dto.getTotalLoan());
        entity.setLoanNumber(dto.getLoanNumber());
        entity.setMobileNumber(dto.getMobileNumber());
        entity.setAmountPaid(dto.getAmountPaid());
        entity.setOutstandingAmount(dto.getOutstandingAmount());
        return entity;
    }

    public static LoansDto mapEntityToDto(Loans entity, LoansDto dto) {
        dto.setLoanNumber(entity.getLoanNumber());
        dto.setLoanType(entity.getLoanType());
        dto.setTotalLoan(entity.getTotalLoan());
        dto.setAmountPaid(entity.getAmountPaid());
        dto.setMobileNumber(entity.getMobileNumber());
        dto.setOutstandingAmount(entity.getOutstandingAmount());
        return dto;
    }
}
