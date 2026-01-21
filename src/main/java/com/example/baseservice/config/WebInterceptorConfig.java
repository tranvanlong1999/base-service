package com.example.baseservice.config;

import com.example.baseservice.interceptor.DataContextInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebInterceptorConfig implements WebMvcConfigurer {

	private final DataContextInterceptor dataContextInterceptor;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(dataContextInterceptor).order(1);
	}

//	@Bean
//	public FilterRegistrationBean<Filter> loggingFilter(LoggingFilter requestLoggingFilter) {
//		FilterRegistrationBean<Filter> registrationBean = new FilterRegistrationBean<>();
//		registrationBean.setFilter(requestLoggingFilter);
//		registrationBean.addUrlPatterns("*");
//		return registrationBean;
//	}

}
