package com.stopsnearme.app.ws;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;

import com.stopsnearme.app.ws.shared.RateLimitingFilter;

@EnableMethodSecurity
@SpringBootApplication
public class MobileAppWsApplication {

	public static void main(String[] args) {
		SpringApplication.run(MobileAppWsApplication.class, args);
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
		return authConfig.getAuthenticationManager();
	}

	@Bean
	SecurityFilterChain appSecurity(HttpSecurity http, AuthenticationEntryPoint entryPoint)
		throws Exception {
		http
			.authorizeHttpRequests((authorize) -> authorize
					.requestMatchers(HttpMethod.GET,"/users/**")
					.hasAuthority("SCOPE_cashcard:read")
					.requestMatchers("/users/**")
					.hasAuthority("SCOPE_cashcard:write")
					.anyRequest().authenticated()
					)
			.oauth2ResourceServer((oauth2) -> oauth2
					.authenticationEntryPoint(entryPoint)
					.jwt(Customizer.withDefaults())
					);
		return http.build();
	}
/* 
	@Bean
	public FilterRegistrationBean<RateLimitingFilter> rateLimitingFilter_() {
		FilterRegistrationBean<RateLimitingFilter> registrationBean = new FilterRegistrationBean<>();
		registrationBean.setFilter(new RateLimitingFilter());
		registrationBean.addUrlPatterns("/user/*"); // Register filter for API endpoints
		return registrationBean;
	}
*/
}
