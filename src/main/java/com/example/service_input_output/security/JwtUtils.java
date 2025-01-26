package com.example.service_input_output.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import com.example.service_input_output.exceptions.InvalidJwtStateException;

@Component
public class JwtUtils {

	   public String getEmailFromJwt() {
	        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

	        if (authentication != null && authentication.getPrincipal() instanceof Jwt) {
	            Jwt jwt = (Jwt) authentication.getPrincipal();
	            return jwt.getClaimAsString("email");
	        }

	        throw new InvalidJwtStateException("Unauthenticated user or invalid JWT.");
	    }
	   
}
