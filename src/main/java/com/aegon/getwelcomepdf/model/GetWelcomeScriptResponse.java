package com.aegon.getwelcomepdf.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class GetWelcomeScriptResponse {
	private static final long serialVersionUID = 2818301496513572827L;
	private Integer isThereError;
	private String errorText;
	private Double error_code;
	private byte [] Value;
	

}
