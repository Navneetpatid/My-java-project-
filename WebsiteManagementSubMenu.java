package com.janaushadhi.adminservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
@Data
@Entity
public class WebsiteManagementSubMenu {
	@Id
	@GeneratedValue(strategy =  GenerationType.IDENTITY)
	private Long id;
	private Short isBanner;
	private Short isTender;
	private Short isImage;
	private Short isProduct;
	private Short isKendra;
	private Short isLocate;
	private Short isPharmacist;
	private Short isContact;
	private Short isTeam;
	private Short isNews;
	private Short isReports;
}
