package com.janaushadhi.adminservice.entity;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
@Data
@Entity
public class AnnualAndFinancialReport {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String title;
	private String discription;
	private String reportType;
	private String docFile;
	private Integer status;
	private Date createdDate;
	private Date updatedDate;
}
