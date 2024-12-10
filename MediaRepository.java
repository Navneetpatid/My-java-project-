package com.janaushadhi.adminservice.repository;

import com.janaushadhi.adminservice.entity.MediaCalender;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MediaRepository extends JpaRepository<MediaCalender,Long> {
    List<MediaCalender> findAllByMonth(int month, Sort sort);

    List<MediaCalender> findAllByYearAndMonth(int year, String month, Sort sort);

   Page<MediaCalender> findAllByYearAndMonth(int year, String month, Pageable pageable);
}
