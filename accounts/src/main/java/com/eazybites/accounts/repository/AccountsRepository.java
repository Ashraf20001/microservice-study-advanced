package com.eazybites.accounts.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import com.eazybites.accounts.entity.AccountsEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface AccountsRepository extends JpaRepository<AccountsEntity, Long>{  // using this interface, you can invoke spring data jpa methods to interact with database.
        Optional<AccountsEntity> findByCustomerId(Long customerId);

        @Transactional
        @Modifying
        void deleteByCustomerId(Long customerId);
}
