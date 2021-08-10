package com.aegon.getwelcomepdf.repository;

import com.aegon.getwelcomepdf.entity.EDeliveryPDFCoordinatesModel;
import com.aegon.getwelcomepdf.entity.PdfFormatsModel;
import com.aegon.getwelcomepdf.entity.WelcomeCallAddressModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EDeliveryPDFCoordinatesRepository extends JpaRepository<EDeliveryPDFCoordinatesModel,String> {

    List<EDeliveryPDFCoordinatesModel> findAllByProjectAndProductNameAndIsvalid(String projectName, String productName, Integer isValid);
}
