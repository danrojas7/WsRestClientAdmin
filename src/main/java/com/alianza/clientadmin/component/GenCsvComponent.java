package com.alianza.clientadmin.component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
	public byte[] generateCsvFile(List<LinkedHashMap<String, Object>> lstFileContents,
			Map<String, String> columnFileTitle, boolean writeHeaders, char separator, char quotechar, char escapechar,
			String lineEnd) throws IOException {
		CSVWriter csvWriter = null;
		OutputStreamWriter osWriter = null;
		ByteArrayOutputStream bos = null;
		List<Object> lstRow = null;
		boolean isHeaderWrited = true;
		Set<String> headerSet = null;

		bos = new ByteArrayOutputStream();
		osWriter = new OutputStreamWriter(bos);
		csvWriter = new CSVWriter(osWriter, separator, quotechar, escapechar, lineEnd);

		if (columnFileTitle != null) {
			headerSet = columnFileTitle.keySet();
		} else {
			headerSet = lstFileContents.get(0).keySet();
		}

		for (Map<String, Object> row : lstFileContents) {
			if (isHeaderWrited && writeHeaders) {
				lstRow = new ArrayList<Object>();
				for (String header : headerSet) {
					if (columnFileTitle != null && columnFileTitle.containsKey(header)) {
						lstRow.add(columnFileTitle.get(header));
					} else {
						lstRow.add(header);
					}
				}
				csvWriter.writeNext(lstRow.stream().toArray(String[]::new));
				isHeaderWrited = false;
			}
			lstRow = new ArrayList<Object>();
			for (String header : headerSet) {
				lstRow.add(row.get(header));
			}
			csvWriter.writeNext(lstRow.stream().toArray(String[]::new));
		}
		csvWriter.close();
		return bos.toByteArray();
	}

}
