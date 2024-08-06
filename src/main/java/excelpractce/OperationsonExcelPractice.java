package excelpractce;
import java.sql.Connection;
import java.util.List;
import java.util.Scanner;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import excelpractice.exceptions.InvalidInputException;

public class OperationsonExcelPractice extends DatabaseUtilty {
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

	static Scanner input = new Scanner(System.in);
	private static final Logger logger = LoggerFactory.getLogger(OperationsonExcelPractice.class);

	/**
	 * Method: This message takes the input of some and return x
	 * @param userInputForWhichMethodToExecute
	 * @throws Exception
	 */
	public static void executeTheUserInputtedMethod(int userInputForWhichMethodToExecute) throws Exception
	{
		//initialize variable to null
		String messageAboutFunctionExecuting = null;
		//Switch case to execute
		switch (userInputForWhichMethodToExecute) {
		case 0:  messageAboutFunctionExecuting="Please enter correct input";
		break;

		case 1: messageAboutFunctionExecuting="Compare and print duplicates in first names";
		printDuplicatesfromGivenTable(Constants.FIRST_STUDENT_PROGRAM_FILE_PATH, Constants.FIRST_STUDENT_PROGRAM_TABLE);
		break;

		case 2: messageAboutFunctionExecuting="Compare and print duplicates in the first and last names";
		break;

		case 3: messageAboutFunctionExecuting="Compare and print duplicates of first names in the second excel sheet";
		printDuplicatesfromGivenTable( Constants.SECOND_PROGRAM_EXCEL_FILE_PATH, Constants.Second_Student_Program_table);
		break;

		case 4:  messageAboutFunctionExecuting= "Compare and print duplicates of first names of first excel sheet and first names of second excel sheet.";
		compareFirstAndSecondTabelsNames();
		break;
		
		case 5:  messageAboutFunctionExecuting = "Compare and print duplicates of first names of first excel sheet with last names of second excel sheet";
		break;
		
		case 6:  messageAboutFunctionExecuting = "Compare and print total differences in both the excel sheets: In all columns\n";
		break;
		
		case 7:  messageAboutFunctionExecuting = "Compare and insert all the same rows into a new table : \"Common_Student_Table.\n";
		break;
		}
		
		logger.info(messageAboutFunctionExecuting);
	}

	public static void readInputDataAndExecuteTheAction() throws Exception {
		//get input from command line
		int userInputForMethod = getUserInputfromCommandLine();

		//execute the user input function and return void
		executeTheUserInputtedMethod(userInputForMethod);
	}

	//option 1 and option 3
	public static void printDuplicatesfromGivenTable(String filepath, String tablename) throws Exception {
		List<String> student_names=readNamesFromFilesAndInsertAndReplyNames(filepath, tablename);
		printDuplicatesinList(student_names);
	}

	public static void printDuplicatesinList(List<String> listofNames)
	{
		for(String name: listofNames)
		{
			for(String differentName: listofNames) {
				if(name.equalsIgnoreCase(differentName) && !name.equals(differentName))
				{
					logger.info("Duplicates name are"+name);
				}
			}
		}
	}

	//4. Compare and print duplicates of first names of first excel sheet and first names of second excel sheet.
	/**
	 * This will compare first and second names in the table
	 * @param Empty
	 * @return Empty : This prints the value in the system out and print ln.
	 * @throws Exception
	 */
	public static void compareFirstAndSecondTabelsNames() throws Exception {

		//find list of names from first table.
		List<String> first_Student_Program_table_names = 
				readNamesFromFilesAndInsertAndReplyNames(
						Constants.FIRST_STUDENT_PROGRAM_FILE_PATH, Constants.FIRST_STUDENT_PROGRAM_TABLE);

		//find list of names from second table.
		List<String> second_Student_Program_table_names = 
				readNamesFromFilesAndInsertAndReplyNames(
						Constants.SECOND_PROGRAM_EXCEL_FILE_PATH, Constants.Second_Student_Program_table);

		if (    first_Student_Program_table_names == null    ||
				first_Student_Program_table_names.isEmpty()  ||
				second_Student_Program_table_names == null   ||
				second_Student_Program_table_names.isEmpty()   )
		{
			logger.info("The first list is empty or null. Please check the database. ", first_Student_Program_table_names);
			throw new InvalidInputException("The first list is empty or null. Please update the list with new values");
		}

		if ( first_Student_Program_table_names.retainAll(second_Student_Program_table_names))
		{
			logger.info("Duplicates names of first and second table are " + first_Student_Program_table_names);
		}
	}

	public static final List<String> readNamesFromFilesAndInsertAndReplyNames(String filepath, String tablename) throws Exception {
		logger.info("Received the request for reading names from the table and file path");

		XSSFSheet spreadsheet = ExcelUtility.readSheetfromExcelFile(filepath);
		logger.info("Sucessfully read the details from spread sheet");

		Connection connection =getConnection();
		logger.info("Sucessfully able to create a connection");

		DatabaseDAO.insertSheetValuesIntoDatabase(tablename, spreadsheet, connection);
		logger.info("Inserted Sucessfully into the connection");
		
		logger.info("Called function to return the values from database");
		return DatabaseDAO.readAllValuesFromDatabase(tablename,connection);
	}

	public static int getUserInputfromCommandLine()
	{
		return input.nextInt();
	}

	public static void main(String[]args) throws Exception {
		try {
			readInputDataAndExecuteTheAction();
		} catch (InvalidInputException e)
		{
			logger.error("This method is executed with invalid input: " + e.getLocalizedMessage());
		}
		input.close();
	}
}
