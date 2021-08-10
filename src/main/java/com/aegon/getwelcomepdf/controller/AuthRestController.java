package com.aegon.getwelcomepdf.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aegon.getwelcomepdf.model.TokenResponse;
import com.aegon.getwelcomepdf.service.TokenService;

@RequestMapping("/EDelivery_API")
@RestController
public class AuthRestController {

	@Autowired
	private TokenService tokenService;

	@Value(value = "${get.welcome.script.jwtExpirationMs}")
	private Long jwtExpirationMs;

	@PostMapping("/token")
	public TokenResponse createToken(@RequestParam("username") String username,
			@RequestParam("password") String password, @RequestParam("grant_type") String grant_type) throws Exception {
		return new TokenResponse(tokenService.authenticateUser(username, password, grant_type), "bearer",
				 jwtExpirationMs / 1000);
	}
}
