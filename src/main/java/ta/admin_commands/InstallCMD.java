package ta.admin_commands;


import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ta.config.Config;
import ta.util.IntCommand;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.List;

public class InstallCMD implements IntCommand {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) throws ClassNotFoundException, IOException {
        Config config = new Config(new File("botconfig.json"));

        final Logger logger = LoggerFactory.getLogger(InstallCMD.class);

        String guild = event.getGuild().getName();

        //Create Basic Roles
        event.getGuild().getController().createRole().setName("Members").setColor(0x05920F).setPermissions(Permission.MESSAGE_READ, Permission.MESSAGE_HISTORY, Permission.CREATE_INSTANT_INVITE, Permission.NICKNAME_CHANGE, Permission.MESSAGE_WRITE, Permission.MESSAGE_TTS, Permission.MESSAGE_EMBED_LINKS, Permission.MESSAGE_ATTACH_FILES, Permission.MESSAGE_MENTION_EVERYONE, Permission.MESSAGE_EXT_EMOJI, Permission.MESSAGE_ADD_REACTION, Permission.VOICE_CONNECT, Permission.VOICE_SPEAK, Permission.VOICE_USE_VAD).setMentionable(false).queue();
        event.getChannel().sendMessage("Role Members created").queue();

        event.getGuild().getController().createRole().setName("muted").setColor(0xBF0000).setPermissions(Permission.MESSAGE_READ, Permission.MESSAGE_HISTORY).setMentionable(false).queue();
        event.getChannel().sendMessage("Role muted created").queue();


        //Create database for discord server defined by 'guild'
        try {

            Connection con = DriverManager.getConnection(
                    config.getString("host"), config.getString("uname"), config.getString("upass"));
            String sqlCreate = "CREATE TABLE IF NOT EXISTS " + guild + " (`id` VARCHAR(45) NOT NULL,`name` VARCHAR(45) NOT NULL,`mute` INT(11) NULL DEFAULT '0',`rmute` VARCHAR(45) NULL DEFAULT 'N/A',`warnings` INT(11) NULL DEFAULT '0',`rwarnings` VARCHAR(45) NULL DEFAULT 'N/A',`kicks` INT(11) NULL DEFAULT '0',`rkicks` VARCHAR(45) NULL DEFAULT 'N/A',`bans` INT(11) NULL DEFAULT '0',`rbans` VARCHAR(45) NULL DEFAULT 'N/A',`mmod` VARCHAR(45) NULL,`mdatetime` VARCHAR(45) NULL,`wmod` VARCHAR(45) NULL,`wdatetime` VARCHAR(45) NULL,`kmod` VARCHAR(45) NULL,`kdatetime` VARCHAR(45) NULL,`bmod` VARCHAR(45) NULL,`bdatetime` VARCHAR(45) NULL,PRIMARY KEY (`id`))";

            // create the java statement
            Statement st = con.createStatement();
            st.execute(sqlCreate);

            st.close();
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }


        try {

            Connection ver = DriverManager.getConnection(
                    config.getString("host"), config.getString("uname"), config.getString("upass"));
            DatabaseMetaData dbm = ver.getMetaData();
            ResultSet tables = dbm.getTables(null, null, guild, null);
            {//Verify table exists or was created.
                if (tables.next()) {
                    logger.info(String.format("There is already a table " + guild + " no further action is needed."));
                    event.getChannel().sendMessage("There is already a table " + guild + " no further action is needed.").queue();
                } else {
                    logger.info(String.format("The table for " + guild + " was not created. Please contact an administrator to diagnose the issue."));
                    event.getChannel().sendMessage("The table for " + guild + " was not created. Please contact an administrator to diagnose the issue.").queue();
                }
            }
        }catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
    }

    @Override
    public String getHelp() {
        return "Sets up backend server for data loging and moderation purposes.";
    }

    @Override
    public String getInvoke() {
        return "install";
    }
}
