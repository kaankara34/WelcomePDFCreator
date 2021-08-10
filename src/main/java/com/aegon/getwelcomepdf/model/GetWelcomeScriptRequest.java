package com.aegon.getwelcomepdf.model;

import java.io.Serializable;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class GetWelcomeScriptRequest implements Serializable {

	private static final long serialVersionUID = -5883262811222969458L;
	private Long polid;
	private String agentName;
	private String agentSurname;
	private String GSMphone;
}
