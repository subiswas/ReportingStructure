package util;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import actions.ResultUtil;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//import xslt.debugger.Value;

import javax.swing.text.html.HTMLDocument;

public class ExcelUtils {

	public static Iterator<Object[]> getTestData(String strWorkbookPath, String strWorksheetName) {
		List<Object[]> data = new ArrayList<Object[]>();

		int inRowCounter = 0;

		try {
			FileInputStream file = new FileInputStream(new File(strWorkbookPath));

			//Get the workbook instance for XLS file
			HSSFWorkbook workbook = new HSSFWorkbook(file);

			//Get first sheet from the workbook
			HSSFSheet sheet = workbook.getSheet(strWorksheetName);

			//Get iterator to all the rows in current sheet
			Iterator<Row> rowIterator = sheet.rowIterator();

			Row firstRow = rowIterator.next();

			Map<String, String> columnNamesMap = getColumnNames(firstRow);

			while (rowIterator.hasNext()) {
				//Iterator<Cell> cellIterator=rowIterator.next().cellIterator();
				Row row = rowIterator.next();
				if(row.getCell(4).toString().contentEquals("EXIT")){
					break;
				}
				Map<String, String> rowMap = new LinkedHashMap();
				for (Entry entry : columnNamesMap.entrySet()) {
					String strColumnName = entry.getKey().toString();
					int inColumnIndex = getColumnIndex(firstRow, strColumnName);
					if (inColumnIndex >= 0) {
						String strValue = "";
						try {
							strValue = row.getCell(inColumnIndex).toString();
						} catch (Exception e) {
						}
						rowMap.put(strColumnName, strValue);
					/*if(!strValue.isEmpty() && strValue !=null){
						rowMap.put(strColumnName, strValue);
					}*/
					}

				}
				//if(rowMap !=null && !rowMap.isEmpty()){
				data.add(new Object[]{rowMap});
				//}


			}

			file.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return data.iterator();
	}

	public static void updateTestData(String strWorkbookPath, String strWorksheetName, String strIteration, String strColName, String strValue) {
		try {
			FileInputStream file = new FileInputStream(new File(strWorkbookPath));
			HSSFWorkbook workbook = new HSSFWorkbook(file);
			HSSFSheet sheet = workbook.getSheet(strWorksheetName);

			Iterator<Row> rowIterator = sheet.rowIterator();

			Row firstRow = rowIterator.next();

			while (rowIterator.hasNext()) {
				Row row = rowIterator.next();
				if (row.getCell(getColumnIndex(firstRow, "Iteration")).getStringCellValue().trim().equalsIgnoreCase(strIteration)) {
					row.getCell(getColumnIndex(firstRow, strColName)).setCellValue(strValue);
					break;
				}
			}

			file.close();

			FileOutputStream outFile = new FileOutputStream(new File(strWorkbookPath));
			workbook.write(outFile);
			outFile.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static Map<String, String> getColumnNames(Row row) {
		Map<String, String> columnNamesMap = new LinkedHashMap();

		Iterator<Cell> cells = row.cellIterator();

		while (cells.hasNext()) {
			String strColumnName = cells.next().toString();
			columnNamesMap.put(strColumnName, strColumnName);
		}

		return columnNamesMap;
	}

	private static int getColumnIndex(Row row, String strColumnName) {
		Map<String, String> columnNamesMap = new LinkedHashMap();
		int inColumnIndex = -1;

		Iterator<Cell> cells = row.cellIterator();

		while (cells.hasNext()) {
			Cell cell = cells.next();
			if (cell.toString().equalsIgnoreCase(strColumnName)) {
				inColumnIndex = cell.getColumnIndex();
				break;
			}
		}

		return inColumnIndex;
	}

	public static  List<String> verifyDataInInventory(String filePath) {
		List<String> excelValues = new ArrayList<>();
		try {
			InputStream ExcelFileToRead = new FileInputStream(new File(filePath));
			XSSFWorkbook wb = new XSSFWorkbook(ExcelFileToRead);
			XSSFSheet sheet = wb.getSheetAt(0);
			Row row;
			Cell cell;
			Iterator<Row> rowIterator = sheet.iterator();
			while (rowIterator.hasNext()) {
				row = rowIterator.next();
				Iterator<Cell> cellIterator = row.cellIterator();
				while (cellIterator.hasNext()) {
					cell = cellIterator.next();
					switch (cell.getCellType()) {
						case Cell.CELL_TYPE_BOOLEAN:
							excelValues.add(String.valueOf(cell.getBooleanCellValue()));
							break;

						case Cell.CELL_TYPE_NUMERIC:
							excelValues.add(Double.toString(cell.getNumericCellValue()));
							break;

						case Cell.CELL_TYPE_STRING:
							excelValues.add(cell.getStringCellValue());
							break;

						case Cell.CELL_TYPE_BLANK:
							System.out.println(" ");
							break;
					}
				}
			}
		} catch (Exception e) {
			System.err.println("Exception :" + e.getMessage());
		}
		return excelValues;
	}
	public static  List<String> verifyDataInInventorySecondSheet(String filePath) {
		List<String> excelValues = new ArrayList<>();
		try {
			InputStream ExcelFileToRead = new FileInputStream(new File(filePath));
			XSSFWorkbook wb = new XSSFWorkbook(ExcelFileToRead);
			XSSFSheet sheet = wb.getSheetAt(1);
			Row row;
			Cell cell;
			Iterator<Row> rowIterator = sheet.iterator();
			while (rowIterator.hasNext()) {
				row = rowIterator.next();
				Iterator<Cell> cellIterator = row.cellIterator();
				while (cellIterator.hasNext()) {
					cell = cellIterator.next();
					switch (cell.getCellType()) {
						case Cell.CELL_TYPE_BOOLEAN:
							excelValues.add(String.valueOf(cell.getBooleanCellValue()));
							break;

						case Cell.CELL_TYPE_NUMERIC:
							excelValues.add(Double.toString(cell.getNumericCellValue()));
							break;

						case Cell.CELL_TYPE_STRING:
							excelValues.add(cell.getStringCellValue());
							break;

						case Cell.CELL_TYPE_BLANK:
							System.out.println(" ");
							break;
					}
				}
			}
		} catch (Exception e) {
			System.err.println("Exception :" + e.getMessage());
		}
		return excelValues;
	}
}
