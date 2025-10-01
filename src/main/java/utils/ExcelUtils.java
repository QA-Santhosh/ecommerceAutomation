package utils;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.*;

public class ExcelUtils {

    public static String getCellValue(String path, String sheetName, int row, int col) {
        try {
            FileInputStream fis = new FileInputStream(path);
            Workbook wb = WorkbookFactory.create(fis);
            Sheet sheet = wb.getSheet(sheetName);
            return sheet.getRow(row).getCell(col).getStringCellValue();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    // New helper method to read all brands from a column
    public static List<String> getColumnValues(String path, String sheetName, String columnName) {
        List<String> values = new ArrayList<>();
        try {
            FileInputStream fis = new FileInputStream(path);
            Workbook wb = WorkbookFactory.create(fis);
            Sheet sheet = wb.getSheet(sheetName);
            if (sheet == null) return values;

            // Find column index
            Row headerRow = sheet.getRow(0);
            int colIndex = -1;
            for (Cell cell : headerRow) {
                if (cell.getStringCellValue().trim().equalsIgnoreCase(columnName)) {
                    colIndex = cell.getColumnIndex();
                    break;
                }
            }

            if (colIndex == -1) return values; // column not found

            // Read all rows in the column (starting from row 1)
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row != null) {
                    Cell cell = row.getCell(colIndex);
                    if (cell != null && cell.getCellType() == CellType.STRING) {
                        String val = cell.getStringCellValue().trim();
                        if (!val.isEmpty()) values.add(val);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return values;
    }
}
