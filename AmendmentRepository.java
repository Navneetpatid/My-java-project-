package com.janaushadhi.adminservice.repository;

import com.janaushadhi.adminservice.entity.Amendment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AmendmentRepository extends JpaRepository<Amendment,Long> {
}
