package com.aegon.getwelcomepdf.repository;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.OutputStream;
import java.sql.Clob;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;

import com.aegon.getwelcomepdf.entity.ApiLogModel;
import com.aegon.getwelcomepdf.model.GetWelcomeScriptResponse;
import com.aegon.getwelcomepdf.model.WelcomeData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

@Repository
public class WelcomeCallInfoCustomRepositoryImpl implements WelcomeCallInfoCustomRepository {
	@PersistenceContext
	private EntityManager entityManager;
	@Value(value ="${get.welcome.script.procedure}")
	private String getWelcomeProcedure;

	@Override
	public Map<String, Object> getWelcomeCallInfo(Long polId) {
		StoredProcedureQuery query = entityManager.createStoredProcedureQuery(getWelcomeProcedure);
		query.registerStoredProcedureParameter(0, Long.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(1, String.class, ParameterMode.OUT);
		query.registerStoredProcedureParameter(2, String.class, ParameterMode.OUT);
		query.registerStoredProcedureParameter(3, String.class, ParameterMode.OUT);
		query.registerStoredProcedureParameter(4, String.class, ParameterMode.OUT);
		query.registerStoredProcedureParameter(5, String.class, ParameterMode.OUT);
		query.registerStoredProcedureParameter(6, String.class, ParameterMode.OUT);
		query.registerStoredProcedureParameter(7, String.class, ParameterMode.OUT);
		query.registerStoredProcedureParameter(8, String.class, ParameterMode.OUT);
		query.registerStoredProcedureParameter(9, String.class, ParameterMode.OUT);
		query.registerStoredProcedureParameter(10, String.class, ParameterMode.OUT);
		query.registerStoredProcedureParameter(11, String.class, ParameterMode.OUT);
		query.registerStoredProcedureParameter(12, String.class, ParameterMode.OUT);
		query.registerStoredProcedureParameter(13, String.class, ParameterMode.OUT);
		query.registerStoredProcedureParameter(14, String.class, ParameterMode.OUT);
		query.registerStoredProcedureParameter(15, String.class, ParameterMode.OUT);
		query.registerStoredProcedureParameter(16, Long.class, ParameterMode.OUT);
		query.registerStoredProcedureParameter(17, String.class, ParameterMode.OUT);
		query.registerStoredProcedureParameter(18, String.class, ParameterMode.OUT);
		query.registerStoredProcedureParameter(19, String.class, ParameterMode.OUT);
		query.registerStoredProcedureParameter(20, Timestamp.class, ParameterMode.OUT);
		query.registerStoredProcedureParameter(21, Long.class, ParameterMode.OUT);
		query.registerStoredProcedureParameter(22, String.class, ParameterMode.OUT);
		query.registerStoredProcedureParameter(23, Timestamp.class, ParameterMode.OUT);
		query.registerStoredProcedureParameter(24, String.class, ParameterMode.OUT);
		query.registerStoredProcedureParameter(25, Double.class, ParameterMode.OUT);
		query.registerStoredProcedureParameter(26, String.class, ParameterMode.OUT);
		query.registerStoredProcedureParameter(27, Timestamp.class, ParameterMode.OUT);
		query.registerStoredProcedureParameter(28, Clob.class, ParameterMode.OUT);
		query.registerStoredProcedureParameter(29, Long.class, ParameterMode.OUT);
		query.registerStoredProcedureParameter(30, String.class, ParameterMode.OUT);
		query.registerStoredProcedureParameter(31, Long.class, ParameterMode.OUT);
		query.registerStoredProcedureParameter(32, Long.class, ParameterMode.OUT);
		query.registerStoredProcedureParameter(33, String.class, ParameterMode.OUT);
		query.registerStoredProcedureParameter(34, String.class, ParameterMode.OUT);
		query.registerStoredProcedureParameter(35, Timestamp.class, ParameterMode.OUT);
		query.registerStoredProcedureParameter(36, String.class, ParameterMode.OUT);
		query.registerStoredProcedureParameter(37, String.class, ParameterMode.OUT);
		query.registerStoredProcedureParameter(38, String.class, ParameterMode.OUT);
		query.registerStoredProcedureParameter(39, Long.class, ParameterMode.OUT);
		query.setParameter(0, polId);
		query.execute();
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("sigortaEttrenTck", query.getOutputParameterValue(1));
		resultMap.put("sigortaEttirenAd", query.getOutputParameterValue(2));
		resultMap.put("sigortaEttirenSoyad", query.getOutputParameterValue(3));
		resultMap.put("sigortaEttirenPid", query.getOutputParameterValue(4));
		resultMap.put("sigortaliTck", query.getOutputParameterValue(5));
		resultMap.put("sigortaliAd", query.getOutputParameterValue(6));
		resultMap.put("sigortaliSoyad", query.getOutputParameterValue(7));
		resultMap.put("policeNo", query.getOutputParameterValue(8));
		resultMap.put("aciklama", query.getOutputParameterValue(9));
		resultMap.put("urunTip", query.getOutputParameterValue(10));
		resultMap.put("tahsTip", query.getOutputParameterValue(11));
		resultMap.put("tarifeNo", query.getOutputParameterValue(12));
		resultMap.put("urunAdi", query.getOutputParameterValue(13));
		resultMap.put("bransKod", query.getOutputParameterValue(14));
		resultMap.put("email", query.getOutputParameterValue(15));
		resultMap.put("tahsTutar", query.getOutputParameterValue(16));
		resultMap.put("odeTip", query.getOutputParameterValue(17));
		resultMap.put("odeFrekans", query.getOutputParameterValue(18));
		resultMap.put("hataMes", query.getOutputParameterValue(19));
		resultMap.put("polBasTar", query.getOutputParameterValue(20));
		resultMap.put("polDuration", query.getOutputParameterValue(21));
		resultMap.put("basvuruNo", query.getOutputParameterValue(22));
		resultMap.put("polBitTar", query.getOutputParameterValue(23));
		resultMap.put("outSatisKanali", query.getOutputParameterValue(24));
		resultMap.put("yillikPrim", query.getOutputParameterValue(25));
		resultMap.put("oran", query.getOutputParameterValue(26));
		resultMap.put("caymaTar", query.getOutputParameterValue(27));
		resultMap.put("teminatlar", query.getOutputParameterValue(28));
		resultMap.put("hktdmTutar", query.getOutputParameterValue(29));
		resultMap.put("anaTeminatIsmi", query.getOutputParameterValue(30));
		resultMap.put("faizOran", query.getOutputParameterValue(31));
		resultMap.put("geriodemeyil", query.getOutputParameterValue(32));
		resultMap.put("adres", query.getOutputParameterValue(33));
		resultMap.put("adresIl", query.getOutputParameterValue(34));
		resultMap.put("dogTar", query.getOutputParameterValue(35));
		resultMap.put("dovKod", query.getOutputParameterValue(36));
		resultMap.put("bonoNo", query.getOutputParameterValue(37));
		resultMap.put("bonovadetar", query.getOutputParameterValue(38));
		resultMap.put("sonZeylNo", query.getOutputParameterValue(39));

		return resultMap;
	}



}
