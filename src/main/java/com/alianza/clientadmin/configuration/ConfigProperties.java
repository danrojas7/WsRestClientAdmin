package com.alianza.clientadmin.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author Daniel Alejandro
 *
 */
@Configuration
@ConfigurationProperties(prefix = "com.alianza.clientadmin.param")
public class ConfigProperties {

	private boolean enableCachingSave;
	private String defaultDateFormat;
	private char defaultCsvSeparator;
	private char defaultCsvQuoteChar;
	private char defaultCsvEscapeChar;
	private String defaultCsvLineEnd;

	public ConfigProperties() {
		super();
	}

	public boolean isEnableCachingSave() {
		return enableCachingSave;
	}

	public void setEnableCachingSave(boolean enableCachingSave) {
		this.enableCachingSave = enableCachingSave;
	}

	public String getDefaultDateFormat() {
		return defaultDateFormat;
	}

	public void setDefaultDateFormat(String defaultDateFormat) {
		this.defaultDateFormat = defaultDateFormat;
	}

	public char getDefaultCsvSeparator() {
		return defaultCsvSeparator;
	}

	public void setDefaultCsvSeparator(char defaultCsvSeparator) {
		this.defaultCsvSeparator = defaultCsvSeparator;
	}

	public char getDefaultCsvQuoteChar() {
		return defaultCsvQuoteChar;
	}

	public void setDefaultCsvQuoteChar(char defaultCsvQuoteChar) {
		this.defaultCsvQuoteChar = defaultCsvQuoteChar;
	}

	public char getDefaultCsvEscapeChar() {
		return defaultCsvEscapeChar;
	}

	public void setDefaultCsvEscapeChar(char defaultCsvEscapeChar) {
		this.defaultCsvEscapeChar = defaultCsvEscapeChar;
	}

	public String getDefaultCsvLineEnd() {
		return defaultCsvLineEnd;
	}

	public void setDefaultCsvLineEnd(String defaultCsvLineEnd) {
		this.defaultCsvLineEnd = defaultCsvLineEnd;
	}

}
