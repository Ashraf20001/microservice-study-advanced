package com.eazybites.accounts.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.eazybites.accounts.entity.CustomerEntity;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<CustomerEntity, Long>{  // using this interface, you can invoke spring data jpa methods to interact with database.
        Optional<CustomerEntity> findByMobileNumber(String mobileNumber);
}
