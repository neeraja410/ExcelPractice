package excelpractce;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * 
Read the input from terminal : Console  -
1. Compare and print duplicates in first names
2. Compare and print duplicates in the first and last names
3. Compare and print duplicates of first names in the second excel sheet
4. Compare and print duplicates of first names of first excel sheet and first names of second excel sheet.
5. Compare and print duplicates of first names of first excel sheet with last names of second excel sheet
6. Compare and print total differences in both the excel sheets: In all columns
7. Compare and insert all the same rows into a new table : "Common_Student_Table.
 */

public class ReadExcel {
	public static void main(String[] args) {
		String JDBC_URL = "jdbc:mysql://localhost:3306/testdb?useSSL=false";
		String JDBC_USER = "root";
		String JDBC_PASSWORD = "neeraJAgadesh1!";
		File fileFound = new File("/Users/jagadesm/eclipse-workspace/First_Student_Program.xlsx");

		FileInputStream fileInputStream;
		try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
			String createStudent_ProgramTable = "CREATE TABLE IF NOT EXISTS Sudent_Program (name VARCHAR(255), lastname VARCHAR(255), studentid VARCHAR(255), address VARCHAR(255), phonenumber VARCHAR(255),class_name VARCHAR(255),age VARCHAR(255),teacher VARCHAR(255),fathername VARCHAR(255),student_join_date  VARCHAR(255))";
			String deleteStudent_ProgramTable = "DELETE FROM Sudent_Program";
			// delete all rows
			connection.createStatement().execute(deleteStudent_ProgramTable);
			// creating table if it doesn't exist.
			connection.createStatement().execute(createStudent_ProgramTable);
			fileInputStream = new FileInputStream(fileFound);
			Workbook workbook = new XSSFWorkbook(fileInputStream);
			XSSFSheet spreadsheet = (XSSFSheet) workbook.getSheetAt(0);
			Iterator<Row> rowIterator = spreadsheet.iterator();
			// connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
            ArrayList list2=new ArrayList();
			connection.setAutoCommit(false);
			String sql = "INSERT INTO Sudent_Program (name, lastname, studentid,address,phonenumber,class_name,age,teacher,fathername,student_join_date) VALUES (?, ?, ?,?,?,?,?,?,?,?)";
			PreparedStatement statement = connection.prepareStatement(sql);

			rowIterator.next(); // skip the header row

			int rowCount = 0;
			while (rowIterator.hasNext()) {
				rowCount++;
				Row nextRow = rowIterator.next();
				Iterator<Cell> cellIterator = nextRow.cellIterator();

				int ColNumber =0;
				while (cellIterator.hasNext()) {

					System.out.println("Row Number :" + rowCount + "  Col Number" + ColNumber++);
					Cell nextCell = cellIterator.next();

					int columnIndex = nextCell.getColumnIndex();
					switch (columnIndex) {
					case 0:
						String name = nextCell.getStringCellValue();
						statement.setString(1, name);
						break;
					case 1:
						String lastname = nextCell.getStringCellValue();
						statement.setString(2, lastname);
						break;

					case 2:

						String studentid = String.valueOf(nextCell.getNumericCellValue());
						statement.setString(3, studentid);
						break;
					case 3:
						String address = nextCell.getStringCellValue();
						statement.setString(4, address);
						break;
					case 4:
						String phonenumber = String.valueOf(nextCell.getNumericCellValue());
						statement.setString(5, phonenumber);
						break;

					}
				}
				statement.addBatch();
			}
			statement.executeBatch();
			connection.commit();
			System.out.println("Inserted Successfully intothe database");
			//count the rows from database and print names and their counts into a text file.
//execute a get query
			//copy them into a list 
			//compare names 
			//if duplicates are count the names
			//print the people names and their count
			//output: Raju - 2, ramana - 3, rajesh -4
			//this should be printed into a file.
			
			//List<ArrayList> student_program=new ArrayList<>();
			//ArrayList list=null;
			String sql1="SELECT * FROM Sudent_Program";
			PreparedStatement statement2 = connection.prepareStatement(sql1);
			statement2.execute(sql1);
			ResultSet resultset=statement2.executeQuery(sql1);
			List<String> names = new ArrayList<String>();

			/*while(resultset.next()) {
				names.add(resultset.getString(1));  
			}*/
			while(resultset.next()) {
				names.addAll((Collection<? extends String>) resultset.getArray(1));  
			}
			 
			 System.out.println(names);
			 /*int count =0;
			 for(int i=0;i<names.size();i++) {
				 for(int j=i+1;j<names.size();j++) {
				if( names.get(i).equalsIgnoreCase(names.get(j)))
				{
					count++;
					System.out.println(names.get(i)+"-"+count);
				}
			 }
			 }*/
		}

		// System.out.println(sheet.getRow(1).getCell(0));
		catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}

}
