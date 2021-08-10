package com.aegon.getwelcomepdf.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.aegon.getwelcomepdf.model.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private PasswordEncoder encoder;

	@Value(value = "${get.welcome.script.username}")
	private String username;

	@Value(value = "${get.welcome.script.password}")
	private String password;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		if (username.equals(this.username)) {
			User user = new User(username, encoder.encode(password));
			return UserDetailsImpl.build(user);
		} else {
			throw new UsernameNotFoundException(username);
		}
	}

}
