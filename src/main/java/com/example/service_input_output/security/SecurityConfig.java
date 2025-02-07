package com.example.service_input_output.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

import com.example.service_input_output.enums.UserRoleEnum;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Value("${cloud.aws.cognito.jwkSetUri}")
	private String jwkSetUri;


	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
		.csrf(csrf -> csrf.disable())
		.authorizeHttpRequests(auth -> auth
				.requestMatchers("/auth/**").permitAll()
				.requestMatchers("/auth/accept-terms").authenticated()
				//TODO
				//.requestMatchers("/auth/create-user-admin").hasRole(UserRoleEnum.ADMIN.getCode())
				.requestMatchers("/videos/**").hasRole(UserRoleEnum.USER.getCode())

				//.anyRequest().authenticated()
				//Testes
				.anyRequest().permitAll()
				)
		.oauth2ResourceServer(oauth2 -> oauth2
				.jwt(jwt -> jwt
						.decoder(jwtDecoder())
						.jwtAuthenticationConverter(jwtAuthenticationConverter())
						)
				)
		.sessionManagement(sessionManagement ->
		sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				);
		return http.build();
	}

	@Bean
	JwtDecoder jwtDecoder() {
		return NimbusJwtDecoder.withJwkSetUri(jwkSetUri).build();
	}

	@Bean
	JwtAuthenticationConverter jwtAuthenticationConverter() {
		JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
		jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(new AwsCognitoJwtGrantedAuthoritiesConverter());
		return jwtAuthenticationConverter;
	}
}
