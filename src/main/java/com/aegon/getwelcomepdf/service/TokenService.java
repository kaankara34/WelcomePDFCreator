package com.aegon.getwelcomepdf.service;

public interface TokenService {
	String authenticateUser(String username, String password, String grant_type) throws Exception;

}
