package com.cowboy76.database.handler;

import com.cowboy76.database.config.DataSourceType;

public class DataSourceContextHolder {
	
	public static final ThreadLocal<DataSourceType> DATASOURCE_CONTEXT_HOLDER = new ThreadLocal<DataSourceType>();
	
	public static void setDataSourceType(DataSourceType dataSourceType)	{
		DATASOURCE_CONTEXT_HOLDER.set(dataSourceType);
	}

	public static DataSourceType getDataSourceType() {
		return (DataSourceType)DATASOURCE_CONTEXT_HOLDER.get();
	}

	public static void clearDataSourceType() {
		DATASOURCE_CONTEXT_HOLDER.remove();
	}

}
