package com.alianza.clientadmin.component;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFFormulaEvaluator;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

@Component
public class GenExcelComponent {

	/**
	 * @param baFile
	 * @param initPostRow
	 * @param initPosColumn
	 * @param lstFileContents
	 * @param columnFileTitle
	 * @param writeHeader
	 * @param fileName
	 * @param sheetName
	 * @param sheetNumber
	 * @param updateFormulas
	 * @param makeContent
	 * @return
	 * @throws IOException
	 */
	public byte[] generarByteArrayArchivoExcel(byte[] baFile, int initPostRow, int initPosColumn,
			List<LinkedHashMap<String, Object>> lstFileContents, Map<String, String> columnFileTitle,
			boolean writeHeader, String fileName, String sheetName, Integer sheetNumber, boolean updateFormulas,
			boolean makeContent) throws IOException {
		byte[] arrayFile = null;

		Workbook objExcelBook = null;
		Sheet objExcelSheet = null;
		Row objExcelRow = null;
		Cell objExcelCell = null;
		int countColumn = 0;
		int countRow = 0;
		Set<String> headerSet = null;
		Object cellContent = null;
		ByteArrayOutputStream bos = null;

		if (fileName.toLowerCase().endsWith(".xls")) {
			if (baFile == null) {
				objExcelBook = new HSSFWorkbook();
			} else {
				objExcelBook = new HSSFWorkbook(new ByteArrayInputStream(baFile));
			}
		} else if (fileName.toLowerCase().endsWith(".xlsx")) {
			if (baFile == null) {
				objExcelBook = new XSSFWorkbook();
			} else {
				objExcelBook = new XSSFWorkbook(new ByteArrayInputStream(baFile));
			}
		} else {
			throw new IllegalArgumentException("The file specified doesn't a Excel file.");
		}

		if (baFile == null) {
			objExcelSheet = objExcelBook.createSheet(sheetName);
		} else {
			objExcelSheet = objExcelBook.getSheet(sheetName);
		}
		if (objExcelSheet == null) {
			if (sheetName != null) {
				throw new IllegalArgumentException(
						String.format("The sheet \"%s\" doesn't exists in the Excel file.", sheetName));
			} else {
				throw new IllegalArgumentException(
						String.format("The sheet with index \"%s\" doesn't exists in the Excel file.", sheetNumber));
			}
		}

		if (columnFileTitle != null) {
			headerSet = columnFileTitle.keySet();
		} else {
			headerSet = lstFileContents.get(0).keySet();
		}

		countRow = initPostRow;
		countColumn = initPosColumn;
		for (Map<String, Object> row : lstFileContents) {
			if (writeHeader && countRow == 0) {
				objExcelRow = (makeContent ? objExcelSheet.createRow(countRow) : objExcelSheet.getRow(countRow));
				for (String header : headerSet) {
					if (columnFileTitle != null && columnFileTitle.containsKey(header)) {
						cellContent = columnFileTitle.get(header);
					} else {
						cellContent = header;
					}
					objExcelCell = (makeContent ? objExcelRow.createCell(countColumn)
							: objExcelRow.getCell(countColumn));
					setCellValue(objExcelCell, cellContent);
					countColumn++;
				}
				countRow++;
			}
			countColumn = initPosColumn;
			objExcelRow = (makeContent ? objExcelSheet.createRow(countRow) : objExcelSheet.getRow(countRow));
			for (String header : headerSet) {
				cellContent = row.get(header);
				objExcelCell = (makeContent ? objExcelRow.createCell(countColumn) : objExcelRow.getCell(countColumn));
				setCellValue(objExcelCell, cellContent);
				countColumn++;
			}
			countRow++;
		}

		if (updateFormulas) {
			if (sheetName.toLowerCase().endsWith(".xls")) {
				HSSFFormulaEvaluator.evaluateAllFormulaCells(objExcelBook);
			} else {
				XSSFFormulaEvaluator.evaluateAllFormulaCells(objExcelBook);
			}
		}

		bos = new ByteArrayOutputStream();
		objExcelBook.write(bos);
		arrayFile = bos.toByteArray();
		objExcelBook.close();
		bos.close();
		return arrayFile;
	}

	/**
	 * @param objExcelCell
	 * @param cellContent
	 */
	private void setCellValue(Cell objExcelCell, Object cellContent) {
		if (cellContent instanceof Double) {
			objExcelCell.setCellValue((Double) cellContent);
		} else if (cellContent instanceof Date) {
			objExcelCell.setCellValue((Date) cellContent);
		} else if (cellContent instanceof Calendar) {
			objExcelCell.setCellValue((Calendar) cellContent);
		} else if (cellContent instanceof RichTextString) {
			objExcelCell.setCellValue((RichTextString) cellContent);
		} else if (cellContent instanceof String) {
			objExcelCell.setCellValue((String) cellContent);
		} else if (cellContent instanceof Boolean) {
			objExcelCell.setCellValue((Boolean) cellContent);
		}
	}

}
