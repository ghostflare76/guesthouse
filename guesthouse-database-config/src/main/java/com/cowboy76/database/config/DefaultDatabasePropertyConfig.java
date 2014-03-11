package com.cowboy76.database.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

@Configuration
public class DefaultDatabasePropertyConfig {
	
	@Profile("loc")
	@PropertySource("classpath:environment/database_loc.properties")
	static class databaseLocalProp {
	}
}
