package com.core.microbill.management.infrastructure.adapter.out.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CustomerJpaRepository extends JpaRepository<CustomerEntity, Long> {
    
    boolean existsByDocNumber(String docNumber);
    
    @Query("SELECT c FROM CustomerEntity c WHERE " +
           "(:search IS NULL OR :search = '' OR " +
           "LOWER(c.name) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(c.email) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(c.docNumber) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<CustomerEntity> findAllWithSearch(@Param("search") String search, Pageable pageable);
}
