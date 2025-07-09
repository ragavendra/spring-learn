package com.stopsnearme.app.ws;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

import com.stopsnearme.app.ws.shared.RateLimitingFilter;

@SpringBootApplication
public class MobileAppWsApplication {

	public static void main(String[] args) {
		SpringApplication.run(MobileAppWsApplication.class, args);
	}

	@Bean
    public FilterRegistrationBean<RateLimitingFilter> rateLimitingFilter_() {
        FilterRegistrationBean<RateLimitingFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new RateLimitingFilter());
		// this is not working
        registrationBean.addUrlPatterns("/user/*"); // Register filter for API endpoints
        return registrationBean;
    }
}
