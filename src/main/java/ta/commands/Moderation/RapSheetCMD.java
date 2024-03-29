package ta.commands.Moderation;

import com.jagrosh.jdautilities.commons.utils.FinderUtil;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import ta.Constants;
import ta.config.Config;
import ta.util.IntCommand;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class RapSheetCMD implements IntCommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) throws ClassNotFoundException, IOException {
        Config config = new Config(new File("botconfig.json"));

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
                    config.getString("host"), config.getString("uname"), config.getString("upass"));
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
                    if (!rs.wasNull()){
                        rwar = " **Last Reason:** `" + rwar + "` ";
                    }
                    String kik = rs.getString("kicks");
                    String rkik = rs.getString("rkicks");
                    if (rs.wasNull()){
                        rkik = "";
                    }
                    if (!rs.wasNull()){
                        rkik = " **Last Reason:** `" + rkik + "` ";
                    }
                    String ban = rs.getString("bans");
                    String rban = rs.getString("rbans");
                    if (rs.wasNull()){
                        rban = "";
                    }
                    if (!rs.wasNull()){
                        rban = " **Last Reason:** `" + rban + "` ";
                    }
                    String mut = rs.getString("mute");
                    String rmute = rs.getString("rmute");
                    if (rs.wasNull()){
                        rmute = "";
                    }
                    if (!rs.wasNull()){
                        rmute = " **Last Reason:** `" + rmute + "` ";
                    }
                    String mdatetime = rs.getString("mdatetime");
                    if (rs.wasNull()){
                        mdatetime = "";
                    }
                    String mmod =rs.getString("mmod");
                    if (rs.wasNull()){
                        mmod = "";
                    }
                    if (!rs.wasNull()){
                        mmod = " **Moderator:** " + mmod;
                    }
                    String wdatetime = rs.getString("wdatetime");
                    if (rs.wasNull()){
                        wdatetime = "";
                    }
                    String wmod =rs.getString("wmod");
                    if (rs.wasNull()){
                        wmod = "";
                    }
                    if (!rs.wasNull()){
                        wmod = " **Moderator:** "+ wmod;
                    }
                    String kdatetime = rs.getString("kdatetime");
                    if (rs.wasNull()){
                        kdatetime = "";
                    }
                    String kmod =rs.getString("kmod");
                    if (rs.wasNull()){
                        kmod = "";
                    }
                    if (!rs.wasNull()){
                        kmod = " **Moderator:** " + kmod;
                    }
                    String bdatetime = rs.getString("bdatetime");
                    if (rs.wasNull()){
                        bdatetime = "";
                    }
                    String bmod =rs.getString("bmod");
                    if (rs.wasNull()){
                        bmod = "";
                    }
                    if (!rs.wasNull()){
                        bmod = " **Moderator:** " + bmod ;
                    }


                    MessageEmbed embed = EmbedUtils.defaultEmbed()
                            .setColor(member.getColor())
                            .setThumbnail(user.getEffectiveAvatarUrl().replaceFirst("gif", "png"))
                            .addField("**"+nam+"**" + " **(ID:"+sid+")**","", false)
                            .addField("__**MODERATION STATISTICS**__", "", true)
                            .addField("Warnings: " + war,  wmod+" "+rwar + wdatetime+"GMT", false)
                            .addField("Mutes: " + mut, mmod+" "+rmute + mdatetime+"GMT", false)
                            .addField("Kicks: " + kik, kmod+" "+ rkik + kdatetime+"GMT", false)
                            .addField("Bans: " + ban, bmod+" "+ rban + bdatetime+"GMT", false)
                            .addField("Joined Server: ", member.getTimeJoined().format(DateTimeFormatter.RFC_1123_DATE_TIME), true)
                            //.addField("Time Since Moderation: ", String.valueOf(latest), true)
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
        return "Displays Moderation information about a specific user."+
                "Usage: `" + Constants.prefix + getInvoke() + " rapsheet <user>`";
    }

    @Override
    public String getInvoke() {
        return "rapsheet";
    }
}
