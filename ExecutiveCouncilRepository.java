package com.janaushadhi.adminservice.repository;

import com.janaushadhi.adminservice.entity.ExecutiveCouncil;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExecutiveCouncilRepository extends JpaRepository<ExecutiveCouncil,Long> {
}
