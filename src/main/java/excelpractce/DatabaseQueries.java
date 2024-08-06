package excelpractce;

public class DatabaseQueries {
	static String delete_First_Student_Table = "DROP TABLE First_Student_Program";
	static String deleteSecond_Student_Table = "DROP TABLE Second_Student_Program";
	static String createFirst_Student_Program = "CREATE TABLE IF NOT EXISTS First_Student_Program ( name VARCHAR(255), lastname VARCHAR(255), id VARCHAR(255),address VARCHAR(255),phonenumber VARCHAR(255))";
	static String createSecond_Student_Program = "CREATE TABLE IF NOT EXISTS Second_Student_Program ( name VARCHAR(255), lastname VARCHAR(255), id VARCHAR(255),address VARCHAR(255),phonenumber VARCHAR(255))";
    static String select_Star_From = "SELECT * FROM ";
}
