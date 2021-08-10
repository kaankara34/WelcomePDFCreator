package com.aegon.getwelcomepdf.repository;

import com.aegon.getwelcomepdf.entity.ApiCallAddressModel;
import com.aegon.getwelcomepdf.entity.WelcomeCallAddressModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApiCallAddressRepository extends JpaRepository<ApiCallAddressModel, Long> {
    List<ApiCallAddressModel> getAllByCdTc(Long cdTc);
}
