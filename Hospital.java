package com.janaushadhi.adminservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Hospital {
	@Id
	@GeneratedValue(strategy =  GenerationType.IDENTITY)
	private Long id;
	private Long hsplno;
	private String hsplid;
	private String hsplType;
	private String state;
	private String district;
	private String hsplName;
	private String address;
	private String pincode;
	private String currentStatus;
	private String creationDate;
	private String updationDate;
}

