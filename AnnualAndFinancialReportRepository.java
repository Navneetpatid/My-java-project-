package com.janaushadhi.adminservice.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.janaushadhi.adminservice.entity.AnnualAndFinancialReport;

@Repository
public interface AnnualAndFinancialReportRepository extends JpaRepository<AnnualAndFinancialReport, Long> {

	List<AnnualAndFinancialReport> findAllByReportTypeAndStatusOrderByIdDesc(String type,int i);

	List<AnnualAndFinancialReport> findAllByReportTypeAndTitleContainingIgnoreCaseAndStatusOrderByIdDesc(
			String reportType, String title, int i);

    List<AnnualAndFinancialReport> findAllByReportTypeAndStatus(String reportType, int i);

	Page<AnnualAndFinancialReport> findAllByReportTypeAndTitleContainingIgnoreCaseAndStatusOrderByIdDesc(
			String reportType, String title, int i, Pageable pageable);

	Page<AnnualAndFinancialReport> findAllByReportTypeAndStatusOrderByIdDesc(String reportType, int i,
			Pageable pageable);

	Page<AnnualAndFinancialReport> findAllByReportTypeAndStatus(String reportType, Integer status, Pageable pageable);

}
