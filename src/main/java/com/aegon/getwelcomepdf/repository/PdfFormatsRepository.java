package com.aegon.getwelcomepdf.repository;



import com.aegon.getwelcomepdf.entity.PdfFormatsModel;
import com.aegon.getwelcomepdf.entity.WelcomeCallAddressModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface PdfFormatsRepository extends JpaRepository<PdfFormatsModel,String> {
    List<PdfFormatsModel> getAllByBranchCodeAndTarifeNoAndIsValid(String branchCode, String tarifeNo, Integer isValid);

}
