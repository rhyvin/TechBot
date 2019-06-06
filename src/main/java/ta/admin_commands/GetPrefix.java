package ta.admin_commands;

import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import ta.util.IntCommand;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

public class GetPrefix implements IntCommand {
    public static String prefx;

    @Override

        public void handle (List < String > args, GuildMessageReceivedEvent event) throws ClassNotFoundException {

            String guild = event.getGuild().getName();
            String query = "SELECT `prefix`  FROM  botman WHERE `guild` = '" + guild +"'";
            try {

                Connection con = DriverManager.getConnection(
                        "jdbc:mysql://157.230.191.75:3306/techbot?useSSL=false&autoReconnect=true", "botmanrm", "rYxG0Drk9fSO!2fAX");
                PreparedStatement pst = con.prepareStatement(query);
                ResultSet rs = pst.executeQuery();
                while (rs.next()) {
                    String prfx = rs.getString("prefix");

                    prefx = prfx;

                }
                pst.close();

            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        MessageEmbed embed = EmbedUtils.defaultEmbed()
                .addField("your prefix is: " + prefx,"",false)
                .build();
        event.getChannel().sendMessage(embed).queue();
        }



    @Override
    public String getHelp() {
        return "it borked up.";
    }

    @Override
    public String getInvoke() {
        return "getprefix";
    }
}
