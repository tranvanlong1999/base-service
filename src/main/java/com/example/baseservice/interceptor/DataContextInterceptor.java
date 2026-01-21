package com.example.baseservice.interceptor;

import com.example.baseservice.context.DataContext;
import com.example.baseservice.context.DataContextHelper;
import com.example.baseservice.context.DataKey;
import jakarta.servlet.DispatcherType;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class DataContextInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) {
		if (!request.getDispatcherType().equals(DispatcherType.REQUEST)) return true;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		DataContextHelper.setDataContext(DataContext.builder().build());
		if (!isAnonymousUser(authentication)) {
			DataContextHelper.setAuthentication(authentication);
		}
		DataContextHelper.setOtherData(DataKey.HEADER_REQUEST, getHeaders(request));
		DataContextHelper.setOtherData(DataKey.REQUEST_ID, request.getRequestId());
		return true;
	}

	@Override
	public void afterCompletion(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
	                            @NonNull Object handler, @Nullable Exception ex) {
		DataContextHelper.clear();
	}

	private boolean isAnonymousUser(Authentication authentication) {
		return authentication == null || authentication.getPrincipal().equals("anonymousUser");
	}

	private Map<String, String> getHeaders(HttpServletRequest request) {
		return Collections.list(request.getParameterNames())
				.stream()
				.collect(Collectors.toMap(parameterName -> parameterName, (s) -> request.getParameterValues(s)[0]));
	}
}
