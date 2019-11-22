package com.glogger.automation.mina;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.filterchain.IoFilter;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.filter.logging.MdcInjectionFilter;

public class FilterChainBuilder extends DefaultIoFilterChainBuilder{
	

	public FilterChainBuilder() {
		Map<String, IoFilter> filters = new LinkedHashMap<String, IoFilter>();
		filters.put("executor", new ExecutorFilter());
		filters.put("mdcInjectionFilter", new MdcInjectionFilter());
		filters.put("codecFilter", new ProtocolCodecFilter(new ObjectSerializationCodecFactory()));
		filters.put("loggingFilter", new LoggingFilter());
		
		this.setFilters(filters);
	}
}
