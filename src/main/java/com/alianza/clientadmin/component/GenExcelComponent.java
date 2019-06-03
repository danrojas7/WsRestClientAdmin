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

import org.apache.commons.lang3.StringUtils;
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
	 * @param makingContent
	 * @return
	 * @throws IOException
	 */
	public byte[] generarByteArrayArchivoExcel(byte[] baFile, int initPostRow, int initPosColumn,
			List<LinkedHashMap<String, Object>> lstFileContents, Map<String, String> columnFileTitle,
			boolean writeHeader, String fileName, String sheetName, Integer sheetNumber, boolean updateFormulas,
			boolean makingContent) throws IOException {
		byte[] arrayFile = null;

		Workbook objExcelBook = null;
		Sheet objExcelSheet = null;
		int countRow = 0;
		Set<String> headerSet = null;
		ByteArrayOutputStream bos = null;

		objExcelBook = openExcelFileInWorkbook(baFile, fileName);
		objExcelSheet = openExcelSheet(baFile, objExcelBook, sheetName, sheetNumber);

		if (columnFileTitle != null) {
			headerSet = columnFileTitle.keySet();
		} else {
			headerSet = lstFileContents.get(0).keySet();
		}

		countRow = initPostRow;
		for (Map<String, Object> row : lstFileContents) {
			if (writeHeader && countRow == 0) {
				writeExcelHeaders(objExcelSheet, headerSet, makingContent, columnFileTitle, initPostRow, initPosColumn);
				countRow++;
			}
			writeContentRows(objExcelSheet, headerSet, makingContent, row, initPosColumn, countRow);
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
	 * @param baFile
	 * @param fileName
	 * @return
	 * @throws Exception
	 */
	private Workbook openExcelFileInWorkbook(byte[] baFile, String fileName) throws IOException {
		Workbook objExcelBook = null;
		if (StringUtils.isEmpty(fileName)) {
			throw new IllegalArgumentException("The extension of file is required.");
		} else if (fileName.toLowerCase().endsWith(".xls")) {
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
		return objExcelBook;
	}

	/**
	 * @param baFile
	 * @param objExcelBook
	 * @param sheetName
	 * @param sheetNumber
	 * @return
	 */
	private Sheet openExcelSheet(byte[] baFile, Workbook objExcelBook, String sheetName, Integer sheetNumber) {
		Sheet objExcelSheet = null;

		if (baFile == null) {
			if (StringUtils.isEmpty(sheetName)) {
				throw new IllegalArgumentException("The sheet name is required.");
			}
			objExcelSheet = objExcelBook.createSheet(sheetName);
		} else {
			if (StringUtils.isEmpty(sheetName) && sheetNumber == null) {
				throw new IllegalArgumentException("The sheet name or index number is required.");
			}
			if (StringUtils.isNotEmpty(sheetName)) {
				objExcelSheet = objExcelBook.getSheet(sheetName);
			} else {
				objExcelSheet = objExcelBook.getSheetAt(sheetNumber);
			}
		}
		if (baFile != null && objExcelSheet == null) {
			if (StringUtils.isNotEmpty(sheetName)) {
				throw new IllegalArgumentException(
						String.format("The sheet \"%s\" doesn't exists in the Excel file.", sheetName));
			} else {
				throw new IllegalArgumentException(
						String.format("The sheet with index \"%s\" doesn't exists in the Excel file.", sheetNumber));
			}
		}
		return objExcelSheet;
	}

	/**
	 * @param objExcelSheet
	 * @param headerSet
	 * @param makingContent
	 * @param columnFileTitle
	 * @param initPostRow
	 * @param initPosColumn
	 */
	private void writeExcelHeaders(Sheet objExcelSheet, Set<String> headerSet, boolean makingContent,
			Map<String, String> columnFileTitle, int initPostRow, int initPosColumn) {
		int countColumn = 0;
		int countRow = 0;
		Row objExcelRow = null;
		Cell objExcelCell = null;
		Object cellContent = null;

		countRow = initPostRow;
		countColumn = initPosColumn;

		objExcelRow = (makingContent ? objExcelSheet.createRow(countRow) : objExcelSheet.getRow(countRow));
		for (String header : headerSet) {
			if (columnFileTitle != null && columnFileTitle.containsKey(header)) {
				cellContent = columnFileTitle.get(header);
			} else {
				cellContent = header;
			}
			objExcelCell = (makingContent ? objExcelRow.createCell(countColumn) : objExcelRow.getCell(countColumn));
			setCellValue(objExcelCell, cellContent);
			countColumn++;
		}
	}

	/**
	 * @param objExcelSheet
	 * @param headerSet
	 * @param makingContent
	 * @param row
	 * @param initPosColumn
	 * @param countRow
	 */
	private void writeContentRows(Sheet objExcelSheet, Set<String> headerSet, boolean makingContent,
			Map<String, Object> row, int initPosColumn, int countRow) {
		int countColumn = 0;
		Row objExcelRow = null;
		Cell objExcelCell = null;
		Object cellContent = null;

		countColumn = initPosColumn;
		objExcelRow = (makingContent ? objExcelSheet.createRow(countRow) : objExcelSheet.getRow(countRow));
		for (String header : headerSet) {
			cellContent = row.get(header);
			objExcelCell = (makingContent ? objExcelRow.createCell(countColumn) : objExcelRow.getCell(countColumn));
			setCellValue(objExcelCell, cellContent);
			countColumn++;
		}
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
