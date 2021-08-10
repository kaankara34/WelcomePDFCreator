package com.aegon.getwelcomepdf.service.impl;

import com.aegon.getwelcomepdf.exception.BadRequestException;
import com.aegon.getwelcomepdf.service.TokenService;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.aegon.getwelcomepdf.jwt.JwtUtils;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {

	private static final String USERNAME_REQUIRED = "username alanının doldurulması zorunludur.";
	private static final String PASSWORD_REQUIRED = "password alanının doldurulması zorunludur.";
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private JwtUtils jwtUtils;

	@Override
	public String authenticateUser(String username, String password, String grant_type) throws Exception {
		if (Objects.isNull(username) || username.equals("")) {
			throw new BadRequestException(USERNAME_REQUIRED);
		}
		if (Objects.isNull(password) || password.equals("")) {
			throw new BadRequestException(PASSWORD_REQUIRED);
		}

		UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
				username, password);
		Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);

		SecurityContextHolder.getContext().setAuthentication(authentication);
		return jwtUtils.generateJwtToken(authentication);

	}
}
