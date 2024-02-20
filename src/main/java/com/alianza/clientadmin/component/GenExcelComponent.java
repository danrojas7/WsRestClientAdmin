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

/**
 * Clase comoponente en la que se define la lógica genérica para convertir una
 * lista de mapas, en un archivo de Microsoft Office Excel 2003 ó 2007
 * 
 * @author Daniel Alejandro
 *
 */
@Component
public class GenExcelComponent {

    /**
     * Método que genera de un contenido de tipo lista de mapas, a un archivo de
     * Microsoft Office Excel 2003 ó 2007, con los atributos de configuración
     * enviados
     * 
     * @param baFile          Bytearray de un archivo de Excel existente, en caso de
     *                        que se vaya a utilizar como un archivo de plantilla
     *                        para modificarlo y agregar la información enviada como
     *                        parámetro
     * @param initPostRow     Posición inicial del registro en el que el método
     *                        empezará a escribir la información enviada, dentro de
     *                        la hoja de cáculo
     * @param initPosColumn   Posición inicial de la columna en el que el método
     *                        empezará a escribir la información enviada, dentro de
     *                        la hoja de cáculo
     * @param lstFileContents Lista de mapas, con el nombre del campo como llave, y
     *                        el contenido del archivo como valor, a exportar al
     *                        archivo de hoja de cálculo
     * @param columnFileTitle Mapa en el que se define el nombre del atributo,
     *                        definido como llave, y el nombre final que llevará la
     *                        columna de información como valor, para definir los
     *                        encabezados finales del archivo de hoja de cáculo, si
     *                        este atributo no se envía, se toma por defecto el
     *                        nombre del atributo como llave extraído del primer
     *                        registro
     * @param writeHeader     Bandera que indica si en el archivo de hoja de cálculo
     *                        se van a escribir los nombres de las columnas o no
     * @param fileName        Nombre que va a llevar el archivo de hoja de cáculo
     *                        generado, es utilizado para definir el formato del
     *                        archivo y así las clases controladoras de la librería
     *                        de Apache POI a utilizar
     * @param sheetName       Nombre de la hoja de cálculo en donde se va a exportar
     *                        la información dentro del libro de la hoja de cáculo
     * @param sheetNumber     Número índice de la hoja de cálculo en donde se va a
     *                        escribir la información, útil si el archivo a
     *                        modificar es un archivo plantilla, y no se conoce la
     *                        hoja a escribir sino por el índice o posición relativa
     *                        dentro del libro
     * @param updateFormulas  Bandera que indica si se actualizarán todos los campos
     *                        formulados del archivo de hoja de cáculo o no, de
     *                        acuerdo a la información agregada
     * @param makingContent   Bandera que indica si se van a crear los registros del
     *                        archivo de Excel (nuevas celdas), en caso de un
     *                        archivo nuevo, o si se va a modificar en caso de
     *                        agregar contenido a un archivo de Excel utilizado como
     *                        plantilla
     * @return Bytearray con el contenido del archivo de Excel generado a partir de
     *         la información enviada
     * @throws IOException Si ocurre un error inesperado al momento de convertir en
     *                     bytearray la informacón del libro de Excel, contenido
     *                     dentro de las clases controladoras de Apache POI
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
     * Método auxiliar utilizado para inicializar las clases controladoras de Apache
     * POI, para administrar el contenido del archivo de Excel, a partir de un
     * archivo nuevo o de un archivo de Excel empleado como plantilla, y también
     * dependiendo del tipo de formato de archivo de Excel a generar (Hoja de
     * cálculo de Microsoft Office Excel 2003, .xls, ó una hoja de cálculo de
     * Microsoft Office Excel 2007, .xlsx)
     * 
     * @param baFile   Bytearray de un archivo de Excel existente, en caso de que se
     *                 vaya a utilizar como un archivo de plantilla para modificarlo
     *                 y agregar la información enviada
     * @param fileName Nombre que va a llevar el archivo de hoja de cáculo generado,
     *                 es utilizado para definir el formato del archivo y así las
     *                 clases controladoras de la librería de Apache POI a utilizar
     * @return Objeto Workbook de Apache POI, representación de un archivo de
     *         Microsoft Office Excel de Apache POI, para administrar su contenido
     * @throws IOException Si ocurre un error inesperado al momento de generar la
     *                     instancia del
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
     * Método auxiliar utlizado para inicializar la clase para manipular el
     * contenido de una hoja de cálculo de Microsoft Office Excel
     * 
     * @param baFile       Bytearray de un archivo de Excel existente, en caso de
     *                     que se vaya a utilizar como un archivo de plantilla para
     *                     modificarlo y agregar la información enviada
     * @param objExcelBook Clase de Apache POI, utilizada para administrar el
     *                     contenido del archivo de hoja de cáculo de Microsoft
     *                     Office Excel
     * @param sheetName    Nombre de la hoja de cálculo en donde se va a exportar la
     *                     información dentro del libro de la hoja de cáculo
     * @param sheetNumber  Número índice de la hoja de cálculo en donde se va a
     *                     escribir la información, útil si el archivo a modificar
     *                     es un archivo plantilla, y no se conoce la hoja a
     *                     escribir sino por el índice o posición relativa dentro
     *                     del libro
     * @return Objeto Sheet de Apache POI, utilizado para administrar el contenido
     *         de la hoja del archivo de Excel
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
     * Método auxiliar para escribir los encabezados del archivo de Excel, como los
     * encabezados de las columnas
     * 
     * @param objExcelSheet   Objeto Sheet de Apache POI, utilizado para administrar
     *                        el contenido de la hoja del archivo de Excel
     * @param headerSet       Conjunto de cadena de caracteres, con los campos como
     *                        llaves del mapa, para recuperar el contenido de los
     *                        mapas que representan los registros a escribir en la
     *                        hoja de cálculo
     * @param makingContent   Bandera que indica si se van a crear los registros del
     *                        archivo de Excel (nuevas celdas), en caso de un
     *                        archivo nuevo, o si se va a modificar en caso de
     *                        agregar contenido a un archivo de Excel utilizado como
     *                        plantilla
     * @param columnFileTitle Mapa en el que se define el nombre del atributo,
     *                        definido como llave, y el nombre final que llevará la
     *                        columna de información como valor, para definir los
     *                        encabezados finales del archivo de hoja de cáculo, si
     *                        este atributo no se envía, se toma por defecto el
     *                        nombre del atributo como llave extraído del primer
     *                        registro
     * @param initPostRow     Posición inicial del registro en el que el método
     *                        empezará a escribir la información enviada, dentro de
     *                        la hoja de cáculo
     * @param initPosColumn   Posición inicial de la columna en el que el método
     *                        empezará a escribir la información enviada, dentro de
     *                        la hoja de cáculo
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
     * Método auxiliar para escribir el contenido en la hoja de cálculo
     * 
     * @param objExcelSheet Objeto Sheet de Apache POI, utilizado para administrar
     *                      el contenido de la hoja del archivo de Excel
     * @param headerSet     Conjunto de cadena de caracteres, con los campos como
     *                      llaves del mapa, para recuperar el contenido de los
     *                      mapas que representan los registros a escribir en la
     *                      hoja de cálculo
     * @param makingContent Bandera que indica si se van a crear los registros del
     *                      archivo de Excel (nuevas celdas), en caso de un archivo
     *                      nuevo, o si se va a modificar en caso de agregar
     *                      contenido a un archivo de Excel utilizado como plantilla
     * @param row           Mapa con la llave-valor, que representa el campo, y el
     *                      contenido a escribir en el archivo de Excel
     * @param initPostRow   Posición inicial del registro en el que el método
     *                      empezará a escribir la información enviada, dentro de la
     *                      hoja de cáculo
     * @param countRow      Variable que representa el registro relativo en el cual
     *                      se va a escribir el contenido a través del método
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
     * Método auxiliar que setea la información en la celda, de acuerdo al tipo de
     * dato del valor a escribir
     * 
     * @param objExcelCell Objeto instancia de la clase de Apache POI en el que se
     *                     administra el contenido de la celda
     * @param cellContent  Objeto instancia de la clase, del valor a escribir en la
     *                     celda
     */
    private void setCellValue(Cell objExcelCell, Object cellContent) {
        if (cellContent instanceof Double || cellContent instanceof Integer) {
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
