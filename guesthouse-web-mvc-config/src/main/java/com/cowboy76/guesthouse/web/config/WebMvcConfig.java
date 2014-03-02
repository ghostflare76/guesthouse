package com.cowboy76.guesthouse.web.config;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import javax.inject.Inject;

import nz.net.ultraq.thymeleaf.LayoutDialect;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.env.Environment;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.xml.Jaxb2RootElementHttpMessageConverter;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.TransactionException;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.accept.ContentNegotiationStrategy;
import org.springframework.web.accept.HeaderContentNegotiationStrategy;
import org.springframework.web.accept.ParameterContentNegotiationStrategy;
import org.springframework.web.accept.PathExtensionContentNegotiationStrategy;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;
import org.springframework.web.servlet.view.BeanNameViewResolver;
import org.springframework.web.servlet.view.ContentNegotiatingViewResolver;
import org.springframework.web.servlet.view.DefaultRequestToViewNameTranslator;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;
import org.thymeleaf.extras.springsecurity3.dialect.SpringSecurityDialect;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import com.cowboy76.guesthouse.web.servlet.view.json.MappingJackson2JsonpView;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "com.cowboy76", useDefaultFilters = false, includeFilters = @ComponentScan.Filter(Controller.class))
public class WebMvcConfig extends WebMvcConfigurerAdapter {

	@Inject
	private Environment environment;

	// set Default servlet Handler, 
	// <mvc:default-servlet-handler>
	public void configureDefaultServletHandler(DefaultServletHandlerConfigurer configurer) {
		configurer.enable();
	}

	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		converters.add(new FormHttpMessageConverter());
		converters.add(new StringHttpMessageConverter(Charset.forName("UTF-8")));
		converters.add(new MappingJackson2HttpMessageConverter());
		converters.add(new Jaxb2RootElementHttpMessageConverter());
		super.configureMessageConverters(converters);
	}

	@Override
	public Validator getValidator() {
		LocalValidatorFactoryBean factory = new LocalValidatorFactoryBean();
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		messageSource.setBasename("classpath:messages/validation");
		if (environment.acceptsProfiles("local")) {
			messageSource.setCacheSeconds(0);
		}
		factory.setValidationMessageSource(messageSource);
		return factory;
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/favicon.ico").addResourceLocations("/favicon.ico");
		registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
	}

	@Bean
	public HandlerExceptionResolver simpleMappingExceptionResolver() {
		Properties mappings = new Properties();
		mappings.put(NoSuchRequestHandlingMethodException.class.getName(), "/error/pageNotFound");
		mappings.put(HttpRequestMethodNotSupportedException.class.getName(), "/error/pageNotFound");
		mappings.put(DataAccessException.class.getName(), "/error/dataAccessFailure");
		mappings.put(TransactionException.class.getName(), "/error/dataAccessFailure");

		SimpleMappingExceptionResolver exceptionResolver = new SimpleMappingExceptionResolver();
		exceptionResolver.setExceptionMappings(mappings);
		exceptionResolver.setDefaultErrorView("/error/defaultError");
		exceptionResolver.setDefaultStatusCode(HttpStatus.BAD_REQUEST.value());
		exceptionResolver.setOrder(1);

		return exceptionResolver;
	}

	@Bean
	public LocaleChangeInterceptor localChangeInterceptor() {
		LocaleChangeInterceptor localChangeInterceptor = new LocaleChangeInterceptor();
		localChangeInterceptor.setParamName("lang");
		return localChangeInterceptor;
	}

	@Bean
	public SessionLocaleResolver sessionLocaleResolver() {
		Locale locale = new Locale("ko");
		SessionLocaleResolver sessionLocaleResolver = new SessionLocaleResolver();
		sessionLocaleResolver.setDefaultLocale(locale);
		return sessionLocaleResolver;
	}

	@Bean
	public DefaultRequestToViewNameTranslator viewNameTranslater() {
		return new DefaultRequestToViewNameTranslator();
	}

	/**
	 * Commons FileUpload API를 통하여 Multipart 처리
	 * 
	 * @return
	 */
	@Bean
	public MultipartResolver multipartResolver() {
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
		multipartResolver.setMaxUploadSize(10240000);
		multipartResolver.setDefaultEncoding("UTF-8");
		return multipartResolver;
	}

	@Bean(name = "viewNameTranslator")
	public DefaultRequestToViewNameTranslator viewNameTranslator() {
		return new DefaultRequestToViewNameTranslator();
	}

	@Bean(name = "templateResolver")
	public ServletContextTemplateResolver templateResolver() {
		ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver();
		templateResolver.setPrefix("/WEB-INF/views/");
		templateResolver.setSuffix(".html");
		templateResolver.setTemplateMode("HTML5");
		templateResolver.setCacheable(false);
		return templateResolver;
	}

	@Bean(name = "templateEngine")
	public SpringTemplateEngine templateEngine() {
		SpringTemplateEngine templateEngine = new SpringTemplateEngine();
		templateEngine.setTemplateResolver(templateResolver());
		templateEngine.addDialect(new LayoutDialect());
		templateEngine.addDialect(new SpringSecurityDialect());

		return templateEngine;
	}

	@Bean(name = "thymeleafViewResolver")
	public ThymeleafViewResolver thymeleafViewResolver() {
		ThymeleafViewResolver thymeleafViewResolver = new ThymeleafViewResolver();
		thymeleafViewResolver.setTemplateEngine(templateEngine());
		thymeleafViewResolver.setCharacterEncoding("UTF-8");
		return thymeleafViewResolver;
	}

	@Bean
	public ContentNegotiatingViewResolver contentNegotiatingViewResolver() {
		ContentNegotiatingViewResolver contentNegotiatingViewResolver = new ContentNegotiatingViewResolver();

		Map<String, MediaType> mediaTypes = new HashMap<String, MediaType>();
		mediaTypes.put("html", MediaType.TEXT_HTML);
		mediaTypes.put("json", MediaType.APPLICATION_JSON);
		mediaTypes.put("jsonp", MediaType.APPLICATION_JSON);
		mediaTypes.put("xml", MediaType.APPLICATION_XML);
		mediaTypes.put("atom", MediaType.APPLICATION_ATOM_XML);

		ContentNegotiationStrategy pathExensionContentNegotiationStrategy = new PathExtensionContentNegotiationStrategy(
			mediaTypes);
		ContentNegotiationStrategy headerContentNegotiationStrategy = new HeaderContentNegotiationStrategy();
		ContentNegotiationStrategy parameterContentNegotiationStrategy = new ParameterContentNegotiationStrategy(
			mediaTypes);

		ContentNegotiationManager contentNegotiationManager = new ContentNegotiationManager(
			pathExensionContentNegotiationStrategy, headerContentNegotiationStrategy,
			parameterContentNegotiationStrategy);

		/**
		 * 요청 컨텐츠 유형 결정 
		 * 1. headerContentNegotiationStrategy (Accept-Header 값 정의) : default 설정임 
		 * 2. pathExtensionContentNegotiationStrategy (File 확장자 미디어 유형으로 정의) 
		 * 3. ParameterContentNegotiationStrategy (format 파라미터값으로 정의)
		 */		
		contentNegotiatingViewResolver.setContentNegotiationManager(contentNegotiationManager);

		List<ViewResolver> viewResolvers = new ArrayList<ViewResolver>();
		viewResolvers.add(new BeanNameViewResolver());
		viewResolvers.add(thymeleafViewResolver());
		
		InternalResourceViewResolver internalResourceViewResolver = new InternalResourceViewResolver();
		internalResourceViewResolver.setPrefix("/WEB-INF/view");
		internalResourceViewResolver.setSuffix(".html");

		viewResolvers.add(internalResourceViewResolver);
		
		/**
		 * 컨텐츠 출력 View 타입 결정 
		 */
		contentNegotiatingViewResolver.setViewResolvers(viewResolvers);
		
		List<View> jsonViews = new ArrayList<View>();
		MappingJackson2JsonView mappingJackSon2JsonView = new MappingJackson2JsonView();
		mappingJackSon2JsonView.setModelKey("result");
		mappingJackSon2JsonView.setExtractValueFromSingleKeyModel(true);
		jsonViews.add(mappingJackSon2JsonView);
		
		
		MappingJackson2JsonpView mappingJackson2JsonpView = new MappingJackson2JsonpView();
		mappingJackSon2JsonView.setModelKey("result");
		mappingJackSon2JsonView.setExtractValueFromSingleKeyModel(true);
		jsonViews.add(mappingJackson2JsonpView);
		
		contentNegotiatingViewResolver.setDefaultViews(jsonViews);
		contentNegotiatingViewResolver.setOrder(2);
		
		return contentNegotiatingViewResolver;
		
	}
}
