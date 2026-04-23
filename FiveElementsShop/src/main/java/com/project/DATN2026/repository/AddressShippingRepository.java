package com.project.DATN2026.repository;

import com.project.DATN2026.entity.AddressShipping;
import com.project.DATN2026.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AddressShippingRepository extends JpaRepository<AddressShipping, Long> {
    List<AddressShipping> findAllByCustomer_Account_Id(Long accountId);
}
