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
	private char defaultCsvSeparator;
	private char defaultCsvQuoteChar;
	private char defaultCsvEscapeChar;
	private String defaultCsvLineEnd;
	private String defaultHeadersExportFile;
	private String defaultDateFormat;

	public ConfigProperties() {
		super();
	}

	public boolean isEnableCachingSave() {
		return enableCachingSave;
	}

	public void setEnableCachingSave(boolean enableCachingSave) {
		this.enableCachingSave = enableCachingSave;
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

	public String getDefaultHeadersExportFile() {
		return defaultHeadersExportFile;
	}

	public void setDefaultHeadersExportFile(String defaultHeadersExportFile) {
		this.defaultHeadersExportFile = defaultHeadersExportFile;
	}

	public String getDefaultDateFormat() {
		return defaultDateFormat;
	}

	public void setDefaultDateFormat(String defaultDateFormat) {
		this.defaultDateFormat = defaultDateFormat;
	}

}
