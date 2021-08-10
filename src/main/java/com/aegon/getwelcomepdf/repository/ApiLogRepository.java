package com.aegon.getwelcomepdf.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.aegon.getwelcomepdf.entity.ApiLogModel;

@Repository
public interface ApiLogRepository extends JpaRepository<ApiLogModel, Long> {
   
}
