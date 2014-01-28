package com.cowboy76.guesthouse.web.support;

import org.joda.time.DateTimeZone;
import org.springframework.core.MethodParameter;
import org.springframework.format.datetime.joda.JodaTimeContextHolder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class DateTimeZoneHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

	@Override
	public boolean supportsParameter(MethodParameter parameter) {		
		return DateTimeZone.class.isAssignableFrom(parameter.getParameterType());
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
		NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		 
		return JodaTimeContextHolder.getJodaTimeContext().getTimeZone();
	}

}
