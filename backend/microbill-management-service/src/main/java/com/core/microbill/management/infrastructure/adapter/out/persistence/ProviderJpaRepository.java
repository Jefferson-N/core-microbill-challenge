package com.core.microbill.management.infrastructure.adapter.out.persistence;

import com.core.microbill.management.infrastructure.adapter.out.entities.ProviderEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProviderJpaRepository extends JpaRepository<ProviderEntity, Long> {
    
    boolean existsByTaxId(String taxId);
    
    @Query("SELECT p FROM ProviderEntity p WHERE " +
           "(:search IS NULL OR :search = '' OR " +
           "LOWER(p.name) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(p.email) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(p.taxId) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<ProviderEntity> findAllWithSearch(@Param("search") String search, Pageable pageable);
}
