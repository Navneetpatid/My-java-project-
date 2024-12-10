package com.janaushadhi.adminservice.entity;

import java.util.Date;

import jakarta.persistence.*;
import lombok.Data;
@Data
@Entity
//public class AnnualAndFinancialReport {
public class WhatsNewAndNewsLatter {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String title;
	@Lob
	private String discription;
	private String type;
	private String docFile;
	private String image;
	private Integer status;
	private Date createdDate;
	private Date updatedDate;
}
