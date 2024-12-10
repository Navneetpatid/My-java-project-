package com.janaushadhi.adminservice.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import com.janaushadhi.adminservice.entity.WhatsNewAndNewsLatter;

@Repository
public interface WhatsNewAndNewsLatterRepository extends JpaRepository<WhatsNewAndNewsLatter, Long> {

	List<WhatsNewAndNewsLatter> findAllByTypeAndStatusOrderByIdDesc(String type, int i);

    List<WhatsNewAndNewsLatter> findAllByTypeAndTitleContainingIgnoreCaseAndStatusOrderByIdDesc(String type, String searchByTitle, int i);

	Page<WhatsNewAndNewsLatter> findAllByTypeAndTitleContainingIgnoreCaseAndStatusOrderByIdDesc(String type,
			String searchByTitle, int i, Pageable pageable);

	Page<WhatsNewAndNewsLatter> findAllByTypeAndStatusOrderByIdDesc(String type, int i, Pageable pageable);
}
