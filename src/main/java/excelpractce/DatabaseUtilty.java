package excelpractce;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DatabaseUtilty {
private static final Logger logger = LoggerFactory.getLogger(DatabaseUtilty.class);

static {
    try (Connection connection = DriverManager.getConnection(Constants.JDBC_URL, Constants.JDBC_USER, Constants.JDBC_PASSWORD)) {
		connection.createStatement().execute(DatabaseQueries.delete_First_Student_Table);
		connection.createStatement().execute(DatabaseQueries.deleteSecond_Student_Table);
        connection.createStatement().execute(DatabaseQueries.createFirst_Student_Program);
        connection.createStatement().execute(DatabaseQueries.createSecond_Student_Program);
        connection.close();
    } catch (SQLException e) {
        logger.error("Error creating tables", e);
    }
}
public static  Connection getConnection() throws SQLException{
	logger.info("Getting database connection");
	return DriverManager.getConnection(Constants.JDBC_URL, Constants.JDBC_USER, Constants.JDBC_PASSWORD);
}

}
