package com.aegon.getwelcomepdf.controller;

import com.aegon.getwelcomepdf.service.WelcomeScriptPDFService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aegon.getwelcomepdf.model.GetWelcomeScriptRequest;
import com.aegon.getwelcomepdf.model.GetWelcomeScriptResponse;

@RestController
@AllArgsConstructor
public class WelcomeScriptPdfController {

    private final WelcomeScriptPDFService welcomeScriptPDFService;


    @GetMapping("/EDelivery_API/api/v1/GetWelcomeScriptPDF")
    public ResponseEntity<?> getPdf(@RequestParam(value = "polid", required = false) Long polid,
                                    @RequestParam(value = "agentName", required = false) String agentName,
                                    @RequestParam(value = "agentSurname", required = false) String agentSurname,
                                    @RequestParam(value = "GSMphone", required = false) String GSMphone) throws Exception {
        GetWelcomeScriptRequest getWelcomeScriptRequest = new GetWelcomeScriptRequest(polid, agentName, agentSurname,
                GSMphone);
        return new ResponseEntity<GetWelcomeScriptResponse>(
                welcomeScriptPDFService.getWelcomeScriptPDFParameters(getWelcomeScriptRequest), HttpStatus.OK);
    }


}
