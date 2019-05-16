package ta.admin_commands;

import com.jagrosh.jdautilities.commons.utils.FinderUtil;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import ta.Constants;
import ta.util.IntCommand;

import java.sql.*;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class RapSheetCMD implements IntCommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) throws ClassNotFoundException {

        if (args.isEmpty()) {
            event.getChannel().sendMessage("Missing arguments, check `" + Constants.prefix + "help " + getInvoke() + "`").queue();
            return;
        }

        String joined = String.join("", args);
        List<User> foundUsers = FinderUtil.findUsers(joined, event.getJDA());

        if (foundUsers.isEmpty()) {

            List<Member> foundMember = FinderUtil.findMembers(joined, event.getGuild());

            if (foundMember.isEmpty()) {
                event.getChannel().sendMessage("No user found for `" + joined + "`").queue();
                return;
            }

            foundUsers = foundMember.stream().map(Member::getUser).collect(Collectors.toList());

        }

        User user = foundUsers.get(0);
        Member member = event.getGuild().getMember(user);
        String id = user.getId();
        String guild = event.getGuild().getName();

        String query = "SELECT *  FROM " + guild + " WHERE ID = '" + id + "'";
        try {

            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/techbot?useSSL=false", DSecretsAdm.UNAME, DSecretsAdm.UPASS);
            PreparedStatement pst = con.prepareStatement(query);
            ResultSet rs = pst.executeQuery();
            {
                while (rs.next()) {
                    String sid = rs.getString("id");
                    String nam = rs.getString("name");
                    String war = rs.getString("warnings");
                    String rwar = rs.getString("rwarnings");
                    if (rs.wasNull()){
                        rwar = "";
                    }
                    String kik = rs.getString("kicks");
                    String rkik = rs.getString("rkicks");
                    if (rs.wasNull()){
                        rkik = "";
                    }
                    String ban = rs.getString("bans");
                    String rban = rs.getString("rbans");
                    if (rs.wasNull()){
                        rban = "";
                    }
                    String mut = rs.getString("mute");
                    String rmute = rs.getString("rmute");
                    if (rs.wasNull()){
                        rmute = "";
                    }
                    String mdatetime = rs.getString("mdatetime");
                    if (rs.wasNull()){
                        mdatetime = "";
                    }
                    String mmod =rs.getString("mmod");
                    if (rs.wasNull()){
                        mmod = "";
                    }
                    String wdatetime = rs.getString("wdatetime");
                    if (rs.wasNull()){
                        wdatetime = "";
                    }
                    String wmod =rs.getString("wmod");
                    if (rs.wasNull()){
                        wmod = "";
                    }
                    String kdatetime = rs.getString("kdatetime");
                    if (rs.wasNull()){
                        kdatetime = "";
                    }
                    String kmod =rs.getString("kmod");
                    if (rs.wasNull()){
                        kmod = "";
                    }
                    String bdatetime = rs.getString("bdatetime");
                    if (rs.wasNull()){
                        bdatetime = "";
                    }
                    String bmod =rs.getString("bmod");
                    if (rs.wasNull()){
                        bmod = "";
                    }


                    MessageEmbed embed = EmbedUtils.defaultEmbed()
                            .setColor(member.getColor())
                            .setThumbnail(user.getEffectiveAvatarUrl().replaceFirst("gif", "png"))
                            .addField("**"+nam+"**" + " **(ID:"+sid+")**","", false)
                            .addField("__**MODERATION STATISTICS**__", "", true)
                            .addField("Warnings: " + war,  "<"+wdatetime+"> ***"+wmod+"*** ---"+rwar, false)
                            .addField("Mutes: " + mut, "<"+mdatetime+"> ***"+mmod+"*** ---"+rmute, false)
                            .addField("Kicks: " + kik, "<"+kdatetime+"> ***"+kmod+"*** ---"+ rkik, false)
                            .addField("Bans: " + ban, "<"+bdatetime+"> ***"+bmod+"*** ---"+ rban, false)
                            .addField("Joined Server: ", member.getJoinDate().format(DateTimeFormatter.RFC_1123_DATE_TIME), true)
                            .addField("Time Since Moderation: ", "TODO", true)
                            .build();

                    event.getChannel().sendMessage(embed).queue();

                }

            }

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

        @Override
    public String getHelp() {
        return "Displays information about a specific user.";
    }

    @Override
    public String getInvoke() {
        return "rapsheet";
    }
}
