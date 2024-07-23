package com.eazybites.loans.audit;

import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component("auditImpl")
public class ApplicationAuditor implements AuditorAware<String> {

    /**
     * @return
     */
    @Override
    public Optional<String> getCurrentAuditor() {
        return Optional.of("LOANS_AUDIT");
    }
}
