package com.janaushadhi.adminservice.repository;

import com.janaushadhi.adminservice.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {
    Optional<Role> findById(Integer roleid);

    //  Page<Admin> findBySearchTextWithPagination(String searchText, Pageable pageable);
}
