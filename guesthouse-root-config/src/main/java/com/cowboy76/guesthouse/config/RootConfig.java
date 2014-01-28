package com.cowboy76.guesthouse.config;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;

@Configuration
@ComponentScan(basePackages = "com.cowboy76", excludeFilters = {@ComponentScan.Filter(Controller.class)})
public class RootConfig {
	
	public RootConfig() {
		StringBuffer sb = new StringBuffer("\n");
		sb.append("____________welcome spring start____________");
		sb.append("\n");

		System.out.println(sb.toString());
	}
	
	@Inject
	private Environment environment;
	
	@Bean(name="messageSource")
	public MessageSource messageSource() {
		List<String> fileList = new ArrayList<String>();
		fileList.add("classpath:messages/message");
		fileList.add("classpath:messages/validation");
		
		String[] files = (String[]) fileList.toArray(new String[fileList.size()]);
		
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		messageSource.setBasenames(files);
		messageSource.setDefaultEncoding("UTF-8");
		/**
		 * 화면언어 표시시 파라미터 언어 설정 리소스 파일이 존재하는 경우 해당 언어로 표시 
		 * 설정 언어 리소스가 없는 경우 기본 설정 언어 리소스로 표시
		 */
		messageSource.setFallbackToSystemLocale(false);
		
		//로컬 개발환경인 경우엔 캐쉬방지 
		if (environment.acceptsProfiles("local")) {
			messageSource.setCacheSeconds(0);
		}
		
		return messageSource;		
	}
	
	@Bean(name = "messageSourceAccessor")
	public MessageSourceAccessor messageSourceAccessor() {
		return new MessageSourceAccessor(messageSource());
	}
	
}
