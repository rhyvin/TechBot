package ta.admin_commands;


import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ta.util.DbAttach;
import ta.util.IntCommand;

import java.sql.*;
import java.util.List;

public class InstallCMD implements IntCommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) throws ClassNotFoundException {

        final Logger logger = LoggerFactory.getLogger(DbAttach.class);

        //Create database for discord server defined by 'guild'
        try {
            String guild = event.getGuild().getName();
            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/techbot?useSSL=false", DSecretsAdm.UNAME, DSecretsAdm.UPASS);
            String sqlCreate = "CREATE TABLE IF NOT EXISTS "+guild+" (`id` INT NOT NULL, `name` VARCHAR(45) NOT NULL, `mute` TINYINT NULL DEFAULT 0,`rmute` VARCHAR(45) NULL,`warnings` TINYINT NULL DEFAULT 0, `rwarnings` VARCHAR(45) NULL, `kicks` TINYINT NULL DEFAULT 0, `rkicks` VARCHAR(45) NULL, `bans` TINYINT NULL DEFAULT 0, `rbans` VARCHAR(45) NULL, PRIMARY KEY (`id`))";
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


        //Create default entries into a database for managing discord server
        try {
            String guild = event.getGuild().getName();
            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/techbot?useSSL=false", DSecretsAdm.UNAME, DSecretsAdm.UPASS);
            String sqlCreate = "CREATE TABLE IF NOT EXISTS "+guild+" (`id` INT NOT NULL, `name` VARCHAR(45) NOT NULL, `mute` TINYINT NULL DEFAULT 0,`rmute` VARCHAR(45) NULL,`warnings` TINYINT NULL DEFAULT 0, `rwarnings` VARCHAR(45) NULL, `kicks` TINYINT NULL DEFAULT 0, `rkicks` VARCHAR(45) NULL, `bans` TINYINT NULL DEFAULT 0, `rbans` VARCHAR(45) NULL, PRIMARY KEY (`id`))";
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
        return null;
    }

    @Override
    public String getInvoke() {
        return "install";
    }
}
