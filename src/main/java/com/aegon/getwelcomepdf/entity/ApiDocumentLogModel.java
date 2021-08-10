package com.aegon.getwelcomepdf.entity;

import java.io.Serializable;
import java.sql.Blob;
import java.util.Date;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity(name = "API_DOCUMENT_LOG")
@Table(name = "api_document_log")
public class ApiDocumentLogModel implements Serializable {
	private static final long serialVersionUID = 246197438843297736L;
	
	@Id
	@Column(name="CD_API_LOG_ID")
	private Long cdApiLogId;
	@Column(name="DT_CREATE_DATE")
	private Date dtCreateDate;
	@Column(name="CD_POLID")
	private Long cdPolid;
	@Column(name="CH__FILENAME")
	private String chFilename;
	@Column(name="CB__DOC",length=100000)
	@Lob
	private byte[] cbDoc;
	

}
