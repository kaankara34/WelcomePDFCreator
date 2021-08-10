package com.aegon.getwelcomepdf.model;

import java.io.Serializable;

import lombok.Data;

import lombok.NoArgsConstructor;

import lombok.AllArgsConstructor;

@Data
public class TokenResponse implements Serializable {
	
	private static final long serialVersionUID = 3552449560548247374L;
	private String access_token;
    private String token_type;
    private Long expires_in;
	public TokenResponse(String access_token, String token_type, Long expires_in) {
		super();
		this.access_token = access_token;
		this.token_type = token_type;
		this.expires_in = expires_in;
	}

}
