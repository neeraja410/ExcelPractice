package excelpractce;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtility {
	public static XSSFSheet readSheetfromExcelFile(String filepath)
	{
		File First_Student_Program_File = new File(filepath);
		FileInputStream fileInputStream;
		Workbook workbook = null;
		try {
			fileInputStream = new FileInputStream(First_Student_Program_File);
			workbook = new XSSFWorkbook(fileInputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		XSSFSheet spreadsheet = (XSSFSheet) workbook.getSheetAt(0);
		return spreadsheet;
	}
}
