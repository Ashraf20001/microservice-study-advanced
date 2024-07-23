package com.eazybites.accounts.audit;

import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component("customAuditImpl")
public class CustomAudit implements AuditorAware<String> {
    /**
     *
     * @return the current auditor
     */
    @Override
    public Optional<String> getCurrentAuditor() {
        return Optional.of("ACCOUNTS_AUDIT");
    }
}
