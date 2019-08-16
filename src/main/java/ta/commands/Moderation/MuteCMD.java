package ta.commands.Moderation;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.entities.Role;
import ta.Constants;
import ta.config.Config;
import ta.util.IntCommand;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;



public class MuteCMD implements IntCommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) throws IOException {

        TextChannel channel = event.getChannel();
        Member member = event.getMember();
        Member selfMember = event.getGuild().getSelfMember();
        List<Member> mentionedMembers = event.getMessage().getMentionedMembers();

        if (args.isEmpty() || mentionedMembers.isEmpty()) {
            channel.sendMessage("Missing arguments").queue();
            return;
        }

        Member target = mentionedMembers.get(0);
        String reason = String.join(" ", args.subList(1, args.size()));

        if (!member.hasPermission(Permission.VOICE_MUTE_OTHERS) || !member.canInteract(target)) {
            channel.sendMessage("You don't have permission to use this command").queue();
            return;
        }


        if (!selfMember.hasPermission(Permission.VOICE_MUTE_OTHERS) || !selfMember.canInteract(target)) {
            channel.sendMessage("I can't kick that user or I don't have the kick members permission").queue();
            return;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat stf = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());

        List<Role> muted = event.getGuild().getRolesByName("Muted", false);
        event.getGuild().modifyMemberRoles(mentionedMembers.get(0), muted).queue();
        channel.sendMessage(String.format("Muted by: %#s, with reason: %s on, " + sdf.format(date) + " at " + stf.format(date) + ". ", event.getAuthor(), reason)).queue();
        Config config = new Config(new File("botconfig.json"));
        try {
            String mod = member.getEffectiveName();
            String id = target.getUser().getId();
            String uname = target.getEffectiveName();
            String guild = event.getGuild().getName();
            long datetime = date.getTime();
            Connection con = DriverManager.getConnection(
                    config.getString("host"), config.getString("uname"), config.getString("upass"));
            String sqlCreate = "INSERT INTO " + guild + " (ID, `name`, `mute`, `rmute`, `mmod`, `mdatetime`) VALUES (" + id +",'" +uname+"',1,'" + reason + "','"+mod+"', CURRENT_TIMESTAMP) ON DUPLICATE KEY UPDATE `mute` = `mute` + 1,`rmute` = '" +reason+"',`mmod`= '"+mod+"',`mdatetime` = CURRENT_TIMESTAMP;";

            // create the java statement
            Statement st = con.createStatement();
            st.execute(sqlCreate);
            st.close();
        }        catch (Exception e)
        {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }

    }

    @Override
    public String getHelp() {
        return "Mute a member who is misbehaving"+
                "Usage: `" + Constants.prefix + getInvoke() + " mute <user> <reason>`";
    }

    @Override
    public String getInvoke() {
        return "mute";
    }
}
