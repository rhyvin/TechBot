package ta.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DbAttach{


    private static void dbnfo(String[] args) {
        Logger logger = LoggerFactory.getLogger(DbAttach.class);

        Connection conn = null;
        Statement stmt = null;

        try {
            logger.info("Testing Database connection...");
            Connection con = DriverManager.getConnection(DSecrets.HOST, DSecrets.UNAME, DSecrets.UPASS);
            logger.info("Database Connection Successfull.");
            con.close();
            logger.info("Database connection Terminated.");
        }
        catch (SQLException err) {
            logger.error(err.getMessage());
        }

    }

}
