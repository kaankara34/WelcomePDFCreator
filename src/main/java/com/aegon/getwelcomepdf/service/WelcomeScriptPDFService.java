package com.aegon.getwelcomepdf.service;

import com.aegon.getwelcomepdf.entity.ApiCallAddressModel;
import com.aegon.getwelcomepdf.entity.ApiLogModel;
import com.aegon.getwelcomepdf.entity.EDeliveryPDFCoordinatesModel;
import com.aegon.getwelcomepdf.model.GetWelcomeScriptRequest;
import com.aegon.getwelcomepdf.model.GetWelcomeScriptResponse;
import com.aegon.getwelcomepdf.model.WelcomeData;
import com.itextpdf.text.DocumentException;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

public interface WelcomeScriptPDFService {
    public GetWelcomeScriptResponse getWelcomeScriptPDFParameters(GetWelcomeScriptRequest getWelcomeScriptRequest) throws Exception;
    public WelcomeData formatGetWelcomeCallInfo(Map<String, Object> parameterList, Long polid, ApiLogModel apiLogModel) throws ParseException;
}
