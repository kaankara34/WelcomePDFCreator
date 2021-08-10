package com.aegon.getwelcomepdf.repository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;
import java.sql.Clob;
import java.util.HashMap;
import java.util.Map;

@Repository
public class WelcomeCallSabitKurluCustomRepositoryImpl implements WelcomeCallSabitKurluCustomRepository {
    @PersistenceContext
    private EntityManager entityManager;
    @Value(value ="${get.welcome.sabit.script.procedure}")
    private String getWelcomeSabitProcedure;

    @Override
    public Map<String, Object> getWelcomeSabitKurlu(Long inpt_polid) {
        StoredProcedureQuery query = entityManager.createStoredProcedureQuery(getWelcomeSabitProcedure);
        query.registerStoredProcedureParameter(0, Long.class, ParameterMode.IN);
        query.registerStoredProcedureParameter(1, String.class, ParameterMode.OUT);
        query.registerStoredProcedureParameter(2, Long.class, ParameterMode.OUT);
        query.registerStoredProcedureParameter(3, Double.class, ParameterMode.OUT);
        query.registerStoredProcedureParameter(4, Double.class, ParameterMode.OUT);
        query.registerStoredProcedureParameter(5, String.class, ParameterMode.OUT);
        query.registerStoredProcedureParameter(6, Long.class, ParameterMode.OUT);
        query.registerStoredProcedureParameter(7, String.class, ParameterMode.OUT);
        query.registerStoredProcedureParameter(8, Long.class, ParameterMode.OUT);
        query.registerStoredProcedureParameter(9, Long.class, ParameterMode.OUT);
        query.registerStoredProcedureParameter(10, String.class, ParameterMode.OUT);
        query.registerStoredProcedureParameter(11, Clob.class, ParameterMode.OUT);
        query.registerStoredProcedureParameter(12, String.class, ParameterMode.OUT);
        query.registerStoredProcedureParameter(13, Long.class, ParameterMode.OUT);
        query.registerStoredProcedureParameter(14, Long.class, ParameterMode.OUT);
        query.registerStoredProcedureParameter(15, Long.class, ParameterMode.OUT);
        query.registerStoredProcedureParameter(16, String.class, ParameterMode.OUT);
        query.setParameter(0, inpt_polid);
        query.execute();
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("ek2", query.getOutputParameterValue(1));
        resultMap.put("dovTutar", query.getOutputParameterValue(2));
        resultMap.put("dovKur", query.getOutputParameterValue(3));
        resultMap.put("firstPremium", query.getOutputParameterValue(4));
        resultMap.put("tahsTip", query.getOutputParameterValue(5));
        resultMap.put("polDuration", query.getOutputParameterValue(6));
        resultMap.put("odeFrekans", query.getOutputParameterValue(7));
        resultMap.put("zeylYıllıkPrim", query.getOutputParameterValue(8));
        resultMap.put("tahsTutar", query.getOutputParameterValue(9));
        resultMap.put("anaTeminatIsmi", query.getOutputParameterValue(10));
        resultMap.put("teminatlar", query.getOutputParameterValue(11));
        resultMap.put("odeTip", query.getOutputParameterValue(12));
        resultMap.put("hktdmTutar", query.getOutputParameterValue(13));
        resultMap.put("zydonem", query.getOutputParameterValue(14));
        resultMap.put("aylıkPrim", query.getOutputParameterValue(15));
        resultMap.put("hataMes", query.getOutputParameterValue(16));
        return resultMap;

    }
}
