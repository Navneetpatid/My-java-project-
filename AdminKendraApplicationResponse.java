package com.hsbc.hap.cer.repository;

import com.hsbc.hap.cer.entity.DmzLbMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DmzLbMasterRepository extends JpaRepository<DmzLbMaster, Long> {

    // Find by ID
    Optional<DmzLbMaster> findById(Long id);

    // Find by environment and region
    Optional<DmzLbMaster> findByEnvironmentAndRegion(String environment, String region);
}
