package com.janaushadhi.adminservice.repository;

import com.janaushadhi.adminservice.entity.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseRepository  extends JpaRepository<Purchase,Long> {
}
