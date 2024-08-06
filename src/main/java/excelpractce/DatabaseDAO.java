package excelpractce;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import excelpractice.exceptions.InvalidExcelValueException;

public class DatabaseDAO {
	private static final Logger logger = LoggerFactory.getLogger(OperationsonExcelPractice.class);

	public static final List<String> readAllValuesFromDatabase(String tablename, Connection connection) throws SQLException
	{
		List<String> names = new ArrayList<String>();
		
		checkIfInputIsValid(tablename, connection);
		
		String queryToReadNamesfromTable = DatabaseQueries.select_Star_From + tablename;
		
		PreparedStatement statementToReadFromTable = connection.prepareStatement(queryToReadNamesfromTable);
		
		ResultSet resultset=statementToReadFromTable.executeQuery(queryToReadNamesfromTable);
		
		if(resultset == null)
		{
			logger.error("Failed to read all values from database. Connection is not created: ", tablename);
			return names;
		}

		while( resultset.next()) {
			names.add(resultset.getString(Constants.startingIndex));  
		}
		return names;
	}

	private static final List<String> checkIfInputIsValid(String tablename, Connection connection) throws SQLException
	{
		List<String> names = new ArrayList<String>();

		if(tablename == null || tablename.isBlank())
		{
			logger.error("Failed to read all values from database. Table Doesnt exist or null: ", tablename);
			return names;
		}
		
		if(connection == null || !connection.isValid(10))
		{
			logger.error("Failed to read all values from database. Connection is not created: ", tablename);
			return names;
		}
		return names;
	}
	
	
	public static void insertSheetValuesIntoDatabase(String tableName, XSSFSheet spreadsheet, Connection connection ) throws Exception
	{
		if(tableName == null || tableName.isEmpty())
		{
			logger.error("Failed to the excel sheet: ");
			return ;
		}
		
		String sql = "INSERT INTO " + tableName + "(name, lastname, id, address, phonenumber) VALUES (?, ?, ?,?,?)";

		try(PreparedStatement preparedstatement=connection.prepareStatement(sql);)
		{
			Iterator<Row> rowIterator = spreadsheet.iterator();
			if(rowIterator==null || !rowIterator.hasNext())
			{
				logger.info("Failed to insert into the database because file is empty");
				throw new Exception("Failed to insert because empty values are present");
			}
			rowIterator.next(); // skip the header row

			int rowCount = 0;
			int atleastOneRow = 0;
			int atLeastOneColumn = 0;
			while (rowIterator.hasNext()) 
			{
				atleastOneRow++;
				rowCount++;
				Row nextRow = rowIterator.next();
				Iterator<Cell> cellIterator = nextRow.cellIterator();
				int ColNumber = 0;
				while (cellIterator.hasNext()) {
					atLeastOneColumn++;
					ColNumber++;
					//logger.info("Row Number :" + rowCount + "  Col Number" + ColNumber++);
					Cell nextCell = cellIterator.next();
					int columnIndex = nextCell.getColumnIndex();
					switch (columnIndex) {
					case 0:
						String name = nextCell.getStringCellValue();
						if(name.isEmpty() || name.isBlank())
						{
							throw new InvalidExcelValueException("empty value  and catch it");
						}
						preparedstatement.setString(1, name);
						break;
					case 1:
						String lastname = nextCell.getStringCellValue();
						if(lastname.isEmpty() || lastname.isBlank())
						{
							throw new InvalidExcelValueException("empty value  and catch it");
						}
						preparedstatement.setString(2, lastname);
						break;

					case 2:
						String studentid = String.valueOf(nextCell.getNumericCellValue());
						preparedstatement.setString(3, studentid);
						break;
					case 3:
						String address = nextCell.getStringCellValue();
						preparedstatement.setString(4, address);
						break;
					case 4:
						String phonenumber = String.valueOf(nextCell.getNumericCellValue());
						preparedstatement.setString(5, phonenumber);
						break;
					}
				}

				if(ColNumber == 5)
				{
					preparedstatement.addBatch();
				}
				else 
				{
					logger.info("We found one value is missing in the row number " + rowCount + " hence skipped it");
				}
			}
			if(atLeastOneColumn>=1 || atleastOneRow >=1)
			{
				preparedstatement.executeBatch();
				logger.info("Inserted Successfully intothe database");
			}

		} catch (InvalidExcelValueException e) {
			logger.warn("Customer has passed invalid Input file:" + e.getMessage());
			throw e;
		}
	}
}
