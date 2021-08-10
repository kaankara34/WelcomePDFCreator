package com.aegon.getwelcomepdf.repository;

import com.aegon.getwelcomepdf.entity.ApiDocumentLogModel;
import com.aegon.getwelcomepdf.entity.WelcomeCallAddressModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WelcomeCallAddressRepository extends JpaRepository<WelcomeCallAddressModel, Long> {
    List<WelcomeCallAddressModel> getAllByCdTc(Long cdTc);
}
