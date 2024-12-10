package com.janaushadhi.adminservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "upload_image_hindi")
public class UploadImageHindi {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "name")
	private String name;

	@Column(name = "title")
	private String title;

	@Column(name = "designation")
	private String designation;

	@Lob
	@Column(name = "description", columnDefinition = "Text")
	private String description;

}
