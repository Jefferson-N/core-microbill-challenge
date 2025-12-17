package com.core.microbill.billing.infrastructure.adapter.out.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface InvoiceJpaRepository extends JpaRepository<InvoiceEntity, Long> {
    
    @Query("SELECT i FROM InvoiceEntity i WHERE :customerId IS NULL OR i.customerId = :customerId")
    Page<InvoiceEntity> findAllByCustomerId(@Param("customerId") Long customerId, Pageable pageable);
}
