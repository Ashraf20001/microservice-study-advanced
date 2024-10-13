package com.eazybytes.message.functions;

import com.eazybytes.message.dto.MessageDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Function;

@Configuration
public class MessageFunction {
    public static final Logger LOGGER = LoggerFactory.getLogger(MessageFunction.class);

    @Bean
    public Function<MessageDto,MessageDto> email(){
        return email ->{
            LOGGER.info("Sending email ....");
            return email;
        };
    }

    @Bean
    public Function<MessageDto,Long> sms(){
        return sms ->{
            LOGGER.info("Sending sms ....");
            return sms.accountNumber();
        };
    }
}
