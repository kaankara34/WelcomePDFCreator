package com.aegon.getwelcomepdf.model;
import java.io.Serializable;

import javax.validation.constraints.NotNull;



public class User implements Serializable {

	private static final long serialVersionUID = -4569359012875175263L;
	@NotNull
	private String username;
	@NotNull
	private String password;
	
	public User(@NotNull String username, @NotNull String password) {
		super();
		this.username = username;
		this.password = password;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
}
