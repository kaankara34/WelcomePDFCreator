package com.aegon.getwelcomepdf.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity(name = "api_log")
@Table(name = "api_log")
public class ApiLogModel implements Serializable {
	private static final long serialVersionUID = 248687438843297736L;
	@Id
	//@GeneratedValue(strategy = GenerationType.IDENTITY)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="API_LOG_SEQ_GEN")
	@SequenceGenerator(name="API_LOG_SEQ_GEN", sequenceName = "API_LOG_SEQ", allocationSize=1)
	private Long logId;
	private Long makbuzId;
	private Date dtReport;
	private Date logTime;
	private Integer isThereError;
	private Integer errorCode;
	private Long inptPolid;
	private String detail;
	private String inptAgentName;
	private String inptGsmphone;
	private String errMsg;
	private String returnVal;

}
