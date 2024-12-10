package com.janaushadhi.adminservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.janaushadhi.adminservice.entity.WebsiteManagementSubMenu;
@Repository
public interface WebsiteManagementSubMenuRepository extends JpaRepository<WebsiteManagementSubMenu, Long>{

}
