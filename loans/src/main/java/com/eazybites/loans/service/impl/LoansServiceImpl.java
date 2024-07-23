package com.eazybites.loans.service.impl;

import com.eazybites.loans.constants.LoansConstants;
import com.eazybites.loans.dto.LoansDto;
import com.eazybites.loans.entity.Loans;
import com.eazybites.loans.exception.LoanAlreadyExistException;
import com.eazybites.loans.exception.ResourceNotFoundException;
import com.eazybites.loans.mapper.LoansMapper;
import com.eazybites.loans.repository.LoansRepository;
import com.eazybites.loans.service.ILoansService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
@AllArgsConstructor
public class LoansServiceImpl implements ILoansService {

    private LoansRepository repository;
    /**
     * @param mobileNumber - Mobile Number of the Customer
     */
    @Override
    public void createLoan(String mobileNumber) {
        Optional<Loans> existingLoan = repository.findByMobileNumber(mobileNumber);
        if(existingLoan.isPresent()){
            throw new LoanAlreadyExistException("Loan exist already");
        }

        Loans loans = new Loans();
        loans.setMobileNumber(mobileNumber);
        long loanNumber = 100000000000L + new Random().nextInt(999999999);
        loans.setLoanNumber(Long.toString(loanNumber));
        loans.setAmountPaid(0);
        loans.setLoanType(LoansConstants.HOME_LOAN);
        loans.setTotalLoan(LoansConstants.NEW_LOAN_LIMIT);
        loans.setOutstandingAmount(LoansConstants.NEW_LOAN_LIMIT);
        repository.save(loans);

    }

    /**
     * @param mobileNumber - Input mobile Number
     * @return
     */
    @Override
    public LoansDto fetchLoan(String mobileNumber) {

        Loans loans = repository.findByMobileNumber(mobileNumber).orElseThrow(() -> {
            return new ResourceNotFoundException("Loan","mobileNumber",mobileNumber);
        });
        LoansDto loansDto = new LoansDto();
        LoansMapper.mapEntityToDto(loans, loansDto);

        return loansDto;
    }

    /**
     * @param loansDto - LoansDto Object
     * @return
     */
    @Override
    public boolean updateLoan(LoansDto loansDto) {
        Loans loans = repository.findByLoanNumber(loansDto.getLoanNumber()).orElseThrow(() -> {
            return new ResourceNotFoundException("LoansDto", "loanNumber", loansDto.getLoanNumber());
        });
        LoansMapper.mapDtoToEntity(loansDto,loans);
        repository.save(loans);
        return true;
    }

    /**
     * @param mobileNumber - Input Mobile Number
     * @return
     */
    @Override
    public boolean deleteLoan(String mobileNumber) {
        Loans loans = repository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException("Loan", "mobileNumber", mobileNumber)
        );
        repository.deleteById(loans.getLoanId());
        return true;
    }
}
