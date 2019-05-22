package ta.commands.Moderation;

import ta.Constants;
import ta.util.IntCommand;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.List;

public class KickCMD implements IntCommand {
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

        if (!member.hasPermission(Permission.KICK_MEMBERS) || !member.canInteract(target)) {
            channel.sendMessage("You don't have permission to use this command").queue();
            return;
        }


        if (!selfMember.hasPermission(Permission.KICK_MEMBERS) || !selfMember.canInteract(target)) {
            channel.sendMessage("I can't kick that user or I don't have the kick members permission").queue();
            return;
        }


        event.getGuild().getController().kick(target, String.format("Kick by: %#s, with reason: %s",
                event.getAuthor(), reason)).queue();

        channel.sendMessage("Success!").queue();
        try {
            String id = target.getUser().getId();
            String uname = target.getEffectiveName();
            String guild = event.getGuild().getName();
            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://157.230.191.75:3306/techbot?useSSL=false", DSecretsMod.UNAME, DSecretsMod.UPASS);
            String sqlCreate = "INSERT INTO " + guild + " (ID, `name`, `kicks`, `rkicks`) VALUES (" + id +",'" +uname+"',1,'" + reason + "') ON DUPLICATE KEY UPDATE `kicks` = `kicks` + 1,`rkicks` = '" +reason+"'";

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
        return "Kicks a user off the server.\n" +
                "Usage: `"  + Constants.prefix + getInvoke() + " <user> <reason>`";
    }

    @Override
    public String getInvoke() {
        return "kick";
    }
}