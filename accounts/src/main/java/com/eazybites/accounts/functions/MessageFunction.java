package com.eazybites.accounts.functions;

import com.eazybites.accounts.service.IAccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
@Slf4j
public class MessageFunction {

    @Bean
    public Consumer<Long> updateAccount(IAccountService accountService){
        return accountNumber ->{
            log.info("Account number received from message stream via sms");
            boolean updatedStatus = accountService.updateAccountsForCommunication(accountNumber);
            log.info("Is Account successfully updated = {}", updatedStatus);
        };
    }
}
