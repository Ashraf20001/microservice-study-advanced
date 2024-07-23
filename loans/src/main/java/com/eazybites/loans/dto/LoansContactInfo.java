package com.eazybites.loans.dto;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;
import java.util.Map;

@ConfigurationProperties(prefix = "loans")
@Data
public class LoansContactInfo{
    private String message;
    private Map<String, Object> contactDetails;
    private List<String> onCallSupport;
}
