package com.challenge.fastfood.service_io.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

public class AwsCognitoJwtGrantedAuthoritiesConverter  implements Converter<Jwt, Collection<GrantedAuthority>>{

	private final JwtGrantedAuthoritiesConverter defaultGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();

	@Override
	public Collection<GrantedAuthority> convert(Jwt jwt) {
		Collection<GrantedAuthority> authorities = defaultGrantedAuthoritiesConverter.convert(jwt);
		
		List<GrantedAuthority> cognitoAuthorities = null;
		
		if(jwt.getClaims().get("cognito:groups") != null) {
			cognitoAuthorities = ((List<String>) jwt.getClaims().get("cognito:groups")).stream()
			.map(group -> new SimpleGrantedAuthority("ROLE_" + group.toUpperCase()))
			.collect(Collectors.toList());
		} else {
			cognitoAuthorities = new ArrayList<GrantedAuthority>();
		}
		
		
		authorities.addAll(cognitoAuthorities);
		return authorities;
	}

}
