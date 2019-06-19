package ta.tickets;

import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import ta.config.Config;
import ta.util.IntCommand;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.List;

public class ticketUpdate implements IntCommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) throws ClassNotFoundException, IOException {

        TextChannel channel = event.getChannel();
        Member member = event.getMember();
        Member selfMember = event.getGuild().getSelfMember();

        Message info = event.getMessage();



        String ticketNum = String.join(" ", args.subList(1, args.size()));
        String note = String.join(" ", args.subList(1, args.size()));

        if (ticketNum.isEmpty() || args.size() < 2) {
            channel.sendMessage("Missing Arguments").queue();
            return;
        }

        Config config = new Config(new File("botconfig.json"));
        try {
            String user = member.getEffectiveName();
            String guild = event.getGuild().getName();

            Connection con = DriverManager.getConnection(
                    config.getString("host"), config.getString("uname"), config.getString("upass"));
            String sqlCreate = "UDPATE `tickets` SET `notes` = '"+note+"' AND `ticketID` = '"+ticketNum+"' WHERE `ticketID` = '"+ticketNum+"');";




            // create the java statement
            Statement st = con.createStatement();
            st.execute(sqlCreate);
            st.close();

        }catch (Exception e)
        {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        try{
            String user = member.getEffectiveName();
            String guild = event.getGuild().getName();
            String query = "SELECT *  FROM `tickets` WHERE  `ticketID` = '"+ticketNum+"';";

            Connection con2 = DriverManager.getConnection(
                    config.getString("host"), config.getString("uname"), config.getString("upass"));

            PreparedStatement pst = con2.prepareStatement(query);
            ResultSet rs = pst.executeQuery();
            {
                while (rs.next()) {
                    String ticketID = rs.getString("ticketID");
                    String usr = rs.getString("user");
                    String guld = rs.getString("guild");
                    String assignedTo = rs.getString("assignedTo");
                    String status = rs.getString("status");
                    String notes =rs.getString("notes");
                    if (rs.wasNull()) {
                        notes = "";
                    }


                    MessageEmbed embed = EmbedUtils.defaultEmbed()
                            .setColor(member.getColor())
                            .addField("__**Trouble Ticket**__", "", true)
                            .addField("User: " + usr + " Server: " + guld, "Ticket info: " + ticketNum, false)
                            .addField("Ticket ID: " + ticketID, "", false)
                            .addField("Assigned to: " + assignedTo, "", false)
                            .addField("Status: " + status, "", false)
                            .addField("Admin Notes: " + notes, "", false)
                            .build();

                    event.getChannel().sendMessage(embed).queue();
                }
            }

        }catch (Exception e)
        {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }

    }

    @Override
    public String getHelp() {
        return null;
        //TODO add help info here
    }

    @Override
    public String getInvoke() {
        return "ticket update";
    }
}
