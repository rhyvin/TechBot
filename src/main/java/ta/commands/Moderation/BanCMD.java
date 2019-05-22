package ta.commands.Moderation;

import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import ta.Constants;

import ta.util.IntCommand;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.List;

public class BanCMD implements IntCommand {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {

        TextChannel channel = event.getChannel();
        Member member = event.getMember();
        Member selfMember = event.getGuild().getSelfMember();
        List<Member> mentionedMembers = event.getMessage().getMentionedMembers();

        if (mentionedMembers.isEmpty() || args.size() < 2) {
            channel.sendMessage("Missing Arguments").queue();
            return;
        }
        Member target = mentionedMembers.get(0);
        String reason = String.join(" ", args.subList(1, args.size()));

        if (!member.hasPermission(Permission.BAN_MEMBERS) || !member.canInteract(target)) {
            channel.sendMessage("You don't have permission to use this command").queue();
            return;
        }


        if (!selfMember.hasPermission(Permission.BAN_MEMBERS) || !selfMember.canInteract(target)) {
            channel.sendMessage("I can't ban that user or I don't have the ban members permission").queue();
            return;
        }


        event.getGuild().getController().ban(target, 1)
                .reason(String.format("Banned by: %#s, with reason: %s", event.getAuthor(), reason)).queue();

        channel.sendMessage("Success!").queue();
        try {
            String id = target.getUser().getId();
            String uname = target.getEffectiveName();
            String guild = event.getGuild().getName();
            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://157.230.191.75:3306/techbot?useSSL=false", DSecretsMod.UNAME, DSecretsMod.UPASS);
            String sqlCreate = "INSERT INTO " + guild + " (ID, `name`, `bans`, `rbans`) VALUES (" + id +",'" +uname+"',1,'" + reason + "') ON DUPLICATE KEY UPDATE `bans` = `bans` + 1,`rbans` = '" +reason+"'";

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
        return "Bans a user from the server.\n" +
                "Usage: `" + Constants.prefix + getInvoke() + "<user> <reason>`";
    }

    @Override
    public String getInvoke() {
        return "ban";
    }
}
