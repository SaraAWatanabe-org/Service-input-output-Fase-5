package com.example.service_input_output.security;

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

        List<GrantedAuthority> customRoleAuthorities = new ArrayList<>();

        System.out.println("Get custom role");
        
        if (jwt.getClaims().get("custom:role") != null) {
            customRoleAuthorities = List.of(jwt.getClaimAsString("custom:role").split(","))
                    .stream()
                    .map(role -> new SimpleGrantedAuthority("ROLE_" + role.toUpperCase().trim()))
                    .collect(Collectors.toList());
        }

        authorities.addAll(customRoleAuthorities);
        return authorities;
    }

}
