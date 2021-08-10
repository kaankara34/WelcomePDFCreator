package com.aegon.getwelcomepdf.repository;

import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import java.util.Map;


public interface WelcomeCallInfoCustomRepository {

    Map<String, Object> getWelcomeCallInfo(Long polId);
}
