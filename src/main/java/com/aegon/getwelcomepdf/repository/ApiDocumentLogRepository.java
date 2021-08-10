package com.aegon.getwelcomepdf.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import com.aegon.getwelcomepdf.entity.ApiDocumentLogModel;

import java.util.List;

@Repository
public interface ApiDocumentLogRepository extends JpaRepository<ApiDocumentLogModel, Long> {

}
