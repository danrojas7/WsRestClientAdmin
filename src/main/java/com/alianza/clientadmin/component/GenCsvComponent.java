package com.alianza.clientadmin.component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.stereotype.Component;

import com.opencsv.CSVWriter;

/**
 * @author drojas
 *
 */
@Component
public class GenCsvComponent {

	/**
	 * @param lstFileContents
	 * @param writeHeaders
	 * @param separator
	 * @param quotechar
	 * @param escapechar
	 * @param lineEnd
	 * @return
	 * @throws IOException
	 */
	public byte[] generateCsvFile(List<LinkedHashMap<String, Object>> lstFileContents, boolean writeHeaders,
			char separator, char quotechar, char escapechar, String lineEnd) throws IOException {
		CSVWriter csvWriter = null;
		OutputStreamWriter osWriter = null;
		ByteArrayOutputStream bos = null;
		List<Object> lstRow = null;
		boolean header = true;

		bos = new ByteArrayOutputStream();
		osWriter = new OutputStreamWriter(bos);
		csvWriter = new CSVWriter(osWriter, separator, quotechar, escapechar, lineEnd);

		lstRow = new ArrayList<Object>();
		for (Map<String, Object> registro : lstFileContents) {
			if (header && writeHeaders) {
				for (Entry<String, Object> entry : registro.entrySet()) {
					lstRow.add(entry.getKey());
				}
				csvWriter.writeNext(lstRow.stream().toArray(String[]::new));
				header = false;
			}
			lstRow = new ArrayList<Object>();
			for (Entry<String, Object> entry : registro.entrySet()) {
				lstRow.add(entry.getValue());
			}
			csvWriter.writeNext(lstRow.stream().toArray(String[]::new));
		}
		csvWriter.close();
		return bos.toByteArray();
	}

}
