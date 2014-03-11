package com.cowboy76.database.annotation;

import javax.inject.Inject;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement(proxyTargetClass = true, order = 2)
public class AbstractDataSourceConfig {
	
	@Inject
	private Environment environment;
	
	@Autowired
    private ResourceLoader resourceLoader;
	
	@Bean(destroyMethod = "close")
	@Scope("prototype")
	public DataSource parentDatasource() {
		DataSource ds = new DataSource();	
		ds.setDriverClassName(environment.getRequiredProperty("default.ds.jdbc.driverClassName"));
		ds.setUrl(environment.getRequiredProperty("default.ds.jdbc.url"));		
		ds.setUsername(environment.getRequiredProperty("default.ds.jdbc.username"));
		ds.setPassword(environment.getRequiredProperty("default.ds.jdbc.password"));
		ds.setMaxActive(environment.getRequiredProperty("default.ds.jdbc.maxActive", int.class));
		ds.setMinIdle(environment.getRequiredProperty("default.ds.jdbc.minIdle", int.class));
		ds.setMaxIdle(environment.getRequiredProperty("default.ds.jdbc.maxIdle", int.class));
		ds.setMaxWait(environment.getRequiredProperty("default.ds.jdbc.maxWait", int.class));
		ds.setInitialSize(environment.getRequiredProperty("default.ds.jdbc.initialSize", int.class));
		ds.setValidationQuery(environment.getRequiredProperty("default.ds.jdbc.validationQuery"));
		ds.setValidationInterval(environment.getRequiredProperty("default.ds.jdbc.validationInterval", long.class));
		ds.setTestOnBorrow(environment.getRequiredProperty("default.ds.jdbc.testOnBorrow", boolean.class));
		ds.setTestWhileIdle(environment.getRequiredProperty("default.ds.jdbc.testWhileIdle", boolean.class));
		ds.setTimeBetweenEvictionRunsMillis(environment.getRequiredProperty("default.ds.jdbc.timeBetweenEvictionRunsMillis", int.class));
		ds.setRemoveAbandoned(environment.getRequiredProperty("default.ds.jdbc.removeAbandoned", boolean.class));
		ds.setRemoveAbandonedTimeout(environment.getRequiredProperty("default.ds.jdbc.removeAbandonedTimeout", int.class));
		ds.setLogAbandoned(environment.getRequiredProperty("default.ds.jdbc.logAbandoned", boolean.class));
		ds.setAbandonWhenPercentageFull(environment.getRequiredProperty("default.ds.jdbc.abandonWhenPercentageFull", int.class));
		ds.setJdbcInterceptors(environment.getRequiredProperty("default.ds.jdbc.jdbcInterceptors"));
		ds.setConnectionProperties(environment.getRequiredProperty("default.ds.jdbc.connectionProperties"));
		return ds;
	}
}
