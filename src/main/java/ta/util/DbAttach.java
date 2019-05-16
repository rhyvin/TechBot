package ta.util;

import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.sql.Connection;

import java.sql.*;
import java.util.List;

public class DbAttach implements IntCommand{
    private final Logger logger = LoggerFactory.getLogger(DbAttach.class);
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {

        final Logger logger = LoggerFactory.getLogger(DbAttach.class);

        try {
            String guild = event.getGuild().getName();
            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/techbot?useSSL=false", DSecrets.UNAME, DSecrets.UPASS);
            String sqlCreate = "CREATE TABLE IF NOT EXISTS "+guild+" (`id` VARCHAR(45) NOT NULL,`name` VARCHAR(45) NOT NULL,`mute` INT(11) NULL DEFAULT '0',`rmute` VARCHAR(45) NULL DEFAULT 'N/A',`warnings` INT(11) NULL DEFAULT '0',`rwarnings` VARCHAR(45) NULL DEFAULT 'N/A',`kicks` INT(11) NULL DEFAULT '0',`rkicks` VARCHAR(45) NULL DEFAULT 'N/A',`bans` INT(11) NULL DEFAULT '0',`rbans` VARCHAR(45) NULL DEFAULT 'N/A',`mmod` VARCHAR(45) NULL,`mdatetime` VARCHAR(45) NULL,`wmod` VARCHAR(45) NULL,`wdatetime` VARCHAR(45) NULL,`kmod` VARCHAR(45) NULL,`kdatetime` VARCHAR(45) NULL,`bmod` VARCHAR(45) NULL,`bdatetime` VARCHAR(45) NULL,PRIMARY KEY (`id`))";
            DatabaseMetaData dbm =con.getMetaData();
            ResultSet tables = dbm.getTables(null,null, guild,null);

            // create the java statement
            Statement st = con.createStatement();
            st.execute(sqlCreate);
            //Verify table exists or was created.
            if (tables.next()){
                logger.info(String.format("There is already a table " + guild +" no further action is needed."));
            }
            else {
                logger.info(String.format("The table for " + guild +" was not created. Please contact an administrator to diagnose the issue."));
            }
            st.close();
        }
        catch (Exception e)
        {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
    }

    @Override
    public String getHelp() {
        return "used to test database connection";
    }

    @Override
    public String getInvoke() {
        return "dbtest";
    }
}
