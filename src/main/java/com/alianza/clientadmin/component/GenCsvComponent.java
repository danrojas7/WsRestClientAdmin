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
 * Clase componente en la que se define la lógica de los métodos genéricos para
 * convertir una lista de mapas, a un archivo CSV, de acuerdo a la configuración
 * y data enviada
 * 
 * @author Daniel Alejandro
 *
 */
@Component
public class GenCsvComponent {

	/**
	 * Método que realiza la conversión de una lista de Mapas llave-valor, a un
	 * archivo CSV representado por un bytearray, de acuerdo al separador entre
	 * otros atributos de configuración
	 * 
	 * @param lstFileContents Listado de mapas con la infomación a exportar al
	 *                        archivo CSV
	 * @param columnFileTitle Mapa con la llave-valor, en donde la llave es el
	 *                        nombre del atributo, y el valor es el título que
	 *                        llevará el campo dentro del archivo CSV
	 * @param writeHeaders    Bandera que indica si se deben escribir los
	 *                        encabezados de los campos en el archivo CSV
	 * @param separator       Atributo de tipo caracter en el que se indica el tipo
	 *                        de separador a utilizar en el archivo CSV
	 * @param quotechar       Atributo de tipo caracter el cual es utilizado por la
	 *                        librería OpenCSV, para especificar que una columna es
	 *                        de tipo de atributo texto
	 * @param escapecha       Atributo de tipo texto que es utilizado para indicar
	 *                        el caracter de escape empleado por la librería OpenCSV
	 * @param lineEnd         Atributo de tipo cadena de caracteres, en el cual se
	 *                        especifican los caracteres empleados por la librería
	 *                        OpenCSV para especificar un salto de línea del archivo
	 *                        CSV
	 * @return Bytearray con la representación del contenido del archivo CSV
	 * @throws IOException Si ocurre un error inesperado al momento de generar el
	 *                     archivo CSV
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
				writeHeaders(headerSet, columnFileTitle, csvWriter);
				isHeaderWrited = false;
			}
			lstRow = new ArrayList<>();
			for (String header : headerSet) {
				lstRow.add(row.get(header));
			}
			csvWriter.writeNext(lstRow.stream().toArray(String[]::new));
		}
		csvWriter.close();
		return bos.toByteArray();
	}

	/**
	 * Método auxiliar utilizado para escribir los encabezados del archivo CSV, de
	 * acuerdo a los encabezados enviados como parámetro, si no se especifican los
	 * encabezados , por omisión tomará los nombres de los campos de la lista de
	 * Mapas, del primer atributo
	 * 
	 * @param headerSet       Conjunto de cadena de caracteres con los nombres de
	 *                        los campos del archivo CSV, para homologarlos en caso
	 *                        de enviar los nombres de los campos en el parámetro
	 *                        columnFileTitle
	 * @param columnFileTitle Mapa con la especificación del nombre del campo, y
	 *                        nombre final que llevará en la generación del archivo
	 *                        CSV
	 * @param csvWriter       Objeto instancia de la clase de OpenCSV utilizada para
	 *                        administrar el contenido del archivo CSV
	 */
	private void writeHeaders(Set<String> headerSet, Map<String, String> columnFileTitle, CSVWriter csvWriter) {
		List<Object> lstRow = null;

		lstRow = new ArrayList<>();
		for (String header : headerSet) {
			if (columnFileTitle != null && columnFileTitle.containsKey(header)) {
				lstRow.add(columnFileTitle.get(header));
			} else {
				lstRow.add(header);
			}
		}
		csvWriter.writeNext(lstRow.stream().toArray(String[]::new));
	}

}
