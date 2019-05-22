package ta.commands.Moderation;

import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import ta.util.IntCommand;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;



public class MuteCMD implements IntCommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {

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
        event.getGuild().getController().addRolesToMember(mentionedMembers.get(0), event.getGuild().getRolesByName("Muted", true)).queue();
        channel.sendMessage(String.format("Muted by: %#s, with reason: %s on, " + sdf.format(date) + " at " + stf.format(date) + ". ", event.getAuthor(), reason)).queue();
        try {
            String mod = member.getEffectiveName();
            String id = target.getUser().getId();
            String uname = target.getEffectiveName();
            String guild = event.getGuild().getName();
            long datetime = date.getTime();
            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://157.230.191.75:3306/techbot?useSSL=false", DSecretsMod.UNAME, DSecretsMod.UPASS);
            String sqlCreate = "INSERT INTO " + guild + " (ID, `name`, `mute`, `rmute`, `mmod`, `mdatetime`) VALUES (" + id +",'" +uname+"',1,'" + reason + "','"+mod+"','"+sdf.format(date)+" @ "+stf.format(date)+"') ON DUPLICATE KEY UPDATE `mute` = `mute` + 1,`rmute` = '" +reason+"',`mmod`= '"+mod+"',`mdatetime` = '"+sdf.format(date)+" @ "+stf.format(date)+"'";

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
        return "Mute an unruley player";
    }

    @Override
    public String getInvoke() {
        return "mute";
    }
}
