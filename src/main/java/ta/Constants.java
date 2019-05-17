package ta;

import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

public class Constants {
    public static String prefix = "~";
    public void handle(List<String> args, GuildMessageReceivedEvent event) throws ClassNotFoundException {
        String guild = event.getGuild().getName();
        String query = "SELECT `prefix`  FROM  botman WHERE `guild` = '" + guild + "'";
        try {

            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://157.230.191.75:3306/techbot?useSSL=false", "botmanrm", "rYxG0Drk9fSO!2fAX");
            PreparedStatement pst = con.prepareStatement(query);
            ResultSet rs = pst.executeQuery();
            {
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

    }

    public static final long OWNER = 255934842078756864L;
}
